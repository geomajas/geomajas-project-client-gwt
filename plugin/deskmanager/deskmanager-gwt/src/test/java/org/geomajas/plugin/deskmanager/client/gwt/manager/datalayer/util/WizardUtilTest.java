package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.util;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Test class for {@link WizardUtil}.
 *
 * @author Jan Venstermans
 */
public class WizardUtilTest {

	/* urlQueryToMap */

	@Test
	public void testUrlQueryToMap() throws Exception {
		Map<String, String> parameters = WizardUtil.urlQueryToMap("service=wfs&version=2.0.0&request=GetCapabilities");

		Assert.assertTrue(parameters.containsKey("service"));
		Assert.assertEquals("wfs", parameters.get("service"));
		Assert.assertTrue(parameters.containsKey("version"));
		Assert.assertEquals("2.0.0", parameters.get("version"));
		Assert.assertTrue(parameters.containsKey("request"));
		Assert.assertEquals("GetCapabilities", parameters.get("request"));
	}

	@Test
	public void testUrlQueryToMapWithNullValues() throws Exception {
		Map<String, String> parameters = WizardUtil.urlQueryToMap("service=&version=2.0.0&request=");

		Assert.assertTrue(parameters.containsKey("service"));
		Assert.assertNull(parameters.get("service"));
		Assert.assertTrue(parameters.containsKey("version"));
		Assert.assertEquals("2.0.0", parameters.get("version"));
		Assert.assertTrue(parameters.containsKey("request"));
		Assert.assertNull(parameters.get("request"));
	}

	@Test
	public void testUrlQueryToMapWithEmptyInput() throws Exception {
		Map<String, String> parameters = WizardUtil.urlQueryToMap("");

		Assert.assertNotNull(parameters);
		Assert.assertEquals(0, parameters.size());
	}

	@Test
	public void testUrlQueryToMapWithNullInput() throws Exception {
		Map<String, String> parameters = WizardUtil.urlQueryToMap(null);

		Assert.assertNull(parameters);
	}

	/* getQueryPartOfUrl */

	@Test
	public void testGetQueryPartOfUrl() throws Exception {
		String queryPart = "service=wfs&version=2.0.0&request=GetCapabilities";
		String input = "http://apps.geomajas.org/geoserver/ows?" + queryPart;

		String queryResult = WizardUtil.getQueryPartOfUrl(input);

		Assert.assertEquals(queryPart, queryResult);
	}

	@Test
	public void testGetQueryPartOfUrlEmpty() throws Exception {
		String input = "http://apps.geomajas.org/geoserver/ows?";

		String queryResult = WizardUtil.getQueryPartOfUrl(input);

		Assert.assertEquals("", queryResult);
	}

	@Test
	public void testGetQueryPartOfUrlNon() throws Exception {
		String input = "http://apps.geomajas.org/geoserver/ows";

		String queryResult = WizardUtil.getQueryPartOfUrl(input);

		Assert.assertNull(queryResult);
	}

	/* getBaseOfUrl */

	@Test
	public void testGetBaseOfUrl() throws Exception {
		String baseUrl = "http://apps.geomajas.org/geoserver/ows";

		String baseResult1 = WizardUtil.getBaseOfUrl(baseUrl);
		String baseResult2 = WizardUtil.getBaseOfUrl(baseUrl + "?");
		String baseResult3 = WizardUtil.getBaseOfUrl(baseUrl + "?service=wfs&version=2.0.0&request=GetCapabilities");

		Assert.assertEquals(baseUrl, baseResult1);
		Assert.assertEquals(baseUrl, baseResult2);
		Assert.assertEquals(baseUrl, baseResult3);
	}

	/* WfsGetCapabilities */

	@Test
	public void testWfsGetCapabilitiesFullUrl() throws Exception {
		String input = "http://apps.geomajas.org/geoserver/ows?service=wfs&version=2.0.0&request=GetCapabilities";
		String expectedOutput = "http://apps.geomajas.org/geoserver/ows?service=wfs&version=2.0.0&request=GetCapabilities";

		assertWfsGetCapabilitiesCreation(input, expectedOutput);
	}

	@Test
	public void testWfsGetCapabilitiesFullUrlOrderParametersNotImportant() throws Exception {
		String input = "http://apps.geomajas.org/geoserver/ows?service=wfs&version=2.0.0&request=GetCapabilities";
		String expectedOutput = "http://apps.geomajas.org/geoserver/ows?version=2.0.0&request=GetCapabilities&service=wfs";

		assertWfsGetCapabilitiesCreation(input, expectedOutput);
	}

	@Test
	public void testWfsGetCapabilitiesBaseUrlOnly() throws Exception {
		String input = "http://apps.geomajas.org/geoserver/ows";
		String expectedOutput = "http://apps.geomajas.org/geoserver/ows?service=wfs&request=GetCapabilities&version=1.0.0";

		assertWfsGetCapabilitiesCreation(input, expectedOutput);
	}

	@Test
	public void testWfsGetCapabilitiesVersionOnly() throws Exception {
		String input = "http://apps.geomajas.org/geoserver/ows?version=2.0.0";
		String expectedOutput = "http://apps.geomajas.org/geoserver/ows?service=wfs&request=GetCapabilities&version=2.0.0";

		assertWfsGetCapabilitiesCreation(input, expectedOutput);
	}

