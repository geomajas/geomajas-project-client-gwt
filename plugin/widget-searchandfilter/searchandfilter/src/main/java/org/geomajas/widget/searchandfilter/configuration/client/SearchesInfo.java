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
package org.geomajas.widget.searchandfilter.configuration.client;

import org.geomajas.configuration.client.ClientWidgetInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ClientWidgetInfo} object for saved searches.
 * This widget is a DTO object linked to a specific (vector) layer.
 * 
 * @author Jan Venstermans
 * 
 */
public class SearchesInfo implements ClientWidgetInfo {

	/**
	 * Use this identifier in your configuration files (beans).
	 */
	public static final String IDENTIFIER = "SearchesInfo";

	private static final long serialVersionUID = 100L;

	private List<SearchConfig> searchConfigs = new ArrayList<SearchConfig>();

	public List<SearchConfig> getSearchConfigs() {
		return searchConfigs;
	}

	public void setSearchConfigs(List<SearchConfig> searchConfigs) {
		this.searchConfigs = searchConfigs;
	}
}