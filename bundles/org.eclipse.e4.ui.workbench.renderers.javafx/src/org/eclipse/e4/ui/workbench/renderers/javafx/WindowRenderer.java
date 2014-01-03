package org.eclipse.e4.ui.workbench.renderers.javafx;

import javafx.stage.Stage;

import org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.widgets.Display;

@SuppressWarnings("restriction")
public class WindowRenderer extends AbstractPartRenderer {

	@Override
	public Object createWidget(MUIElement element, Object parent) {
		if (element instanceof MWindow) {
			final MWindow window = (MWindow)element;
			
			final Stage[] result = new Stage[1];
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					Stage stage;
					if (Display.primaryStage != null) {
						stage = Display.primaryStage;
						Display.primaryStage = null;
					} else {
						stage = new Stage();
					}
					
					stage.setTitle(window.getLocalizedLabel());
					stage.show();
					result[0] = stage;
				}
			});
			return result[0];
		}
		return null;
	}

	@Override
	public void processContents(MElementContainer<MUIElement> container) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindWidget(MUIElement me, Object widget) {
		me.setWidget(widget);
	}

	@Override
	protected Object getParentWidget(MUIElement element) {
		return null;
	}

	@Override
	public void disposeWidget(MUIElement part) {
		// TODO Auto-generated method stub

	}

	@Override
	public void hookControllerLogic(MUIElement me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void childRendered(MElementContainer<MUIElement> parentElement, MUIElement element) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getImage(MUILabel element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean requiresFocus(MPart element) {
		// TODO Auto-generated method stub
		return false;
	}

}
