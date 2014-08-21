package org.geomajas.plugin.deskmanager.client.gwt.manager.util;

import junit.framework.Assert;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.GeometryAttributeInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Geometry;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.deskmanager.command.manager.dto.DynamicVectorLayerConfiguration;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.LineSymbolizerInfo;
import org.geomajas.sld.PointSymbolizerInfo;
import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.expression.FunctionTypeInfo;
import org.geomajas.sld.expression.LiteralTypeInfo;
import org.geomajas.sld.expression.PropertyNameInfo;
import org.geomajas.sld.filter.BinaryLogicOpTypeInfo;
import org.geomajas.sld.filter.FilterTypeInfo;
import org.geomajas.sld.filter.OrInfo;
import org.geomajas.sld.filter.PropertyIsEqualToInfo;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class containing the mock objects for the views and services uses in User/Groups presenters.
 *
 * @author Jan Venstermans
 */
//@RunWith(GwtMockitoTestRunner.class)
public class SldUtilsTest {

	private DynamicVectorLayerConfiguration dynamicVectorLayerConfiguration;

	private VectorLayerInfo vectorLayerInfo;
	private FeatureInfo featureInfo;
	private GeometryAttributeInfo geometryAttributeInfo;
	private ClientVectorLayerInfo clientVectorLayerInfo;
	private NamedStyleInfo namedStyleInfo;

	@Before
	public void createBasicDynamicVectorLayerConfiguration() throws Exception {
		// construct
		dynamicVectorLayerConfiguration = new DynamicVectorLayerConfiguration();
		clientVectorLayerInfo = new ClientVectorLayerInfo();
		vectorLayerInfo = new VectorLayerInfo();
		geometryAttributeInfo = new GeometryAttributeInfo();
		featureInfo = new FeatureInfo();
		namedStyleInfo = new NamedStyleInfo();

		// link
		featureInfo.setGeometryType(geometryAttributeInfo);
		vectorLayerInfo.setFeatureInfo(featureInfo);
		dynamicVectorLayerConfiguration.setClientVectorLayerInfo(clientVectorLayerInfo);
		dynamicVectorLayerConfiguration.setVectorLayerInfo(vectorLayerInfo);
		clientVectorLayerInfo.setNamedStyleInfo(namedStyleInfo);
	}

	@Test
	public void testCreateSimpleSldStyleLayerTypePoint() throws Exception {
		String exampleName = "exampleName";
		namedStyleInfo.setName(exampleName);
		vectorLayerInfo.setLayerType(LayerType.POINT);

		Map<String, Object> properties = new HashMap<String, Object>();

		UserStyleInfo userStyleInfo = SldUtils.createSimpleSldStyle(dynamicVectorLayerConfiguration, properties);

		Assert.assertNotNull(userStyleInfo);
		Assert.assertNotNull(userStyleInfo.getFeatureTypeStyleList());
		Assert.assertEquals(1, userStyleInfo.getFeatureTypeStyleList().size());
		FeatureTypeStyleInfo featureTypeStyleInfo = userStyleInfo.getFeatureTypeStyleList().get(0);
		Assert.assertEquals(exampleName, featureTypeStyleInfo.getName());

		Assert.assertEquals(1, featureTypeStyleInfo.getRuleList().size());
		assertRuleWithoudChoice(featureTypeStyleInfo.getRuleList().get(0), "default", vectorLayerInfo.getLayerType());
	}

	@Test
	public void testCreateSimpleSldStyleLayerTypeLine() throws Exception {
		String exampleName = "exampleName";
		namedStyleInfo.setName(exampleName);
		vectorLayerInfo.setLayerType(LayerType.LINESTRING);

		Map<String, Object> properties = new HashMap<String, Object>();

		UserStyleInfo userStyleInfo = SldUtils.createSimpleSldStyle(dynamicVectorLayerConfiguration, properties);

		Assert.assertNotNull(userStyleInfo);
		Assert.assertNotNull(userStyleInfo.getFeatureTypeStyleList());
		Assert.assertEquals(1, userStyleInfo.getFeatureTypeStyleList().size());
		FeatureTypeStyleInfo featureTypeStyleInfo = userStyleInfo.getFeatureTypeStyleList().get(0);
		Assert.assertEquals(exampleName, featureTypeStyleInfo.getName());

		Assert.assertEquals(1, featureTypeStyleInfo.getRuleList().size());
		assertRuleWithoudChoice(featureTypeStyleInfo.getRuleList().get(0), "default", vectorLayerInfo.getLayerType());
	}

