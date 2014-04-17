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
package org.geomajas.gwt.client.util.css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;

/**
 * @author Dosi Bingov
 *         <p/>
 *         Implementation of {@link org.geomajas.gwt.client.css.CssLoader}.  Adds css link in DOM via native javasript.
 */
public final class CssLoaderUtil {

	private CssLoaderUtil() {
		//util class
	}

	/**
	 * @param pathToStyleSheet full path to the css file.
	 */
	public static void loadStyleSheet(String pathToStyleSheet) {

		if (Document.get().getElementById(pathToStyleSheet) == null) {
			CssLoaderUtil.loadCss(pathToStyleSheet);
		}

		GWT.log("CssLoaderUtil => stylesheet " + pathToStyleSheet + " is loaded");

	}

	public void unloadCss(String pathToStyleSheet) {
		Element cssElement = Document.get().getElementById(pathToStyleSheet);
		if (cssElement != null) {
			cssElement.removeFromParent();
		}
	}

	public static native Boolean isCssLinkLoaded(String cssFileName) /*-{
		var link = $doc.getElementById(cssFileName);
		try {
			if (link.sheet && link.sheet.cssRules.length > 0) {
				return true;
			} else if (link.styleSheet && link.styleSheet.cssText.length > 0) {
				return true;
			} else if (link.innerHTML && link.innerHTML.length > 0) {
				return true;
			}

		} catch (ex) {

		}
		return false;
	}-*/;

	private static native void loadCss(String url)  /*-{
		url = url + "?v=1"; // Make sure this request is not cached

		if ($doc.createStyleSheet) {
			//add stylesheet in IE
			$doc.createStyleSheet(url);
		} else {
			var l = $doc.createElement("link");
			l.setAttribute("id", url);
			l.setAttribute("rel", "stylesheet");
			l.setAttribute("type", "text/css");
			l.setAttribute("href", url);
			$doc.getElementsByTagName("head")[0].appendChild(l);
		}

	}-*/;


}
