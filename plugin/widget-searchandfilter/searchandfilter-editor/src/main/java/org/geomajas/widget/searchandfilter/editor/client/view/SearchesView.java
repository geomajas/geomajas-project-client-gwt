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
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.impl.DeskmanagerIcon;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.editor.client.configuration.SearchConfig;
import org.geomajas.widget.searchandfilter.editor.client.configuration.SearchesStatus;
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchesPresenter;

/**
 * Default implementation of {@link SearchesPresenter.View}.
 * 
 * @author Jan Venstermans
 * 
 */
public class SearchesView implements SearchesPresenter.View {

	private static final SearchAndFilterMessages MESSAGES = GWT.create(SearchAndFilterMessages.class);

	private SearchListGrid grid;

	private VLayout layout;

	private SearchesStatus status;

	private SearchesPresenter.Handler handler;

	public SearchesView() {
		layout = new VLayout(5);
		layout.setIsGroup(true);
		layout.setGroupTitle(MESSAGES.searchesGroupTitle());
		layout.setOverflow(Overflow.AUTO);

		// the grid
		VLayout gridLayout = new VLayout();
		grid = new SearchListGrid();
		gridLayout.addMember(grid);
		layout.addMember(gridLayout);

		Layout addImgContainer = new Layout();
		addImgContainer.setWidth(64 + 16); //16 from scroller in grid
		addImgContainer.setAlign(Alignment.CENTER);
		addImgContainer.setHeight(16);
		addImgContainer.setLayoutAlign(Alignment.RIGHT);

		ImgButton addImg = new ImgButton();
		addImg.setSrc(WidgetLayout.iconAdd);
		addImg.setShowDown(false);
		addImg.setShowRollOver(false);
		addImg.setPrompt("tooltip to do");
		addImg.setHeight(16);
		addImg.setWidth(16);
		addImg.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				handler.onAddSearch();
			}
		});
		addImgContainer.addMember(addImg);
		layout.addMember(addImgContainer);
	}

	@Override
	public void setStatus(SearchesStatus status) {
		this.status = status;
		update();
	}

	@Override
	public void setHandler(SearchesPresenter.Handler handler) {
		this.handler = handler;
	}

	@Override
	public void update() {
		if (status.getSearchesInfo() != null) {
			grid.fillGrid();
		}
	}

	@Override
	public Canvas getCanvas() {
		return layout;
	}

	/**
	 * Used by {@link SearchesView}.
	 *
	 * @author Jan Venstermans
	 *
	 */
	public class SearchListGrid extends ListGrid {

		public static final String FLD_NAME = "name";

		public static final String FLD_DESCRIPTION = "e-mail";

		public static final String FLD_ACTIONS = "actions";

		public static final String FLD_OBJECT = "object";

		private static final int FLD_ACTIONS_WIDTH = 60;

		//private ListGridRecord rollOverRecord;

		private HLayout rollOverCanvas;

		public SearchListGrid() {
			super();
			setWidth100();
			setHeight100();

		/*grid config*/
			setAlternateRecordStyles(true);
			setSelectionType(SelectionStyle.SINGLE);
			setShowRollOverCanvas(true);
			setShowAllRecords(true);
			setAlternateRecordStyles(true);
			setShowRecordComponents(true);
			setShowRecordComponentsByCell(true);

		/*columns*/
			ListGridField nameFld = new ListGridField(FLD_NAME, MESSAGES.searchesGridColumnSearchName());
			nameFld.setWidth(70);
			nameFld.setPrompt(MESSAGES.searchesGridColumnSearchNameTooltip());

			ListGridField publicFld = new ListGridField(FLD_DESCRIPTION, MESSAGES.searchesGridColumnSearchDescription());
			publicFld.setWidth("*");
			publicFld.setPrompt(MESSAGES.searchesGridColumnSearchDescriptionTooltip());

			ListGridField actionsFld = new ListGridField(FLD_ACTIONS, MESSAGES.searchesGridColumnActions());
			actionsFld.setType(ListGridFieldType.ICON);
			actionsFld.setWidth(FLD_ACTIONS_WIDTH);
			actionsFld.setCanEdit(false);
			actionsFld.setAlign(Alignment.CENTER);
			publicFld.setPrompt(MESSAGES.searchesGridColumnActionsTooltip());

			setFields(nameFld, publicFld, actionsFld);
		}

		@Override
		protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
			ListGridRecord rollOverRecord = getRecord(rowNum);
			final SearchConfig searchConfig = (SearchConfig) rollOverRecord.getAttributeAsObject(FLD_OBJECT);

			if (rollOverCanvas == null) {
				rollOverCanvas = new HLayout(3);
				rollOverCanvas.setSnapTo("TR");
				rollOverCanvas.setWidth(FLD_ACTIONS_WIDTH);
				rollOverCanvas.setHeight(22);

				ImgButton editProps = new ImgButton();
				editProps.setShowDown(false);
				editProps.setShowRollOver(false);
				editProps.setLayoutAlign(Alignment.CENTER);
				editProps.setSrc(DeskmanagerIcon.IMG_SRC_COG);
				editProps.setPrompt("jabada");
				editProps.setShowDisabledIcon(false);
				editProps.setHeight(16);
				editProps.setWidth(16);
				editProps.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

					public void onClick(ClickEvent event) {
						handler.onEdit(searchConfig);
					}
				});
				rollOverCanvas.addMember(editProps);
			}
			return rollOverCanvas;
		}

		public void fillGrid() {
			deselectAllRecords();
			setData(new ListGridRecord[]{});
			// fill
			for (SearchConfig config : status.getSearchesInfo().getSearchConfigs()) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute(FLD_NAME, config.getTitle());
				record.setAttribute(FLD_DESCRIPTION, config.getDescription());
				record.setAttribute(FLD_OBJECT, config);
				addData(record);
			}
		}
	}
}
