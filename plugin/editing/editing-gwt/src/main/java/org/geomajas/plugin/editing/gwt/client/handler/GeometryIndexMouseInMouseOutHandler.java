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

package org.geomajas.plugin.editing.gwt.client.handler;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.EventBus;
import org.geomajas.plugin.editing.client.handler.AbstractGeometryIndexMapHandler;
import org.geomajas.plugin.editing.gwt.client.event.GeometryIndexMouseOverOutEvent;

/**
 * Will throw specific event when moving mouse over and out an geometryIndex.
 * 
 * @author Jan Venstermans
 */
public class GeometryIndexMouseInMouseOutHandler extends AbstractGeometryIndexMapHandler implements MouseOverHandler,
		MouseOutHandler {

	private EventBus eventBus;

	public GeometryIndexMouseInMouseOutHandler(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void onMouseOver(MouseOverEvent event) {
		eventBus.fireEvent(new GeometryIndexMouseOverOutEvent(index
				, GeometryIndexMouseOverOutEvent.MouseTypeEvent.MOUSE_OVER));
	}

	public void onMouseOut(MouseOutEvent event) {
		eventBus.fireEvent(new GeometryIndexMouseOverOutEvent(index
				, GeometryIndexMouseOverOutEvent.MouseTypeEvent.MOUSE_OUT));
	}
}