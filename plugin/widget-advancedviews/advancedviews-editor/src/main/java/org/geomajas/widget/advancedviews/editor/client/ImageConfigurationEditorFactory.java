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

import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditor;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditorFactory;
import org.geomajas.widget.advancedviews.client.AdvancedViewsMessages;
import org.geomajas.widget.advancedviews.configuration.client.ImageInfo;

import com.google.gwt.core.client.GWT;

/**
 * Factory for {@link ImageConfigurationEditor}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ImageConfigurationEditorFactory implements WidgetEditorFactory {

	private static final AdvancedViewsMessages MESSAGES = GWT.create(AdvancedViewsMessages.class);

	@Override
	public WidgetEditor createEditor() {
		return new ImageConfigurationEditor();
	}

	@Override
	public String getKey() {
		return ImageInfo.IDENTIFIER;
	}

	@Override
	public String getName() {
		return MESSAGES.imageConfigurationEditorName();
	}

}
