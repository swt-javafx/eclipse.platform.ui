package org.eclipse.e4.ui.workbench.renderers.javafx;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.bindings.keys.KeyBindingDispatcher;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.internal.workbench.swt.E4Testable;
import org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine;
import org.eclipse.e4.ui.internal.workbench.swt.ResourceUtility;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IResourceUtilities;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.testing.TestableObject;

@SuppressWarnings("restriction")
public class JavaFXPresentationEngine extends PartRenderingEngine {

	static JavaFXPresentationEngine instance;
	private MApplicationElement uiRoot;
	private IEclipseContext runContext;
	
	@Inject
	@Optional
	protected IEventBroker eventBroker;

	private org.eclipse.swt.widgets.Listener keyListener;
	private Shell limbo;
	private IEclipseContext appContext;
	private boolean spinOnce;
	
	public static final class App extends Application {
		@Override
		public void start(Stage primaryStage) throws Exception {
			instance.doStart(primaryStage);
		}
		
		@Override
		public void stop() throws Exception {
			if (!instance.spinOnce)
				instance.cleanUp();
		}
	}

	@Inject
	public JavaFXPresentationEngine(
			IEclipseContext appContext,
			@Named(E4Workbench.RENDERER_FACTORY_URI) @Optional String factoryUrl) {
		super(factoryUrl);
		this.appContext = appContext;
	}

	@Override
	public Object run(MApplicationElement uiRoot, final IEclipseContext runContext) {
		instance = this;
		this.uiRoot = uiRoot;
		this.runContext = runContext;
		App.launch(App.class, new String[0]);
		return IApplication.EXIT_OK;
	}
	
	private void doStart(Stage primaryStage) {
		final Display display;
		if (runContext.get(Display.class) != null) {
			display = runContext.get(Display.class);
		} else {
			display = Display.getDefault();
			runContext.set(Display.class, display);
		}

		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				initializeStyling(display, runContext);

				Display.running = true;
				Display.primaryStage = primaryStage;
				
				// Register an SWT resource handler
				runContext.set(IResourceUtilities.class.getName(),
						new ResourceUtility());
		
				// set up the keybinding manager
				KeyBindingDispatcher dispatcher = (KeyBindingDispatcher) ContextInjectionFactory
						.make(KeyBindingDispatcher.class, runContext);
				runContext.set(KeyBindingDispatcher.class.getName(), dispatcher);
				keyListener = dispatcher.getKeyDownFilter();
				display.addFilter(SWT.KeyDown, keyListener);
				display.addFilter(SWT.Traverse, keyListener);

				// Show the initial UI

				// Create a 'limbo' shell (used to host controls that shouldn't
				// be in the current layout)
				Shell limbo = getLimboShell();
				runContext.set("limbo", limbo);

				// HACK!! we should loop until the display gets disposed...
				// ...then we listen for the last 'main' window to get disposed
				// and dispose the Display
				testShell = null;
				theApp = null;
				spinOnce = true;
				if (uiRoot instanceof MApplication) {
					ShellActivationListener shellDialogListener = new ShellActivationListener(
							(MApplication) uiRoot);
					display.addFilter(SWT.Activate, shellDialogListener);
					display.addFilter(SWT.Deactivate, shellDialogListener);
					spinOnce = false; // loop until the app closes
					theApp = (MApplication) uiRoot;
					// long startTime = System.currentTimeMillis();
					MWindow selected = theApp.getSelectedElement();
					if (selected == null) {
						for (MWindow window : theApp.getChildren()) {
							createGui(window);
						}
					} else {
						// render the selected one first
						createGui(selected);
						for (MWindow window : theApp.getChildren()) {
							if (selected != window) {
								createGui(window);
							}
						}
					}
					// long endTime = System.currentTimeMillis();
					// System.out.println("Render: " + (endTime - startTime));
					// tell the app context we are starting so the splash is
					// torn down
					IApplicationContext ac = appContext
							.get(IApplicationContext.class);
					if (ac != null) {
						ac.applicationRunning();
						if (eventBroker != null)
							eventBroker.post(
									UIEvents.UILifeCycle.APP_STARTUP_COMPLETE,
									theApp);
					}
				} else if (uiRoot instanceof MUIElement) {
					if (uiRoot instanceof MWindow) {
						testShell = (Shell) createGui((MUIElement) uiRoot);
					} else {
						// Special handling for partial models (for testing...)
						testShell = new Shell(display, SWT.SHELL_TRIM);
						createGui((MUIElement) uiRoot, testShell, null);
					}
				}

				// allow any early startup extensions to run
				Runnable earlyStartup = (Runnable) runContext
						.get(EARLY_STARTUP_HOOK);
				if (earlyStartup != null) {
					earlyStartup.run();
				}

				TestableObject testableObject = (TestableObject) runContext
						.get(TestableObject.class.getName());
				if (testableObject instanceof E4Testable) {
					((E4Testable) testableObject).init(display,
							(IWorkbench) runContext.get(IWorkbench.class
									.getName()));
				}

			}
		});
	}
	
	private Shell getLimboShell() {
		if (limbo == null) {
			limbo = new Shell(Display.getCurrent(), SWT.NONE);
			limbo.setText("PartRenderingEngine's limbo"); //$NON-NLS-1$ // just for debugging, not shown anywhere

			// Place the limbo shell 'off screen'
			limbo.setLocation(0, 10000);

			limbo.setBackgroundMode(SWT.INHERIT_DEFAULT);
			limbo.setData(ShellActivationListener.DIALOG_IGNORE_KEY,
					Boolean.TRUE);
		}
		return limbo;
	}

	private void cleanUp() {
		if (keyListener != null) {
			Display display = Display.getDefault();
			if (!display.isDisposed()) {
				display.removeFilter(SWT.KeyDown, keyListener);
				display.removeFilter(SWT.Traverse, keyListener);
				keyListener = null;
			}
		}
	}

}
