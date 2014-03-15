package org.eclipse.ui.ide.javafx;

import javafx.stage.Stage;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.swt.ResourceUtility;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IResourceUtilities;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.renderers.swt.WorkbenchRendererFactory;
import org.eclipse.e4.ui.workbench.swt.factories.IRendererFactory;
import org.eclipse.e4.ui.workbench.swt.util.ISWTResourceUtilities;
import org.eclipse.fx.ui.workbench.renderers.base.widget.WWindow;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.application.IDEWorkbenchAdvisor;

/**
 * @since 3.3
 *
 */
@SuppressWarnings("restriction")
public class IDELifeCycleManager {

	private IWorkbench legacyWorkbench;
	
	private ISWTResourceUtilities swtResourceUtilities;
	
	@ProcessAdditions
	void processAdditions(final IEclipseContext context) {
		Display display = context.get(Display.class);
		if (display == null) {
			// Make sure we have SWT inited.
			display = new Display();
			context.set(Display.class, display);
		}
		
		WorkbenchRendererFactory swtRendererFactory = new WorkbenchRendererFactory();
		ContextInjectionFactory.inject(swtRendererFactory, context);
		context.set(IRendererFactory.class, swtRendererFactory); 

		Platform.getAdapterManager().registerAdapters(new IAdapterFactory() {
			@Override
			public Class[] getAdapterList() {
				return new Class[] { Shell.class };
			}
			
			@Override
			public Object getAdapter(Object adaptableObject, Class adapterType) {
				for (Shell shell : context.get(Display.class).getShells()) {
					if (shell.getData(MWindow.class.getName()) == adaptableObject)
						return shell;
				}
				System.out.println("Unknown stage/shell");
				return null;
			}
		}, MWindow.class);
		
		Platform.getAdapterManager().registerAdapters(new IAdapterFactory() {
			@Override
			public Class[] getAdapterList() {
				return new Class[] { ISWTResourceUtilities.class };
			}
			
			@Override
			public Object getAdapter(Object adaptableObject, Class adapterType) {
				if (swtResourceUtilities == null)
					swtResourceUtilities = new ResourceUtility();
				return swtResourceUtilities;
			}
		}, IResourceUtilities.class);

		legacyWorkbench = PlatformUI.createWorkbench(context, new IDEWorkbenchAdvisor());
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				MApplication app = context.get(MApplication.class);
				for (MWindow window : app.getChildren()) {
					WWindow<Stage> wwindow = (WWindow<Stage>) window.getWidget();
					Stage stage = (Stage) wwindow.getWidget();
					Shell shell = new Shell(stage);
					shell.setData(MWindow.class.getName(), window);
					if (stage.isShowing())
						shell.activateShell();
				}
			}
		});
	}

	// TODO this doesn't really work for some reason.
	@PreSave
	void preSave() {
		if (legacyWorkbench != null)
			legacyWorkbench.close();
	}

}
