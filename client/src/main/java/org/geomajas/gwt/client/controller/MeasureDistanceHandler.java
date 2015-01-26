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
package org.geomajas.gwt.client.controller;

/**
 * Handles measure distance context information, e.g. shows it in a label.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface MeasureDistanceHandler {

	/**
	 * State of the event that triggers {@link
	 * #onChange(org.geomajas.gwt.client.controller.MeasureDistanceHandler.State, MeasureDistanceContext)}.
	 */
	enum State {
		/** user starts clicking. */
		START,
		/** user moves mouse. */
		MOVE,
		/** user clicks another point. */
		CLICK,
		/** user stops by double-clicking. */
		STOP
	}

	/**
	 * Called when the user interacts with the map.
	 * 
	 * @param state interaction state
	 * @param context contextual info about distances measured
	 */
	void onChange(State state, MeasureDistanceContext context);

}
