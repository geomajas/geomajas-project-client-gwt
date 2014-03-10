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
package org.geomajas.plugin.deskmanager.client.gwt.manager.service;

/**
 * Use for async methods to be notified when they are done.
 * 
 * @param <T> the class of the result you would like to get returned
 * 
 * @author Bruce Palmkoeck
 */
public interface DataCallback<T> {

	void execute(T result);
}