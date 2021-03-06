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
package org.geomajas.widget.layer.client.view;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.DropCompleteEvent;
import com.smartgwt.client.widgets.events.DropCompleteHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.widget.layer.client.i18n.LayerMessages;
import org.geomajas.widget.layer.client.presenter.LayerListPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * A grid that displays a list of layers.
 * This default implementation has two columns: visibility and layer name.
 *
 * @author Jan Venstermans
 *
 */
public class LayerListGrid  extends ListGrid implements LayerListPresenter.View {

	protected static final LayerMessages MESSAGES = GWT.create(LayerMessages.class);

	private LayerListPresenter.Handler handler;

	public static final String FLD_VISIBILITY = "fldVisibility";
	private static final int FLD_VISIBILITY_WIDTH = 50;

	public static final String FLD_LABEL = "fldLabel";
	private static final int FLD_LABEL_WIDTH = 200;

	public static final String FLD_OBJECT = "object";

	public LayerListGrid() {
		super();
		/*grid config*/
		setEditEvent(ListGridEditEvent.CLICK);
		setEditByCell(true);
		setAllowFilterExpressions(false);
		setCanSort(false);
		setCanMultiSort(false);
		setWidth100();
		setHeight100();
		setAlternateRecordStyles(true);
		setSelectionType(SelectionStyle.SINGLE);
		setShowRollOverCanvas(true);
		setShowAllRecords(true);
		setShowRecordComponents(true);
		setShowRecordComponentsByCell(true);
		setShowHeader(false);

		setDragDataAction(DragDataAction.MOVE);
		setCanDragRecordsOut(false);

		/*columns*/
		List<ListGridField> fields = createAndGetListGridFields();
		setFields(fields.toArray(new ListGridField[fields.size()]));
	}

	/**
	 * Extensible method to create a list of {@link com.smartgwt.client.widgets.grid.ListGridField} objects.
	 * The order in which the fields are provided will be the order of the columns.
	 *
	 * @return
	 */
	protected List<ListGridField> createAndGetListGridFields() {
		List<ListGridField> fields = new ArrayList<ListGridField>();

		ListGridField labelFld = new ListGridField(FLD_LABEL,
				MESSAGES.layerListGridColumnLayerName(), FLD_LABEL_WIDTH);

		ListGridField visibilityFld = new ListGridField(FLD_VISIBILITY,
				MESSAGES.layerListGridColumnVisibilityName(), FLD_VISIBILITY_WIDTH);
		visibilityFld.setType(ListGridFieldType.IMAGE);
		visibilityFld.setAlign(Alignment.CENTER);
		addCellClickHandler(new CellClickHandler() {
			@Override
			public void onCellClick(CellClickEvent cellClickEvent) {
				if (cellClickEvent.getColNum() == getFieldNum(FLD_VISIBILITY)) {
					handler.onToggleVisibility((Layer) cellClickEvent.getRecord().getAttributeAsObject(FLD_OBJECT));
				}
			}
		});

		fields.add(visibilityFld);
		fields.add(labelFld);
		return fields;
	}

	public void fillGrid(List<Layer<?>> layers) {
		deselectAllRecords();
		setData(new ListGridRecord[]{});
		ListIterator layersIterator = layers.listIterator(layers.size());
		// fill
		while (layersIterator.hasPrevious()) {
			addRow((Layer) layersIterator.previous());
		}
	}

	public void addRow(Layer layer) {
		addData(createAndFillListGridRecord(layer));
	}

	/**
	 * Extensible method to fill a {@link com.smartgwt.client.widgets.grid.ListGridRecord}
	 * based on the Layer information.
	 *
	 * @param layer
	 * @return
	 */
	protected ListGridRecord createAndFillListGridRecord(Layer layer) {
		ListGridRecord record = new ListGridRecord();
		record.setAttribute(FLD_LABEL, layer.getLayerInfo().getLabel());
		record.setAttribute(FLD_VISIBILITY, layer.isShowing() ?
				WidgetLayout.iconLayerShow : WidgetLayout.iconLayerHide);
		record.setAttribute(FLD_OBJECT, layer);
		return record;
	}

	@Override
	public void setDragDropEnabled(boolean dragDropEnabled) {
		/* drag drop*/
		setCanAcceptDroppedRecords(dragDropEnabled);
		setCanReorderRecords(dragDropEnabled);
		addDropCompleteHandler(new DropCompleteHandler() {
			@Override
			public void onDropComplete(DropCompleteEvent dropCompleteEvent) {
				if (dropCompleteEvent.getTransferredRecords().length == 1) {
					ListGridRecord record = (ListGridRecord) dropCompleteEvent.getTransferredRecords()[0];
					int index = getRecordIndex(record);
					if (index >= 0) { // for some reason dropComplete is done twice, second time index = -1
						Layer layer = (Layer) record.getAttributeAsObject(FLD_OBJECT);
						int recordsAmount = getTotalRows();
						// return the index of the rendered list, not the inverse list as presented in the view
						handler.onMoveLayer(layer, recordsAmount - 1 - index);
					}
				}
			}
		});
	}

	@Override
	public void setHandler(LayerListPresenter.Handler handler) {
		this.handler = handler;
	}

	@Override
	public void updateView(List<Layer<?>> layers) {
		fillGrid(layers);
	}

	@Override
	public ListGrid getWidget() {
		return this;
	}
}
