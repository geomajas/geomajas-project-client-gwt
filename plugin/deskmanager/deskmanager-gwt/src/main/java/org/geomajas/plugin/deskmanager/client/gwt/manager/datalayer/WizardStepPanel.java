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

import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 * @author Jan Venstermans
 */
public abstract class WizardStepPanel extends VLayout implements WizardStep {

	protected String name;

	protected String title;

	protected boolean lastStep;

	protected String windowTitle;

	protected Wizard parent;

	public WizardStepPanel(String name, String title, boolean isLastStep, Wizard parent) {
		super();
		if (parent == null) {
			throw new IllegalArgumentException("Need a parent!!");
		}
		this.name = name;
		this.title = title;
		this.parent = parent;
		this.lastStep = isLastStep;
		this.setPadding(10);
		this.setIsGroup(true);
		this.setGroupTitle(title);
		this.setWidth100();
		this.setHeight100();
	}

	/**
	 * only called on nextstep, not on previousstep, to prevent reloading of data.
	 */
	public void initialize() {
	}

	// -------------------------------------------------
	// getters and setters
	// -------------------------------------------------

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public boolean isLastStep() {
		return lastStep;
	}

	@Override
	public void setLastStep(boolean lastStep) {
		this.lastStep = lastStep;
	}

	@Override
	public String getWindowTitle() {
		return windowTitle;
	}

	@Override
	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}

	// -------------------------------------------------

	protected void fireChangedEvent() {
		parent.onChanged(this);
	}

}
