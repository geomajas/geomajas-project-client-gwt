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
package org.geomajas.plugin.deskmanager.client.gwt.common.editor.loadingscreen;

import com.google.gwt.core.client.GWT;
import org.geomajas.plugin.deskmanager.client.gwt.common.i18n.CommonMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditor;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditorFactory;


/**
 * Loading screen EditorFactory.
 *
 * @author Jan De Moerloose
 *
 */
public class LoadingScreenEditorFactory implements WidgetEditorFactory {

	private static final CommonMessages MESSAGES = GWT.create(CommonMessages.class);

	@Override
	public WidgetEditor createEditor() {
		return new LoadingScreenEditor();
	}

	@Override
	public String getKey() {
		return LoadingScreenInfo.IDENTIFIER;
	}

	@Override
	public String getName() {
		return MESSAGES.loadingScreenEditorName();
	}

}
