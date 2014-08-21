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
package org.geomajas.plugin.deskmanager.client.gwt.manager.util;

import com.google.gwt.core.client.GWT;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Geometry;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.deskmanager.command.manager.dto.DynamicVectorLayerConfiguration;
import org.geomajas.sld.CssParameterInfo;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.FillInfo;
import org.geomajas.sld.GraphicInfo;
import org.geomajas.sld.GraphicInfo.ChoiceInfo;
import org.geomajas.sld.LabelInfo;
import org.geomajas.sld.LineSymbolizerInfo;
import org.geomajas.sld.MarkInfo;
import org.geomajas.sld.PointSymbolizerInfo;
import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.SizeInfo;
import org.geomajas.sld.StrokeInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.TextSymbolizerInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.WellKnownNameInfo;
import org.geomajas.sld.expression.FunctionTypeInfo;
import org.geomajas.sld.expression.LiteralTypeInfo;
import org.geomajas.sld.expression.PropertyNameInfo;
import org.geomajas.sld.filter.BinaryLogicOpTypeInfo;
import org.geomajas.sld.filter.FilterTypeInfo;
import org.geomajas.sld.filter.OrInfo;
import org.geomajas.sld.filter.PropertyIsEqualToInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Some sld utilities.
 * 
 * @author Kristof Heirwegh
 */
public final class SldUtils {

	public static final String FILLCOLOR = "fill";
	public static final String FILLOPACITY = "fill-opacity";
	public static final String STROKECOLOR = "stroke";
	public static final String STROKEOPACITY = "stroke-opacity";
	public static final String STROKEWIDTH = "stroke-width";
	public static final String SIZE = "size";
	public static final String WELLKNOWNNAME = "well-known-name"; // MARK
	public static final String STYLENAME = "style-name"; // default = namedStyleInfo.getName();
	public static final String LABELFEATURENAME = "label-field-name";

	public static final String DEFAULT_FILLCOLOR = "#CCCCCC";
	public static final Float DEFAULT_FILLOPACITY = 0.5f;
	public static final String DEFAULT_STROKECOLOR = "#000000";
	public static final Float DEFAULT_STROKEOPACITY = 1f;
	public static final Float DEFAULT_STROKEWIDTH = 1f;
	public static final String DEFAULT_SIZE = "6"; // don't ask
	public static final String DEFAULT_WELLKNOWNNAME = "circle"; // MARK

	private SldUtils() {
	}

	/**
	 * Create a simple SLD style.
	 *
	 * @param dvc the dynamic vector layer configuration
	 * @param properties configuration properties
	 * @return the simple sld style
	 */
	public static UserStyleInfo createSimpleSldStyle(DynamicVectorLayerConfiguration dvc, 
			Map<String, Object> properties) {
		UserStyleInfo usi = new UserStyleInfo();
		usi.setTitle(getPropValue(STYLENAME, properties, dvc.getClientVectorLayerInfo().getNamedStyleInfo().getName()));
		usi.setName(usi.getTitle());
		FeatureTypeStyleInfo fsi = new FeatureTypeStyleInfo();
		fsi.setName(usi.getTitle());
		usi.getFeatureTypeStyleList().add(fsi);
		String geometryName = dvc.getVectorLayerInfo().getFeatureInfo().getGeometryType().getName();

		List<RuleInfo> ri = createRules(dvc.getVectorLayerInfo().getLayerType(), properties, geometryName);
		fsi.getRuleList().addAll(ri);

		return usi;
	}

	/**
	 * Create one or more rules for the given layer type and properties.
	 * (Multi)Point, linestring and polygons return a single rule, geometry returns three rules.
	 * 
	 * @param type the layer type
	 * @param properties the configuration properties
	 * @param geometryName the name of the geometry attribute
	 * @return a list of rules
	 */
	public static List<RuleInfo> createRules(LayerType type, Map<String, Object> properties, String geometryName) {

		List<RuleInfo> rules = new ArrayList<RuleInfo>();

		switch (type) {
		case POINT:
		case MULTIPOINT:
			rules.add(createPointRule(properties));
			break;
		case LINESTRING:
		case MULTILINESTRING:
			rules.add(createLineStringRule(properties));
			break;
		case POLYGON:
		case MULTIPOLYGON:
			rules.add(createPolygonRule(properties));
			break;
		case GEOMETRY:
			RuleInfo pointRule = createRules(LayerType.POINT, properties, geometryName).get(0);
			pointRule.setName(pointRule.getName() + "_point");
			pointRule.setChoice(createChoice(geometryName,
					new String[] {Geometry.POINT, Geometry.MULTI_POINT}));
			rules.add(pointRule);

			RuleInfo lineRule = createRules(LayerType.LINESTRING, properties, geometryName).get(0);
			lineRule.setName(lineRule.getName() + "_line");
			lineRule.setChoice(createChoice(geometryName,
					new String[]{Geometry.LINE_STRING, Geometry.MULTI_LINE_STRING}));
			rules.add(lineRule);

			RuleInfo polygonRule = createRules(LayerType.POLYGON, properties, geometryName).get(0);
			polygonRule.setName(polygonRule.getName() + "_polygon");
			polygonRule.setChoice(createChoice(geometryName,
					new String[] {Geometry.POLYGON, Geometry.MULTI_POLYGON}));
			rules.add(polygonRule);
			break;
		default:
			GWT.log("unsupported geometrytype");
		}


		// -- Add textsymbolizers
		if (properties.containsKey(LABELFEATURENAME)) {
			TextSymbolizerInfo tsi = new TextSymbolizerInfo();
			tsi.setLabel(new LabelInfo());
			PropertyNameInfo pni = new PropertyNameInfo();
			pni.setValue(properties.get(LABELFEATURENAME).toString());
			tsi.getLabel().getExpressionList().add(pni);
			tsi.getLabel().setValue("\n  ");
			for (RuleInfo rule : rules) {
				rule.getSymbolizerList().add(tsi);
			}
		}

		return rules;
	}

