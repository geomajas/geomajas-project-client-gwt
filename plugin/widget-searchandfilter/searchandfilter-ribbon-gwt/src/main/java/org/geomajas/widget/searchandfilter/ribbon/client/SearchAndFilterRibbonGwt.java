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

package org.geomajas.widget.searchandfilter.ribbon.client;

import com.google.gwt.core.client.EntryPoint;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.gwt.client.action.ToolCreator;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.toolbar.DropDownButtonAction;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.configuration.client.SearchesInfo;
import org.geomajas.widget.searchandfilter.ribbon.client.DropDownWithSearchesRibbonButton;
import org.geomajas.widget.searchandfilter.ribbon.client.action.SavedSearchAction;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonColumnRegistry;
import org.geomajas.widget.utility.gwt.client.ribbon.dropdown.DropDownRibbonButton;

import java.util.List;

/**
 * Extension of {@link org.geomajas.widget.utility.gwt.client.ribbon.dropdown.DropDownRibbonButton}: will show the configured list and will add the list of
 * saved searches for the layers of the map.
 *
 * @author Jan Venstermans
 */
public class SearchAndFilterRibbonGwt implements EntryPoint {


	@Override
	public void onModuleLoad() {
		RibbonColumnRegistry.put("ToolbarDropDownAndSearchesButton", new RibbonColumnRegistry.RibbonColumnCreator() {

			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				return new DropDownWithSearchesRibbonButton(new DropDownButtonAction(), tools, mapWidget);
			}
		});

		ToolbarRegistry.put(SavedSearchAction.IDENTIFIER, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new SavedSearchAction();
			}
		});
	}
}