package org.eclipse.e4.ui.workbench.renderers.javafx;

import javafx.application.Application;
import javafx.stage.Stage;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;

public class JavaFXApplication implements IApplication {

	private static IApplicationContext applicationContext;
	private static IApplication ideApplication;
	
	public static class App extends Application {
		@Override
		public void start(Stage primaryStage) throws Exception {
			Display.primaryStage = primaryStage;
			ideApplication.start(applicationContext);
		}

		@Override
		public void stop() {
			ideApplication.stop();
		}
	}
	
	@Override
	public Object start(IApplicationContext context) throws Exception {
		applicationContext = context;
		ideApplication = getIDEApplication();
		App.launch(App.class, new String[0]);
		return Status.OK_STATUS;
	}

	@Override
	public void stop() {
		ideApplication.stop();
	}
	
	private static IApplication getIDEApplication() {
		IExtension extension = Platform.getExtensionRegistry().getExtension(Platform.PI_RUNTIME,
				Platform.PT_APPLICATIONS, "org.eclipse.ui.ide.workbench"); //$NON-NLS-1$
		if (extension == null)
			return null;
		
		try {
			IConfigurationElement[] elements = extension.getConfigurationElements();
			if (elements.length > 0) {
				IConfigurationElement[] runs = elements[0].getChildren("run"); //$NON-NLS-1$
				if (runs.length > 0) {
					Object runnable = runs[0].createExecutableExtension("class");//$NON-NLS-1$
					if (runnable instanceof IApplication)
						return (IApplication)runnable;
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}

}
