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
package org.geomajas.widget.featureinfo.client.widget.factory;

import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.widget.featureinfo.client.widget.DockableWindow;

import java.util.Map;
import java.util.Map.Entry;


/**
 * <p>
 * The <code>RasterLayerAttriuteWindow</code> is a floating window that displays raster layer feature information
 * using a {@link ListGrid}.
 * </p>
 * 
 * @author Oliver May
 *
 */
public class RasterLayerAttributeWindow extends DockableWindow {

	private static final String TEXT_ATTRIBUTE = "wmsStringAttribute";
	private static final String HTML_ATTRIBUTE = "wmsHtmlAttribute";

	/**
	 * @param rasterFeature
	 */
	public RasterLayerAttributeWindow(Feature rasterFeature) {
		super();
		setWidth(300);
		setHeight(200);
		setCanDragReposition(true);
		setCanDragResize(true);
		setOverflow(Overflow.AUTO);
		setKeepInParentRect(true);
		setTitle("Object Detail - " + rasterFeature.getLabel());
		addItem(new RasterLayerAttributeCanvas(rasterFeature));
	}
	
	/**
	 * Helper canvas that contains the attribute grid.
	 * @author Oliver May
	 *
	 */
	private class RasterLayerAttributeCanvas extends VLayout {
		
		public RasterLayerAttributeCanvas(final Feature feature) {
			super();

			if (feature.getAttributes().size() == 1 && feature.getAttributes().containsKey(HTML_ATTRIBUTE)) {
				renderHtmlAttribute(feature.getAttributes().get(HTML_ATTRIBUTE));
			} else if (feature.getAttributes().size() == 1 && feature.getAttributes().containsKey(TEXT_ATTRIBUTE)) {
				renderHtmlAttribute(feature.getAttributes().get(TEXT_ATTRIBUTE));
			} else {
				renderAttributes(feature.getAttributes());
			}
		}

		private void renderHtmlAttribute(Attribute feature) {
			HTMLPane pane = new HTMLPane();
			pane.setContentsType(ContentsType.PAGE);
			pane.setContents(feature.getValue().toString());
			addMember(pane);
		}

		private void renderAttributes(Map<String, Attribute> attributes) {
			ListGrid grid = new ListGrid();

			grid.setCanEdit(false);
			grid.setShowSelectedStyle(false);
			grid.setShowRollOver(false);
			grid.setShowHeader(false);

			grid.setShowRecordComponents(true);
			grid.setShowRecordComponentsByCell(true);
			grid.setShowAllRecords(true);
			grid.setLeaveScrollbarGap(false);

			ListGridField keyField = new ListGridField("keyField");
			ListGridField valueField = new ListGridField("valueField");

			grid.setFields(keyField, valueField);

			final RecordList recordList = new RecordList();

			for (Entry<String, Attribute> entry : attributes.entrySet()) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute("keyField", entry.getKey());
				record.setAttribute("valueField", toString(entry));
				recordList.add(record);
			}

			grid.setData(recordList);

			addMember(grid);
		}

		private String toString(Entry entry) {
			return entry.getValue() == null ? "null" : entry.getValue().toString();
			
		}
	}

}
