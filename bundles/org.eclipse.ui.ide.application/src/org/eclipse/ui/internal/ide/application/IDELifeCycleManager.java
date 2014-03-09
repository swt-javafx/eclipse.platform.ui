package org.eclipse.ui.internal.ide.application;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.ui.PlatformUI;

/**
 * @since 3.3
 *
 */
public class IDELifeCycleManager {

	@ProcessAdditions
	void processAdditions(final IEclipseContext context) {
		PlatformUI.createWorkbench(context, new IDEWorkbenchAdvisor());
	}

}
