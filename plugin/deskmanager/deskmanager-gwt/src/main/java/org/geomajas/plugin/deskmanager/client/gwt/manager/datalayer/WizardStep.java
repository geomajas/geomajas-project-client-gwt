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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer;

/**
 * Interface for common methods of the steps of the Add Layer Wizard.
 *
 * @author Jan Venstermans
 */
public interface WizardStep {

	boolean isValid();

	String getNextStep();

	String getPreviousStep();

	void reset();

	/**
	 * Called when user selects [next]. Prepare data for the next step here.
	 */
	boolean stepFinished();

	/* getters and setters */

	String getName();

	void setName(String name);

	String getTitle();

	boolean isLastStep();

	void setLastStep(boolean lastStep);

	String getWindowTitle();

	void setWindowTitle(String windowTitle);
}
