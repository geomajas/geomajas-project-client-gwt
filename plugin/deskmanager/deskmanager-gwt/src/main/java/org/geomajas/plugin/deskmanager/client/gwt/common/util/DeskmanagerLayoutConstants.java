/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.client.gwt.common.util;


import com.google.gwt.core.client.GWT;

/**
 * Common css styles, icons and margins.
 *
 * @author Dosi Bingov
 *
 */
public final class DeskmanagerLayoutConstants {

	private DeskmanagerLayoutConstants() {}

	// CHECKSTYLE VISIBILITY MODIFIER: OFF

	/*============================================================
		Margins
	============================================================*/
	/** Manager content center width. */
	public static String centerContentWidth = "100%";

	public static int loadingScreenHeight = 300;

	public static int loadingScreenWidth = 500;

	public static int progressBarHeight = 30;


	/*============================================================
		Style names
	============================================================*/
	/** desk manager bottom style tabpane style. */
	public static final String STYLE_DESKMANAGER_TABPANE_DETAIL = "gm-deskmanager-tabpane-detail";

	/** center content style. */
	public static final String STYLE_CENTER_CONTENT = "gm-deskmanager-center-content";

	/** loading screen */
	public static final String STYLE_LOADING_SCREEN = "gm-deskmanger-loadingscreen";

	/** loading bar layout */
	public static final String STYLE_PROGRESSBAR_LAYOUT = "gm-deskmanger-progressbar-layout";

	/** loading bar */
	public static final String STYLE_PROGRESSBAR = "gm-deskmanger-progressbar";


	/*============================================================
		Icons
	============================================================*/

	public static String iconHelp = "[ISOMORPHIC]/images/osgeo/help.png";

	public static String iconSave = "[ISOMORPHIC]/images/silk/accept.png";

	public static String iconCog = "[ISOMORPHIC]/images/icons/cog.png";

	public static String iconOpensampleloket = "[ISOMORPHIC]/images/silk/application_view_gallery.png";

	public static String iconClearSelection = "[ISOMORPHIC]/geomajas/osgeo/selected-delete.png";


	// CHECKSTYLE VISIBILITY MODIFIER: ON

}

