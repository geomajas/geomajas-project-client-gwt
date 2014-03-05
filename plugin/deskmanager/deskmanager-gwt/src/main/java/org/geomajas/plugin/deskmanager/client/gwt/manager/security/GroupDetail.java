package org.geomajas.plugin.deskmanager.client.gwt.manager.security;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class GroupDetail extends VLayout {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private IButton editButton;

	private IButton saveButton;

	private IButton cancelButton;

	private TextItem name;

	private TextItem key;

	private StaticTextItem territory;

	private DynamicForm form;
	
	private MapWidget territoryMap;

	public GroupDetail() {
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

		name = new TextItem("name");
		name.setTitle(MESSAGES.securityGroupDetailName());

		key = new TextItem("key");
		key.setTitle(MESSAGES.securityGroupDetailKey());

		territory = new StaticTextItem();
		territory.setTitle(MESSAGES.securityGroupDetailTerritory());
		territory.setValue(MESSAGES.securityGroupDetailTerritoryHint());
		
		HLayout formAndMap = new HLayout();
		form.setFields(name, key, territory);		
		formAndMap.addMember(form);
		territoryMap = new MapWidget("mapOsm", "appDeskManager");
		formAndMap.addMember(territoryMap);
		
		containerLayout.addMember(formAndMap);

		addMember(containerLayout);
	}

}
