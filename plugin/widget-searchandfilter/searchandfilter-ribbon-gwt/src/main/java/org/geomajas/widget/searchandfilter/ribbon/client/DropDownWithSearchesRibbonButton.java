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

import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.toolbar.ButtonGroup;
import org.geomajas.gwt.client.action.toolbar.DropDownButtonAction;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.configuration.client.SearchConfig;
import org.geomajas.widget.searchandfilter.configuration.client.SearchesInfo;
import org.geomajas.widget.searchandfilter.ribbon.client.action.SavedSearchAction;
import org.geomajas.widget.utility.gwt.client.action.ButtonAction;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonColumnRegistry;
import org.geomajas.widget.utility.gwt.client.ribbon.dropdown.DropDownRibbonButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Extension of {@link DropDownRibbonButton}: will show the configured list and will add the list of
 * saved searches for the layers of the map.
 * 
 * @author Jan Venstermans
 */
public class DropDownWithSearchesRibbonButton extends DropDownRibbonButton implements MapModelChangedHandler {

	private MapWidget mapWidget;

	public DropDownWithSearchesRibbonButton(final DropDownButtonAction action, List<ClientToolInfo> tools, MapWidget mapWidget) {
		this(action, 24, TitleAlignment.BOTTOM, tools, mapWidget);
	}

	public DropDownWithSearchesRibbonButton(final DropDownButtonAction action, int iconSize,
								TitleAlignment titleAlignment, List<ClientToolInfo> tools, MapWidget mapWidget) {
		super(action, iconSize, titleAlignment, tools, mapWidget);
		this.mapWidget = mapWidget;
		mapWidget.getMapModel().addMapModelChangedHandler(this);
		addSavedSearches();
	}

	private void addSavedSearches() {
		if (mapWidget.getMapModel().isInitialized()) {
			Map<String, List<SearchConfig>> searches = new LinkedHashMap<String, List<SearchConfig>>();
			for (ClientLayerInfo clientLayerInfo : mapWidget.getMapModel().getMapInfo().getLayers()) {
				if (clientLayerInfo.getWidgetInfo().containsKey(SearchesInfo.IDENTIFIER)) {
					SearchesInfo searchesInfo = (SearchesInfo) clientLayerInfo.getWidgetInfo().get(SearchesInfo.IDENTIFIER);
					List<SearchConfig> searchConfigs = searchesInfo.getSearchConfigs();
					if (searchConfigs != null && searchConfigs.size() > 0) {
						searches.put(clientLayerInfo.getId(), searchConfigs);
					}
				}
			}
			if (searches.size() > 0) {
				/* define group*/
				//ToolbarBaseAction group = ToolbarRegistry.getToolbarAction("ButtonGroup", mapWidget);
				//group.setTitle("Saved Searches");
				//group.configure("buttonLayout","ICON_TITLE_AND_DESCRIPTION");
						/* fill with actions */
				//List<ClientToolInfo> clientToolInfos = new ArrayList<ClientToolInfo>();
				List<ButtonAction> actions = new ArrayList<ButtonAction>();
				for (Map.Entry<String, List<SearchConfig>> entry : searches.entrySet()) {
					for (SearchConfig searchConfig : entry.getValue()) {
						ClientToolInfo info = new ClientToolInfo();
						info.setTitle(searchConfig.getTitle());
						info.setDescription(searchConfig.getDescription());
						info.setIcon(searchConfig.getIconUrl());
						info.setId(SavedSearchAction.IDENTIFIER);
						ButtonAction innerAction = RibbonColumnRegistry.getAction(info, mapWidget);
						actions.add(innerAction);
					}
				}
				getPanel().addGroup(createGroup(), actions);
				// hard coded style names, that have been set before
				getPanel().setStyleName(getPanel().getStyleName());
			}
		}
	}

	@Override
	public void onMapModelChanged(MapModelChangedEvent event) {
		addSavedSearches();
	}

	private ButtonGroup createGroup() {
		ButtonGroup buttonGroup = null;
		ToolbarBaseAction toolbarAction = ToolbarRegistry.getToolbarAction("ButtonGroup", mapWidget);
		if (toolbarAction != null) {
			if (toolbarAction instanceof ButtonGroup) {
				buttonGroup = (ButtonGroup) toolbarAction;
				buttonGroup.setTitle("Saved Searches");
				//TODO: make configurable?
				buttonGroup.configure("buttonLayout", "ICON_TITLE_AND_DESCRIPTION");
			}
		}
		return buttonGroup;
	}
}