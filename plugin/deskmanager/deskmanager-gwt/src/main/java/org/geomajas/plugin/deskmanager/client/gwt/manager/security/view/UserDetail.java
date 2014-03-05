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
package org.geomajas.plugin.deskmanager.client.gwt.manager.security.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiField;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.validator.MatchesFieldValidator;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import org.geomajas.gwt.client.widget.editor.EditorCheckboxItem;
import org.geomajas.gwt.client.widget.editor.EditorTextItem;
import org.geomajas.plugin.deskmanager.client.gwt.manager.blueprints.BlueprintGrid;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.DetailHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.UserDetailHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.Manager;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Default implementation of {@link org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.UserDetailView}.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class UserDetail extends AbstractEditableLoadingLayout implements UserDetailView, Editor<UserDto> {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private UserDetailHandler handler;

	/* form and its items */
	@UiField
	@Path("email") // UserDto field name
	protected EditorTextItem email;

	@UiField
	@Path("name")
	protected EditorTextItem name;

	@UiField
	@Path("surname")
	protected EditorTextItem surname;

	@UiField
	//@Path("active")
	protected EditorCheckboxItem active;

	private PasswordItem password;
	private PasswordItem passwordConfirm;
	private DynamicForm form;

	/* form item names */
	public static final String FLD_EMAIL = "email";
	public static final String FLD_NAME = "name";
	public static final String FLD_SURNAME = "surname";
	public static final String FLD_ACTIVE = "active";
	private static final String FLD_PASSWORD = "password";
	private static final String FLD_PASSWORD_CONFIRM = "confirmPassword";

	// validators
	private RegExpValidator passwordRegExpValidator;
	//private RegExpValidator mailRegExpValidator;

	/**
	 * UI driver interface.
	 *
	 * @author timothy
	 */
	interface UserDriver extends SimpleBeanEditorDriver<UserDto, UserDetail> {

	}

	private final UserDriver driver = GWT.create(UserDriver.class);

	public UserDetail() {
		super();

		// create form
		form = new DynamicForm();

		email = new EditorTextItem(FLD_EMAIL);
		email.setTitle(MESSAGES.securityUserDetailEmail());

		name = new EditorTextItem(FLD_NAME);
		name.setTitle(MESSAGES.securityUserDetailName());

		surname = new EditorTextItem(FLD_SURNAME);
		surname.setTitle(MESSAGES.securityUserDetailSurname());

		active = new EditorCheckboxItem(FLD_ACTIVE);
		active.setTitle(MESSAGES.securityUserDetailActiveLabel());
		active.setWrapTitle(false);
		active.setTooltip(MESSAGES.securityUserDetailActiveTooltip());

		password = new PasswordItem(FLD_PASSWORD);
		password.setTitle(MESSAGES.securityUserDetailPassword());

		passwordConfirm = new PasswordItem(FLD_PASSWORD_CONFIRM);
		passwordConfirm.setTitle(MESSAGES.securityUserDetailPasswordConfirm());

		// add non user-specific validators

		// password validator using regular expressions
		passwordRegExpValidator = new RegExpValidator();
		passwordRegExpValidator.setErrorMessage(MESSAGES.securityUserDetailErrorPasswordSyntax());
		password.setValidators(passwordRegExpValidator);
		password.setValidateOnExit(true);

		// password matching validator
		MatchesFieldValidator matchValidator = new MatchesFieldValidator();
		matchValidator.setOtherField(FLD_PASSWORD);
		matchValidator.setErrorMessage(MESSAGES.securityUserDetailErrorPasswordsDoNotMatch());
		passwordConfirm.setValidators(matchValidator);
		passwordConfirm.setValidateOnExit(true);

		form.setFields(email, name, surname, active, password, passwordConfirm);

		setNewUserSettings(false);
		containerLayout.addMember(form);
		driver.initialize(this);
	}

	@Override
	public void setHandler(DetailHandler<UserDto> handler) {
		if (handler instanceof UserDetailHandler) {
			this.handler = (UserDetailHandler) handler;
		}
	}

	public void onSelectionChanged(SelectionEvent event) {
		setDisabled(true);
		if (event.getState()) { // true == selected
			ListGridRecord record = (ListGridRecord) event.getRecord();
			if (record != null && record.getAttributeAsLong(UserGrid.FLD_ID) != null) {
				loadUser(record.getAttributeAsLong(BlueprintGrid.FLD_ID));
			} else {
				setObject(null);
			}
		}
	}

	@Override
	public void setObject(UserDto user) {
		form.clearValues();
		driver.edit(user);
		email.setValue(user.getEmail());
		setLoaded();
	}

	@Override
	public void setPasswordValidationRule(String regex) {
		passwordRegExpValidator.setExpression(regex);
	}

	@Override
	public void setCreateUserMode() {
		setNewUserSettings(true);
	}

	@Override
	public void focusOnFirstField() {
		name.focusInItem();
	}

	@Override
	public void setEditable(boolean editable) {
		form.setDisabled(!editable);
	}

	@Override
	public void clearValues() {
		form.clearValues();
	}

	// ---------------------------------------------------------
	// button action methods
	// ---------------------------------------------------------

	@Override
	public void onEdit() {
		handler.onEdit();
		setNewUserSettings(false);
	}

	@Override
	public void onCancel() {
		handler.onCancel();
		setNewUserSettings(false);
	}

	@Override
	public void onSave() {
		if (form.validate()) {
			UserDto userDto = validateDriver();
			if (userDto != null) {
				handler.onSave(userDto, password.getValueAsString());
			}
		}
		setNewUserSettings(false);
	}

	// ---------------------------------------------------------
	// private methods
	// ---------------------------------------------------------

	private void loadUser(long id) {
		setLoading(); /* Clear edit form */
		ManagerCommandService.getUser(id, new DataCallback<UserDto>() {

			public void execute(UserDto result) {
				setObject(result);
			}
		});
	}

	private UserDto validateDriver() {
		UserDto user = driver.flush();
		Set<ConstraintViolation<?>> violations = (Set) Manager.getValidator().validate(user);
		driver.setConstraintViolations(violations);
		return violations.size() > 0 ? null : user;
	}

	private void setNewUserSettings(boolean newUser) {
		email.setDisabled(!newUser);
		password.setRequired(newUser);
		passwordConfirm.setRequired(newUser);
	}
}
