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
import org.geomajas.widget.searchandfilter.client.util.AttributeCriterionUtil;
import org.geomajas.widget.searchandfilter.editor.client.presenter.ConfiguredSearchPresenter;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute;
import org.geomajas.widget.searchandfilter.editor.client.i18n.SearchAndFilterEditorMessages;

import java.util.List;

/**
 * Default implementation of {@link org.geomajas.widget.searchandfilter.editor.client
 * .presenter.ConfiguredSearchPresenter.View}.
 *
 * @author Jan Venstermans
 */
public class ConfiguredSearchView implements ConfiguredSearchPresenter.View {

	private final SearchAndFilterEditorMessages messages =
			GWT.create(SearchAndFilterEditorMessages.class);

	private static final int FORMITEM_WIDTH = 300;

	private static final int ATTRIBUTES_GRID_WIDTH = 420;
	private static final int ATTRIBUTES_GRID_HEIGHT = 100;

	public static final String FLD_NAME = "Name";

	private ConfiguredSearchPresenter.Handler handler;

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
	public ConfiguredSearchView() {
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
		titleFld.setTitle(messages.searchDetailsWindowFieldTitleLabel());
		titleFld.setRequired(true);
		titleFld.setWidth(FORMITEM_WIDTH);
		titleFld.setWrapTitle(false);

		descriptionFld = new TextAreaItem();
		descriptionFld.setTitle(messages.searchDetailsWindowFieldDescriptionLabel());
		descriptionFld.setRequired(true);
		descriptionFld.setWidth(FORMITEM_WIDTH);
		descriptionFld.setWrapTitle(false);

		titleInWindowFld = new TextItem();
		titleInWindowFld.setTitle(messages.searchDetailsWindowFieldTitleInWindowLabel());
		titleInWindowFld.setRequired(true);
		titleInWindowFld.setWidth(FORMITEM_WIDTH);
		titleInWindowFld.setWrapTitle(false);

		uploadForm = new FileUploadForm();
		CanvasItem uploadItem = new CanvasItem();
		uploadItem.setCanvas(uploadForm);
		uploadItem.setWidth(FORMITEM_WIDTH);
		uploadItem.setTitle(messages.searchDetailsWindowFieldIconUrlLabel());
		uploadItem.setRequired(false);
		uploadItem.setWrapTitle(false);

		form.setFields(titleFld, descriptionFld, titleInWindowFld, uploadItem);

		// attribute table //

		VLayout gridLayout = new VLayout();
		gridLayout.setWidth(ATTRIBUTES_GRID_WIDTH);
		gridLayout.setHeight(ATTRIBUTES_GRID_HEIGHT);
		grid = new SearchAttributeListGrid();
		gridLayout.addMember(grid);

		Layout addImgContainer = new Layout();
		addImgContainer.setWidth(64 + 16); //16 from scroller in grid
		addImgContainer.setAlign(Alignment.CENTER);
		addImgContainer.setHeight(16);
		addImgContainer.setLayoutAlign(Alignment.RIGHT);

		ImgButton addImg = new ImgButton();
		addImg.setSrc(WidgetLayout.iconAdd);
		addImg.setShowDown(false);
		addImg.setShowRollOver(false);
		addImg.setPrompt(messages.searchDetailsWindowAddSearchAttributeButtonTooltip());
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
		window.setTitle(messages.searchDetailsWindowTitle());
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
	public void setHandler(ConfiguredSearchPresenter.Handler handler) {
		window.setSaveHandler(handler);
		this.handler = handler;
	}

	@Override
	public void updateGrid(List<ConfiguredSearchAttribute> searchAttributeList) {
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
	 * Used by {@link ConfiguredSearchView}.
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

		private ConfiguredSearchAttribute rollOverSearchAttribute;

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
			ListGridField attributeNameFld = new ListGridField(FLD_ATTRIBUTE_NAME,
					messages.searchDetailsWindowGridColumnAttributeName());
			attributeNameFld.setWidth(FLD_ATTRIBUTE_NAME_WIDTH);
			attributeNameFld.setCanEdit(true);

			ListGridField labelFld = new ListGridField(FLD_LABEL, messages.searchDetailsWindowGridColumnLabel());
			labelFld.setWidth(FLD_LABEL_WIDTH);
			labelFld.setCanEdit(true);

			ListGridField operationFld = new ListGridField(FLD_OPERATION,
					messages.searchDetailsWindowGridColumnOperation());
			operationFld.setWidth(FLD_OPERATION_WIDTH);
			operationFld.setCanEdit(true);

			ListGridField inputTypeFld = new ListGridField(FLD_INPUT_TYPE,
					messages.searchDetailsWindowGridColumnInputType());
			inputTypeFld.setWidth("*");

			ListGridField actionsFld = new ListGridField(FLD_ACTIONS, messages.searchDetailsWindowGridColumnActions());
			actionsFld.setType(ListGridFieldType.ICON);
			actionsFld.setWidth(FLD_ACTIONS_WIDTH);
			actionsFld.setCanEdit(false);
			actionsFld.setAlign(Alignment.CENTER);

			setFields(attributeNameFld, labelFld, operationFld, inputTypeFld, actionsFld);

			addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					ListGridRecord record = getSelectedRecord();
					if (record != null) {
						ConfiguredSearchAttribute layerConfig =
								(ConfiguredSearchAttribute) record.getAttributeAsObject(FLD_OBJECT);
						//themeConfigurationPanel.selectLayerConfig(layerConfig);
					}
				}
			});
		}

		@Override
		protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
			ListGridRecord rollOverRecord = getRecord(rowNum);
			rollOverSearchAttribute = (ConfiguredSearchAttribute)
					rollOverRecord.getAttributeAsObject(FLD_OBJECT);

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
				editProps.setPrompt(messages.searchDetailsWindowGridColumnActionsTooltip());
				editProps.setShowDisabledIcon(false);
				editProps.setHeight(16);
				editProps.setWidth(16);
				editProps.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

					public void onClick(ClickEvent event) {
						handler.onEdit(rollOverSearchAttribute);
					}
				});
				rollOverCanvas.addMember(editProps);

				ImgButton removeSearchAttributeButton = new ImgButton();
				removeSearchAttributeButton.setShowDown(false);
				removeSearchAttributeButton.setShowRollOver(false);
				removeSearchAttributeButton.setLayoutAlign(Alignment.CENTER);
				removeSearchAttributeButton.setSrc(WidgetLayout.iconRemove);
				removeSearchAttributeButton.setPrompt(messages.searchesGridRemoveSearchTooltip());
				removeSearchAttributeButton.setShowDisabledIcon(false);
				removeSearchAttributeButton.setHeight(16);
				removeSearchAttributeButton.setWidth(16);
				removeSearchAttributeButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

					public void onClick(ClickEvent event) {
						handler.onRemove(rollOverSearchAttribute);
					}
				});
				rollOverCanvas.addMember(removeSearchAttributeButton);
			}
			return rollOverCanvas;
		}

		public void fillGrid(List<ConfiguredSearchAttribute> searchAttributeList) {
			deselectAllRecords();
			setData(new ListGridRecord[]{});
			// fill
			for (ConfiguredSearchAttribute attribute : searchAttributeList) {
				addRow(attribute);
			}
		}

		public void addRow(ConfiguredSearchAttribute attribute) {
			ListGridRecord record = new ListGridRecord();
			record.setAttribute(FLD_ATTRIBUTE_NAME, attribute.getAttributeName());
			record.setAttribute(FLD_LABEL, attribute.getDisplayText());
			record.setAttribute(FLD_OPERATION, AttributeCriterionUtil.
					getOperationStringRepresentation(attribute.getAttributeType(), attribute.getOperation()));
			record.setAttribute(FLD_INPUT_TYPE, AttributeCriterionUtil.
					getInputTypeStringRepresentation(attribute.getAttributeType(), attribute.getInputType()));
			record.setAttribute(FLD_OBJECT, attribute);
			addData(record);
		}
	}
}
