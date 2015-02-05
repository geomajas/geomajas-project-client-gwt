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
package org.geomajas.gwt.client.widget.editor;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;

import java.util.List;

/**
 * Editor wrapper around a {@link CheckboxItem} object, so it can be used in a
 * {@link com.google.gwt.editor.client.SimpleBeanEditorDriver}.
 *
 * @author Jan Venstermans
 */
public class EditorCheckboxItem extends CheckboxItem implements HasEditorErrors<Boolean>,
		IsEditor<LeafValueEditor<Boolean>> {

	private FormItemEditor<Boolean> editor;

	public EditorCheckboxItem(String name) {
		super(name);
		editor = new FormItemEditor<Boolean>(this);
	}

	@Override
	public LeafValueEditor<Boolean> asEditor() {
		return editor.asEditor();
	}


	@Override
	public void showErrors(List<EditorError> errors) {
		editor.showErrors(errors);
	}
}