	@Test
	public void testCreateSimpleSldStyleLayerTypePolygon() throws Exception {
		String exampleName = "exampleName";
		namedStyleInfo.setName(exampleName);
		vectorLayerInfo.setLayerType(LayerType.POLYGON);

		Map<String, Object> properties = new HashMap<String, Object>();

		UserStyleInfo userStyleInfo = SldUtils.createSimpleSldStyle(dynamicVectorLayerConfiguration, properties);

		Assert.assertNotNull(userStyleInfo);
		Assert.assertNotNull(userStyleInfo.getFeatureTypeStyleList());
		Assert.assertEquals(1, userStyleInfo.getFeatureTypeStyleList().size());
		FeatureTypeStyleInfo featureTypeStyleInfo = userStyleInfo.getFeatureTypeStyleList().get(0);
		Assert.assertEquals(exampleName, featureTypeStyleInfo.getName());

		Assert.assertEquals(1, featureTypeStyleInfo.getRuleList().size());
		assertRuleWithoudChoice(featureTypeStyleInfo.getRuleList().get(0), "default", vectorLayerInfo.getLayerType());
	}

	@Test
	public void testCreateSimpleSldStyleGeometryLayerWithStyleName() throws Exception {
		String exampleName = "exampleName";
		String geometryName = "geometryName";
		String styleName = "styleName";
		geometryAttributeInfo.setName(geometryName);
		namedStyleInfo.setName(exampleName);
		vectorLayerInfo.setLayerType(LayerType.GEOMETRY);
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(SldUtils.STYLENAME, styleName);

		UserStyleInfo userStyleInfo = SldUtils.createSimpleSldStyle(dynamicVectorLayerConfiguration, properties);

		Assert.assertNotNull(userStyleInfo);
		Assert.assertNotNull(userStyleInfo.getFeatureTypeStyleList());
		Assert.assertEquals(1, userStyleInfo.getFeatureTypeStyleList().size());
		FeatureTypeStyleInfo featureTypeStyleInfo = userStyleInfo.getFeatureTypeStyleList().get(0);
		Assert.assertEquals(styleName, featureTypeStyleInfo.getName());

		Assert.assertEquals(3, featureTypeStyleInfo.getRuleList().size());
		assertRuleInfoWithChoice(featureTypeStyleInfo.getRuleList().get(0), styleName, LayerType.POINT,
				geometryName, "_point",
				new String[]{Geometry.POINT, Geometry.MULTI_POINT});
		assertRuleInfoWithChoice(featureTypeStyleInfo.getRuleList().get(1), styleName, LayerType.LINESTRING,
				geometryName, "_line",
				new String[]{Geometry.LINE_STRING, Geometry.MULTI_LINE_STRING});
		assertRuleInfoWithChoice(featureTypeStyleInfo.getRuleList().get(2), styleName, LayerType.POLYGON,
				geometryName, "_polygon",
				new String[]{Geometry.POLYGON, Geometry.MULTI_POLYGON});
	}

	@Test
	public void testCreateSimpleSldStyleGeometryLayerNoStyleName() throws Exception {
		String exampleName = "exampleName";
		String geometryName = "geometryName";
		geometryAttributeInfo.setName(geometryName);
		namedStyleInfo.setName(exampleName);
		vectorLayerInfo.setLayerType(LayerType.GEOMETRY);
		Map<String, Object> properties = new HashMap<String, Object>();

		UserStyleInfo userStyleInfo = SldUtils.createSimpleSldStyle(dynamicVectorLayerConfiguration, properties);

		Assert.assertNotNull(userStyleInfo);
		Assert.assertNotNull(userStyleInfo.getFeatureTypeStyleList());
		Assert.assertEquals(1, userStyleInfo.getFeatureTypeStyleList().size());
		FeatureTypeStyleInfo featureTypeStyleInfo = userStyleInfo.getFeatureTypeStyleList().get(0);
		Assert.assertEquals(exampleName, featureTypeStyleInfo.getName()); // I don't think this is absolutely necessary
		Assert.assertEquals(3, featureTypeStyleInfo.getRuleList().size());
		assertRuleInfoWithChoice(featureTypeStyleInfo.getRuleList().get(0), "default", LayerType.POINT,
				geometryName, "_point",
				new String[]{Geometry.POINT, Geometry.MULTI_POINT});
		assertRuleInfoWithChoice(featureTypeStyleInfo.getRuleList().get(1), "default", LayerType.LINESTRING,
				geometryName, "_line",
				new String[]{Geometry.LINE_STRING, Geometry.MULTI_LINE_STRING});
		assertRuleInfoWithChoice(featureTypeStyleInfo.getRuleList().get(2), "default", LayerType.POLYGON,
				geometryName, "_polygon",
				new String[]{Geometry.POLYGON, Geometry.MULTI_POLYGON});
	}

