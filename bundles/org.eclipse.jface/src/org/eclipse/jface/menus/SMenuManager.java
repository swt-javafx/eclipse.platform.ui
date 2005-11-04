/******************************************************************************* * Copyright (c) 2005 IBM Corporation and others. * All rights reserved. This program and the accompanying materials * are made available under the terms of the Eclipse Public License v1.0 * which accompanies this distribution, and is available at * http://www.eclipse.org/legal/epl-v10.html * * Contributors: *     IBM Corporation - initial API and implementation ******************************************************************************/package org.eclipse.jface.menus;import java.util.HashMap;import java.util.HashSet;import java.util.Map;import java.util.Set;import org.eclipse.core.commands.util.ListenerList;/** * <p> * <strong>EXPERIMENTAL</strong>. This class or interface has been added as * part of a work in progress. There is a guarantee neither that this API will * work nor that it will remain the same. Please do not use this API without * consulting with the Platform/UI team. * </p> *  * @since 3.2 */public final class SMenuManager implements IActionSetListener, IGroupListener,		IItemListener, IMenuListener, IWidgetListener {	/**	 * The map of action set identifiers (<code>String</code>) to action set (	 * <code>SActionSet</code>). This collection may be empty, but it is	 * never <code>null</code>.	 */	private final Map actionSetsById = new HashMap();	/**	 * The set of identifiers for those action sets that are defined. This value	 * may be empty, but it is never <code>null</code>.	 */	private final Set definedActionSetIds = new HashSet();	/**	 * The set of identifiers for those groups that are defined. This value may	 * be empty, but it is never <code>null</code>.	 */	private final Set definedGroupIds = new HashSet();	/**	 * The set of identifiers for those items that are defined. This value may	 * be empty, but it is never <code>null</code>.	 */	private final Set definedItemIds = new HashSet();	/**	 * The set of identifiers for those menus that are defined. This value may	 * be empty, but it is never <code>null</code>.	 */	private final Set definedMenuIds = new HashSet();	/**	 * The set of identifiers for those widgets that are defined. This value may	 * be empty, but it is never <code>null</code>.	 */	private final Set definedWidgetIds = new HashSet();	/**	 * The map of group identifiers (<code>String</code>) to groups (	 * <code>SGroup</code>). This collection may be empty, but it is never	 * <code>null</code>.	 */	private final Map groupsById = new HashMap();	/**	 * The map of item identifiers (<code>String</code>) to items (	 * <code>SItem</code>). This collection may be empty, but it is never	 * <code>null</code>.	 */	private final Map itemsById = new HashMap();	/**	 * A collection of objects listening to changes to this menu manager. This	 * collection is <code>null</code> if there are no listeners.	 */	private transient ListenerList listenerList = null;	/**	 * The map of menu identifiers (<code>String</code>) to menus (	 * <code>SMenu</code>). This collection may be empty, but it is never	 * <code>null</code>.	 */	private final Map menusById = new HashMap();	/**	 * The map of widget identifiers (<code>String</code>) to widgets (	 * <code>SWidget</code>). This collection may be empty, but it is never	 * <code>null</code>.	 */	private final Map widgetsById = new HashMap();	/**	 * Adds a listener to this menu manager that will be notified when this	 * manager's state changes.	 * 	 * @param listener	 *            The listener to be added; must not be <code>null</code>.	 */	public final void addListener(final IMenuManagerListener listener) {		if (listenerList == null) {			listenerList = new ListenerList(1);		}		listenerList.add(listener);	}	/**	 * Notifies listeners to this menu manager that it has changed in some way.	 * 	 * @param event	 *            The event to fire; may be <code>null</code>.	 */	private final void fireMenuManagerChanged(final MenuManagerEvent event) {		if (event == null) {			return;		}		if (listenerList != null) {			final Object[] listeners = listenerList.getListeners();			for (int i = 0; i < listeners.length; i++) {				final IMenuManagerListener listener = (IMenuManagerListener) listeners[i];				listener.menuManagerChanged(event);			}		}	}	/**	 * Gets the action set with the given identifier. If no such action set	 * currently exists, then the action set will be created (but will be	 * undefined).	 * 	 * @param actionSetId	 *            The identifier to find; must not be <code>null</code> and	 *            must not be zero-length.	 * @return The action set with the given identifier; this value will never	 *         be <code>null</code>, but it might be undefined.	 * @see SActionSet	 */	public final SActionSet getActionSet(final String actionSetId) {		if (actionSetId == null) {			throw new NullPointerException(					"An action set may not have a null identifier"); //$NON-NLS-1$		}		if (actionSetId.length() < 1) {			throw new IllegalArgumentException(					"The action set must not have a zero-length identifier"); //$NON-NLS-1$		}		SActionSet actionSet = (SActionSet) actionSetsById.get(actionSetId);		if (actionSet == null) {			actionSet = new SActionSet(actionSetId);			actionSetsById.put(actionSetId, actionSet);			actionSet.addListener(this);		}		return actionSet;	}	/**	 * Gets the group with the given identifier. If no such group currently	 * exists, then the group will be created (but will be undefined).	 * 	 * @param groupId	 *            The identifier to find; must not be <code>null</code> and	 *            must not be zero-length.	 * @return The group with the given identifier; this value will never be	 *         <code>null</code>, but it might be undefined.	 * @see SGroup	 */	public final SGroup getGroup(final String groupId) {		if (groupId == null) {			throw new NullPointerException(					"A group may not have a null identifier"); //$NON-NLS-1$		}		if (groupId.length() < 1) {			throw new IllegalArgumentException(					"The group must not have a zero-length identifier"); //$NON-NLS-1$		}		SGroup group = (SGroup) groupsById.get(groupId);		if (group == null) {			group = new SGroup(groupId);			groupsById.put(groupId, group);			group.addListener(this);		}		return group;	}	/**	 * Gets the item with the given identifier. If no such item currently	 * exists, then the item will be created (but will be undefined).	 * 	 * @param itemId	 *            The identifier to find; must not be <code>null</code> and	 *            must not be zero-length.	 * @return The item with the given identifier; this value will never be	 *         <code>null</code>, but it might be undefined.	 * @see SItem	 */	public final SItem getItem(final String itemId) {		if (itemId == null) {			throw new NullPointerException(					"An item may not have a null identifier"); //$NON-NLS-1$		}		if (itemId.length() < 1) {			throw new IllegalArgumentException(					"The item must not have a zero-length identifier"); //$NON-NLS-1$		}		SItem item = (SItem) itemsById.get(itemId);		if (item == null) {			item = new SItem(itemId);			itemsById.put(itemId, item);			item.addListener(this);		}		return item;	}	/**	 * Gets the menu with the given identifier. If no such menu currently	 * exists, then the menu will be created (but will be undefined).	 * 	 * @param menuId	 *            The identifier to find; must not be <code>null</code> and	 *            must not be zero-length.	 * @return The menu with the given identifier; this value will never be	 *         <code>null</code>, but it might be undefined.	 * @see SMenu	 */	public final SMenu getMenu(final String menuId) {		if (menuId == null) {			throw new NullPointerException(					"A menu may not have a null identifier"); //$NON-NLS-1$		}		if (menuId.length() < 1) {			throw new IllegalArgumentException(					"The menu must not have a zero-length identifier"); //$NON-NLS-1$		}		SMenu menu = (SMenu) menusById.get(menuId);		if (menu == null) {			menu = new SMenu(menuId);			menusById.put(menuId, menu);			menu.addListener(this);		}		return menu;	}	/**	 * Gets the widget with the given identifier. If no such widget currently	 * exists, then the widget will be created (but will be undefined).	 * 	 * @param widgetId	 *            The identifier to find; must not be <code>null</code> and	 *            must not be zero-length.	 * @return The item with the given identifier; this value will never be	 *         <code>null</code>, but it might be undefined.	 * @see SWidget	 */	public final SWidget getWidget(final String widgetId) {		if (widgetId == null) {			throw new NullPointerException(					"A widget may not have a null identifier"); //$NON-NLS-1$		}		if (widgetId.length() < 1) {			throw new IllegalArgumentException(					"The widget must not have a zero-length identifier"); //$NON-NLS-1$		}		SWidget widget = (SWidget) widgetsById.get(widgetId);		if (widget == null) {			widget = new SWidget(widgetId);			widgetsById.put(widgetId, widget);			widget.addListener(this);		}		return widget;	}	public final void actionSetChanged(final ActionSetEvent actionSetEvent) {		if (actionSetEvent.isDefinedChanged() && isListenerAttached()) {			final SActionSet actionSet = actionSetEvent.getActionSet();			final String actionSetId = actionSet.getId();			final boolean actionSetIdAdded = actionSet.isDefined();			if (actionSetIdAdded) {				definedActionSetIds.add(actionSetId);			} else {				definedActionSetIds.remove(actionSetId);			}			fireMenuManagerChanged(new MenuManagerEvent(this, null, false,					null, false, null, false, null, false, actionSetId,					actionSetIdAdded));		}	}	public final void groupChanged(final GroupEvent groupEvent) {		if (groupEvent.isDefinedChanged() && isListenerAttached()) {			final SGroup group = groupEvent.getGroup();			final String groupId = group.getId();			final boolean groupIdAdded = group.isDefined();			if (groupIdAdded) {				definedGroupIds.add(groupId);			} else {				definedGroupIds.remove(groupId);			}			fireMenuManagerChanged(new MenuManagerEvent(this, groupId,					groupIdAdded, null, false, null, false, null, false, null,					false));		}	}	/**	 * Whether one or more listeners are attached to the menu manager.	 * 	 * @return <code>true</code> if listeners are attached to the menu	 *         manager; <code>false</code> otherwise.	 */	protected final boolean isListenerAttached() {		return listenerList != null;	}	public final void itemChanged(final ItemEvent itemEvent) {		if (itemEvent.isDefinedChanged() && isListenerAttached()) {			final SItem item = itemEvent.getItem();			final String itemId = item.getId();			final boolean itemIdAdded = item.isDefined();			if (itemIdAdded) {				definedItemIds.add(itemId);			} else {				definedItemIds.remove(itemId);			}			fireMenuManagerChanged(new MenuManagerEvent(this, null, false,					itemId, itemIdAdded, null, false, null, false, null, false));		}	}	public final void menuChanged(final MenuEvent menuEvent) {		if (menuEvent.isDefinedChanged() && isListenerAttached()) {			final SMenu menu = menuEvent.getMenu();			final String menuId = menu.getId();			final boolean menuIdAdded = menu.isDefined();			if (menuIdAdded) {				definedMenuIds.add(menuId);			} else {				definedMenuIds.remove(menuId);			}			fireMenuManagerChanged(new MenuManagerEvent(this, null, false,					null, false, menuId, menuIdAdded, null, false, null, false));		}	}	/**	 * Removes a listener from this menu manager.	 * 	 * @param listener	 *            The listener to be removed; must not be <code>null</code>.	 */	public final void removeListener(final IMenuManagerListener listener) {		if (listenerList != null) {			listenerList.remove(listener);		}		if (listenerList.isEmpty()) {			listenerList = null;		}	}	public final void widgetChanged(final WidgetEvent widgetEvent) {		if (widgetEvent.isDefinedChanged() && isListenerAttached()) {			final SWidget widget = widgetEvent.getWidget();			final String widgetId = widget.getId();			final boolean widgetIdAdded = widget.isDefined();			if (widgetIdAdded) {				definedWidgetIds.add(widgetId);			} else {				definedWidgetIds.remove(widgetId);			}			fireMenuManagerChanged(new MenuManagerEvent(this, null, false,					null, false, null, false, widgetId, widgetIdAdded, null,					false));		}	}}