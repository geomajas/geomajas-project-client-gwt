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
package org.geomajas.widget.searchandfilter.editor.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.configuration.client.SearchesInfo;

/**
 * Panel to allow configuration of searches.
 * 
 * @author Jan Venstermans
 * 
 */
public class SearchConfigurationPanel extends VLayout {

	private static final SearchAndFilterMessages MESSAGES = GWT.create(SearchAndFilterMessages.class);

	private SearchesInfo searchesInfo;

	public SearchConfigurationPanel() {
		super(5);
		HLayout layout = new HLayout();
		layout.setHeight(10);
		addMember(layout);
	}

	public SearchesInfo getSearchConfig() {
		return searchesInfo;
	}

	public void setSearchConfig(SearchesInfo searchesInfo) {
		this.searchesInfo = searchesInfo;
	}
}
