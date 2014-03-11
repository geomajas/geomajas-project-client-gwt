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
package org.geomajas.widget.searchandfilter.client.widget.configuredsearch;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.widget.search.DockableWindowSearchWidget;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidgetCreator;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearch;

/**
 * Creator (or factory) pattern needed because GWT cannot instantiate classes
 * starting from name (as String).
 * 
 * @author Kristof Heirwegh
 */
public class ConfiguredSearchCreator implements SearchWidgetCreator {

	public static final String IDENTIFIER = "ConfiguredSearch";

	//private final SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	public ConfiguredSearchCreator() {
	}

	public String getSearchWidgetId() {
		return IDENTIFIER;
	}

	public String getSearchWidgetName() {
		//return messages.configuredSearchWidgetTitle();
		return "Configured Search Creator"; //TODO put this test value in messages
	}

	public SearchWidget createInstance(MapWidget mapWidget) {
		return new DockableWindowConfiguredSearchWidget(IDENTIFIER,
				getSearchWidgetName(), new ConfiguredSearchPanel2(mapWidget));
	}

	/**
	 * Extention of {@link DockableWindowSearchWidget} for passing searchConfig.
	 */
	public class DockableWindowConfiguredSearchWidget extends DockableWindowSearchWidget {

		private ConfiguredSearchPanel2 configuredSearchPanel;

		/**
		 * @param widgetId    needed to find the widget in the SearchWidgetRegistry.
		 * @param name        name of the widget to show in window title and on buttons.
		 * @param configuredSearchPanel
		 */
		public DockableWindowConfiguredSearchWidget(String widgetId, String name,
													ConfiguredSearchPanel2 configuredSearchPanel) {
			super(widgetId, name, configuredSearchPanel);
			this.configuredSearchPanel = configuredSearchPanel;
		}

		public void setSearchConfig(ConfiguredSearch searchConfig, String layerId) {
			configuredSearchPanel.setSearchConfig(searchConfig, layerId);
			setTitle(searchConfig.getTitleInWindow());
		}
	}
}
