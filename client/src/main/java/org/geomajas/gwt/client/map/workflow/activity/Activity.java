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

package org.geomajas.gwt.client.map.workflow.activity;

import org.geomajas.gwt.client.map.workflow.WorkflowContext;
import org.geomajas.gwt.client.map.workflow.WorkflowErrorHandler;
import org.geomajas.gwt.client.map.workflow.WorkflowException;

/**
 * <p>
 * Definition of a single activity/step in a workflow process.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface Activity {

	/**
	 * Get the error handler that is specifically tuned for the activity.
	 */
	WorkflowErrorHandler getErrorHandler();

	/**
	 * This method is called by the encompassing {@link org.geomajas.gwt.client.map.workflow.WorkflowProcessor} to
	 * execute the activity.
	 *
	 * @param context
	 *            The context object used in the workflow.
	 * @return Returns a (the same) context object. It's contents may have changed during this activity.
	 * @throws WorkflowException
	 *             In case something goes wrong, throw an exception.
	 */
	WorkflowContext execute(WorkflowContext context) throws WorkflowException;
}
