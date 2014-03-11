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
package org.geomajas.widget.searchandfilter.client.widget.configuredsearch;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.BlurbItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.widget.search.AbstractSearchPanel;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearch;
import org.geomajas.widget.searchandfilter.search.dto.AndCriterion;
import org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;

import java.util.ArrayList;
import java.util.List;

/**
 * Geometric Search Widget. Contains framework + search functionality. But does
 * not create the geometries itself.
 * <p>
 * You add specific search methods through addSearchMethod().
 * 
 * @author Jan Venstermans
 */
public class ConfiguredSearchPanel extends AbstractSearchPanel {

	private final ShapeStyle selectionStyle;

	private final SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	private ConfiguredSearch searchConfig;

	private String layerId;

	private SearchConfigurationCanvas searchConfigurationCanvas;

	private static final String STYLE_SEARCH_ROW = "searchRow";
	private static final String STYLE_SEARCH_HEADER = "searchHeader";
	private static final String STYLE_HEADER_BAR = "headerBar";

	/**
	 * @param mapWidget map widget
	 */
	public ConfiguredSearchPanel(final MapWidget mapWidget) {
		super(mapWidget);
		this.mapWidget = mapWidget;

		selectionStyle = new ShapeStyle();
		selectionStyle.setFillColor("#FFFF00");
		selectionStyle.setFillOpacity(0.3f);
		selectionStyle.setStrokeColor("#B45F04");
		selectionStyle.setStrokeOpacity(0.9f);
		selectionStyle.setStrokeWidth(2f);

		/*VLayout layout = new VLayout(0);
		layout.setWidth100();*/
		/*layout.setWidth(GsfLayout.geometricSearchPanelTabWidth);
		layout.setHeight(GsfLayout.geometricSearchPanelTabHeight);*/
		/*layout.setWidth("400");
		layout.setHeight("250");
		addChild(layout);  */

		searchConfigurationCanvas = new SearchConfigurationCanvas(mapWidget.getMapModel());
		addChild(searchConfigurationCanvas);
	}

	public void setSearchConfig(final ConfiguredSearch searchConfig, final String layerId) {
		this.searchConfig = searchConfig;
		this.layerId = layerId;
		searchConfigurationCanvas.setSearchConfig(searchConfig.getAttributes(), layerId);
	}

	@Override
	public boolean validate() {
		return false;
	}

	@Override
	public Criterion getFeatureSearchCriterion() {
		return searchConfigurationCanvas.getSearchCriteria();
	}

	@Override
	public VectorLayer getFeatureSearchVectorLayer() {
		return searchConfigurationCanvas.getLayer();
	}

	@Override
	public void reset() {
	   searchConfigurationCanvas.empty();
	}

	@Override
	public void initialize(Criterion featureSearch) {
		// should not be used?
	}


	// ----------------------------------------------------------

	/**
	 * Adapted from {@link org.geomajas.widget.searchandfilter.client.widget.configuredsearch
	 * .ConfiguredSearchPanel.SearchConfigurationCanvas}
	 * by Pieter De Graef
	 * and from {@link org.geomajas.widget.searchandfilter.client.widget.attributesearch
	 * .AttributeSearchPanel.FeatureSearch}
	 * by Kristof Heirwegh.
	 *
	 * @author Jan Venstermans
	 */
	private class SearchConfigurationCanvas extends Canvas {

		// The vector layer to search in.
		private VectorLayer layer;

		// A form item that shows the user what layer he's searching in.
		private FormItem selectedLayer;
		// Logical operator; match one or match all criteria?
		private RadioGroupItem logicalOperatorRadio;

		private VStack criterionStack;

		private MapModel mapModel;

		private List<ConfiguredSearchAttributeCriterionPane> criterionPanes;

		private List<ConfiguredSearchAttribute> searchAttributes;

		// -------------------------------------------------------------------------
		// Constructors:
		// -------------------------------------------------------------------------

		/**
		 * Create a search widget for searching in a specific map model. This
		 * widget will automatically react to the selection of layers within
		 * that map model, and redraw to compensate for the selected layer. In
		 * other words, searching always happens on the selected layer.
		 *
		 * @param mapModel
		 *            The MapModel containing the possible layer to search in.
		 *
		 */
		public SearchConfigurationCanvas(MapModel mapModel) {
			super();
			this.mapModel = mapModel;
			criterionPanes = new ArrayList<ConfiguredSearchAttributeCriterionPane>();
			buildUI();

		}


