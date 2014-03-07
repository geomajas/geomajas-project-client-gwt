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
package org.geomajas.widget.searchandfilter.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.widget.searchandfilter.editor.client.i18n.SearchAndFilterEditorMessages;
import org.geomajas.widget.searchandfilter.editor.client.presenter.EditorPresenter;

/**
 * Wrapper around a layout, resulting in a window with cancel and save button.
 *
 * @author Jan Venstermans
 */
public class AddableListGridLayout extends VLayout {

	private Handler handler;

	private ImgButton addImg;

	private ListGrid grid;

	public AddableListGridLayout() {
		super();
		layout();
	}

	/**
	 *
	 */
	private void layout() {
		grid = new ListGrid();
		addMember(grid);

		Layout addImgContainer = new Layout();
		addImgContainer.setWidth(64 + 16); //16 from scroller in grid
		addImgContainer.setAlign(Alignment.CENTER);
		addImgContainer.setHeight(16);
		addImgContainer.setLayoutAlign(Alignment.RIGHT);

		addImg = new ImgButton();
		addImg.setSrc(WidgetLayout.iconAdd);
		addImg.setShowDown(false);
		addImg.setShowRollOver(false);
		addImg.setHeight(16);
		addImg.setWidth(16);
		addImg.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				handler.onAddItem();
			}
		});
		addImgContainer.addMember(addImg);
		addMember(addImgContainer);
	}

	public void setGeneralAddButtonTooltip(String text) {
		addImg.setPrompt(text);
	}

	/**
	 * Interface for this grid.
	 */
	public interface Handler {
		void onAddItem();
	}

	public interface Grid {

	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public abstract class AddableListGrid extends ListGrid {


	}
}
