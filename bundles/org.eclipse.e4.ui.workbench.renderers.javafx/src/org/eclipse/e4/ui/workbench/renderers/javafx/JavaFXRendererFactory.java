package org.eclipse.e4.ui.workbench.renderers.javafx;

import org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.swt.factories.IRendererFactory;

@SuppressWarnings("restriction")
public class JavaFXRendererFactory implements IRendererFactory {

	@Override
	public AbstractPartRenderer getRenderer(MUIElement uiElement, Object parent) {
		if (uiElement instanceof MWindow)
			return new WindowRenderer();
		return null;
	}
	
}
