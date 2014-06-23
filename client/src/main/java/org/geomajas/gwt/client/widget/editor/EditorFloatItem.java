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
package org.geomajas.gwt.client.widget.editor;

import java.util.List;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.smartgwt.client.widgets.form.fields.FloatItem;

/**
 * @author Jan De Moerloose
 */
public class EditorFloatItem extends FloatItem implements HasEditorErrors<Float>, IsEditor<LeafValueEditor<Float>> {

	private FormItemEditor<Float> editor;

	public EditorFloatItem(String name) {
		super(name);
		editor = new FormItemEditor<Float>(this);
	}

	@Override
	public LeafValueEditor<Float> asEditor() {
		return editor.asEditor();
	}

	@Override
	public void showErrors(List<EditorError> errors) {
		editor.showErrors(errors);
	}

}