	private static RuleInfo createPolygonRule(Map<String, Object> properties) {
		RuleInfo rule = new RuleInfo();
		rule.setName(getPropValue(STYLENAME, properties, "default"));
		PolygonSymbolizerInfo symbolizer = new PolygonSymbolizerInfo();
		symbolizer.setFill(createFill(properties));
		symbolizer.setStroke(createStroke(properties));
		rule.getSymbolizerList().add(symbolizer);
		return rule;
	}

	private static RuleInfo createLineStringRule(Map<String, Object> properties) {
		RuleInfo rule = new RuleInfo();
		rule.setName(getPropValue(STYLENAME, properties, "default"));
		LineSymbolizerInfo symbolizer = new LineSymbolizerInfo();
		symbolizer.setStroke(createStroke(properties));
		rule.getSymbolizerList().add(symbolizer);
		return rule;
	}

	private static RuleInfo createPointRule(Map<String, Object> properties) {
		RuleInfo rule = new RuleInfo();
		rule.setName(getPropValue(STYLENAME, properties, "default"));
		SymbolizerTypeInfo symbolizer = new PointSymbolizerInfo();
		GraphicInfo gi = new GraphicInfo();
		((PointSymbolizerInfo) symbolizer).setGraphic(gi);
		gi.setSize(createSize(properties));
		List<ChoiceInfo> list = new ArrayList<ChoiceInfo>();
		ChoiceInfo choiceInfoGraphic = new ChoiceInfo();
		list.add(choiceInfoGraphic);
		((PointSymbolizerInfo) symbolizer).getGraphic().setChoiceList(list);
		choiceInfoGraphic.setMark(createMark(properties));

		rule.getSymbolizerList().add(symbolizer);
		return rule;
	}

	private static Map<String, Object> getProperties(ClientVectorLayerInfo cvli) {
		return getProperties(cvli.getNamedStyleInfo().getUserStyle());
	}

	/**
	 * This will extract the properties from the sld (first occurence / best effort (not every posibility is checked)).
	 * 
	 * @return map of properties
	 */
	private static Map<String, Object> getProperties(UserStyleInfo usi) {
		Map<String, Object> props = new HashMap<String, Object>();
		if (usi != null) {
			if (usi.getTitle() != null) {
				props.put(STYLENAME, usi.getTitle());
			}
			SymbolizerTypeInfo symbolizer = extractSymbolizer(usi);
			if (symbolizer != null) {
				if (symbolizer instanceof PointSymbolizerInfo) {
					extractProperties((PointSymbolizerInfo) symbolizer, props);
				} else if (symbolizer instanceof LineSymbolizerInfo) {
					extractProperties((LineSymbolizerInfo) symbolizer, props);
				} else if (symbolizer instanceof PolygonSymbolizerInfo) {
					extractProperties((PolygonSymbolizerInfo) symbolizer, props);
				}
			}
			TextSymbolizerInfo textSym = extractTextSymbolizer(usi);
			if (textSym != null && textSym.getLabel() != null && textSym.getLabel().getExpressionList().size() > 0) {
				if (textSym.getLabel().getExpressionList().get(0) instanceof PropertyNameInfo) {
					props.put(LABELFEATURENAME, textSym.getLabel().getExpressionList().get(0).getValue());
				}
			}
		}
		return props;
	}

	// ---------------------------------------------------------------

	private static String getPropValue(String propName, Map<String, Object> properties, String defaultValue) {
		if (properties.containsKey(propName)) {
			return (String) properties.get(propName);
		} else {
			return defaultValue;
		}
	}

