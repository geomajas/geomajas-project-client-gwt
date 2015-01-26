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

import java.util.List;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.user.client.TakesValue;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.validator.CustomValidator;

/**
 * @author Jan De Moerloose
 * @param <T>
 */
public class FormItemEditor<T> {

	private LeafValueEditor<T> editor;

	private FormItem item;

	private CopyFromGWTValidator validator = new CopyFromGWTValidator();


	public FormItemEditor(FormItem formItem) {
		item = formItem;
		item.setValidators(validator);
		editor = TakesValueEditor.of(new TakesValue<T>() {

			@Override
			public void setValue(T value) {
				item.setValue(value);
			}

			@Override
			public T getValue() {
				return (T) item.getValue();
			}
		});
	}	
	
	public LeafValueEditor<T> asEditor() {
		return editor;
	}

	public void showErrors(List<EditorError> errors) {
		validator.setCondition(true);
		for (EditorError editorError : errors) {
			validator.setCondition(false);
			validator.setErrorMessage(editorError.getMessage());
		}
		item.validate();
	}

	/**
	 * @author Jan De Moerloose
	 */
	public class CopyFromGWTValidator extends CustomValidator {

		private boolean condition = true;

		@Override
		protected boolean condition(Object value) {
			return condition;
		}

		public boolean isCondition() {
			return condition;
		}

		public void setCondition(boolean condition) {
			this.condition = condition;
		}

	}

}
