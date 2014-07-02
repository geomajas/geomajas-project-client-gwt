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

package org.geomajas.gwt.client.gfx.paintable.mapaddon;

import com.smartgwt.client.widgets.Canvas;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * <p>
 * Extension of {@link MapAddon} to display {@link Canvas} widgets on the map.
 * </p>
 * 
 * @author Jan Venstermans
 */
public class CanvasMapAddon extends MapAddon {

	private Canvas canvas;

	private MapWidget mapWidget;

	/**
	 * This is the vertical offset in pixels of the {@link #canvas} from the border of the map.
	 * To indicate whether the offset needs to be left or wright, use the {@link #setVerticalAlignment} method.
	 */
	private int verticalOffset;

	/**
	 * This is the horizontal offset in pixels of the {@link #canvas} from the border of the map.
	 * To indicate whether the offset needs to be top or bottom, use the {@link #setAlignment} method.
	 */
	private int horizontalOffset;

	public CanvasMapAddon(String id, final Canvas canvas, final MapWidget mapWidget) {
		super(id, canvas.getOffsetWidth(), canvas.getOffsetHeight());
		//this.widget = widget;
		this.mapWidget = mapWidget;
		this.canvas = canvas;
		canvas.setVisible(false);

		this.mapWidget.addChild(canvas);
	}

	@Override
	public void onDraw() {
		if (mapWidget.getMapModel().isInitialized()) {
			canvas.setVisible(true);
			canvas.bringToFront();
		}
	}

	@Override
	public void onRemove() {
		mapWidget.removeChild(canvas);
	}

	@Override
	public void accept(PainterVisitor painterVisitor, Object o, Bbox bbox, boolean b) {
		setPosition();
	}

	/**
	 * Getter for the {@link #horizontalOffset}.
	 * @return
	 */
	public int getHorizontalOffset() {
		return horizontalOffset;
	}

	/**
	 * Setter for the {@link #horizontalOffset}.
	 * @param horizontalOffset
	 */
	public void setHorizontalOffset(int horizontalOffset) {
		this.horizontalOffset = horizontalOffset;
	}

	/**
	 * Getter for the {@link #verticalOffset}.
	 * @return verticalOffset
	 */
	public int getVerticalOffset() {
		return verticalOffset;
	}

	/**
	 * Setter for the {@link #verticalOffset}.
	 * @param verticalOffset
	 */
	public void setVerticalOffset(int verticalOffset) {
		this.verticalOffset = verticalOffset;
	}

	/* private method */
	private void setPosition() {
		switch (getAlignment()) {
			case LEFT:
				canvas.setLeft(getHorizontalOffset());
				break;
			case RIGHT:
				canvas.setLeft(mapWidget.getVectorContext().getWidth() - getWidth() - getHorizontalOffset());
				break;
		}
		switch (getVerticalAlignment()) {
			case TOP:
				canvas.setTop(getVerticalOffset());
				break;
			case BOTTOM:
				canvas.setTop(mapWidget.getVectorContext().getHeight() - getHeight() - getVerticalOffset());
				break;
		}
	}
}
