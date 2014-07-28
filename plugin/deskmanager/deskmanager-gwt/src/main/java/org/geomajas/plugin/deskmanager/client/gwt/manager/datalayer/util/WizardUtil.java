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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.util;

import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.NotEncodingUrlBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *  Util methods for the Add Layer Wizard.
 *
 * @author Jan Venstermans
 */
public final class WizardUtil {

	private static final Map<Capabilities_Parameter, String> DEFAULT_WMS_CAPABILITIES_PARAMETERS;
	private static final Map<Capabilities_Parameter, String> DEFAULT_WFS_CAPABILITIES_PARAMETERS;

	static {
		DEFAULT_WMS_CAPABILITIES_PARAMETERS = new HashMap<Capabilities_Parameter, String>();
		DEFAULT_WMS_CAPABILITIES_PARAMETERS.put(Capabilities_Parameter.SERVICE, "wfs");
		DEFAULT_WMS_CAPABILITIES_PARAMETERS.put(Capabilities_Parameter.REQUEST, "GetCapabilities");
		DEFAULT_WMS_CAPABILITIES_PARAMETERS.put(Capabilities_Parameter.VERSION, "1.0.0");

		DEFAULT_WFS_CAPABILITIES_PARAMETERS = new HashMap<Capabilities_Parameter, String>();
		DEFAULT_WFS_CAPABILITIES_PARAMETERS.put(Capabilities_Parameter.SERVICE, "wms");
		DEFAULT_WFS_CAPABILITIES_PARAMETERS.put(Capabilities_Parameter.REQUEST, "GetCapabilities");
		DEFAULT_WFS_CAPABILITIES_PARAMETERS.put(Capabilities_Parameter.VERSION, "1.1.1");
	}

	/**
	 * Enum for url query paramaters of a GetCapabilities url.
	 */
	public enum Capabilities_Parameter {
		SERVICE("service"),
		REQUEST("request"),
		VERSION("version");

		private String urlKey;

		/**
		 * Default constructor.
		 * @param urlKey in lower case
		 */
		Capabilities_Parameter(String urlKey) {
			this.urlKey = urlKey;
		}

		public String getUrlKey() {
			return urlKey;
		}
	}

	/**
	 * Util constructor.
	 */
	private WizardUtil() {
	}

	public static String constructWfsGetCapabilities(String input) throws GetCapabilitiesIllegalArgumentException {
		return constructGetCapabilities(input, DEFAULT_WMS_CAPABILITIES_PARAMETERS);
	}

	public static String constructWmsGetCapabilities(String input) throws GetCapabilitiesIllegalArgumentException {
		return constructGetCapabilities(input, DEFAULT_WFS_CAPABILITIES_PARAMETERS);
	}

	public static Map<String, String> urlQueryToMap(String queryPartOfUrl) {
		if (queryPartOfUrl != null) {
			Map<String, String> parametersMap = new HashMap<String, String>();
			for (String queryArgument : queryPartOfUrl.split("&")) {
				String[] queryElements = queryArgument.split("=");
				if (!queryElements[0].isEmpty()) {
					parametersMap.put(queryElements[0], queryElements.length > 1 ? queryElements[1] : null);
				}
			}
			return parametersMap;
		}
		return null;
	}

	public static String getBaseOfUrl(String inputUrl) {
		try {
			URL url1 = new URL(inputUrl);
			return url1.getProtocol() + "://" + url1.getHost() + url1.getPath();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getQueryPartOfUrl(String inputUrl) {
		try {
			URL url1 = new URL(inputUrl);
			return url1.getQuery();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* private methods */

	private static String constructGetCapabilities(String input, Map<Capabilities_Parameter, String> defaultParameters)
			throws GetCapabilitiesIllegalArgumentException {
		NotEncodingUrlBuilder urlBuilder = new NotEncodingUrlBuilder(getBaseOfUrl(input));
		Map<String, String> queryParameters = urlQueryToMap(getQueryPartOfUrl(input));
		// start with default values
		Map<Capabilities_Parameter, String> capabilitiesParameters =
				new HashMap<Capabilities_Parameter, String>(defaultParameters);
		if (queryParameters != null) {
			for (Map.Entry<String, String> parameterEntry : queryParameters.entrySet()) {
				String key = parameterEntry.getKey().toLowerCase();
				Capabilities_Parameter capabilitiesParameter =
						getCapabilitiesParameter(key, capabilitiesParameters.keySet());
				String defaultValue = defaultParameters.get(capabilitiesParameter);
				boolean defaultValueUsed = defaultValue.equalsIgnoreCase(parameterEntry.getValue());
				if (!defaultValueUsed) {
					switch (capabilitiesParameter) {
						case SERVICE:
						case REQUEST:
							throw new GetCapabilitiesIllegalArgumentException(capabilitiesParameter, defaultValue);
						default:
							capabilitiesParameters.put(capabilitiesParameter, parameterEntry.getValue());
					}
				}
			}
		}
		for (Map.Entry<Capabilities_Parameter, String> parameterEntry : capabilitiesParameters.entrySet()) {
			urlBuilder.addParameter(parameterEntry.getKey().getUrlKey(), parameterEntry.getValue());
		}
		return urlBuilder.toString();
	}

	private static Capabilities_Parameter getCapabilitiesParameter(
			String key, Set<Capabilities_Parameter> capabilitiesParameters) {
		for (Capabilities_Parameter capabilitiesParameter : capabilitiesParameters) {
			if (capabilitiesParameter.getUrlKey().equals(key)) {
				return capabilitiesParameter;
			}
		}
		return null;
	}
}
