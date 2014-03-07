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
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.FileUploadForm;
import org.geomajas.plugin.deskmanager.client.gwt.common.impl.DeskmanagerIcon;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;
import org.geomajas.widget.searchandfilter.configuration.client.SearchAttribute;
import org.geomajas.widget.searchandfilter.configuration.client.SearchConfig;
import org.geomajas.widget.searchandfilter.editor.client.SearchAndFilterEditor;
import org.geomajas.widget.searchandfilter.editor.client.i18n.SearchAndFilterEditorMessages;
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchPresenter;

import java.util.List;

/**
 * Default implementation of {@link SearchPresenter.View}.
 *
 * @author Jan Venstermans
 */
public class SearchView implements SearchPresenter.View {

	private final SearchAndFilterEditorMessages MESSAGES =
			GWT.create(SearchAndFilterEditorMessages.class);

	private static final int FORMITEM_WIDTH = 300;

	private static final int ATTRIBUTES_GRID_WIDTH = 420;
	private static final int ATTRIBUTES_GRID_HEIGHT = 100;

	public static final String FLD_NAME = "Name";

	private SearchPresenter.Handler handler;

	private DynamicForm form;

	private TextItem titleFld, titleInWindowFld;

	private TextAreaItem descriptionFld;

	private FileUploadForm uploadForm;

	private SearchAttributeListGrid grid;

	private VLayout layout;

	private SaveCancelWindow window;

	/**
	 * Construct a search configuration window.
	 *
	 */
	public SearchView() {
		layout();
	}

