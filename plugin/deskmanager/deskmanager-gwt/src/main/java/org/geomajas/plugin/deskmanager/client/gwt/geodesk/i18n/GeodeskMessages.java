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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Interface for i18n messages.
 * 
 * @author Jan De Moerloose
 *
 */
public interface GeodeskMessages extends Messages {

	String viewFeatureDetailButtonTitle();

	String zoomFeatureButtonTitle();

	String zoomFeatureButtonTooltip();

	String loadingScreenMessage();
	
	String userFriendlyLayerErrorMessage();
	
	String userFriendlySecurityErrorMessage();
	
	String userFriendlyCommunicationErrorMessage();

	String noGeodeskIdGivenError();
	String noSuchGeodeskExists();

	String refreshLayersActionReload();
	String refreshLayersActionReloadTooltip();

	String stressTestActionTitle();
	String stressTestActionTooltip();
	String stressTestActionParameters();
	String stressTestActionSpeedParameter();
	String stressTestActionProgressParameter();
	String stressTestActionStart();

	String featureSelectionInfoWindowOneFeatureSelected();
	String featureSelectionInfoWindowXFeaturesSelected(int selectionCount);

	String abstractUserApplicationTitle();
	
}
