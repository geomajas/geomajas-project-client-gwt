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
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

/**
 * Event thrown when mouse over / mouse out of a vertex.
 * 
 * @author Jan Venstermans
 * 
 */
public class GeometryIndexMouseOverOutEvent extends Event<GeometryIndexMouseOverOutEvent.Handler> {

	private MouseTypeEvent type;

	private GeometryIndex index, vertexOrEdgeIndex;

	/**
	 * Handler for this event.
	 *
	 * @author Jan Venstermans
	 *
	 */
	public interface Handler {

		/**
		 * Notifies mouse over vertex.
		 *
		 * @param event the event
		 */
		void onMouseOverGeometryIndex(GeometryIndexMouseOverOutEvent event);

		/**
		 * Notifies mouse out vertex.
		 *
		 * @param event the event
		 */
		void onMouseOutGeometryIndex(GeometryIndexMouseOverOutEvent event);

	}

	/**
	 * Enum to distinguish between mouse over and mouse out.
	 *
	 * @author Jan Venstermans
	 *
	 */
	public enum MouseTypeEvent {
		MOUSE_OVER, MOUSE_OUT;
	}

	public GeometryIndexMouseOverOutEvent(GeometryIndex index, MouseTypeEvent type) {
		this.index = index;
		this.type = type;
		vertexOrEdgeIndex = index;
		while (vertexOrEdgeIndex.getType().equals(GeometryIndexType.TYPE_GEOMETRY)) {
			vertexOrEdgeIndex = vertexOrEdgeIndex.getChild();
		}
	}

	private static final Type<Handler> TYPE = new Type<Handler>();

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		switch (type) {
			case MOUSE_OVER:
				handler.onMouseOverGeometryIndex(this);
				break;
			case MOUSE_OUT:
				handler.onMouseOutGeometryIndex(this);
				break;
		}
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
		return "GeometryIndexMouseOverOutEvent[" + "]";
	}

	public static Type<Handler> getType() {
		return TYPE;
	}

	public GeometryIndex getIndex() {
		return index;
	}

	public GeometryIndex getVertexOrEdgeIndex() {
		return vertexOrEdgeIndex;
	}
}
