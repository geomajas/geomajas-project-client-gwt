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
package org.geomajas.widget.layer.client.widget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.widget.MapWidget;

import java.util.List;

/**
 * Opens panel in closeableDialog.
 * 
 * @author Jan Venstermans
 * 
 */
public class LayersManagementWidget extends Canvas {

	private MapWidget mapWidget;

	public LayersManagementWidget(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
		if (mapWidget.getMapModel().isInitialized()) {

		} else {
			mapWidget.getMapModel().addMapModelChangedHandler(new MapModelChangedHandler() {
				@Override
				public void onMapModelChanged(MapModelChangedEvent event) {
					buildUi();
				}
			});
		}
	}

	private void buildUi() {
		List<Layer<?>> layers = mapWidget.getMapModel().getLayers();
		addChild(new Label("Number of layers " + layers.size()));
	}

}
