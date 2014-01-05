package org.eclipse.e4.ui.workbench.renderers.javafx;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine;
import org.eclipse.e4.ui.internal.workbench.swt.ResourceUtility;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IResourceUtilities;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;

@SuppressWarnings("restriction")
public class JavaFXPresentationEngine extends PartRenderingEngine {

	static JavaFXPresentationEngine instance;
	private MApplicationElement uiRoot;
	private IEclipseContext runContext;
	
	public static final class App extends Application {
		@Override
		public void start(Stage primaryStage) throws Exception {
			instance.doStart(primaryStage);
		}
	}

	@Inject
	public JavaFXPresentationEngine(
			@Named(E4Workbench.RENDERER_FACTORY_URI) @Optional String factoryUrl) {
		super(factoryUrl);
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
		
				if (uiRoot instanceof MApplication) {
					MApplication app = (MApplication)uiRoot;
					for (MWindow window : app.getChildren()) {
						createGui(window);
					}
				}
			}
		});
	}
	
}
