/******************************************************************************* * Copyright (c) 2005 IBM Corporation and others. * All rights reserved. This program and the accompanying materials * are made available under the terms of the Eclipse Public License v1.0 * which accompanies this distribution, and is available at * http://www.eclipse.org/legal/epl-v10.html * * Contributors: *     IBM Corporation - initial API and implementation ******************************************************************************/package org.eclipse.jface.menus;import org.eclipse.core.commands.ParameterizedCommand;import org.eclipse.core.commands.common.NotDefinedException;import org.eclipse.core.commands.util.ListenerList;import org.eclipse.jface.util.Util;/** * <p> * An item in a menu, tool bar or status line. An item is characterized as a * button of some kind, that executes a command when clicked. An item can * optionally * </p> * <p> * Clients may instantiate this class, but must not extend. * </p> * <p> * <strong>EXPERIMENTAL</strong>. This class or interface has been added as * part of a work in progress. There is a guarantee neither that this API will * work nor that it will remain the same. Please do not use this API without * consulting with the Platform/UI team. * </p> *  * @since 3.2 */public final class SItem extends MenuElement {	/**	 * The command that should be executed when this item is clicked. This value	 * must not be <code>null</code>.	 */	private ParameterizedCommand command;	/**	 * The identifier of the menu attached to this item. This menu will be shown	 * when the user performs some action. This member variable may be	 * <code>null</code> if there is no menu associated.	 */	private String menuId;	/**	 * Constructs a new instance of <code>SItem</code>.	 * 	 * @param id	 *            The identifier of the item to create; must not be	 *            <code>null</code>	 */	SItem(final String id) {		super(id);	}	/**	 * Adds a listener to this item that will be notified when this item's state	 * changes.	 * 	 * @param listener	 *            The listener to be added; must not be <code>null</code>.	 */	public final void addListener(final IItemListener listener) {		if (listenerList == null) {			listenerList = new ListenerList(1);		}		listenerList.add(listener);	}	/**	 * <p>	 * Defines this item by providing the fully-parameterized command. The	 * locations and menu identifier are optional. The defined property	 * automatically becomes <code>true</code>.	 * </p>	 * 	 * @param command	 *            The fully-parameterized command to execute when this item is	 *            triggered; must not be <code>null</code>.	 * @param menuId	 *            The identifier of the menu to display along with this item;	 *            may be <code>null</code> if there is no such menu.	 * @param locations	 *            The locations in which this item will appear; may be	 *            <code>null</code> or empty.	 */	public final void define(final ParameterizedCommand command,			final String menuId, final SLocation[] locations) {		if (command == null) {			throw new NullPointerException("An item needs a command"); //$NON-NLS-1$		}		ItemEvent event = null;		if (isListenerAttached()) {			final boolean commandChanged = !Util.equals(this.command, command);			final boolean menuChanged = !Util.equals(this.menuId, menuId);			final boolean locationsChanged = !Util.equals(this.locations,					locations);			final boolean definedChanged = !this.defined;			event = new ItemEvent(this, commandChanged, menuChanged,					locationsChanged, definedChanged, false);		}		this.command = command;		this.menuId = menuId;		this.locations = locations;		this.defined = true;		fireItemChanged(event);	}	/**	 * Notifies listeners to this item that it has changed in some way.	 * 	 * @param event	 *            The event to fire; may be <code>null</code>.	 */	private final void fireItemChanged(final ItemEvent event) {		if (event == null) {			return;		}		if (listenerList != null) {			final Object[] listeners = listenerList.getListeners();			for (int i = 0; i < listeners.length; i++) {				final IItemListener listener = (IItemListener) listeners[i];				listener.itemChanged(event);			}		}	}	/**	 * Notifies listeners to this item that it has changed visibility.	 */	protected final void fireVisibleChanged() {		if (listenerList != null) {			final Object[] listeners = listenerList.getListeners();			final ItemEvent event = new ItemEvent(this, false, false, false,					false, true);			for (int i = 0; i < listeners.length; i++) {				final IItemListener listener = (IItemListener) listeners[i];				listener.itemChanged(event);			}		}	}	/**	 * Returns the fully-parameterized command that is triggered by this item.	 * 	 * @return The command for this item; never <code>null</code>.	 * @throws NotDefinedException	 *             If the handle is not currently defined.	 */	public final ParameterizedCommand getCommand() throws NotDefinedException {		if (!isDefined()) {			throw new NotDefinedException(					"Cannot get the command from an undefined item"); //$NON-NLS-1$		}		return command;	}	/**	 * Returns the identifier of the menu that is associated with this item.	 * 	 * @return The menu for this item; never <code>null</code>.	 * @throws NotDefinedException	 *             If the handle is not currently defined.	 */	public final String getMenuId() throws NotDefinedException {		if (!isDefined()) {			throw new NotDefinedException(					"Cannot get the menu from an undefined item"); //$NON-NLS-1$		}		return menuId;	}	/**	 * Removes a listener from this item.	 * 	 * @param listener	 *            The listener to be removed; must not be <code>null</code>.	 */	public final void removeListener(final IItemListener listener) {		if (listenerList != null) {			listenerList.remove(listener);		}		if (listenerList.isEmpty()) {			listenerList = null;		}	}	/**	 * The string representation of this item -- for debugging purposes only.	 * This string should not be shown to an end user.	 * 	 * @return The string representation; never <code>null</code>.	 */	public final String toString() {		if (string == null) {			final StringBuffer stringBuffer = new StringBuffer();			stringBuffer.append("SItem("); //$NON-NLS-1$			stringBuffer.append(id);			stringBuffer.append(',');			stringBuffer.append(command);			stringBuffer.append(',');			stringBuffer.append(menuId);			stringBuffer.append(',');			stringBuffer.append(locations);			stringBuffer.append(',');			stringBuffer.append(defined);			stringBuffer.append(')');			string = stringBuffer.toString();		}		return string;	}	/**	 * Makes this item become undefined. This has the side effect of changing	 * the command, menu id and locations to <code>null</code>. Notification	 * is sent to all listeners.	 */	public final void undefine() {		string = null;		ItemEvent event = null;		if (isListenerAttached()) {			final boolean commandChanged = command != null;			final boolean menuChanged = menuId != null;			final boolean locationsChanged = locations != null;			final boolean definedChanged = this.defined;			event = new ItemEvent(this, commandChanged, menuChanged,					locationsChanged, definedChanged, false);		}		defined = false;		command = null;		menuId = null;		locations = null;		fireItemChanged(event);	}}