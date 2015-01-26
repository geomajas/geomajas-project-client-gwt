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
package org.geomajas.gwt.client.widget.attribute;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.annotation.Api;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;

import com.smartgwt.client.widgets.form.fields.FormItem;

/**
 * <p>
 * An item representing a one-to-many attribute. Implementations should provide a {@link #fromItem(OneToManyAttribute)}
 * method to copy the item value to the attribute and a {@link #toItem(OneToManyAttribute)} method to copy the attribute
 * value to the item. They receive all necessary information on the attribute's metadata through the
 * {@link #init(AssociationAttributeInfo, AttributeProvider)} method.
 * </p>
 * 
 * @param <F> the item class of the form item
 * 
 * @author Jan De Moerloose
 * @since 1.11.1
 */
@Api
public interface OneToManyItem<F extends FormItem> {

	/**
	 * Return the actual form item.
	 * 
	 * @return the actual form item
	 */
	F getItem();

	/**
	 * Copy the attribute value from the attribute to the form item.
	 * 
	 * @param attribute attribute to copy value from
	 */
	void toItem(OneToManyAttribute attribute);

	/**
	 * Copy the attribute value from the form item to the attribute.
	 * 
	 * @param attribute attribute to copy value to
	 */
	void fromItem(OneToManyAttribute attribute);

	/**
	 * Initialize this item with the specified attribute info and provider.
	 * 
	 * @param attributeInfo the attribute info
	 * @param attributeProvider the attribute provider
	 */
	void init(AssociationAttributeInfo attributeInfo, AttributeProvider attributeProvider);

	/**
	 * Clear this item's value.
	 */
	void clearValue();

}
