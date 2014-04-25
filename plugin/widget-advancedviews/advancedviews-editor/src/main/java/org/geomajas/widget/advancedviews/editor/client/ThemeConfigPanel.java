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
package org.geomajas.widget.advancedviews.editor.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.widget.advancedviews.client.AdvancedViewsMessages;
import org.geomajas.widget.advancedviews.configuration.client.ThemesInfo;
import org.geomajas.widget.advancedviews.configuration.client.themes.ViewConfig;
import org.geomajas.widget.advancedviews.editor.client.ThemeConfigurationPanel.State;

/**
 * Gui to configure the first level of the theme configuration: General information and the list of configured Theme's
 * (Views).
 *
 * @author Oliver May
 */
public class ThemeConfigPanel extends Layout {

	private static final AdvancedViewsMessages MESSAGES = GWT.create(AdvancedViewsMessages.class);

	private static final ManagerMessages MANAGERMESSAGES = GWT.create(ManagerMessages.class);

	private DynamicForm form;

	private ThemeGrid grid;

	private static final int FORMITEM_WIDTH = 300;

	private ThemeConfigurationPanel themeConfigurationPanel;

	private CheckboxItem themeTurnsOtherLayersOff;

	/** @param themeConfigurationPanel */
	public ThemeConfigPanel(ThemeConfigurationPanel themeConfigurationPanel) {
		super();

		this.themeConfigurationPanel = themeConfigurationPanel;

		layout();
	}