	private static Float getPropValue(String propName, Map<String, Object> properties, Float defaultValue) {
		if (properties.containsKey(propName)) {
			return (Float) properties.get(propName);
		} else {
			return defaultValue;
		}
	}

	// ---------------------------------------------------------------

	private static void extractProperties(PointSymbolizerInfo psi, Map<String, Object> properties) {
		if (psi.getGraphic() != null && psi.getGraphic() != null) {
			if (psi.getGraphic().getSize() != null && psi.getGraphic().getSize().getValue() != null) {
				properties.put(SIZE, Float.valueOf(psi.getGraphic().getSize().getValue()));
			}
			if (psi.getGraphic().getChoiceList() != null && psi.getGraphic().getChoiceList().size() > 0) {
				ChoiceInfo ci = psi.getGraphic().getChoiceList().get(0);
				if (ci.getMark() != null) {
					if (ci.getMark().getStroke() != null && ci.getMark().getStroke().getCssParameterList() != null) {
						List<CssParameterInfo> plist = ci.getMark().getStroke().getCssParameterList();
						properties.put(STROKECOLOR, extractCssPropertyValue(STROKECOLOR, plist, DEFAULT_STROKECOLOR));
						properties.put(STROKEOPACITY,
								extractCssPropertyValue(STROKEOPACITY, plist, DEFAULT_STROKEOPACITY));
						properties.put(STROKEWIDTH, extractCssPropertyValue(STROKEWIDTH, plist, DEFAULT_STROKEWIDTH));
					}
					if (ci.getMark().getFill() != null && ci.getMark().getFill().getCssParameterList() != null) {
						List<CssParameterInfo> plist = ci.getMark().getFill().getCssParameterList();
						properties.put(FILLCOLOR, extractCssPropertyValue(FILLCOLOR, plist, DEFAULT_FILLCOLOR));
						properties.put(FILLOPACITY, extractCssPropertyValue(FILLOPACITY, plist, DEFAULT_FILLOPACITY));
					}
					if (ci.getMark().getWellKnownName() != null
							&& ci.getMark().getWellKnownName().getWellKnownName() != null) {
						properties.put(WELLKNOWNNAME, ci.getMark().getWellKnownName().getWellKnownName());
					}
				}
			}
		}
	}

	private static void extractProperties(LineSymbolizerInfo lsi, Map<String, Object> properties) {
		if (lsi.getStroke() != null && lsi.getStroke().getCssParameterList() != null) {
			List<CssParameterInfo> plist = lsi.getStroke().getCssParameterList();
			properties.put(STROKECOLOR, extractCssPropertyValue(STROKECOLOR, plist, DEFAULT_STROKECOLOR));
			properties.put(STROKEOPACITY, extractCssPropertyValue(STROKEOPACITY, plist, DEFAULT_STROKEOPACITY));
			properties.put(STROKEWIDTH, extractCssPropertyValue(STROKEWIDTH, plist, DEFAULT_STROKEWIDTH));
		}
	}

	private static void extractProperties(PolygonSymbolizerInfo psi, Map<String, Object> properties) {
		if (psi.getStroke() != null && psi.getStroke().getCssParameterList() != null) {
			List<CssParameterInfo> plist = psi.getStroke().getCssParameterList();
			properties.put(STROKECOLOR, extractCssPropertyValue(STROKECOLOR, plist, DEFAULT_STROKECOLOR));
			properties.put(STROKEOPACITY, extractCssPropertyValue(STROKEOPACITY, plist, DEFAULT_STROKEOPACITY));
			properties.put(STROKEWIDTH, extractCssPropertyValue(STROKEWIDTH, plist, DEFAULT_STROKEWIDTH));
		}
		if (psi.getFill() != null && psi.getFill().getCssParameterList() != null) {
			List<CssParameterInfo> plist = psi.getFill().getCssParameterList();
			properties.put(FILLCOLOR, extractCssPropertyValue(FILLCOLOR, plist, DEFAULT_FILLCOLOR));
			properties.put(FILLOPACITY, extractCssPropertyValue(FILLOPACITY, plist, DEFAULT_FILLOPACITY));
		}
	}

	private static Object extractCssPropertyValue(String key, List<CssParameterInfo> cssParameterList,
			Object defaultValue) {
		for (CssParameterInfo cpi : cssParameterList) {
			if (cpi.getName().equals(key)) {
				if (defaultValue == null) {
					return null;
				} else if (defaultValue instanceof Float) {
					return Float.valueOf(cpi.getValue());
				} else if (defaultValue instanceof String) {
					return cpi.getValue().toString();
				} else {
					return cpi.getValue();
				}
			}
		}
		return defaultValue;
	}

