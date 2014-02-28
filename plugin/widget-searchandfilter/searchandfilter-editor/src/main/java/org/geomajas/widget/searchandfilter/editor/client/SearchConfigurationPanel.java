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
package org.geomajas.widget.searchandfilter.editor.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
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
import org.geomajas.widget.searchandfilter.editor.client.configuration.SearchesInfo;

/**
 * Panel to allow configuration of searches.
 * 
 * @author Jan Venstermans
 * 
 */
public class SearchConfigurationPanel extends VLayout {

	private static final SearchAndFilterMessages MESSAGES = GWT.create(SearchAndFilterMessages.class);

	private SearchListGrid grid;

	private State state = new State();

	public SearchConfigurationPanel() {
		super(5);
		setIsGroup(true);
		setGroupTitle(MESSAGES.searchesGroupTitle());

		// the grid
		grid = new SearchListGrid();
		VLayout gridLayout = new VLayout();
		grid = new SearchListGrid();
		gridLayout.addMember(grid);
		addMember(grid);

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
				final SearchConfig config = new SearchConfig();
				config.setTitle("newSearch");
				SearchDetailsWindow window = new SearchDetailsWindow(config, new BooleanCallback() {

					public void execute(Boolean value) {
						state.getSearchesInfo().getSearchConfigs().add(config);
						selectSearchConfig(config);
					}
				});
			}
		});
		addImgContainer.addMember(addImg);
		addMember(addImgContainer);
	}

	public State getState() {
		return state;
	}

	public SearchesInfo getSearchConfig() {
		return state.getSearchesInfo();
	}

	public void setSearchConfig(SearchesInfo searchesInfo) {
		state.setSearchesInfo(searchesInfo);
		update();
	}

	private void update() {
		if (state.getSearchesInfo() != null) {
			grid.fillGrid(state.getSearchesInfo());
		}
	}

	public void selectSearchConfig(SearchConfig searchConfig) {
		state.setSelectedSearchConfig(searchConfig);
		update();
	}

	/**
	 * Helper class to provide access to the state of.
	 *
	 * @author Jan Venstermans
	 *
	 */
	public class State {

		private SearchesInfo searchesInfo;

		private SearchConfig selectedSearchConfig;

		public SearchesInfo getSearchesInfo() {
			return searchesInfo;
		}

		public void setSearchesInfo(SearchesInfo searchesInfo) {
			this.searchesInfo = searchesInfo;
		}

		public SearchConfig getSelectedSearchConfig() {
			return selectedSearchConfig;
		}

		public void setSelectedSearchConfig(SearchConfig selectedSearchConfig) {
			this.selectedSearchConfig = selectedSearchConfig;
		}
	}

	/**
	 * Used by {@link org.geomajas.widget.searchandfilter.editor.client.SearchConfigurationPanel}.
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

		private ListGridRecord rollOverRecord;

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

			rollOverRecord = getRecord(rowNum);

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
						configureSearch(rollOverRecord);
					}
				});
				rollOverCanvas.addMember(editProps);
			}
			return rollOverCanvas;
		}

		private void configureSearch(final ListGridRecord record) {
			SearchConfig searchConfig = (SearchConfig) record.getAttributeAsObject(FLD_OBJECT);
			SearchDetailsWindow window = new SearchDetailsWindow(searchConfig, new BooleanCallback() {

				public void execute(Boolean value) {
					updateData(record);
				}
			});
			window.show();
		}

		public void fillGrid(SearchesInfo searchesInfo) {
			deselectAllRecords();
			setData(new ListGridRecord[]{});
			// fill
			for (SearchConfig config : searchesInfo.getSearchConfigs()) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute(FLD_NAME, config.getTitle());
				record.setAttribute(FLD_DESCRIPTION, config.getDescription());
				record.setAttribute(FLD_OBJECT, config);
				addData(record);
			}
		}
	}
}