	private void assertRuleWithoudChoice(RuleInfo ruleInfo, String styleName, LayerType layerType) {
		// name of the rule
		Assert.assertEquals(styleName, ruleInfo.getName());

		// assert choice
		Assert.assertNull(ruleInfo.getChoice());

		// assert symbolizers
		Assert.assertEquals(1, ruleInfo.getSymbolizerList().size());
		assertSymbolizer(ruleInfo.getSymbolizerList().get(0), layerType);
	}

	private void assertRuleInfoWithChoice(RuleInfo ruleInfo, String styleName,  LayerType layerType,
										  String geometryName, String suffix, String[] geometryTypes) {
		// name of the rule
		Assert.assertTrue(ruleInfo.getName().endsWith(suffix));
		Assert.assertTrue(ruleInfo.getName().startsWith(styleName));

		// assert choice of point rule
		Assert.assertNotNull(ruleInfo.getChoice());
		RuleInfo.ChoiceInfo pointChoiceInfo = ruleInfo.getChoice();
		Assert.assertTrue(pointChoiceInfo.ifFilter());
		FilterTypeInfo pointFilterTypeInfo = pointChoiceInfo.getFilter();
		Assert.assertNotNull(pointFilterTypeInfo.getLogicOps());
		Assert.assertTrue(pointFilterTypeInfo.getLogicOps() instanceof OrInfo);
		OrInfo pointLogicOpsTypeInfo = (OrInfo) pointFilterTypeInfo.getLogicOps();
		Assert.assertEquals(geometryTypes.length, pointLogicOpsTypeInfo.getChoiceList().size());
		for (int i = 0 ; i < geometryTypes.length; i++) {
			assertBinaryLogicGeometryType(pointLogicOpsTypeInfo.getChoiceList().get(i), geometryTypes[i], geometryName);
		}

		// assert symbolizers
		Assert.assertEquals(1, ruleInfo.getSymbolizerList().size());
		assertSymbolizer(ruleInfo.getSymbolizerList().get(0), layerType);
	}

	private void assertSymbolizer(SymbolizerTypeInfo symbolizerTypeInfo, LayerType layerType) {
		switch (layerType) {
			case POINT:
				Assert.assertTrue(symbolizerTypeInfo instanceof PointSymbolizerInfo);
				// TODO: extend test
				break;
			case LINESTRING:
				Assert.assertTrue(symbolizerTypeInfo instanceof LineSymbolizerInfo);
				// TODO: extend test
				break;
			case MULTILINESTRING:
				Assert.assertTrue(symbolizerTypeInfo instanceof PolygonSymbolizerInfo);
				// TODO: extend test
				break;
		}
	}

	private void assertBinaryLogicGeometryType(BinaryLogicOpTypeInfo.ChoiceInfo binaryChoiceInfo, String geometryType,
											   String geometryName) {
		Assert.assertTrue(binaryChoiceInfo.getComparisonOps() instanceof PropertyIsEqualToInfo);
		PropertyIsEqualToInfo propertyIsEqualToInfo = (PropertyIsEqualToInfo) binaryChoiceInfo.getComparisonOps();
		Assert.assertEquals(2, propertyIsEqualToInfo.getExpressionList().size());
		Assert.assertTrue(propertyIsEqualToInfo.getExpressionList().get(0) instanceof FunctionTypeInfo);
		Assert.assertTrue(propertyIsEqualToInfo.getExpressionList().get(1) instanceof LiteralTypeInfo);

		FunctionTypeInfo functionTypeInfo = (FunctionTypeInfo) propertyIsEqualToInfo.getExpressionList().get(0);
		Assert.assertEquals("", functionTypeInfo.getValue());
		Assert.assertEquals("geometryType", functionTypeInfo.getName());
		Assert.assertEquals(1, functionTypeInfo.getExpressionList().size());
		Assert.assertTrue(functionTypeInfo.getExpressionList().get(0) instanceof PropertyNameInfo);
		PropertyNameInfo propertyNameInfo = (PropertyNameInfo) functionTypeInfo.getExpressionList().get(0);
		Assert.assertEquals(geometryName, propertyNameInfo.getValue());

		LiteralTypeInfo literalTypeInfo = (LiteralTypeInfo) propertyIsEqualToInfo.getExpressionList().get(1);
		Assert.assertEquals(geometryType, literalTypeInfo.getValue());
	}
}