	private static SymbolizerTypeInfo extractSymbolizer(UserStyleInfo usi) {
		if (usi.getFeatureTypeStyleList() != null && usi.getFeatureTypeStyleList().size() > 0) {
			FeatureTypeStyleInfo ftsi = usi.getFeatureTypeStyleList().get(0);
			if (ftsi.getRuleList() != null && ftsi.getRuleList().size() > 0) {
				RuleInfo ri = ftsi.getRuleList().get(0);
				if (ri.getSymbolizerList() != null && ri.getSymbolizerList().size() > 0) {
					for (SymbolizerTypeInfo sti : ri.getSymbolizerList()) {
						if (!(sti instanceof TextSymbolizerInfo)) {
							return sti;
						}
					}
				}
			}
		}
		return null;
	}

	private static TextSymbolizerInfo extractTextSymbolizer(UserStyleInfo usi) {
		if (usi.getFeatureTypeStyleList() != null && usi.getFeatureTypeStyleList().size() > 0) {
			FeatureTypeStyleInfo ftsi = usi.getFeatureTypeStyleList().get(0);
			if (ftsi.getRuleList() != null && ftsi.getRuleList().size() > 0) {
				RuleInfo ri = ftsi.getRuleList().get(0);
				if (ri.getSymbolizerList() != null && ri.getSymbolizerList().size() > 0) {
					for (SymbolizerTypeInfo sti : ri.getSymbolizerList()) {
						if (sti instanceof TextSymbolizerInfo) {
							return (TextSymbolizerInfo) sti;
						}
					}
				}
			}
		}
		return null;
	}

	private static MarkInfo createMark(Map<String, Object> properties) {
		MarkInfo mi = new MarkInfo();
		mi.setWellKnownName(new WellKnownNameInfo());
		mi.getWellKnownName().setWellKnownName(getPropValue(WELLKNOWNNAME, properties, DEFAULT_WELLKNOWNNAME));
		mi.setFill(createFill(properties));
		mi.setStroke(createStroke(properties));
		return mi;
	}

	private static StrokeInfo createStroke(Map<String, Object> properties) {
		StrokeInfo s = new StrokeInfo();
		s.setStrokeColor(getPropValue(STROKECOLOR, properties, DEFAULT_STROKECOLOR));
		s.setStrokeWidth(getPropValue(STROKEWIDTH, properties, DEFAULT_STROKEWIDTH));
		s.setStrokeOpacity(getPropValue(STROKEOPACITY, properties, DEFAULT_STROKEOPACITY));
		return s;
	}

	private static FillInfo createFill(Map<String, Object> properties) {
		FillInfo f = new FillInfo();
		f.setFillColor(getPropValue(FILLCOLOR, properties, DEFAULT_FILLCOLOR));
		f.setFillOpacity(getPropValue(FILLOPACITY, properties, DEFAULT_FILLOPACITY));
		return f;
	}

	private static SizeInfo createSize(Map<String, Object> properties) {
		SizeInfo si = new SizeInfo();
		si.setValue(getPropValue(SIZE, properties, DEFAULT_SIZE));
		return si;
	}

	private static RuleInfo.ChoiceInfo createChoice(String geometryName, String[] geometryTypes) {
		OrInfo orInfo = new OrInfo();
		for (String geometryType : geometryTypes) {
			BinaryLogicOpTypeInfo.ChoiceInfo choice = new BinaryLogicOpTypeInfo.ChoiceInfo();
			choice.setComparisonOps(createPropertyIsEqualToInfo(geometryName, geometryType));
			orInfo.getChoiceList().add(choice);
		}

		RuleInfo.ChoiceInfo choiceInfo = new RuleInfo.ChoiceInfo();
		choiceInfo.setFilter(new FilterTypeInfo());
		choiceInfo.getFilter().setLogicOps(orInfo);
		return choiceInfo;
	}

	private static PropertyIsEqualToInfo createPropertyIsEqualToInfo(String geometryName, String geometryType) {
		PropertyIsEqualToInfo propertyIsEqualToInfo = new PropertyIsEqualToInfo();
		FunctionTypeInfo functionTypeInfo = new FunctionTypeInfo();
		PropertyNameInfo propertyNameInfo = new PropertyNameInfo();
		propertyNameInfo.setValue(geometryName);
		functionTypeInfo.setValue("");
		functionTypeInfo.setName("geometryType");
		functionTypeInfo.getExpressionList().add(propertyNameInfo);

		LiteralTypeInfo literalTypeInfo = new LiteralTypeInfo();
		literalTypeInfo.setValue(geometryType);
		propertyIsEqualToInfo.getExpressionList().add(functionTypeInfo);
		propertyIsEqualToInfo.getExpressionList().add(literalTypeInfo);
		return propertyIsEqualToInfo;
	}
}
