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
package org.geomajas.plugin.deskmanager.client.gwt.manager.security.view;

import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDropEvent;
import com.smartgwt.client.widgets.grid.events.RecordDropHandler;

import java.util.ArrayList;
import java.util.List;

/**
 *  Extension of {@link com.smartgwt.client.widgets.grid.ListGrid} for showing a list of objects {@link T}.
 *  Only one String element of the object is shown, set via method {@link #getObjectName(Object)}.
 *
 * @param <T>
 * @author Jan Venstermans
 */
public abstract class SelectListGrid<T> extends ListGrid {

	public static final String FLD_OBJECT = "object";

	public static final String FLD_NAME = "name";

	private List<DropObjectsInSelectListGridHandler> registeredhandlers =
			new ArrayList<DropObjectsInSelectListGridHandler>();

	public SelectListGrid(String title, boolean editable) {
		super();
		setWidth100();
		setHeight100();

		setCanReorderRecords(true);
		setDragDataAction(DragDataAction.MOVE);
		setCanAcceptDroppedRecords(true);
		setCanDragRecordsOut(true);
		setSelectionType(SelectionStyle.MULTIPLE);
		setShowAllRecords(true);

		ListGridField nameFld = new ListGridField(FLD_NAME);
		nameFld.setWidth("*");
		nameFld.setTitle(title);
		setFields(nameFld);
		addRecordDropHandler(new RecordDropHandler() {
			@Override
			public void onRecordDrop(RecordDropEvent recordDropEvent) {
				ListGridRecord[] droppedRecords = recordDropEvent.getDropRecords();
				List<T> droppedObjects = toObjectList(droppedRecords);
				for (DropObjectsInSelectListGridHandler handler : registeredhandlers) {
					handler.onObjectsDropped(droppedObjects);
				}
				recordDropEvent.cancel();
			}
		});
	}

	public void addRecord(T group) {
		addData(toListGridRecord(group));
	}

	public List<T> getSelectedObjects() {
		ListGridRecord[] selectedRecords = getSelectedRecords();
		return toObjectList(selectedRecords);
	}

	private List<T> toObjectList(ListGridRecord[] listGridRecords) {
		if (listGridRecords != null && listGridRecords.length > 0) {
			List<T> objects = new ArrayList<T>();
			for (ListGridRecord record : listGridRecords) {
				objects.add((T) record.getAttributeAsObject(SelectListGrid.FLD_OBJECT));
			}
			return objects;
		}
		return null;
	}

	public void fillGrid(List<T> dataObjects) {
		deselectAllRecords();
		setData(new ListGridRecord[]{});
		// fill
		for (T object : dataObjects) {
			addData(toListGridRecord(object));
		}
	}

	private ListGridRecord toListGridRecord(T object) {
		ListGridRecord record = new ListGridRecord();
		record.setAttribute(FLD_OBJECT, object);
		record.setAttribute(FLD_NAME, getObjectName(object));
		return record;
	}

	public abstract String getObjectName(T object) ;

	public void addDropObjectsInSelectListGridHandler(DropObjectsInSelectListGridHandler<T> handler) {
		registeredhandlers.add(handler);
	}

	/**
	 * Handler for when a number of objects are dropped into a
	 * {@link org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.SelectListGrid<T>}.
	 *
	 * @author Jan Venstermans
	 * @param <T> Type of object that will be returned after drop.
	 */
	public interface DropObjectsInSelectListGridHandler<T> {

		void onObjectsDropped(List<T> objects);
	}
}
