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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Like {@link org.geomajas.gwt.client.util.UrlBuilder}, but not encoding.
 *
 * @see org.geomajas.gwt.client.util.UrlBuilder
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class NotEncodingUrlBuilder {

	private final Map<String, String> params = new HashMap<String, String>();

	private String baseUrl;

	/**
	 * Constructor using the given base URL.
	 *
	 * @param baseUrl base URL
	 */
	public NotEncodingUrlBuilder(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * Add a parameter.
	 *
	 * @param name
	 *            name of parameter
	 * @param value
	 *            value of parameter
	 * @return this to allow concatenation
	 */
	public NotEncodingUrlBuilder addParameter(String name, String value) {
		if (value == null) {
			value = "";
		}
		params.put(name, value);
		return this;
	}

	/**
	 * Add a path extension.
	 *
	 * @param path
	 *            path
	 * @return this to allow concatenation
	 */
	public NotEncodingUrlBuilder addPath(String path) {
		if (path.startsWith("/") && baseUrl.endsWith("/")) {
			baseUrl = baseUrl + path.substring(1);
		} else if (baseUrl.endsWith("/")) {
			baseUrl = baseUrl + path;
		} else {
			baseUrl = baseUrl + "/" + path;
		}
		return this;
	}

	/**
	 * Build the URL and return it as an encoded string.
	 *
	 * @return the encoded URL string
	 */
	public String toString() {
		StringBuilder url = new StringBuilder(baseUrl);
		if (params.size() > 0) {
			url.append("?");
			for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
				String name = iterator.next();
				url.append(name).append("=").append(params.get(name));
				if (iterator.hasNext()) {
					url.append("&");
				}
			}
		}
		return url.toString();
	}

}
