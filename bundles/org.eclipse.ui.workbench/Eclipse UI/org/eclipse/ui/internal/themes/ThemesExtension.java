/*******************************************************************************
 * Copyright (c) 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.internal.themes;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.eclipse.e4.ui.internal.css.swt.definition.IThemeElementDefinitionOverridable;
import org.eclipse.e4.ui.internal.css.swt.definition.IThemesExtension;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * @since 3.5
 *
 */
public class ThemesExtension implements IThemesExtension {
	public final static String DEFAULT_CATEGORY_ID = "org.eclipse.ui.themes.CssTheme"; //$NON-NLS-1$

	private String description;

	private List<IThemeElementDefinitionOverridable<?>> definitions = new ArrayList<IThemeElementDefinitionOverridable<?>>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.internal.css.swt.definition.IThemesExtension#
	 * addFontDefinition(java.lang.String)
	 */
	public void addFontDefinition(String symbolicName) {
		FontDefinition definition = new FontDefinition(formatDefaultName(FontDefinition.class,
				symbolicName), symbolicName, null, null, DEFAULT_CATEGORY_ID, true,
				getDefaultDescription());
		definition.setAddedByCss(true);
		definitions.add(definition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.internal.css.swt.definition.IThemesExtension#
	 * addColorDefinition(java.lang.String)
	 */
	public void addColorDefinition(String symbolicName) {
		ColorDefinition definition = new ColorDefinition(formatDefaultName(ColorDefinition.class,
				symbolicName), symbolicName, null, null, DEFAULT_CATEGORY_ID, true,
				getDefaultDescription(), getPluginId());
		definition.setAddedByCss(true);
		definitions.add(definition);
	}

	private String getPluginId() {
		return WorkbenchPlugin.getDefault().getBundle().getSymbolicName();
	}

	public List<IThemeElementDefinitionOverridable<?>> getDefinitions() {
		return definitions;
	}

	private String formatDefaultName(Class<?> cls, String symbolicName) {
		return String.format("%s #%s", cls.getSimpleName(), symbolicName); //$NON-NLS-1$
	}

	public String getDefaultDescription() {
		if (description == null) {
			ResourceBundle resourceBundle = ResourceBundle.getBundle(Theme.class.getName());
			description = resourceBundle.getString("Added.by.css.desc"); //$NON-NLS-1$
		}
		return description;
	}
}
