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

import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.EditableLoadingView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.EditableView;

/**
 * Default implementation of {@link EditableHandler}.
 *
 * @author Jan Venstermans
 */
public class AbstractEditablePresenter implements EditableHandler {

	private EditableView view;

	public AbstractEditablePresenter(EditableView view) {
		this.view = view;
		view.setEnabled(false);
		setEditable(false);
	}

	@Override
	public void onSave() {
		setEditable(false);
	}

	@Override
	public void onEdit() {
		setEditable(true);
	}

	@Override
	public void onCancel() {
		setEditable(false);
	}

	@Override
	public void setEnabled(boolean enabled) {
		view.setEnabled(enabled);
		if (!enabled) {
		   view.clearValues();
		}
	}

	@Override
	public void setEditable(boolean editable) {
		view.setButtonEnabled(EditableLoadingView.Button.EDIT, !editable);
		view.setButtonEnabled(EditableLoadingView.Button.CANCEL, editable);
		view.setButtonEnabled(EditableLoadingView.Button.SAVE, editable);
		view.setEditable(editable);
	}

}