	private void layout() {
		// Left layout

		HLayout layout = new HLayout();

		form = new DynamicForm();
		form.setAutoFocus(true);
		form.setWidth(FORMITEM_WIDTH + 100);
		// form.setWrapItemTitles(false);

		themeTurnsOtherLayersOff = new CheckboxItem();
		themeTurnsOtherLayersOff.setTitle(MESSAGES.themeConfigThemeTurnsOtherLayersOff());
		themeTurnsOtherLayersOff.setWidth(FORMITEM_WIDTH);
		themeTurnsOtherLayersOff.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				themeConfigurationPanel.getState().getThemesInfo()
						.setHideOtherlayers(themeTurnsOtherLayersOff.getValueAsBoolean());
			}
		});

		form.setFields(themeTurnsOtherLayersOff);

		layout.addMember(form);

		VLayout gridLayout = new VLayout();
		grid = new ThemeGrid();
		gridLayout.addMember(grid);

		Layout addImgContainer = new Layout();
		addImgContainer.setMargin(2);
		addImgContainer.setWidth(64 + 16); //16 from scroller in grid
		addImgContainer.setAlign(Alignment.CENTER);
		addImgContainer.setLayoutAlign(Alignment.RIGHT);

		addImgContainer.setHeight(25);
		IButton addImg = new IButton();
		addImg.setIcon(WidgetLayout.iconAdd);
		addImg.setTitle("");
		addImg.setHeight(22);
		addImg.setWidth(28);
		addImg.setIconAlign("center");

		addImg.setShowDown(false);
		addImg.setShowRollOver(false);
		addImg.setPrompt(MESSAGES.themeConfigViewAdd());

		addImg.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ViewConfig viewConfig = new ViewConfig();
				viewConfig.setTitle(MESSAGES.themeConfigViewDefaultNewTitle());
				themeConfigurationPanel.getState().getThemesInfo().getThemeConfigs().add(viewConfig);
				themeConfigurationPanel.selectViewConfig(viewConfig);
			}
		});
		addImgContainer.addMember(addImg);
		gridLayout.addMember(addImgContainer);

		HLayout group = new HLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle(MESSAGES.themeConfigThemeConfigGroup());
		group.addMember(form);
		group.addMember(gridLayout);
		group.setOverflow(Overflow.AUTO);

		addMember(group);
	}

	/** @param state */
	public void update(State state) {
		if (state.getThemesInfo() != null) {
			grid.fillGrid(state.getThemesInfo());
			//TODO
			// themeTitle.setValue(state.getThemesInfo().getTitle());
			// mouseOverText.setValue(state.getThemesInfo().getTooltip());
			// TODO: show image?
			themeTurnsOtherLayersOff.setValue(state.getThemesInfo().isHideOtherlayers());
		}
	}

	/** @author Oliver May */
	private class ThemeGrid extends ListGrid {

		private static final String FLD_NAME = "name";

		private static final String FLD_DEL = "delete";

		private static final String FLD_ACTIVE = "defaultActive";

		private static final String FLD_OBJECT = "object";

		public ThemeGrid() {
			super();
			setWidth100();
			setHeight100();
			setAlternateRecordStyles(true);
			setSelectionType(SelectionStyle.SINGLE);
			setShowRollOverCanvas(true);
			setShowAllRecords(true);
			setAlternateRecordStyles(true);
			setShowRecordComponents(true);
			setShowRecordComponentsByCell(true);

			ListGridField name = new ListGridField(FLD_NAME, MESSAGES.themeConfigThemeGridNameField());
			name.setWidth("*");
			name.setType(ListGridFieldType.TEXT);
			name.setRequired(true);

			ListGridField active = new ListGridField(FLD_ACTIVE, MESSAGES.themeConfigDefaultActive());
			active.setWidth(64);
			active.setAlign(Alignment.CENTER);

			ListGridField delete = new ListGridField(FLD_DEL, MANAGERMESSAGES.configAddDelete());
			delete.setWidth(64);
			delete.setAlign(Alignment.CENTER);

			setFields(name, active, delete);

			addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					ListGridRecord record = getSelectedRecord();
					if (record != null) {
						ViewConfig viewConfig = (ViewConfig) record.getAttributeAsObject(FLD_OBJECT);
						themeConfigurationPanel.selectViewConfig(viewConfig);
					}
				}
			});
		}

		private void fillGrid(ThemesInfo themesInfo) {
			// clear
			grid.deselectAllRecords();
			grid.setData(new ListGridRecord[]{});
			// fill
			for (ViewConfig config : themesInfo.getThemeConfigs()) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute(FLD_NAME, config.getTitle());
				record.setAttribute(FLD_OBJECT, config);
				grid.addData(record);
			}
		}

		protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
			if (FLD_DEL.equals(grid.getFieldName(colNum))) {
				HLayout layout = new HLayout();
				layout.setHeight(16);
				layout.setWidth(1);

				ImgButton deleteImg = new ImgButton();
				deleteImg.setSrc(WidgetLayout.iconRemove);
				deleteImg.setShowDown(false);
				deleteImg.setShowRollOver(false);
				deleteImg.setPrompt(MESSAGES.themeConfigViewRemove());
				deleteImg.setHeight(16);
				deleteImg.setWidth(16);
				deleteImg.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {
						SC.ask(MESSAGES.themeConfigViewRemoveConfirm(), new BooleanCallback() {

							public void execute(Boolean value) {
								if (value) {
									themeConfigurationPanel.getThemeConfig().getThemeConfigs()
											.remove(record.getAttributeAsObject(FLD_OBJECT));
									fillGrid(themeConfigurationPanel.getThemeConfig());
								}
							}
						});
					}
				});

				layout.addMember(deleteImg);

				return layout;
			}
			if (FLD_ACTIVE.equals(grid.getFieldName(colNum))) {
				HLayout layout = new HLayout();
				layout.setHeight(16);
				layout.setWidth(1);

				DynamicForm form = new DynamicForm();
				form.setWidth(16);
				form.setHeight(16);

				CheckboxItem checkbox = new CheckboxItem("active", "");
				if (((ViewConfig) record.getAttributeAsObject(FLD_OBJECT)).isActiveByDefault()) {
					checkbox.setValue(true);
				}

				form.setFields(checkbox);

				checkbox.addChangedHandler(new ChangedHandler() {
					@Override
					public void onChanged(ChangedEvent changedEvent) {
						for (ViewConfig viewConfig : themeConfigurationPanel.getThemeConfig().getThemeConfigs()) {
							viewConfig.setActiveByDefault(false);
						}
						((ViewConfig) record.getAttributeAsObject(FLD_OBJECT))
								.setActiveByDefault(((CheckboxItem) changedEvent.getItem()).getValueAsBoolean());
						themeConfigurationPanel.setThemeConfig(themeConfigurationPanel.getThemeConfig());
					}
				});

				layout.addMember(form);
				return layout;
			}
			return super.createRecordComponent(record, colNum);
		}
	}
}
