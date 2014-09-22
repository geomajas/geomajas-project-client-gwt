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
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.widget.layer.client.i18n.LayerMessages;
import org.geomajas.widget.layer.client.presenter.CreateClientWmsPresenter;

import java.util.List;

/**
 * @author Jan Venstermans
 */
public class SelectLayerViewImpl implements CreateClientWmsPresenter.SelectLayerView {

	protected static final LayerMessages MESSAGES = GWT.create(LayerMessages.class);

	/* grid fields */
	private static final String FLD_NAME = "name";
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

		ListGridField nameFld = new ListGridField(FLD_NAME,
				MESSAGES.layerListClientWmsWizardStepSelectLayerGridName());
		nameFld.setType(ListGridFieldType.TEXT);
		nameFld.setWidth("*");

		ListGridField descFld = new ListGridField(FLD_DESC,
				MESSAGES.layerListClientWmsWizardStepSelectLayerGridDescription());
		descFld.setType(ListGridFieldType.TEXT);
		descFld.setWidth("*");

		grid.setFields(nameFld, descFld);
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
	public boolean isValid() {
		return grid.getSelectedRecords().length == 1;
	}

	@Override
	public String getInvalidMessage() {
		return MESSAGES.layerListClientWmsWizardStepSelectLayerInvalidMessage();
	}

	@Override
	public void clear() {
		grid.clear();
	}

	@Override
	public void sendDataToHandler() {
		 handler.onFinishStepSelectLayer((WmsLayerInfo) grid.getSelectedRecord().getAttributeAsObject(FLD_LAYER));
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
			lgr.setAttribute(FLD_DESC, layerInfo.getTitle());
			lgr.setAttribute(FLD_LAYER, layerInfo);
			grid.addData(lgr);
		}
	}
}