		// -------------------------------------------------------------------------
		// Public methods:
		// -------------------------------------------------------------------------

		/**
		 * @param searchAttributes
		 * @param layerId  fixed layerId
		 */
		public void setSearchConfig(final List<ConfiguredSearchAttribute> searchAttributes, final String layerId) {
			// set the layer
			if (null != layerId) {
				if (mapModel.isInitialized()) {
					setLayer(layerId);
				} else {
					mapModel.addMapModelHandler(new MapModelHandler() {
						public void onMapModelChange(MapModelEvent event) {
							setLayer(layerId);
						}
					});
				}
			}
			// set the search criteria
			this.searchAttributes = searchAttributes;
			for (int i = 0 ; i < searchAttributes.size() ; i++) {
				addRow(i, searchAttributes.get(i));
			}
		}

		private void addRow(final int index, ConfiguredSearchAttribute ac) {
			if (null == layer) {
				return;
			}

			// Empty row:
			ConfiguredSearchAttributeCriterionPane newRow = new ConfiguredSearchAttributeCriterionPane(layer);
			newRow.setHeight(32);
			newRow.setStyleName(STYLE_SEARCH_ROW);

			// Add to the stacks:
			criterionStack.addMember(newRow, index + 1);

			// Add to the lists:
			criterionPanes.add(index, newRow);

			if (ac != null) {
				newRow.setSearchCriterion(ac);
			}
		}

		/*public void setSearchCriteria(Criterion criterion) {
			if (criterion != null) {
				List<Criterion> criteria;
				VectorLayer vl = null;
				if (criterion instanceof AndCriterion) {
					criteria = ((AndCriterion) criterion).getCriteria();
				} else {
					SC.warn(MESSAGES.attributeSearchWidgetNoValidCriterionUnsupportedType());
					return;
				}

				for (int i = 0; i < criteria.size(); i++) {
					AttributeCriterion ac = (AttributeCriterion) criteria.get(i);
					if (vl == null) {
						List<VectorLayer> layers = mapModel.getVectorLayersByServerId(ac.getServerLayerId());
						if (layers == null || layers.size() < 1) {
							SC.warn(MESSAGES.attributeSearchWidgetNoValidCriterionNoLayer());
							return;
						}
						vl = layers.get(0);
						setLayer(vl);
						empty(false);
					}
					addNewRow(i, ac);
				}
			}
		}*/

		/**
		 * Get the full list of search criteria from the criterion grid.
		 *
		 * @return entered search criteria
		 */
		public Criterion getSearchCriteria() {
			if (layer != null) {
				Criterion criterion = new AndCriterion();
				List<Criterion> criteria = ((AndCriterion) criterion).getCriteria();
				for (ConfiguredSearchAttributeCriterionPane criterionPane : criterionPanes) {
					if (criterionPane.hasErrors()) {
						SC.warn(I18nProvider.getSearch().warningInvalidCriteria());
						return null;
					}
					AttributeCriterion ac = criterionPane.getSearchCriterion();
					if (ac != null) {
						criteria.add(ac);
					}
				}
				if (criteria == null || criteria.size() == 0) {
					SC.warn(messages.attributeSearchWidgetNoValidCriterionNoCriteria());
					return null;
				}
				return criterion;
			} else {
				SC.warn(messages.attributeSearchWidgetNoLayerSelected());
				return null;
			}
		}

		/**
		 * Empty the grid, thereby removing all rows.
		 *
		 */
		public void empty() {
			for (ConfiguredSearchAttributeCriterionPane criterionPane : criterionPanes) {
				criterionStack.removeMember(criterionPane);
			}
			criterionPanes.clear();
		}

		// -------------------------------------------------------------------------
		// Getters and setters:
		// -------------------------------------------------------------------------

		/**
		 * Return the layer onto which searching should happen.
		 *
		 * @return layer to search
		 */
		public VectorLayer getLayer() {
			return layer;
		}

