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
package org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter;

/**
 * Interface for presenters dealing with editing.
 *
 * @author Jan Venstermans
 */
public interface EditableHandler {

	void onSave();

	void onEdit();

	void onCancel();

	void setEditable(boolean editable);

	void setEnabled(boolean enabled);

}
