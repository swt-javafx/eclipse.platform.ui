package org.eclipse.e4.ui.workbench.renderers.javafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.swt.widgets.Display;

public class JavaFXPresentationEngine implements IPresentationEngine {

	static JavaFXPresentationEngine instance;
	private MApplicationElement uiRoot;
	private IEclipseContext appContext;
	private Stage primaryStage;
	
	public static final class App extends Application {
		@Override
		public void start(Stage primaryStage) throws Exception {
			instance.doStart(primaryStage);
		}
	}
	
	@Override
	public Object createGui(MUIElement element) {
		if (element instanceof MWindow) {
			MWindow window = (MWindow)element;
	        primaryStage.setTitle(window.getLabel());
	        
	        Button btn = new Button();
	        btn.setText("Say 'Hello World'");
	        btn.setOnAction(new EventHandler<ActionEvent>() {
	 
	            @Override
	            public void handle(ActionEvent event) {
	                System.out.println("Hello World!");
	            }
	        });
	        
	        StackPane root = new StackPane();
	        root.getChildren().add(btn);
	        primaryStage.setScene(new Scene(root, 300, 250));
	        primaryStage.show();
		}
		return null;
	}

	@Override
	public Object createGui(MUIElement element, Object parentWidget, IEclipseContext parentContext) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void removeGui(MUIElement element) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusGui(MUIElement element) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object run(MApplicationElement uiRoot, IEclipseContext appContext) {
		instance = this;
		this.uiRoot = uiRoot;
		this.appContext = appContext;
		App.launch(App.class, new String[0]);
		return IApplication.EXIT_OK;
	}
	
	private void doStart(Stage primaryStage) {
		Display.running = true;
		this.primaryStage = primaryStage;
		
		if (uiRoot instanceof MApplication) {
			MApplication app = (MApplication)uiRoot;
			for (MWindow window : app.getChildren()) {
				createGui(window);
			}
		}
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