	/**
	 *
	 */
	private void layout() {
		// form //
		form = new DynamicForm();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setWidth(FORMITEM_WIDTH + 100);
		form.setWrapItemTitles(false);

		titleFld = new TextItem(FLD_NAME);
		titleFld.setTitle(MESSAGES.searchDetailsWindowFieldTitleLabel());
		titleFld.setRequired(true);
		titleFld.setWidth(FORMITEM_WIDTH);
		titleFld.setWrapTitle(false);
		titleFld.setTooltip(MESSAGES.searchDetailsWindowFieldTitleTooltip());

		descriptionFld = new TextAreaItem();
		descriptionFld.setTitle(MESSAGES.searchDetailsWindowFieldDescriptionLabel());
		descriptionFld.setRequired(true);
		descriptionFld.setWidth(FORMITEM_WIDTH);
		descriptionFld.setWrapTitle(false);
		descriptionFld.setTooltip(MESSAGES.searchDetailsWindowFieldDescriptionTooltip());

		titleInWindowFld = new TextItem();
		titleInWindowFld.setTitle(MESSAGES.searchDetailsWindowFieldTitleInWindowLabel());
		titleInWindowFld.setRequired(true);
		titleInWindowFld.setWidth(FORMITEM_WIDTH);
		titleInWindowFld.setWrapTitle(false);
		titleInWindowFld.setTooltip(MESSAGES.searchDetailsWindowFieldTitleInWindowTooltip());

		uploadForm = new FileUploadForm();
		CanvasItem uploadItem = new CanvasItem();
		uploadItem.setCanvas(uploadForm);
		uploadItem.setWidth(FORMITEM_WIDTH);
		uploadItem.setTitle(MESSAGES.searchDetailsWindowFieldIconUrlLabel());
		uploadItem.setRequired(false);
		uploadItem.setWrapTitle(false);
		uploadItem.setTooltip(MESSAGES.searchDetailsWindowFieldIconUrlTooltip());

		form.setFields(titleFld, descriptionFld, titleInWindowFld, uploadItem);

		// attribute table //

		VLayout gridLayout = new VLayout();
		//ScrollPanel panel = new ScrollPanel();
		gridLayout.setWidth(ATTRIBUTES_GRID_WIDTH);
		gridLayout.setHeight(ATTRIBUTES_GRID_HEIGHT);
		grid = new SearchAttributeListGrid();
		gridLayout.addMember(grid);
		//gridLayout.addMember(panel);
		//gridLayout.setWidth(ATTRIBUTES_GRID_WIDTH);
		//grid.setHeight(ATTRIBUTES_GRID_HEIGHT - );
		//grid.setOverflow(Overflow.AUTO);

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
				handler.onAddAttribute();
			}
		});
		addImgContainer.addMember(addImg);
		gridLayout.addMember(addImgContainer);

		// layout structure //
		layout = new VLayout(10);
		//layout.setMargin(10);
		layout.addMember(form);
		layout.addMember(gridLayout);

		window = new SaveCancelWindow(layout);
	}

	@Override
	public void show() {
		window.show();
	}

	@Override
	public void hide() {
		window.hide();
	}

	@Override
	public void setHandler(SearchPresenter.Handler handler) {
		window.setSaveHandler(handler);
		this.handler = handler;
	}

	@Override
	public void updateGrid(List<SearchAttribute> searchAttributeList) {
		grid.fillGrid(searchAttributeList);
	}

	@Override
	public Canvas getCanvas() {
		return window;
	}

	@Override
	public void setTitle(String title) {
		titleFld.setValue(title);
	}

	@Override
	public void setDescription(String description) {
		descriptionFld.setValue(description);
	}

	@Override
	public void setTitleInWindow(String titleInWindow) {
		titleInWindowFld.setValue(titleInWindow);
	}

	@Override
	public void setIconUrl(String iconUrl) {
		uploadForm.setUrl(iconUrl);
	}

	@Override
	public String getTitle() {
		return titleFld.getValueAsString();
	}

	@Override
	public String getDescription() {
		return descriptionFld.getValueAsString();
	}

	@Override
	public String getTitleInWindow() {
		return titleInWindowFld.getValueAsString();
	}

	@Override
	public String getIconUrl() {
		return uploadForm.getUrl();
	}

	@Override
	public boolean validateForm() {
		return form.validate();
	}

	@Override
	public void clearFormValues() {
		form.clearValues();
	}

	/**
	 * Used by {@link SearchView}.
	 *
	 * @author Jan Venstermans
	 *
	 */
	public class SearchAttributeListGrid extends ListGrid {

		public static final String FLD_ATTRIBUTE_NAME = "attributeName";

		public static final String FLD_LABEL = "label";

		public static final String FLD_OPERATION = "operation";

		public static final String FLD_INPUT_TYPE = "inputType";

		public static final String FLD_ACTIONS = "actions";

		public static final String FLD_OBJECT = "object";

		private static final int FLD_ATTRIBUTE_NAME_WIDTH = 80;
		private static final int FLD_LABEL_WIDTH = 80;
		private static final int FLD_OPERATION_WIDTH = 80;
		private static final int FLD_INPUT_TYPE_WIDTH = 80;

		private static final int FLD_ACTIONS_WIDTH = 60;

		private ListGridRecord rollOverRecord;

		private HLayout rollOverCanvas;

		public SearchAttributeListGrid() {
			super();
			/*setWidth100();
			setHeight100();*/
			setCanEdit(true);
			setEditEvent(ListGridEditEvent.CLICK);
			setEditByCell(true);

			setWidth100();
			setHeight100();
			setAlternateRecordStyles(true);
			setSelectionType(SelectionStyle.SINGLE);
			setShowRollOverCanvas(true);
			setShowAllRecords(true);
			setAlternateRecordStyles(true);
			setShowRecordComponents(true);
			setShowRecordComponentsByCell(true);

		/*grid config*/


		/*columns*/
			ListGridField attributeNameFld = new ListGridField(FLD_ATTRIBUTE_NAME, "Attribute");
			attributeNameFld.setWidth(FLD_ATTRIBUTE_NAME_WIDTH);
			attributeNameFld.setPrompt("Attribute tooltip.");
			attributeNameFld.setCanEdit(true);

			ListGridField labelFld = new ListGridField(FLD_LABEL, "Label");
			labelFld.setWidth(FLD_LABEL_WIDTH);
			labelFld.setPrompt("Label tooltip.");
			labelFld.setCanEdit(true);

			ListGridField operationFld = new ListGridField(FLD_OPERATION, "Operation");
			operationFld.setWidth(FLD_OPERATION_WIDTH);
			operationFld.setPrompt("Operation tooltip.");
			operationFld.setCanEdit(true);

			ListGridField inputTypeFld = new ListGridField(FLD_INPUT_TYPE, "Input type");
			inputTypeFld.setWidth("*");
			inputTypeFld.setPrompt("Input type tooltip.");

			ListGridField actionsFld = new ListGridField(FLD_ACTIONS, MESSAGES.searchesGridColumnActions());
			actionsFld.setType(ListGridFieldType.ICON);
			actionsFld.setWidth(FLD_ACTIONS_WIDTH);
			actionsFld.setCanEdit(false);
			actionsFld.setAlign(Alignment.CENTER);

			setFields(attributeNameFld, labelFld, operationFld, inputTypeFld, actionsFld);

			addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					ListGridRecord record = getSelectedRecord();
					if (record != null) {
						SearchAttribute layerConfig = (SearchAttribute) record.getAttributeAsObject(FLD_OBJECT);
						//themeConfigurationPanel.selectLayerConfig(layerConfig);
					}
				}
			});
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

						SearchAttribute searchAttribute = (SearchAttribute)
								rollOverRecord.getAttributeAsObject(FLD_OBJECT);
						handler.onEdit(searchAttribute);
					}
				});
				rollOverCanvas.addMember(editProps);
			}
			return rollOverCanvas;
		}

		public void fillGrid(List<SearchAttribute> searchAttributeList) {
			deselectAllRecords();
			setData(new ListGridRecord[]{});
			// fill
			for (SearchAttribute attribute : searchAttributeList) {
				addRow(attribute);
			}
		}

		public void addRow(SearchAttribute attribute) {
			ListGridRecord record = new ListGridRecord();
			record.setAttribute(FLD_ATTRIBUTE_NAME, attribute.getAttributeName());
			record.setAttribute(FLD_LABEL, attribute.getLabel());
			record.setAttribute(FLD_OPERATION, SearchAndFilterEditor.getSearchAttributeService().
					getOperationStringRepresentation(attribute.getAttributeType(), attribute.getOperation()));
			record.setAttribute(FLD_INPUT_TYPE, SearchAndFilterEditor.getSearchAttributeService().
					getInputTypeStringRepresentation(attribute.getAttributeType(), attribute.getInputType()));
			record.setAttribute(FLD_OBJECT, attribute);
			addData(record);
		}
	}
}
