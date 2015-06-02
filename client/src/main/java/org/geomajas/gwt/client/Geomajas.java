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

package org.geomajas.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.i18n.VersionMessages;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Global settings for the Geomajas GWT face.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Jan Venstermans
 * @since 1.6.0
 */
@Api(allMethods = true)
public final class Geomajas {

	private static final VersionMessages MESSAGES = GWT.create(VersionMessages.class);

	private static String dispatcherUrl;

	private Geomajas() {
		// do not allow instantiation.
	}

	/**
	 * Returns the current version of Geomajas as a string.
	 *
	 * @return Geomajas version
	 */
	public static String getVersion() {
		return MESSAGES.version();
	}

	/**
	 * Returns a list of locales that can be used in this version of Geomajas. The default is english, and 'native'
	 * means that your browsers locale should be used (if supported - default otherwise).
	 *
	 * @return supported locales
	 */
	public static Map<String, String> getSupportedLocales() {
		Map<String, String> locales = new HashMap<String, String>();
		for (String localeName : LocaleInfo.getAvailableLocaleNames()) {
			String displayName = LocaleInfo.getLocaleNativeDisplayName(localeName);
			locales.put(localeName, displayName);
		}
		return locales;
	}

	/**
	 * Return the base directory for the web application.
	 * 
	 * @return base directory for application for SmartGWT
	 */
	public static native String getIsomorphicDir()
	/*-{
		return $wnd.isomorphicDir;
	}-*/;
	
	/**
	 * Return the Spring dispatcher URL for the web application. The URL ends with a slash.
	 * 
	 * @return the dispatcher URL
	 * @since 1.10.0
	 */
	public static String getDispatcherUrl() {
		if (dispatcherUrl == null) {
			dispatcherUrl = getModuleBaseDispatcherUrl();
		}
		return dispatcherUrl;
	}

	/**
	 * Set the Spring dispatcher URL. The URL should ends with a slash.
	 *
	 * @param dispatcherUrl url to desired dispatcher
	 * @since 1.16.0
	 */
	public static void setDispatcherUrl(String dispatcherUrl) {
		Geomajas.dispatcherUrl = dispatcherUrl;
	}

	/**
	 *
	 * @return dispatcher ur based on GWT.getModuleBaseURL().
	 */
	private static String getModuleBaseDispatcherUrl() {
		String moduleBaseUrl = GWT.getModuleBaseURL();
		// remove last slash
		moduleBaseUrl = moduleBaseUrl.substring(0, moduleBaseUrl.length() - 1);
		// replace module part by /d
		int contextEndIndex = moduleBaseUrl.lastIndexOf("/");
		if (contextEndIndex > 6) {
			return moduleBaseUrl.substring(0, contextEndIndex) + "/d/";
		} else {
			// fall back to module base URL
			return GWT.getModuleBaseURL();
		}
	}

}