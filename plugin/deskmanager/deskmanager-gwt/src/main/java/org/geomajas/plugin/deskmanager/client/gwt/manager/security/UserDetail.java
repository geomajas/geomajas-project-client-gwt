package org.geomajas.plugin.deskmanager.client.gwt.manager.security;

import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class UserDetail extends VLayout {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private IButton editButton;

	private IButton saveButton;

	private IButton cancelButton;

	private TextItem email;

	private TextItem name;

	private TextItem surname;

	private PasswordItem password;

	private PasswordItem passwordConfirm;

	private DynamicForm form;

	public UserDetail() {
		super(10);
		editButton = new IButton(MESSAGES.editButtonText());
		saveButton = new IButton(MESSAGES.saveButtonText());
		cancelButton = new IButton(MESSAGES.cancelButtonText());

		VLayout containerLayout = new VLayout();
		containerLayout.setMargin(10);

		HLayout buttonLayout = new HLayout(10);
		buttonLayout.addMember(editButton);
		buttonLayout.addMember(cancelButton);
		buttonLayout.addMember(saveButton);
		buttonLayout.setHeight(50);

		containerLayout.addMember(buttonLayout);

		form = new DynamicForm();

		email = new TextItem("email");
		email.setTitle(MESSAGES.securityUserDetailEmail());

		name = new TextItem("name");
		name.setTitle(MESSAGES.securityUserDetailName());

		surname = new TextItem("surname");
		surname.setTitle(MESSAGES.securityUserDetailSurname());

		password = new PasswordItem();
		password.setTitle(MESSAGES.securityUserDetailPassword());

		passwordConfirm = new PasswordItem();
		passwordConfirm.setTitle(MESSAGES.securityUserDetailPasswordConfirm());

		form.setFields(email, name, surname, password, passwordConfirm);

		containerLayout.addMember(form);

		addMember(containerLayout);
	}

}