		/**
		 * Set a new layer onto which searching should happen.
		 *
		 * @param layer layer
		 */
		public void setLayer(VectorLayer layer) {
			this.layer = layer;
			empty();
		}

		/**
		 * Set a new layer onto which searching should happen.
		 *
		 * @param layerId layer id
		 */
		public void setLayer(String layerId) {
			setLayer(mapModel.getVectorLayer(layerId));
			selectedLayer.setValue(layer.getLabel());
		}

		// -------------------------------------------------------------------------
		// Private methods:
		// -------------------------------------------------------------------------

		private void updateLayerList() {
			List<String> layers = new ArrayList<String>();
			for (Layer<?> vLayer : mapModel.getLayers()) {
				if (vLayer instanceof VectorLayer) {
					layers.add(vLayer.getLabel());
				}
			}
		}

		private void buildUI() {
			// Create the layout:
			VLayout layout = new VLayout();
			layout.setWidth100();

			HLayout optionLayout = new HLayout();
			optionLayout.setHeight(50);
			optionLayout.setWidth100();

			VLayout leftLayout = new VLayout();
			leftLayout.setAlign(Alignment.LEFT);

			/* Layer name indication */
			HLayout layerLayout = new HLayout();
			layerLayout.setWidth(420);
			DynamicForm layerForm = new DynamicForm();
			layerForm.setHeight(30);
			selectedLayer = new BlurbItem();
			selectedLayer.setShowTitle(true);
			selectedLayer.setTitle(I18nProvider.getSearch().labelLayerSelected());
			selectedLayer.setWidth(250);
			selectedLayer.setValue("<b>" + I18nProvider.getSearch().labelNoLayerSelected() + "</b>");
			layerForm.setFields(selectedLayer);
			layerLayout.addMember(layerForm);
			leftLayout.addMember(layerLayout);

			/* AND OR selection */
			/*DynamicForm logicalForm = new DynamicForm();
			logicalOperatorRadio = new RadioGroupItem("logicalOperator");
			logicalOperatorRadio.setValueMap(I18nProvider.getSearch().radioOperatorOr(), I18nProvider.getSearch()
					.radioOperatorAnd());
			logicalOperatorRadio.setVertical(false);
			logicalOperatorRadio.setRequired(true);
			logicalOperatorRadio.setAlign(Alignment.LEFT);
			logicalOperatorRadio.setWidth(250);
			logicalOperatorRadio.setShowTitle(false);
			logicalForm.setAutoWidth();
			logicalForm.setLayoutAlign(Alignment.CENTER);
			logicalForm.setFields(logicalOperatorRadio);
			leftLayout.setWidth(420);
			leftLayout.addMember(logicalForm); */

			optionLayout.addMember(leftLayout);
			optionLayout.addMember(new LayoutSpacer());

			// Create a header for the criterionStack:
			HLayout headerLayout = new HLayout();
			headerLayout.setHeight(26);
			headerLayout.setStyleName(STYLE_HEADER_BAR);
			HTMLPane attrHeader = new HTMLPane();
			attrHeader.setStyleName(STYLE_SEARCH_HEADER);
			attrHeader.setContents("Attribute");
			attrHeader.setWidth(140);
			HTMLPane operatorHeader = new HTMLPane();
			operatorHeader.setContents("Operator");
			operatorHeader.setWidth(140);
			operatorHeader.setStyleName(STYLE_SEARCH_HEADER);
			HTMLPane valueHeader = new HTMLPane();
			valueHeader.setContents("Value");
			valueHeader.setStyleName(STYLE_SEARCH_HEADER);
			valueHeader.setWidth(150);

			criterionStack = new VStack();
			criterionStack.setAlign(VerticalAlignment.TOP);
			headerLayout.addMember(attrHeader);
			headerLayout.addMember(operatorHeader);
			headerLayout.addMember(valueHeader);
			criterionStack.addMember(headerLayout);

			HLayout searchGrid = new HLayout();
			searchGrid.addMember(criterionStack);
			searchGrid.setBorder("1px solid lightgrey");
			searchGrid.setHeight(1);
			searchGrid.setOverflow(Overflow.VISIBLE);

			layout.addMember(optionLayout);
			layout.addMember(searchGrid);
			addChild(layout);
		}
	}
}
