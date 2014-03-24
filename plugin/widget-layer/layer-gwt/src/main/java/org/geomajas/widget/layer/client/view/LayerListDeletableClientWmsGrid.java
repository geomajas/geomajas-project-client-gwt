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
package org.geomajas.widget.layer.client.view;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import org.geomajas.gwt.client.map.layer.InternalClientWmsLayer;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.widget.layer.client.presenter.LayerListClientWmsPresenter;

import java.util.List;

/**
 * Extension of {@link LayerListGrid} to enable removing client wms layers.
 * 
 * @author Jan Venstermans
 * 
 */
public class LayerListDeletableClientWmsGrid extends LayerListGrid implements LayerListClientWmsPresenter.View {

	public static final String FLD_DELETE = "fldDelete";
	private static final int FLD_DELETE_WIDTH = 50;

	private LayerListClientWmsPresenter.Handler handler;

	@Override
	public void setHandler(LayerListClientWmsPresenter.Handler handler) {
		super.setHandler(handler);
		this.handler = handler;
	}

	@Override
	protected List<ListGridField> createAndGetListGridFields() {
		List<ListGridField> fields = super.createAndGetListGridFields();

		ListGridField deleteFld = new ListGridField(FLD_DELETE,
				MESSAGES.layerListGridClientWmsColumnDeleteName(), FLD_DELETE_WIDTH);
		deleteFld.setType(ListGridFieldType.IMAGE);
		deleteFld.setAlign(Alignment.CENTER);
		addCellClickHandler(new CellClickHandler() {
			@Override
			public void onCellClick(CellClickEvent cellClickEvent) {
				if (cellClickEvent.getColNum() == getFieldNum(FLD_DELETE)) {
					Object layer = cellClickEvent.getRecord().getAttributeAsObject(FLD_OBJECT);
					if (layer instanceof InternalClientWmsLayer) {
						handler.onRemoveClientWmsLayer((InternalClientWmsLayer) layer);
					}
				}
			}
		});
		fields.add(deleteFld);
		return fields;
	}

	@Override
	protected ListGridRecord createAndFillListGridRecord(Layer layer) {
		ListGridRecord record = super.createAndFillListGridRecord(layer);
		if (layer instanceof InternalClientWmsLayer) {
			record.setAttribute(FLD_DELETE, WidgetLayout.iconRemove);
		}
		return record;
	}
}
