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
package org.geomajas.widget.advancedviews.editor.client;

import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditor;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditorFactory;
import org.geomajas.widget.advancedviews.client.AdvancedViewsMessages;
import org.geomajas.widget.advancedviews.configuration.client.ThemesInfo;

import com.google.gwt.core.client.GWT;


/**
 * EditorFactory for the themeconfig configuration.
 * 
 * @author Oliver May
 *
 */
public class ThemeConfigurationEditorFactory implements WidgetEditorFactory {

	private static final AdvancedViewsMessages MESSAGES = GWT.create(AdvancedViewsMessages.class);

	@Override
	public WidgetEditor createEditor() {
		return new ThemeConfigurationEditor();
	}

	@Override
	public String getKey() {
		return ThemesInfo.IDENTIFIER;
	}

	@Override
	public String getName() {
		return MESSAGES.detailTabThemes();
	}

}
