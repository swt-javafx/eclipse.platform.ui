/******************************************************************************* * Copyright (c) 2005 IBM Corporation and others. * All rights reserved. This program and the accompanying materials * are made available under the terms of the Eclipse Public License v1.0 * which accompanies this distribution, and is available at * http://www.eclipse.org/legal/epl-v10.html * * Contributors: *     IBM Corporation - initial API and implementation ******************************************************************************/package org.eclipse.core.commands;import java.util.ArrayList;import java.util.Collection;/** * <p> * An abstract implementation of {@link IHandlerWithState}. This provides basic * handling for adding and remove state. When state is added, the handler * attaches itself as a listener and fire a handleStateChange event to notify * this handler. When state is removed, the handler removes itself as a * listener. * </p> * <p> * Clients may extend this class. * </p> * <p> * <strong>EXPERIMENTAL</strong>. This class or interface has been added as * part of a work in progress. There is a guarantee neither that this API will * work nor that it will remain the same. Please do not use this API without * consulting with the Platform/UI team. * </p> *  * @since 3.2 */public abstract class AbstractHandlerWithState extends AbstractHandler		implements IHandlerWithState, IHandlerStateListener {	/**	 * The collection of states currently held by this handler. If this handler	 * has no state (generally, when inactive), then this will be	 * <code>null</code>.	 */	private Collection states = null;	/**	 * <p>	 * Adds a state to this handler. This will add this handler as a listener to	 * the state, and then fire a handleStateChange so that the handler can	 * respond to the incoming state.	 * </p>	 * <p>	 * Clients may extend this method, but they should call this super method	 * first before doing anything else.	 * </p>	 * 	 * @param state	 *            The state to add; must not be <code>null</code>.	 */	public void addState(final IHandlerState state) {		if (state == null) {			throw new NullPointerException("Cannot add a null state"); //$NON-NLS-1$		}		if (states == null) {			states = new ArrayList(1);		}		states.add(state);		state.addListener(this);		handleStateChange(state, null);	}	/**	 * <p>	 * Removes a state from this handler. This will remove this handler as a	 * listener to the state. No event is fired to notify the handler of this	 * change.	 * </p>	 * <p>	 * Clients may extend this method, but they should call this super method	 * first before doing anything else.	 * </p>	 * 	 * @param state	 *            The state to remove; must not be <code>null</code>.	 */	public void removeState(final IHandlerState state) {		if (state == null) {			throw new NullPointerException("Cannot remove a null state"); //$NON-NLS-1$		}		state.removeListener(this);		if (states != null) {			states.remove(state);			if (states.isEmpty()) {				states = null;			}		}	}}