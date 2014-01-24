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
package org.geomajas.plugin.editing.gwt.client.event;

import com.google.web.bindery.event.shared.Event;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;

/**
 * Editing handler that shows a draggable/closeable window with geometry information.
 * 
 * @author Jan Venstermans
 * 
 */
public class InfoDragLineChangedEvent extends Event<InfoDragLineChangedEvent.Handler> {

	private Geometry geometry;

	private Coordinate dragPoint, startA, startB;

	/**
	 * Handler for this event.
	 *
	 * @author Jan Venstermans
	 *
	 */
	public interface Handler {

		/**
		 * Notifies change of geometry.
		 *
		 * @param event the event
		 */
		void onChanged(InfoDragLineChangedEvent event);

	}

	public InfoDragLineChangedEvent(Geometry geometry, Coordinate dragPoint, Coordinate startA, Coordinate startB) {
		this.geometry = geometry;
		this.dragPoint = dragPoint;
		this.startA = startA;
		this.startB = startB;
	}

	private static final Type<Handler> TYPE = new Type<Handler>();

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onChanged(this);
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "InfoDragLineChangedEvent[" + "]";
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public Coordinate getDragPoint() {
		return dragPoint;
	}

	public Coordinate getStartA() {
		return startA;
	}

	public Coordinate getStartB() {
		return startB;
	}

	public static Type<Handler> getType() {
		return TYPE;
	}
}