	@Test(expected = GetCapabilitiesIllegalArgumentException.class)
	public void testWfsGetCapabilitiesIncorrectService() throws Exception {
		String input = "http://apps.geomajas.org/geoserver/ows?service=wms";

		WizardUtil.constructWfsGetCapabilities(input);
	}

	@Test(expected = GetCapabilitiesIllegalArgumentException.class)
	public void testWfsGetCapabilitiesIncorrectRequest() throws Exception {
		String input = "http://apps.geomajas.org/geoserver/ows?request=GetCapability";

		WizardUtil.constructWfsGetCapabilities(input);
	}

	@Test
	public void testWfsGetCapabilitiesOtherCasingDefaultKey() throws Exception {
		String input = "http://apps.geomajas.org/geoserver/ows?REQUEST=GetCapabilities";
		String expectedOutput = "http://apps.geomajas.org/geoserver/ows?service=wfs&request=GetCapabilities&version=1.0.0";

		assertWfsGetCapabilitiesCreation(input, expectedOutput);
	}

	@Test
	public void testWfsGetCapabilitiesOtherCasingDefaultValue() throws Exception {
		String input = "http://apps.geomajas.org/geoserver/ows?request=getcapabilities";
		String expectedOutput = "http://apps.geomajas.org/geoserver/ows?service=wfs&request=GetCapabilities&version=1.0.0";

		assertWfsGetCapabilitiesCreation(input, expectedOutput);
	}

	/* WmsCapabilities */

	@Test
	public void testWmsGetCapabilitiesFullUrl() throws Exception {
		String input = "http://apps.geomajas.org/geoserver/ows?service=wms&version=1.3.0&request=GetCapabilities";
		String expectedOutput = "http://apps.geomajas.org/geoserver/ows?service=wms&version=1.3.0&request=GetCapabilities";

		assertWmsGetCapabilitiesCreation(input, expectedOutput);
	}

	@Test
	public void testWmsGetCapabilitiesFullUrlOrderParametersNotImportant() throws Exception {
		String input = "http://apps.geomajas.org/geoserver/ows?service=wms&version=1.3.0&request=GetCapabilities";
		String expectedOutput = "http://apps.geomajas.org/geoserver/ows?version=1.3.0&request=GetCapabilities&service=wms";

		assertWmsGetCapabilitiesCreation(input, expectedOutput);
	}

	@Test
	public void testWmsGetCapabilitiesBaseUrlOnly() throws Exception {
		String input = "http://apps.geomajas.org/geoserver/ows";
		String expectedOutput = "http://apps.geomajas.org/geoserver/ows?service=wms&request=GetCapabilities&version=1.1.1";

		assertWmsGetCapabilitiesCreation(input, expectedOutput);
	}

	@Test
	public void testWmsGetCapabilitiesVersionOnly() throws Exception {
		String input = "http://apps.geomajas.org/geoserver/ows?version=1.3.0";
		String expectedOutput = "http://apps.geomajas.org/geoserver/ows?service=wms&request=GetCapabilities&version=1.3.0";

		assertWmsGetCapabilitiesCreation(input, expectedOutput);
	}

	@Test(expected = GetCapabilitiesIllegalArgumentException.class)
	public void testWmsGetCapabilitiesIncorrectService() throws Exception {
		String input = "http://apps.geomajas.org/geoserver/ows?service=wfs";

		WizardUtil.constructWmsGetCapabilities(input);
	}

	@Test(expected = GetCapabilitiesIllegalArgumentException.class)
	public void testWmsGetCapabilitiesIncorrectRequest() throws Exception {
		String input = "http://apps.geomajas.org/geoserver/ows?request=GetCapability";

		WizardUtil.constructWmsGetCapabilities(input);
	}

	/* private methods */

	private void assertWfsGetCapabilitiesCreation(String input, String expectedOutput) {
		String resultUrl = WizardUtil.constructWfsGetCapabilities(input);
		assertEqualityUrlsForBaseAndQuery(expectedOutput, resultUrl);
	}

	private void assertWmsGetCapabilitiesCreation(String input, String expectedOutput) {
		String resultUrl = WizardUtil.constructWmsGetCapabilities(input);
		assertEqualityUrlsForBaseAndQuery(expectedOutput, resultUrl);
	}

	private void assertEqualityUrlsForBaseAndQuery(String expectedOutput, String output) {
		Assert.assertEquals(WizardUtil.getBaseOfUrl(expectedOutput), WizardUtil.getBaseOfUrl(output));
		Map<String, String> expectedQueryMap = WizardUtil.urlQueryToMap(WizardUtil.getQueryPartOfUrl(expectedOutput));
		Map<String, String> outputQueryMap = WizardUtil.urlQueryToMap(WizardUtil.getQueryPartOfUrl(output));
		if (expectedQueryMap == null) {
			Assert.assertNull(outputQueryMap);
		} else {
			Assert.assertEquals(expectedQueryMap.size(), outputQueryMap.size());
			for (Map.Entry<String, String> expectedEntry : expectedQueryMap.entrySet()) {
				Assert.assertTrue(outputQueryMap.containsKey(expectedEntry.getKey()));
				Assert.assertEquals(expectedEntry.getValue(), outputQueryMap.get(expectedEntry.getKey()));
			}
		}
	}
}