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

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditor;
import org.geomajas.widget.searchandfilter.configuration.client.SearchesInfo;

/**
 * Editor for the searches configuration.
 * Actualy a wrapper around {@link org.geomajas.widget.searchandfilter.editor.client.SearchConfigurationPanel}.
 *
 * @author Jan Venstermans
 *
 */
public class SearchConfigurationEditor implements WidgetEditor {

	private SearchConfigurationPanel panel;

	private VLayout layout;

	public SearchConfigurationEditor() {
		panel = new SearchConfigurationPanel();

		layout = new VLayout();
		layout.addMember(panel);
		layout.setOverflow(Overflow.AUTO);
	}

	@Override
	public Canvas getCanvas() {
		return layout;
	}

	@Override
	public ClientWidgetInfo getWidgetConfiguration() {
		return panel.getSearchConfig();
	}

	@Override
	public void setWidgetConfiguration(ClientWidgetInfo configuration) {
		if (configuration == null) {
//			panel.setSearchConfig(new SearchesInfo());
		} else if (configuration instanceof SearchesInfo) {
			panel.setSearchConfig((SearchesInfo) configuration);
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void setDisabled(boolean disabled) {
		panel.setDisabled(disabled);
	}
}
