package org.eclipse.e4.ui.workbench.renderers.javafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.swt.widgets.Display;

public class JavaFXPresentationEngine implements IPresentationEngine {

	public static final class App extends Application {
		@Override
		public void start(Stage primaryStage) throws Exception {
			Display.running = true;
			
	        primaryStage.setTitle("Hello World!");
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
	}
	
	@Override
	public Object createGui(MUIElement element) {
		// TODO Auto-generated method stub
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
		App.launch(App.class, new String[0]);
		return IApplication.EXIT_OK;
	}
	
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
