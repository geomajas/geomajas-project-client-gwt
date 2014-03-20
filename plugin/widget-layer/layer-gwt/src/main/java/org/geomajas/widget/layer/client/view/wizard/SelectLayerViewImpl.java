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
package org.geomajas.widget.layer.client.view.wizard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
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
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.widget.layer.client.i18n.LayerMessages;
import org.geomajas.widget.layer.client.presenter.CreateClientWmsPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan Venstermans
 */
public class SelectLayerViewImpl implements CreateClientWmsPresenter.SelectLayerView {

	protected static final LayerMessages MESSAGES = GWT.create(LayerMessages.class);

	/* grid fields */
	private static final String FLD_NAME = "name";
	private static final String FLD_CRS = "crs";
	private static final String FLD_DESC = "description";
	private static final String FLD_LAYER = "layer";

	private CreateClientWmsPresenter.SelectLayerHandler handler;

	private VLayout layout;
	private ListGrid grid;

	public SelectLayerViewImpl() {
		buildGui();
	}

	private void buildGui() {
		grid = new ListGrid();
		grid.setWidth100();
		grid.setHeight("*");
		grid.setSelectionType(SelectionStyle.SINGLE);
		grid.setShowAllRecords(true);
		grid.addSelectionUpdatedHandler(new SelectionUpdatedHandler() {

			public void onSelectionUpdated(SelectionUpdatedEvent event) {
				if (validate()) {
					sendDataToHandler();
				}
			}
		});
		/*grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {

			public void onCellDoubleClick(CellDoubleClickEvent event) {
				parent.fireNextStepEvent();
			}
		}); */

		ListGridField nameFld = new ListGridField(FLD_NAME,
				MESSAGES.layerListClientWmsWizardStepSelectLayerGridName());
		nameFld.setType(ListGridFieldType.TEXT);
		nameFld.setWidth("*");

		ListGridField crsFld = new ListGridField(FLD_CRS,
				MESSAGES.layerListClientWmsWizardStepSelectLayerGridCrs());
		crsFld.setType(ListGridFieldType.TEXT);
		crsFld.setWidth(75);

		ListGridField descFld = new ListGridField(FLD_DESC,
				MESSAGES.layerListClientWmsWizardStepSelectLayerGridDescription());
		descFld.setType(ListGridFieldType.TEXT);
		descFld.setWidth("*");

		grid.setFields(nameFld, crsFld, descFld);
		grid.setCanResizeFields(true);


		layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.addMember(grid);
	}

	@Override
	public Widget getWidget() {
		return layout;
	}

	@Override
	public String getTitle() {
		return MESSAGES.layerListClientWmsWizardStepSelectLayerWindowTitle();
	}

	@Override
	public boolean validate() {
		if (grid.getSelectedRecords().length == 1) {
			return true;
		}
		return false;
	}

	@Override
	public void sendDataToHandler() {
		 handler.onSelectLayer((WmsLayerInfo) grid.getSelectedRecord().getAttributeAsObject(FLD_LAYER));
	}

	@Override
	public void setSelectLayerFromCapabilitiesHandler(
			CreateClientWmsPresenter.SelectLayerHandler handler) {
		this.handler = handler;
	}

	@Override
	public void setWmsLayersData(List<WmsLayerInfo> wmsLayersData) {
		grid.deselectAllRecords();
		grid.setData(new ListGridRecord[]{});
		for (WmsLayerInfo layerInfo : wmsLayersData) {
			ListGridRecord lgr = new ListGridRecord();
			lgr.setAttribute(FLD_NAME, layerInfo.getName());
			lgr.setAttribute(FLD_CRS, listToCommaSeparatedString(layerInfo.getCrs()));
			lgr.setAttribute(FLD_DESC, layerInfo.getTitle());
			lgr.setAttribute(FLD_LAYER, layerInfo);
			grid.addData(lgr);
		}
	}

	private String listToCommaSeparatedString(List<String> list) {
		StringBuilder sb = new StringBuilder();

		for(String s: list) {
			sb.append(s).append(',');
		}

		sb.deleteCharAt(sb.length()-1); //delete last comma

		return sb.toString();
	}
}
