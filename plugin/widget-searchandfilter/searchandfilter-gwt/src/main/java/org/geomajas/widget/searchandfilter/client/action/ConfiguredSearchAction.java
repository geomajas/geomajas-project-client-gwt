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
package org.geomajas.widget.searchandfilter.client.action;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.events.ClickEvent;
import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.util.GsfLayout;
import org.geomajas.widget.searchandfilter.client.widget.configuredsearch.ConfiguredSearchCreator;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidgetRegistry;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearch;

/**
 * Toolbar action for configured searches.
 * 
 * @author Jan Venstermans
 * 
 */
public class ConfiguredSearchAction extends ToolbarAction implements ConfigurableAction {

	private static final SearchAndFilterMessages MESSAGES = GWT.create(SearchAndFilterMessages.class);

	public static final String IDENTIFIER = ConfiguredSearchCreator.IDENTIFIER;

	private ConfiguredSearch searchConfig;

	private String layerId;

	/**
	 * Default constructor. This will create an action with some default values of icon, title and tooltip.
	 * These values should be substituted by setting the {@link org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearch} object for this action, using
	 * {@link ConfiguredSearchAction#setSearchConfig(
	 *org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearch)} method.
	 */
	public ConfiguredSearchAction() {
		super(GsfLayout.iconSearchFree, MESSAGES.searchConfiguredTitleDefault(),
				MESSAGES.searchConfiguredTooltipDefault());
	}

	public ConfiguredSearch getSearchConfig() {
		return searchConfig;
	}

	public void setSearchConfig(ConfiguredSearch searchConfig, String layerId) {
		this.searchConfig = searchConfig;
		this.layerId = layerId;
		if (searchConfig.getIconUrl() != null) {
			super.setIcon(searchConfig.getIconUrl());
		}
		super.setTitle(searchConfig.getTitle());
		super.setTooltip(searchConfig.getDescription());
	}

	public void configure(String key, String value) {
	}

	public void onClick(ClickEvent event) {
		SearchWidget searchWidget = SearchWidgetRegistry.getSearchWidgetInstance(ConfiguredSearchCreator.IDENTIFIER);
		if (searchWidget instanceof ConfiguredSearchCreator.DockableWindowConfiguredSearchWidget) {
			ConfiguredSearchCreator.DockableWindowConfiguredSearchWidget window =
					(ConfiguredSearchCreator.DockableWindowConfiguredSearchWidget) searchWidget;
			window.setSearchConfig(searchConfig, layerId);
			window.showForSearch();
		} else {
			// show nothing; or show exception?
		}
	}

}
