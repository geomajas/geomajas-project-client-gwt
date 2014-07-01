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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.steps;

import java.util.List;
import java.util.Map;

import com.google.web.bindery.event.shared.HandlerRegistration;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.NewLayerModelWizardWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.Wizard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.WizardStepPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DiscoveryCommService;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterCapabilitiesInfo;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedEvent;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedHandler;

/**
 * @author Jan De Moerloose
 */
public class WmsChooseLayerStep extends WizardStepPanel {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private static final String FLD_NAME = "name";

	private static final String FLD_CRS = "crs";

	private static final String FLD_DESC = "description";

	private static final String FLD_INFO = "info";

	private Map<String, String> connectionProps;

	private ListGrid grid;

	private Label warnings;

	private String previousStep = NewLayerModelWizardWindow.STEP_WMS_PROPS;

	private HandlerRegistration selectionUpdatedHandlerRegistration;

	public WmsChooseLayerStep(final Wizard parent) {
		super(NewLayerModelWizardWindow.STEP_WMS_CHOOSE_LAYER, 
			MESSAGES.wmsChooseLayerStepNumbering() + " "  + MESSAGES.wmsChooseLayerStepTitle(), 
				false, parent);
		setWindowTitle(MESSAGES.wmsChooseLayerStepTitle());

		grid = new ListGrid();
		grid.setWidth100();
		grid.setHeight("*");
		grid.setSelectionType(SelectionStyle.SINGLE);
		grid.setShowAllRecords(true);
		grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {

			public void onCellDoubleClick(CellDoubleClickEvent event) {
				parent.fireNextStepEvent();
			}
		});

		ListGridField nameFld = new ListGridField(FLD_NAME, MESSAGES.wmsChooseLayerStepName());
		nameFld.setType(ListGridFieldType.TEXT);
		nameFld.setWidth("*");

		ListGridField crsFld = new ListGridField(FLD_CRS, MESSAGES.wmsChooseLayerStepCRS());
		crsFld.setType(ListGridFieldType.TEXT);
		crsFld.setWidth(75);

		ListGridField descFld = new ListGridField(FLD_DESC, MESSAGES.wmsChooseLayerStepDescription());
		descFld.setType(ListGridFieldType.TEXT);
		descFld.setWidth("*");

		grid.setFields(nameFld, crsFld, descFld);
		grid.setCanResizeFields(true);

		// -------------------------------------------------

		warnings = new Label();
		warnings.setWidth100();
		warnings.setAutoHeight();
		warnings.setPadding(3);
		warnings.setOverflow(Overflow.VISIBLE);
		warnings.setVisible(false);
		warnings.setBackgroundColor("#FFCCCC");

		addMember(grid);
		addMember(warnings);
	}

	private void registerSelectionUpdateHandler(boolean register) {
		if (register) {
			selectionUpdatedHandlerRegistration = grid.addSelectionUpdatedHandler(new SelectionUpdatedHandler() {

				public void onSelectionUpdated(SelectionUpdatedEvent event) {
					fireChangedEvent();
				}
			});
		} else  {
			selectionUpdatedHandlerRegistration.removeHandler();
		}
	}

	@Override
	public void initialize() {
		if (connectionProps != null) {
			reset();
			registerSelectionUpdateHandler(true);
			grid.setShowEmptyMessage(true);
			grid.setEmptyMessage("<i>" + MESSAGES.requestingInfoFromServer() +
					" <img src='" + Geomajas.getIsomorphicDir() +
					"/images/circle.gif' style='height: 1em' /></i>");
			
			grid.redraw();
			DiscoveryCommService.getRasterCapabilities(connectionProps,
					new DataCallback<List<RasterCapabilitiesInfo>>() {

						public void execute(List<RasterCapabilitiesInfo> result) {
							//First add all map crs
							for (RasterCapabilitiesInfo wli : result) {
								ListGridRecord lgr = new ListGridRecord();
								lgr.setAttribute(FLD_NAME, wli.getName());
								lgr.setAttribute(FLD_CRS, wli.getCrs());
								lgr.setAttribute(FLD_DESC, wli.getDescription());
								lgr.setAttribute(FLD_INFO, wli);
								grid.addData(lgr);
							}
							//First add all other crs
						}
					}, new DataCallback<String>() {

						public void execute(String result) {
							reset();
							warnings.setVisible(true);
							warnings.setContents("<b><i>" + result + "</i></b>");
						}
					});
		}
	}

	public void setData(Map<String, String> connectionProps) {
		this.connectionProps = connectionProps;
	}

	@Override
	public boolean isValid() {
		return (grid.getSelectedRecord() != null);
	}

	@Override
	public String getNextStep() {
		return NewLayerModelWizardWindow.STEP_WMS_PREVIEW_LAYER;
	}

	@Override
	public String getPreviousStep() {
		return previousStep;
	}

	public void setPreviousStep(String previousStep) {
		this.previousStep = previousStep;
	}

	@Override
	public void reset() {
		grid.deselectAllRecords();
		grid.setData(new Record[] {});
		warnings.setVisible(false);
		warnings.setContents("");
	}

	@Override
	public void stepFinished() {
		final WmsPreviewLayerStep nextStep = (WmsPreviewLayerStep) parent
				.getStep(NewLayerModelWizardWindow.STEP_WMS_PREVIEW_LAYER);
		RasterCapabilitiesInfo info = (RasterCapabilitiesInfo) grid.getSelectedRecord().getAttributeAsObject(FLD_INFO);
		nextStep.setData(connectionProps, info);
		registerSelectionUpdateHandler(false);
	}
}
