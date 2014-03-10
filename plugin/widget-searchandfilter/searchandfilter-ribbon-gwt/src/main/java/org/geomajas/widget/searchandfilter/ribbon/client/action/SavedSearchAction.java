/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.searchandfilter.ribbon.client.action;

import com.smartgwt.client.widgets.events.ClickEvent;
import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.widget.searchandfilter.client.util.GsfLayout;

/**
 * Toolbar action for saved search.
 * 
 * @author Jan Venstermans
 * 
 */
public class SavedSearchAction extends ToolbarAction implements ConfigurableAction {

	//private static final SearchAndFilterMessages MESSAGES = GWT.create(SearchAndFilterMessages.class);

	public static final String IDENTIFIER = "SavedSearch";

	public SavedSearchAction() {
		super(GsfLayout.iconSearchFree, "Saved Search", "Tooltip");
	}

	public void configure(String key, String value) {
		int i =5;
	}

	public void onClick(ClickEvent event) {
		int i =5;
		//SearchWidgetRegistry.getSearchWidgetInstance(GeometricSearchCreator.IDENTIFIER).showForSearch();
	}

}
