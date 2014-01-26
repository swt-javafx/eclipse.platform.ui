/*******************************************************************************
 * Copyright (c) 2008, 2013 IBM Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.tests.css.swt;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.e4.ui.css.core.resources.CSSResourcesHelpersTest;
import org.eclipse.e4.ui.css.core.resources.SWTResourceRegistryKeyFactoryTest;
import org.eclipse.e4.ui.css.core.resources.SWTResourcesRegistryTest;
import org.eclipse.e4.ui.css.swt.helpers.CSSSWTColorHelperTest;
import org.eclipse.e4.ui.css.swt.helpers.CSSSWTFontHelperTest;

public class CssSwtTestSuite extends TestSuite {
	/**
	 * Returns the suite. This is required to use the JUnit Launcher.
	 */
	public static final Test suite() {
		return new CssSwtTestSuite();
	}

	/**
	 * Construct the test suite.
	 */
	public CssSwtTestSuite() {
		addTestSuite(CSSSWTFontHelperTest.class);
		addTestSuite(CSSSWTColorHelperTest.class);
		addTestSuite(CSSResourcesHelpersTest.class);
		addTestSuite(SWTResourceRegistryKeyFactoryTest.class);
		addTestSuite(SWTResourcesRegistryTest.class);
		addTestSuite(FontDefinitionTest.class);
		addTestSuite(ColorDefinitionTest.class);
		addTestSuite(ThemesExtensionTest.class);
		addTestSuite(CSSSWTWidgetTest.class);
		addTestSuite(LabelTest.class);
		addTestSuite(CTabFolderTest.class);
		addTestSuite(CTabItemTest.class);
		//		addTestSuite(ETabFolderTest.class);
		//		addTestSuite(ETabItemTest.class);
		addTestSuite(IdClassLabelColorTest.class);
		addTestSuite(ShellTest.class);
		addTestSuite(ButtonTest.class);
		//		addTestSuite(ShellActiveTest.class);  //TODO see bug #273582
		addTestSuite(GradientTest.class);
		addTestSuite(MarginTest.class);

		// text-transform tests
		addTestSuite(ButtonTextTransformTest.class);
		addTestSuite(LabelTextTransformTest.class);
		addTestSuite(TextTextTransformTest.class);

		//other
		addTestSuite(DescendentTest.class);

		addTestSuite(ThemeTest.class);
	}
}
