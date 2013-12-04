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

import com.google.gwt.event.shared.EventBus;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.gwt.client.event.InfoDragLineChangedEvent;

/**
 * Editing handler that shows a draggable/closeable window with geometry information.
 * 
 * @author Jan De Moerloose
 * 
 */
public class InfoDragLineHandler extends BaseDragLineHandler implements GeometryEditStartHandler,
		GeometryEditStopHandler {

	private MapWidget map;

	private EventBus eventBus;

	/**
	 * Construct a handler.
	 * 
	 * @param map the map
	 * @param editService the editing service
	 */
	public InfoDragLineHandler(MapWidget map, GeometryEditService editService, EventBus eventBus) {
		super(editService);
		this.map = map;
		this.eventBus = eventBus;
	}

	public void register() {
		super.register();
		registrations.add(editService.addGeometryEditStartHandler(this));
		registrations.add(editService.addGeometryEditStopHandler(this));
		// show initial state
		onDrag(null, null, null);
	}
	
	public void unregister() {
		super.unregister();
	}

	@Override
	protected void onDrag(Coordinate dragPoint, Coordinate startA, Coordinate startB) {
		eventBus.fireEvent(new InfoDragLineChangedEvent(editService.getGeometry(), dragPoint, startA, startB));
	}

	@Override
	protected void onDragStopped() {
		eventBus.fireEvent(new InfoDragLineChangedEvent(editService.getGeometry(), null, null, null));
	}

	@Override
	public void onGeometryEditStop(GeometryEditStopEvent event) {
	}

	@Override
	public void onGeometryEditStart(GeometryEditStartEvent event) {
		// show initial state
		onDrag(null, null, null);
	}

	protected double length(Coordinate[] edge) {
		return Math.hypot(edge[1].getX() - edge[0].getX(), edge[1].getY() - edge[0].getY());
	}

}
