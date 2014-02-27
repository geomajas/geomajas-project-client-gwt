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
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditor;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditorFactory;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.configuration.client.SearchesInfo;


/**
 * {@link WidgetEditorFactory} for the configuration of searches.
 * 
 * @author Jan Venstermans
 *
 */
public class SearchConfigurationEditorFactory implements WidgetEditorFactory {

	private static final SearchAndFilterMessages MESSAGES = GWT.create(SearchAndFilterMessages.class);

	@Override
	public WidgetEditor createEditor() {
		return new SearchConfigurationEditor();
	}

	@Override
	public String getKey() {
		return SearchesInfo.IDENTIFIER;
	}

	@Override
	public String getName() {
		return MESSAGES.detailTabSearches();
	}

}
