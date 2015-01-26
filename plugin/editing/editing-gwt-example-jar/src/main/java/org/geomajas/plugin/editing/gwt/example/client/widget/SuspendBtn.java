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

package org.geomajas.plugin.editing.gwt.example.client.widget;

import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditService;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * A button that suspends/resumes the editing process.
 * 
 * @author Jan De Moerloose
 */
public class SuspendBtn extends ToolStripButton implements GeometryEditStartHandler,
		GeometryEditStopHandler {

	public SuspendBtn(final GeometryEditService service) {
		setIcon("[ISOMORPHIC]/geomajas/silk/control_pause.png");
		setIconSize(24);
		setHeight(32);
		setDisabled(true);
		setHoverWrap(false);
		setTooltip("Suspend the editing process");
		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (service.isStarted() && !service.isSuspended()) {
					service.suspend();
					setIcon("[ISOMORPHIC]/geomajas/silk/control_play.png");
					setTooltip("Resume the editing process");
				} else if (service.isSuspended()) {
					service.resume();
					setIcon("[ISOMORPHIC]/geomajas/silk/control_pause.png");
					setTooltip("Suspend the editing process");
				}
			}
		});
		service.addGeometryEditStartHandler(this);
		service.addGeometryEditStopHandler(this);
	}

	// ------------------------------------------------------------------------
	// GeometryEditWorkflowHandler implementation:
	// ------------------------------------------------------------------------

	public void onGeometryEditStart(GeometryEditStartEvent event) {
		setIcon("[ISOMORPHIC]/geomajas/silk/control_pause.png");
		setTooltip("Suspend the editing process");
		setDisabled(false);
	}

	public void onGeometryEditStop(GeometryEditStopEvent event) {
		setIcon("[ISOMORPHIC]/geomajas/silk/control_pause.png");
		setTooltip("Suspend the editing process");
		setDisabled(true);
	}

}