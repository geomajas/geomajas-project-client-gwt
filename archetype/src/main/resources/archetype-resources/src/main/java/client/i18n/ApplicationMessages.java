#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

package ${package}.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * <p>
 * Localization messages for the GWT test samples.
 * </p>
 *
 * @author geomajas-gwt-archetype
 */
public interface ApplicationMessages extends Messages {

	String applicationTitle(String version);
	
	String overviewMapTitle();
	
	String layerTreeTitle();
	
	String legendTitle();

}