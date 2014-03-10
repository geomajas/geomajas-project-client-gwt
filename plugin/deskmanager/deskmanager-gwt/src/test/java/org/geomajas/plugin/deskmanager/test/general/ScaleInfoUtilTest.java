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
package org.geomajas.plugin.deskmanager.test.general;

import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.internal.configuration.ConfigurationDtoPostProcessor;
import org.geomajas.plugin.deskmanager.client.gwt.manager.util.ScaleInfoUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Oliver May
 * 
 */
public class ScaleInfoUtilTest {

	private static final double MAP_UNIT_IN_PIXELS = 2.0;

	private static final double TEST_SCALE = 1000;

	private static final double DELTA = 1e-15;

	private ConfigurationDtoPostProcessor dtoConfigurationDtoPostProcessor = new ConfigurationDtoPostProcessor();

	@Test
	public void testScaleInfoUpdates() {
		ScaleInfo scaleInfo = new ScaleInfo(1, TEST_SCALE);
		dtoConfigurationDtoPostProcessor.completeScale(scaleInfo, MAP_UNIT_IN_PIXELS);

		// Denominator

		ScaleInfoUtil.changeDenominator(scaleInfo, scaleInfo.getDenominator() * 0.179351932823612021383456502198);
		Assert.assertTrue(scaleInfo.getDenominator() != TEST_SCALE);

		ScaleInfoUtil.changeDenominator(scaleInfo, scaleInfo.getDenominator() / 0.179351932823612021383456502198);

		Assert.assertEquals(TEST_SCALE, scaleInfo.getDenominator(), DELTA);

		ScaleInfoUtil.changeDenominator(scaleInfo, scaleInfo.getDenominator() * 123456789);
		Assert.assertTrue(scaleInfo.getDenominator() != TEST_SCALE);

		ScaleInfoUtil.changeDenominator(scaleInfo, scaleInfo.getDenominator() / 123456789);

		Assert.assertEquals(TEST_SCALE, scaleInfo.getDenominator(), DELTA);

		// PixelPerUnit

		ScaleInfoUtil.changePixelPerUnit(scaleInfo, scaleInfo.getPixelPerUnit() * 0.179351932823612021383456502198);
		Assert.assertTrue(scaleInfo.getDenominator() != TEST_SCALE);

		ScaleInfoUtil.changePixelPerUnit(scaleInfo, scaleInfo.getPixelPerUnit() / 0.179351932823612021383456502198);

		Assert.assertEquals(TEST_SCALE, scaleInfo.getDenominator(), DELTA);

		ScaleInfoUtil.changePixelPerUnit(scaleInfo, scaleInfo.getPixelPerUnit() * 123456789);
		Assert.assertTrue(scaleInfo.getDenominator() != TEST_SCALE);

		ScaleInfoUtil.changePixelPerUnit(scaleInfo, scaleInfo.getPixelPerUnit() / 123456789);

		Assert.assertEquals(TEST_SCALE, scaleInfo.getDenominator(), DELTA);

	}
}
