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
package org.geomajas.widget.searchandfilter.client.util;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute;

/**
 * Util class for {@link org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion} related objects.
 * It contains methods for converting
 * {@link org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion#operator} and enum values of
 * {@link ConfiguredSearchAttribute.Operation} and
 * {@link org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute.InputType} to string values.
 *
 * @author Jan Venstermans
 */
public final class AttributeCriterionUtil {

	private static final SearchAndFilterMessages MESSAGES =
			GWT.create(SearchAndFilterMessages.class);

	private AttributeCriterionUtil() {

	}

	public static String getOperationStringRepresentation(ConfiguredSearchAttribute.AttributeType attributeType,
												   ConfiguredSearchAttribute.Operation operation) {
		if (attributeType != null && operation != null) {
			switch (attributeType) {
			case Integer:
				switch (operation) {
					case EqualTo:
						return MESSAGES.searchAttributeOperationEqualToInteger();
					case LargerThan:
						return MESSAGES.searchAttributeOperationLargerThanInteger();
					case SmallerThan:
						return MESSAGES.searchAttributeOperationSmallerThanInteger();
				}
				break;
			case String:
				switch (operation) {
					case EqualTo:
						return MESSAGES.searchAttributeOperationEqualToString();
					case LargerThan:
						return MESSAGES.searchAttributeOperationLargerThanString();
					case SmallerThan:
						return MESSAGES.searchAttributeOperationSmallerThanString();
				}
				break;
			}
		}
		return null;
	}

	public static String getInputTypeStringRepresentation(ConfiguredSearchAttribute.AttributeType attributeType,
												   ConfiguredSearchAttribute.InputType inputType) {
		if (inputType != null) {
			switch (inputType) {
				case DropDown:
					return MESSAGES.searchAttributeInputTypeDropDownToString();
				case FreeNr:
					return MESSAGES.searchAttributeInputTypeFreeNrToString();
				case FreeText:
					return MESSAGES.searchAttributeInputTypeFreeTextToString();
			}
		}
		return null;
	}

	public static String getLabelFromOperatorCode(String operator) {
		if (operator != null) {
			if ("=".equals(operator)) {
				return I18nProvider.getSearch().operatorEquals();
			} else if ("<>".equals(operator)) {
				return  I18nProvider.getSearch().operatorNotEquals();
			} else if ("<".equals(operator)) {
				return  I18nProvider.getSearch().operatorST();
			} else if ("<=".equals(operator)) {
				return  I18nProvider.getSearch().operatorSE();
			} else if (">".equals(operator)) {
				return  I18nProvider.getSearch().operatorBT();
			} else if (">=".equals(operator)) {
				return  I18nProvider.getSearch().operatorBE();
			} else if ("LIKE".equalsIgnoreCase(operator)) {
				return  I18nProvider.getSearch().operatorContains();
			} else if ("BEFORE".equalsIgnoreCase(operator)) {
				return  I18nProvider.getSearch().operatorBefore();
			} else if ("AFTER".equalsIgnoreCase(operator)) {
				return  I18nProvider.getSearch().operatorAfter();
			}
		}
		return operator;
	}
}
