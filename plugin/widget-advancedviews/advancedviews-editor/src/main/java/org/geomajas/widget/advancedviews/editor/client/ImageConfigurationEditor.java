/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.advancedviews.editor.client;

import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditor;
import org.geomajas.widget.advancedviews.client.AdvancedViewsMessages;
import org.geomajas.widget.advancedviews.configuration.client.ImageInfo;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Editor for {@link ImageInfo}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ImageConfigurationEditor implements WidgetEditor {

	private static final AdvancedViewsMessages MESSAGES = GWT.create(AdvancedViewsMessages.class);

	private ImageConfigPanel panel;

	private VLayout layout;

	public ImageConfigurationEditor() {
		panel = new ImageConfigPanel();
		layout = new VLayout();
		layout.setPadding(10);
		layout.setIsGroup(true);
		layout.setGroupTitle(MESSAGES.imageConfigGroupTitle());
		layout.addMember(panel);
		layout.setOverflow(Overflow.AUTO);
	}

	@Override
	public Canvas getCanvas() {
		return layout;
	}

	@Override
	public ClientWidgetInfo getWidgetConfiguration() {
		return panel.getLogoInfo();
	}

	@Override
	public void setWidgetConfiguration(ClientWidgetInfo configuration) {
		if (configuration != null && configuration instanceof ImageInfo) {
			panel.setLogoInfo((ImageInfo) configuration);
		} else {
			panel.setLogoInfo(new ImageInfo());
		}
	}

	@Override
	public void setDisabled(boolean disabled) {
		panel.setDisabled(disabled);
	}

	protected ImageConfigPanel getPanel() {
		return panel;
	}
}
