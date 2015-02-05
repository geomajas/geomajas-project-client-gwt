/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.searchandfilter.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.widgets.form.fields.FormItem;
import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AbstractReadOnlyAttributeInfo;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AssociationType;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion;
import org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute;

import java.util.Date;

/**
 * Util class for {@link org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion} objects.
 * It contains methods for converting
 * {@link org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion#operator} and enum values of
 * {@link ConfiguredSearchAttribute.Operation} and
 * {@link org.geomajas.widget.searchandfilter.search.dto.ConfiguredSearchAttribute.InputType} to string values.
 *
 * @author Jan Venstermans
 */
public final class AttributeCriterionUtil {

	public static final String CQL_WILDCARD = "*";
	public static final String CQL_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZ";
	public static final String ID_SUFFIX = ".@id";

	private static SearchAndFilterMessages messages;

	private AttributeCriterionUtil() {

	}

	private static SearchAndFilterMessages getSearchAndFilterMessages() {
		if (messages == null) {
			messages = GWT.create(SearchAndFilterMessages.class);
		}
		return messages;
	}

	public static String getOperationStringRepresentation(ConfiguredSearchAttribute.AttributeType attributeType,
												   ConfiguredSearchAttribute.Operation operation) {
		if (attributeType != null && operation != null) {
			switch (attributeType) {
			case Integer:
				switch (operation) {
					case EqualToInteger:
						return getSearchAndFilterMessages().searchAttributeOperationEqualToInteger();
					case LargerThan:
						return getSearchAndFilterMessages().searchAttributeOperationLargerThanInteger();
					case SmallerThan:
						return getSearchAndFilterMessages().searchAttributeOperationSmallerThanInteger();
				}
				break;
			case String:
				switch (operation) {
					case EqualToString:
						return getSearchAndFilterMessages().searchAttributeOperationEqualToString();
					case LargerThan:
						return getSearchAndFilterMessages().searchAttributeOperationLargerThanString();
					case SmallerThan:
						return getSearchAndFilterMessages().searchAttributeOperationSmallerThanString();
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
					return getSearchAndFilterMessages().searchAttributeInputTypeDropDownToString();
				case FreeValue:
					switch (attributeType) {
						case String:
							return getSearchAndFilterMessages().searchAttributeInputTypeFreeTextToString();
						case Integer:
							return getSearchAndFilterMessages().searchAttributeInputTypeFreeNrToString();
					}
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

	/**
	 * Return a search criterion object, or null if not all fields have been properly filled.
	 * @param serverLayerId server id of the vector layer that will be searched in
	 * @param selectedAttribute the attributeInfo of the attribute that is subject of the criterion
	 * @param valueFormItem the form element that contains the value of the attribute
	 * @param operationCode string code representation of operator, as provided by
	 *                       {@link org.geomajas.gwt.client.widget.attribute
	 *                       .AttributeCriterionPane#getOperatorCodeFromLabel(String)}
	 * @return a fully configured attribute criterion that can be used for the search
	 */
	public static AttributeCriterion getSearchCriterion(String serverLayerId,
														AbstractReadOnlyAttributeInfo selectedAttribute,
														FormItem valueFormItem,
														String operationCode) {
		Object value = valueFormItem.getValue();

		if (selectedAttribute != null && operationCode != null) {
			String valueString = "";
			String nameString = selectedAttribute.getName();
			String displayText = nameString + " " + operationCode + " " + valueFormItem.getDisplayValue();
			if (value != null) {
				valueString = value.toString();
			}

			// CQL does not recognize "contains", so change to "like":
			if ("contains".equals(operationCode)) {
				operationCode = "like";
				valueString = CQL_WILDCARD + valueString + CQL_WILDCARD;
			} else if ("like".equals(operationCode) && valueString.isEmpty()) {
				valueString = CQL_WILDCARD + valueString + CQL_WILDCARD;
			}

			// If value was null, and no "contains" operator, return null:
			if (valueString == null || valueString.length() == 0) {
				return null;
			}

			if (selectedAttribute instanceof PrimitiveAttributeInfo) {
				PrimitiveAttributeInfo attr = (PrimitiveAttributeInfo) selectedAttribute;
				if (attr.getType().equals(PrimitiveType.DATE)) {
					if (value instanceof Date) {
						// In case of a date, parse correctly for CQL: 2006-11-30T01:30:00Z
						DateTimeFormat format = DateTimeFormat.getFormat(CQL_TIME_FORMAT);
						valueString = format.format((Date) value);
						if ("=".equals(operationCode)) {
							// Date equals not supported by CQL, so we use the DURING operator instead and
							// create a day period for this day (browser time zone !)
							operationCode = "DURING";
							String startOfDay = valueString.replaceAll("\\d\\d:\\d\\d:\\d\\d", "00:00:00");
							// 1 day period, starting at 0h00
							valueString = startOfDay + "/P1D";
						} else if ("AFTER".equals(operationCode)) {
							// we can't discriminate between date and timestamp values yet, use end of day for now
							valueString = valueString.replaceAll("\\d\\d:\\d\\d:\\d\\d", "23:59:59");
						} else if ("BEFORE".equals(operationCode)) {
							// we can't discriminate between date and timestamp values yet, use start of day for now
							valueString = valueString.replaceAll("\\d\\d:\\d\\d:\\d\\d", "00:00:00");
						}
					}
				}
			} else if (selectedAttribute instanceof AssociationAttributeInfo) {
				AssociationAttributeInfo assInfo = (AssociationAttributeInfo) selectedAttribute;
				if (AssociationType.MANY_TO_ONE == assInfo.getType()) {
					nameString = nameString + ID_SUFFIX;
				}
			}

			// Now create the criterion:
			AttributeCriterion criterion = new AttributeCriterion();
			criterion.setServerLayerId(serverLayerId);
			criterion.setAttributeName(nameString);
			criterion.setOperator(operationCode);
			criterion.setValue(valueString);
			criterion.setDisplayText(displayText);
			return criterion;
		}
		return null;
	}

	public static AbstractReadOnlyAttributeInfo getAbstractReadOnlyAttributeInfo(VectorLayer vectorLayer,
														   AttributeCriterion configuredSearchAttribute) {
		Object value = configuredSearchAttribute.getAttributeName();
		if (value != null) {
			for (AbstractAttributeInfo attributeInfo :
					vectorLayer.getLayerInfo().getFeatureInfo().getAttributes()) {
				if (attributeInfo instanceof AbstractReadOnlyAttributeInfo && value.equals(
						((AbstractReadOnlyAttributeInfo) attributeInfo).getLabel())) {
					return (AbstractReadOnlyAttributeInfo) attributeInfo;
				}
			}
		}
		return null;
	}
}
