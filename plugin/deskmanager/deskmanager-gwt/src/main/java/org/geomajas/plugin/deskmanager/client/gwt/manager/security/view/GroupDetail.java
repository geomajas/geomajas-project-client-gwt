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

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.global.ExceptionDto;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.controller.ZoomToRectangleController;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.CanvasMapAddon;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.editor.EditorTextItem;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.DetailHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.GroupDetailHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.Manager;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.GeometryEditorImpl;
import org.geomajas.plugin.editing.gwt.client.gfx.PointSymbolizerShapeAndSize;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Default implementation of {@link GroupDetailView}.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class GroupDetail extends AbstractEditableLoadingLayout implements GroupDetailView, Editor<TerritoryDto> {

	private static final String GROUP_GEOM_ID = "groupGeometry";

	@UiField
	protected EditorTextItem name;

	@UiField
	@Path("code")
	protected EditorTextItem key;

	private DynamicForm form;

	private MapWidget territoryMap;

	private GfxGeometry groupGeometry;

	/**
	 * The current geometry of the selected group.
	 * This is saved separately, because of editing possibility.
	 */
	private Geometry geometryOfCurrentGroup;

	private GroupDetailHandler handler;

	private GeometryEditor editor;

	private ZoomToGeometryRectangleOnceController zoomToRectangleController;

	/* buttons for editing */
	private VLayout editingButtonsLayout;
	private IButton editorAddButton;
	private IButton editorEditButton;
	private IButton editorDeleteButton;
	private IButton editorSaveButton;
	private IButton editorDefaultButton;
	private IButton editorImportButton;

	private Canvas editorButtonCanvas;

	/**
	 * UI driver interface.
	 * 
	 * @author timothy
	 */
	interface Driver extends SimpleBeanEditorDriver<TerritoryDto, GroupDetail> {

	}

	private final Driver driver = GWT.create(Driver.class);

	/**
	 * Default constructor.
	 */
	public GroupDetail() {
		groupGeometry = new GfxGeometry(GROUP_GEOM_ID);
		groupGeometry.setStyle(new ShapeStyle("#EEEE00", 0.5f, "#EEEE00", 0.8f, 2));
		driver.initialize(this);
	}

	@Override
	public void setHandler(DetailHandler<TerritoryDto> handler) {
		if (handler instanceof GroupDetailHandler) {
			this.handler = (GroupDetailHandler) handler;
		}
	}

	@Override
	public void setObject(TerritoryDto group) {
		driver.edit(group);
		if (group != null && group.getId() != null) {
			setGeometryOfCurrentGroup(group.getGeometry());
			if (geometryOfCurrentGroup != null) {
				zoomToGeometry();
			}
		} else {
			setGeometryOfCurrentGroup(null);
		}
	}

	@Override
	public void setMapEditingButtonsEnabled(boolean editing) {
		if (editingButtonsLayout != null) {
			editingButtonsLayout.setDisabled(!editing);
		}
	}

	@Override
	public void setImportedGeometry(Geometry importedPolygon) {
		setGeometryOfCurrentGroup(importedPolygon);
		zoomToGeometry();
	}

	@Override
	public void setEditable(boolean editable) {
		form.setDisabled(!editable);
		editingButtonsLayout.setDisabled(!editable);
	}

	@Override
	public void clearValues() {
		form.clearValues();
		setGeometryOfCurrentGroup(null);
	}

	@Override
	public void focusOnFirstField() {
		name.focusInItem();
	}

	@Override
	public String getMapCrs() {
		return territoryMap.getMapModel().getCrs();
	}

	// -------------------------------------------------------------
	// Actions of default buttons Edit Cancel Save
	// -------------------------------------------------------------

	@Override
	public void onEdit() {
		handler.onEdit();
	}

	@Override
	public void onCancel() {
		// stop editing service
		if (getGeometryStatus().equals(GeometryStatus.EDITING)) {
			editor.getEditService().stop();
		}
		setGeometryOfCurrentGroup(null);
		handler.onCancel();
	}

	@Override
	public void onSave() {
		// save changes of the editing service
		if (getGeometryStatus().equals(GeometryStatus.EDITING)) {
			onEditingSaveGeometry();
		}
		TerritoryDto group = validate();
		if (group != null) {
			group.setGeometry(geometryOfCurrentGroup);
			setGeometryOfCurrentGroup(null);
			handler.onSave(group);
		}
	}

	// -------------------------------------------------------------
	// creating GUI
	// -------------------------------------------------------------

	@Override
	protected void fillContainerLayout() {
		form = new DynamicForm();

		name = new EditorTextItem("name");
		name.setTitle(MESSAGES.securityGroupDetailName());

		key = new EditorTextItem("key");
		key.setTitle(MESSAGES.securityGroupDetailKey());

		form.setFields(name, key);

		Label territoryLabel = new Label(MESSAGES.securityGroupDetailTerritoryText());
		territoryLabel.setTitle(MESSAGES.securityGroupDetailTerritoryHint());
		territoryLabel.setWordWrap(false);

		VLayout territoryInfo = new VLayout(10);
		territoryInfo.addMember(form);
		territoryInfo.addMember(territoryLabel);

		HLayout formAndMap = new HLayout();
		VLayout formAndEditingButtons = new VLayout();

		formAndEditingButtons.addMember(territoryInfo);
		// TODO: put buttons on map
		//formAndEditingButtons.addMember(createEditorButtonsLayout());
		formAndMap.addMember(formAndEditingButtons);

		// Create the MapWidget and the GeometryEditor
		// don't use:
		// territoryMap = new MapWidget("mapOsm", "appDeskManager");
		// Instead, use a registered geodesk:
		territoryMap = new MapWidget("mainMap", "TEST_NL");
		territoryMap.setStyleName("gm-groupDetailMap");
		editor = new GeometryEditorImpl(territoryMap);
		zoomToRectangleController = new ZoomToGeometryRectangleOnceController(territoryMap);

		// set shape and size of point symbolizer
		editor.getStyleService().getPointSymbolizerShapeAndSize().setShape(PointSymbolizerShapeAndSize.Shape.CIRCLE);
		editor.getStyleService().getPointSymbolizerShapeAndSize().setSize(6);
		formAndMap.addMember(territoryMap);

		containerLayout.addMember(buttonLayout);
		containerLayout.addMember(formAndMap);
		editorButtonCanvas = createEditorButtonsLayout();

		// add buttons as MapAddon
		CanvasMapAddon editorButtonsAddon =
				new CanvasMapAddon("editorButtons", editorButtonCanvas, territoryMap);
		editorButtonsAddon.setAlignment(Alignment.RIGHT);
		editorButtonsAddon.setVerticalAlignment(VerticalAlignment.TOP);
		editorButtonsAddon.setHorizontalOffset(10);
		editorButtonsAddon.setVerticalOffset(20);
		territoryMap.registerMapAddon(editorButtonsAddon);
	}

	private VLayout createEditorButtonsLayout() {
		editingButtonsLayout = new VLayout();
		editingButtonsLayout.setMembersMargin(10);
		editorAddButton = new IButton("Add");
		editorEditButton = new IButton("Edit");
		editorDeleteButton = new IButton("Delete");
		editorSaveButton = new IButton("Save Geom");
		editorDefaultButton = new IButton("Default");
		editorImportButton = new IButton("Import");

		editingButtonsLayout.addMember(editorAddButton);
		editingButtonsLayout.addMember(editorEditButton);
		editingButtonsLayout.addMember(editorDeleteButton);
		editingButtonsLayout.addMember(editorSaveButton);
		editingButtonsLayout.addMember(editorDefaultButton);
		editingButtonsLayout.addMember(editorImportButton);

		editorAddButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				onEditingAddGeometry();
			}
		});
		editorEditButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				onEditingEditGeometry();
			}
		});
		editorDeleteButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				onEditingDeleteGeometry();
			}
		});
		editorSaveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				onEditingSaveGeometry();
			}
		});
		editorDefaultButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				onSetDefaultGeometry();
			}
		});
		editorImportButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				onImportGeometry();
			}
		});

		return editingButtonsLayout;
	}

	// -------------------------------------------------------------
	// Other private methods
	// -------------------------------------------------------------

	private void setGeometryOfCurrentGroup(Geometry geometryOfCurrentGroup) {
		territoryMap.unregisterWorldPaintable(groupGeometry);
		this.geometryOfCurrentGroup = geometryOfCurrentGroup;
		groupGeometry.setGeometry(GeometryConverter.toGwt(geometryOfCurrentGroup));
		updateEditingButtonEnabled();
		territoryMap.registerWorldPaintable(groupGeometry);
	}

	/**
	 * Driver based validation of {@link TerritoryDto} object.
	 *
	 * @return validated group object, null if invalid
	 */
	private TerritoryDto validate() {
		TerritoryDto group = driver.flush();
		Set<ConstraintViolation<?>> violations = (Set) Manager.getValidator().validate(group);
		driver.setConstraintViolations(violations);
		return violations.size() > 0 ? null : group;
	}

	// -------------------------------------------------------------
	// Changes of GUI due to GeometryStatus
	// -------------------------------------------------------------

	/**
	 * Locally used status of the geometry of the selected group.
	 * It is used to toggle editing buttons enabled/disabled and to check if an editing action can be performed.
	 *
	 * @author Jan Venstermans
	 */
	private enum GeometryStatus {
		NONE, // group has no geometry, i.e. polygon
		ONE,  // group has a geometry, i.e. polygon
		EDITING // geometry of the group is being edited
	}

	private GeometryStatus getGeometryStatus() {
		if (editor.getEditService().isStarted() || editor.isBusyEditing()
				|| editor.getEditService().getEditingState().equals(GeometryEditState.INSERTING)) {
			return GeometryStatus.EDITING;
		}
		return geometryOfCurrentGroup == null ? GeometryStatus.NONE : GeometryStatus.ONE;
	}

	private void updateEditingButtonEnabled() {
		GeometryStatus geometryStatus = getGeometryStatus();
		editorSaveButton.setDisabled(!geometryStatus.equals(GeometryStatus.EDITING));
		editorDefaultButton.setDisabled(geometryStatus.equals(GeometryStatus.EDITING));
		editorImportButton.setDisabled(geometryStatus.equals(GeometryStatus.EDITING));
		editorAddButton.setDisabled(!geometryStatus.equals(GeometryStatus.NONE));
		editorEditButton.setDisabled(!geometryStatus.equals(GeometryStatus.ONE));
		editorDeleteButton.setDisabled(!geometryStatus.equals(GeometryStatus.ONE));
	}

	//-------------------------------------------------------------------
	// methods executed after clicking button
	//-------------------------------------------------------------------

	private void onEditingAddGeometry() {
		if (getGeometryStatus().equals(GeometryStatus.NONE)) {
			Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);
			editor.getEditService().start(polygon);
			try {
				GeometryIndex index = editor.getEditService().addEmptyChild();
				editor.getEditService().setInsertIndex(
						editor.getEditService().getIndexService()
								.addChildren(index, GeometryIndexType.TYPE_VERTEX, 0));
				editor.getEditService().setEditingState(GeometryEditState.INSERTING);
			} catch (GeometryOperationFailedException e) {
				editor.getEditService().stop();
				Window.alert(MESSAGES.securityGroupWarningExceptionDuringEditing(e.getMessage()));
			}
		} else {
			Window.alert(MESSAGES.securityGroupWarningOnlyOneGeometryAllowed());
		}
		updateEditingButtonEnabled();
	}

	private void onEditingEditGeometry() {
		if (getGeometryStatus().equals(GeometryStatus.ONE)) {
			editor.getEditService().start(geometryOfCurrentGroup);
			//don't show the current geometry, this interferes with editing
			setGeometryOfCurrentGroup(null);
		} else {
			Window.alert(MESSAGES.securityGroupWarningNoGeometryToEdit());
		}
	}

	private void onEditingDeleteGeometry() {
		if (getGeometryStatus().equals(GeometryStatus.ONE)) {
			setGeometryOfCurrentGroup(null);
		} else {
			editor.getEditService().stop();
			updateEditingButtonEnabled();
			Window.alert(MESSAGES.securityGroupWarningNoGeometryToDelete());
		}
	}

	private void onEditingSaveGeometry() {
		if (getGeometryStatus().equals(GeometryStatus.EDITING)) {
			setGeometryOfCurrentGroup(editor.getEditService().stop());
		} else {
			updateEditingButtonEnabled();
			Window.alert(MESSAGES.securityGroupWarningNoChangesToSave());
		}
	}

	private void onSetDefaultGeometry() {
		if (!getGeometryStatus().equals(GeometryStatus.EDITING)) {
			Bbox mapMaxBounds = null;
			try {
				// this does not give total bounds TODO: configure correctly?
				//ClientMapInfo mapLayer = territoryMap.getMapModel().getMapInfo();
				ClientLayerInfo firstLayer = territoryMap.getMapModel().getMapInfo().getLayers().get(0);
				mapMaxBounds = firstLayer.getMaxExtent();
			} catch (Exception e) {
				mapMaxBounds = new Bbox();
			}
			Geometry geometry = new Geometry(Geometry.POLYGON, -1, 2);
			Coordinate[] coordinates = null;
			if (mapMaxBounds != null) {
				Coordinate startEndPoint = new Coordinate(mapMaxBounds.getX(), mapMaxBounds.getY());
				coordinates = new Coordinate[] {
						startEndPoint, new Coordinate(mapMaxBounds.getMaxX(), mapMaxBounds.getY()),
						new Coordinate(mapMaxBounds.getMaxX(), mapMaxBounds.getMaxY()),
						new Coordinate(mapMaxBounds.getX(), mapMaxBounds.getMaxY()),
						startEndPoint};
			} else {
				// user Double MAX and MIN values
				Coordinate startEndPoint = new Coordinate(Double.MIN_VALUE, Double.MIN_VALUE);
				coordinates = new Coordinate[] {
						startEndPoint, new Coordinate(Double.MAX_VALUE, Double.MIN_VALUE),
						new Coordinate(Double.MAX_VALUE, Double.MAX_VALUE),
						new Coordinate(Double.MIN_VALUE, Double.MAX_VALUE),
						startEndPoint};
			}
			Geometry linearRing = new Geometry(Geometry.LINEAR_RING, 0, 2);
			linearRing.setCoordinates(coordinates);
			geometry.setGeometries(new Geometry[] { linearRing });
			setGeometryOfCurrentGroup(geometry);

		} else {
			Window.alert(MESSAGES.securityGroupWarningStopEditingFirst());
		}
	}

	private void onImportGeometry() {
		if (!getGeometryStatus().equals(GeometryStatus.EDITING)) {
			FileUploadWindow uploadWindow = new FileUploadWindow();
			uploadWindow.setWindowTitle(MESSAGES.securityGroupImportWindowTitle());
			uploadWindow.setButtonText(MESSAGES.securityGroupImportWindowButtonText());
			uploadWindow.showWindowAndUploadFile(new Callback<String, ExceptionDto>() {
				@Override
				public void onFailure(ExceptionDto reason) {
					// can't upload file
					Window.alert(MESSAGES.securityGroupWarningCantImportFile());
				}

				@Override
				public void onSuccess(String result) {
					handler.onFileUploaded(result);
				}
			});
			updateEditingButtonEnabled();
		} else {
			Window.alert(MESSAGES.securityGroupWarningStopEditingFirst());
		}
	}

	private void zoomToGeometry() {
		if (getGeometryStatus().equals(GeometryStatus.ONE)) {
			Bbox geometryBounds = GeometryService.getBounds(geometryOfCurrentGroup);
			zoomToRectangleController.performSelection(geometryBounds);
		} else {
			Window.alert(MESSAGES.securityGroupWarningCantZoomToGeometry());
		}
	}

	/**
	 * Controller the zooms to a rectangle drawn by the user, and then deactivates itself again.
	 *
	 *
	 * @author Pieter De Graef
	 * @author Jan Venstermans
	 * @see org.geomajas.gwt.client.gfx.paintable.mapaddon.ZoomToRectangleAddon
	 */
	private class ZoomToGeometryRectangleOnceController extends ZoomToRectangleController {

		public ZoomToGeometryRectangleOnceController(MapWidget mapWidget) {
			super(mapWidget);
		}

		public void performSelection(Bbox geometryBounds) {
			GraphicsController keepController = mapWidget.getController();
			super.selectRectangle(new org.geomajas.gwt.client.spatial.Bbox(geometryBounds));
			mapWidget.setController(keepController);
		}

	}
}
