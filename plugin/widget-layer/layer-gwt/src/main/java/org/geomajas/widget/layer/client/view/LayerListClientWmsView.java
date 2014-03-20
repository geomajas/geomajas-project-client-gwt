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
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.map.layer.InternalClientWmsLayer;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.widget.layer.client.presenter.LayerListClientWmsPresenter;

import java.util.List;

/**
 * Default implementation of {LayerListPresenter.ControllerButtonsView}.
 * 
 * @author Jan Venstermans
 * 
 */
public class LayerListClientWmsView extends LayerListView implements LayerListClientWmsPresenter.View {

	private LayerListClientWmsPresenter.Handler handler;

	@Override
	protected LayersGrid createLayersGrid() {
		return new LayersGridClientWms();
	}

	protected void buildGui() {
		VLayout layout = new VLayout();
		layout.setWidth(400);
		layout.setHeight(300);

		Layout addImgContainer = new Layout();
		addImgContainer.setWidth(64 + 16); //16 from scroller in grid
		addImgContainer.setAlign(Alignment.CENTER);
		addImgContainer.setHeight(16);
		addImgContainer.setLayoutAlign(Alignment.RIGHT);

		ImgButton addImg = new ImgButton();
		addImg.setSrc(WidgetLayout.iconAdd);
		addImg.setShowDown(false);
		addImg.setShowRollOver(false);
		addImg.setPrompt(MESSAGES.layerListClientWmsAddLayerButton());
		addImg.setHeight(16);
		addImg.setWidth(16);
		addImg.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				handler.onAddClientWmsLayer();
			}
		});
		addImgContainer.addMember(addImg);

		layout.addMember(addImgContainer);
		layout.addMember(grid);
		layout.setBorder("1px solid");
		addChild(layout);
	}

	@Override
	public void setHandler(LayerListClientWmsPresenter.Handler handler) {
		super.setHandler(handler);
		this.handler = handler;
	}

	/**
	 * Extension of {@link LayersGrid} for enable removing client wms layers.
	 */
	public class LayersGridClientWms extends LayersGrid {

		public static final String FLD_DELETE = "fldDelete";
		private static final int FLD_DELETE_WIDTH = 50;

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
}
