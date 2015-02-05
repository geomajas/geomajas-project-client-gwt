/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client.action.layertree;

import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.layer.Layer;

import com.smartgwt.client.util.SC;
import org.geomajas.gwt.client.util.WidgetLayout;

/**
 * An optional action for the LayerTree, that displays a help overview of how to use the LayerTree.
 *
 * @author Pieter De Graef
 */
public class LayerTreeHelpAction extends LayerTreeAction {

	public LayerTreeHelpAction() {
		super(WidgetLayout.iconHelpContents, I18nProvider.getLayerTree().layerTreeHelp(),
				WidgetLayout.iconHelpContents);
	}

	public void onClick(Layer<?> layer) {
		SC.say("I've got something to say.....");
	}

	public boolean isEnabled(Layer<?> layer) {
		return true;
	}
}
