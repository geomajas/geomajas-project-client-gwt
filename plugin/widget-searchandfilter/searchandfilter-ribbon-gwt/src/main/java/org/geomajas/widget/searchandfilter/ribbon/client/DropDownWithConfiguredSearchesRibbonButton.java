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

import com.google.gwt.core.client.GWT;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.toolbar.ButtonGroup;
import org.geomajas.gwt.client.action.toolbar.DropDownButtonAction;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.action.ConfiguredSearchAction;
import org.geomajas.widget.searchandfilter.ribbon.client.i18n.SearchAndFilterRibbonMessages;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearch;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchesInfo;
import org.geomajas.widget.utility.gwt.client.action.ButtonAction;
import org.geomajas.widget.utility.gwt.client.action.ToolbarButtonAction;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonColumnRegistry;
import org.geomajas.widget.utility.gwt.client.ribbon.dropdown.DropDownRibbonButton;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Extension of {@link DropDownRibbonButton}: will show the configured list and will add the list of
 * saved searches for the layers of the map.
 * 
 * @author Jan Venstermans
 */
public class DropDownWithConfiguredSearchesRibbonButton extends DropDownRibbonButton
		implements MapModelChangedHandler {

	private MapWidget mapWidget;

	private static final SearchAndFilterRibbonMessages RIBBON_MESSAGES
			= GWT.create(SearchAndFilterRibbonMessages.class);

	public DropDownWithConfiguredSearchesRibbonButton(final DropDownButtonAction action,
													  List<ClientToolInfo> tools, MapWidget mapWidget) {
		this(action, 24, TitleAlignment.BOTTOM, tools, mapWidget);
	}

	public DropDownWithConfiguredSearchesRibbonButton(final DropDownButtonAction action, int iconSize,
													  TitleAlignment titleAlignment, List<ClientToolInfo> tools,
													  MapWidget mapWidget) {
		super(action, iconSize, titleAlignment, tools, mapWidget);
		this.mapWidget = mapWidget;
		mapWidget.getMapModel().addMapModelChangedHandler(this);
		addSavedSearches();
	}

	private void addSavedSearches() {
		if (mapWidget.getMapModel().isInitialized()) {
			Map<String, List<ConfiguredSearch>> searches = new LinkedHashMap<String, List<ConfiguredSearch>>();
			for (ClientLayerInfo clientLayerInfo : mapWidget.getMapModel().getMapInfo().getLayers()) {
				if (clientLayerInfo.getWidgetInfo().containsKey(ConfiguredSearchesInfo.IDENTIFIER)) {
					ConfiguredSearchesInfo searchesInfo = (ConfiguredSearchesInfo) clientLayerInfo.getWidgetInfo().
							get(ConfiguredSearchesInfo.IDENTIFIER);
					List<ConfiguredSearch> searchConfigs = searchesInfo.getSearchConfigs();
					if (searchConfigs != null && searchConfigs.size() > 0) {
						searches.put(clientLayerInfo.getId(), searchConfigs);
					}
				}
			}
			if (searches.size() > 0) {
				List<ButtonAction> actions = new ArrayList<ButtonAction>();
				for (Map.Entry<String, List<ConfiguredSearch>> entry : searches.entrySet()) {
					for (ConfiguredSearch searchConfig : entry.getValue()) {
						// only add configured searches that have at least one search attribute
						if (searchConfig.getAttributes() != null && searchConfig.getAttributes().size() > 0) {
							ClientToolInfo info = new ClientToolInfo();
							info.setId(ConfiguredSearchAction.IDENTIFIER);
							ButtonAction innerAction = RibbonColumnRegistry.getAction(info, mapWidget);
							((ConfiguredSearchAction) ((ToolbarButtonAction) innerAction).getToolbarAction()).
									setSearchConfig(searchConfig, entry.getKey());
							actions.add(innerAction);
						}
					}
				}
				// it is possible all searches don't have search attributes, so do extra check
				if (actions.size() > 0) {
					getPanel().addGroup(createGroup(), actions);
					// hard coded style names, that have been set before
					getPanel().setStyleName(getPanel().getStyleName());
				}
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
				buttonGroup.setTitle(RIBBON_MESSAGES.dropDownConfiguredSearchesGroupTitle());
				//TODO: make configurable?
				buttonGroup.configure("buttonLayout", "ICON_TITLE_AND_DESCRIPTION");
			}
		}
		return buttonGroup;
	}
}