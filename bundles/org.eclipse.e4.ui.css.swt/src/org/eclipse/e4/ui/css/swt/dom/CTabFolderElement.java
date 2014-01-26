/*******************************************************************************
 * Copyright (c) 2009, 2014 Angelo Zerr and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.css.swt.dom;

import org.eclipse.e4.ui.css.core.dom.CSSStylableElement;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.internal.css.swt.ICTabRendering;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.Node;

/**
 * {@link CSSStylableElement} implementation which wrap SWT {@link CTabFolder}.
 * 
 */
public class CTabFolderElement extends CompositeElement {

	public CTabFolderElement(CTabFolder tabFolder, CSSEngine engine) {
		super(tabFolder, engine);
	}

	/**
	 * Compute static pseudo instances.
	 * 
	 */
	@Override
	protected void computeStaticPseudoInstances() {
		super.computeStaticPseudoInstances();
		// it's CTabFolder. Set selected as static pseudo instance.
		// because this widget define methods
		// CTabFolder#setSelectionBackground (Color color)
		// which set background Color when a CTabItem is selected.
		super.addStaticPseudoInstance("selected");

	}

	@Override
	public Node item(int index) {
		Widget widget = getWidget();
		// retrieve the child control or child item depending on the
		// index
		CTabFolder folder = (CTabFolder) widget;
		int length = folder.getChildren().length;
		if (index >= length) {
			Widget w = folder.getItem(index - length);
			return getElement(w);
		} else {
			Widget w = folder.getChildren()[index];
			return getElement(w);
		}

	}

	@Override
	public int getLength() {
		Widget widget = getWidget();
		int childCount = 0;
		if (widget instanceof Composite) {
			childCount = ((Composite) widget).getChildren().length;

			if (widget instanceof CTabFolder) {
				// if it's a CTabFolder, include the child items in the count
				childCount += ((CTabFolder) widget).getItemCount();
			}
		}
		return childCount;
	}

	@Override
	public void reset() {
		super.reset();
		CTabFolder folder = (CTabFolder) getWidget();
		folder.setSelectionBackground((Color) null);
		folder.setSelectionForeground((Color) null);
		folder.setBackground(null, null);

		if (folder.getRenderer() instanceof ICTabRendering) {
			ICTabRendering renderer = (ICTabRendering) folder
					.getRenderer();
			folder.setRenderer(null);
			renderer.setSelectedTabFill(null);
			renderer.setTabOutline(null);
			renderer.setInnerKeyline(null);
			renderer.setOuterKeyline(null);
			renderer.setShadowColor(null);
			renderer.setActiveToolbarGradient(null, null);
			renderer.setInactiveToolbarGradient(null, null);
		}
	}
}

