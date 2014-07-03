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
package org.geomajas.plugin.deskmanager.client.gwt.manager.i18n;


import com.google.gwt.i18n.client.Messages;

/**
 * @author Oliver May
 * @author BuyleA
 *
 */
public interface ManagerMessages extends Messages {

	String editButtonText();
	String cancelButtonText();
	String saveButtonText();
	String resetButtonText();
	String resetButtonTooltip();
	String configAddDelete(); //TODO: needed?


	String removeTitle();

	String wizardPreviousButtonText();
	String wizardNextButtonText();

	String formWarnNotvalid();
	String loadingConfig();
	String titlePleaseWait();

	String gridColumnActions();

	String settingsLatestChangeBy();
	String settingsLatestChangeWhen();


	// Main section tabs
	String mainTabGeodesks();
	String mainTabDataLayers();
	String mainTabBlueprints();
	String mainTabUsers();

	//Geodesks main Tab
	String newGeodeskButtonText();

	String geodeskGridColumnName();
	String geodeskGridColumnNameBlueprint();
	String geodeskGridColumnDeskId();
	String geodeskGridColumnAuthor();
	String geodeskGridColumnAuthorTooltip();
	String geodeskGridColumnOwner();
	String geodeskGridColumnOwnerTooltip();
	String geodeskGridColumnOwnerNoOwner();
	String geodeskGridColumnPublic();
	String geodeskPublicTooltip();
	String geodeskGridColumnActiv();
	String geodeskActivTooltip();
	String geodeskGridColumnActions();
	String geodeskGridColumnActionsTooltip();
	String geodeskGridActionsColumnPreviewTooltip();
	String geodeskGridActionsColumnRemoveTooltip();

	String geodeskLabel();

	String geodeskRemoveTitle();
	String geodeskRemoveConfirmQuestion(String attribute);

	String geodeskLoading();


	String settingsNameGeodesk();

	String geodeskSettingsNameBlueprint();

	String geodeskSettingsId();

	String settingsNameBlueprintTooltip();

	String settingGeodeskWarnGeodeskIdNotUnique();

	String validatorWarnGeodeskIdNotUnique();

	String geodeskSettingsIdTooltip();

	String geodeskSettingsAdmin();

	String geodeskSettingsActiv();

	String geodeskSettingsPublic();
	String geodeskSettingsWarnCannotBePublic();
	String geodeskSettingsWarnGeodeskInactivedByBlueprint();

	String settingsLimitToTerritoryAdministrator();
	String settingsLimitToTerritoryAdministratorTooltip();
	String settingsLimitToTerritoryUser();
	String settingsLimitToTerritoryUserTooltip();

	String settingsFormGroupSettings();
	String geodeskSettingsWarnNoBlueprints();
	String datalagenGroup();
	String layerTreeFormGroup();
	String geodeskAccessRightsFormGroup();

	String geodeskDetailTabSettings();
	String geodeskDetailTabDataLayers();
	String geodeskDetailTabAccessRights();
	String geodeskDetailTabLayout();
	String geodeskDetailTabWidgets();


	String geodeskDetailTabHTMLCode();

	String geodeskDetailTabThemes();


	String chooseBlueprintTitle();
	String chooseBlueprintRequired();
	String chooseBlueprintLoading();
	String chooseBlueprintTooltip();
	String chooseBlueprintCreate();
	String chooseBlueprinWarnNoBlueprints();

	String newBlueprintButtonText();
	String newBlueprintWindowTitle();
	String chooseAppTitle();

	String datalayerConnectionParametersGroup();
	String datalayerConnectionParametersCapabilitiesURL();
	String datalayerConnectionParametersUserName();
	String datalayerConnectionParametersPassword();
	String datalayerConnectionParametersHost();
	String datalayerConnectionParametersPort();
	String datalayerConnectionParametersScheme();
	String datalayerConnectionParametersDatabase();

	String blueprintGridColumnName();
	String blueprintGridColumnLimitToTerritory();
	String blueprintGridColumnPublic();
	String blueprintAttributePublicTooltip();
	String blueprintGridColumnActiv();
	String blueprintActivTooltip();
	String blueprintAttributeGeodesksActiv();
	String blueprintAttributeGeodesksActivTooltip();
	String blueprintGridColumnActions();
	String bleuprintGridColumnActionsTooltip();
	String blueprintGridActionsColumnRemoveTooltip();
	String blueprintRemoveConfirmQuestion(String attribute);
	String blueprintsLoading();

	String blueprintDetailTabSettings();
	String blueprintDetailTabDataLayers();
	String blueprintDetailTabAccessRights();
	String blueprintDetailLoadingConfig();

	// Blueprint settings form in Blueprint Settings Tab of Blueprint detail panel
	String blueprintSettingsNameBlueprint();
	String blueprintSettingsClientApplicationName();
	String blueprintSettingsActiv();
	String blueprintSettingsActivTooltip();
	String blueprintSettingsPublic();
	String blueprintSettingsWarnCannotBePublic();

	String chooseAppRequired();
	String chooseAppTooltip();
	String chooseAppCreate();

	String datalayerDetailTabSettings();
	String datalayerDetailTabUpload();
	String datalayerDetailTabConnectionparameters();
	String datalayerDetailTabStyle();

	String datalayerGridColumnLayerName();
	String datalayerGridColumnGroup();
	String datalayerGridColumnLayerType();
	String datalayerGridColumnPublic();
	String datalayerGridColumnPublicTooltip();
	String datalayerGridColumnActive();
	String datalayerGridColumnActiveTooltip();
	String datalayerGridColumnActions();
	String datalayerGridColumnUserLayer();
	String datalayerGridColumnUserLayerTooltip();

	String datalayerGridActionsColumnRemoveTooltip();
	String datalayerGridWarnPublicCannotBeRemoved();
	String datalayerGridControlOnLayerUseBeforeRemove();
	String datalayerGridCannotRemoveLayerInUse();
	String datalayerGridRemoveConfirmQuestion(String name);
	String datalayerGridLoading();

	String datalayersNewDatalayerButtonText();
	String datalayersNewLayerIsBeingSaved();
	String datalayerShapeUploadFormGroup();
	String datalayerShapeUploadOverwriteConfirmTitle();
	String datalayerShapeUploadOverwriteConfirmQuestion();
	String datalayerShapeUploadNoFileSelected();
	String datalayerStyleFormGroup();


	String newLayerModelWindowTitle();
	String newLayerModelWizardCreateButtonText();

	String uploadShapefileLabelText();
	String uploadShapefileUploadingFile();
	String uploadShapefileResponseInvalidFile();
	String uploadShapefileResponseNoRights();
	String uploadShapefileResponseOK();
	String uploadShapefileResponseOkButWrong(String result);
	String uploadShapefileResponseDefaultNOK(String result);
	String uploadShapefileResponseInvalidLayer();

	String layerConfigurationConfigureLayer();
	String layerConfigurationLayerProperties();
	String layerConfigurationLayerStyle();
	String layerConfigurationName();
	String layerConfigurationNameTooltip();
	String layerConfigurationPublicLayer();
	String layerConfigurationLayerVisibleByDefault();
	String layerConfigurationLayerVisibleByDefaultTooltip();
	String layerConfigurationMinimumScale();
	String layerConfigurationOpacity();
	String layerConfigurationMinimumScaleTooltip();
	String layerConfigurationMaximumScale();
	String layerConfigurationMaximumScaleTooltip();
	String layerConfigConfirmRestoreText();
	String layerConfigConfirmRestoreTitle();
	String layerConfigExpertEditorBtn();
	String layerConfigExpertEditorMalformedSldException();

	String layerListGridColumnPublicTooltip();
	String layerListGridConfigurate();

	String layerGroupConfigWindowTitle();
	String layerGroupConfigGroupTitleProperties();
	String layerGroupConfigName();
	String layerGroupConfigNameTooltip();
	String layerGroupConfigOpenedAtStart();
	String layerGroupConfigOpenedAtStartTooltip();
	String layerSelectPanelHelpText();
	String layerSelectAvailableLayers();
	String layerSelectUserLayers();
	String layerSelectSelectedLayers();
	String layerSelectSelectedLayersTooltip();

	String layerAttributesGridLoadingText();
	String layerAttributesGridColumnAttribute();
	String layerAttributesGridColumnType();
	String layerAttributesGridColumnCoreInfo();
	String layerAttributesGridColumnCoreInfoTooltip();
	String layerAttributesGridColumnIdField();
	String layerAttributesGridColumnIdFieldTooltip();
	String layerAttributesGridColumnLabelField();
	String layerAttributesGridColumnLabelFieldTooltip();
	String layerAttributesGridColumnName();
	String layerAttributesGridColumnNameTooltip();
	String layerAttributesGriDeselectIdAttribute();
	String layerAttributesGriDeselectLabelAttribute();

	//----------------------------------------------

	String layerSettingsLayerName();
	String layerSettingsPublic();
	String layerSettingsPublicTooltip();
	String layerSettingsActive();
	String layerSettingsActiveTooltip();
	String layerSettingsVisibleByDefault();
	String layerSettingsVisibleByDefaultTooltip();
	String maxBoundsMinX();
	String maxBoundsMinY();
	String maxBoundsMaxX();
	String maxBoundsMaxY();

	String chooseTypeStepNumbering();
	String chooseTypeStepTitle();
	String chooseTypeStepRadioGroupTitle();
	String chooseTypeStepWFS();
	String chooseTypeStepShapeFile();
	String chooseTypeStepWMS();
	String chooseTypeStepDatabase();

	String databaseCapabilitiesStepNumbering();
	String databaseCapabilitiesStepTitle();
	String databaseCapabilitiesStepParametersHost();
	String databaseCapabilitiesStepParametersPort();
	String databaseCapabilitiesStepParametersScheme();
	String databaseCapabilitiesStepParametersDatabase();
	String databaseCapabilitiesStepParametersUserName();
	String databaseCapabilitiesStepParametersPassword();
	String databaseCapabilitiesStepChooseVectorLayerNotFound();

	String requestingInfoFromServer();

	String shapefileUploadStepNumbering();
	String shapefileUploadStepTitle();
	String shapefileUploadStepErrorDuringUpload();
	String shapefileUploadStepNextStepNotFound();

	String wfsCapabilitiesStepNumbering();
	String wfsCapabilitiesStepTitle();
	String wfsCapabilitiesStepParametersCapabilitiesURL();
	String wfsCapabilitiesStepParametersUserName();
	String wfsCapabilitiesStepParametersPassword();
	String wfsCapabilitiesStepNextStepNotFound();

	String vectorChooseLayerStepNumbering();
	String vectorChooseLayerStepTitle();
	String vectorChooseLayerStepParametersGroup();
	String vectorChooseLayerStepParametersName();
	String vectorChooseLayerStepParametersType();
	String vectorChooseLayerStepParametersDescription();
	String vectorChooseLayerStepNextStepNotFound();

	String vectorEditLayerAttributesStepNumbering();
	String vectorEditLayerAttributesStepTitle();
	String vectorEditLayerAttributesStepNextStepNotFound();

	String editLayerSettingsStepNumbering();
	String editLayerSettingsStepTitle();
	String editLayerSettingsStepVisibleArea();

	String vectorEditLayerStyleStepNumbering();
	String vectorEditLayerStyleStepTitle();
	String vectorEditLayerStyleStepSelectColor();
	String vectorEditLayerStyleStepStyleName();
	String vectorEditLayerStyleStepNextStepNotFound();

	String wmsCapabilitiesStepNumbering();
	String wmsCapabilitiesStepTitle();
	String wmsCapabilitiesStepParametersCapabilitiesURL();
	String wmsCapabilitiesStepParametersUserName();
	String wmsCapabilitiesStepParametersPassword();
	String wmsCapabilitiesStepNextStepNotFound();

	String wmsChooseLayerStepNumbering();
	String wmsChooseLayerStepTitle();
	String wmsChooseLayerStepName();
	String wmsChooseLayerStepCRS();
	String wmsChooseLayerStepDescription();

	String wmsPreviewLayerStepNumbering();
	String wmsPreviewLayerStepTitle();
	String wmsPreviewLayerStepNextStepNotFound();

	/* ## tab labels ## */
	String securityUsersTab();
	String securityGroupsTab();
	String securityAdminsTab();
	String securityUserDetailTab();
	String securityGroupDetailTab();
	String securityUserAssignTab();
	String securityGroupAssignTab();

	/* new item buttons */
	String securityNewUserButtonText();
	String securityNewGroupButtonText();

	/* user grid */
	String securityUserGridColumnEmail();
	String securityUserGridColumnName();
	String securityUserGridColumnActions();
	String securityUserGridColumnActionsTooltip();
	String securityUserGridColumnActiv();
	String securityUserGridColumnSurname();
	String securityUserGridActivTooltip();

	/* group grid */
	String securityGroupGridColumnActions();
	String securityGroupGridColumnActionsTooltip();
	String securityGroupGridColumnKey();
	String securityGroupGridColumnName();
	String securityGroupGridColumnTerritory();
	String securityGroupGridColumnTerritoryTooltip();

	/* user detail */
	String securityUserDetailEmail();
	String securityUserDetailName();
	String securityUserDetailSurname();
	String securityUserDetailOldPassword();
	String securityUserDetailPassword();
	String securityUserDetailPasswordConfirm();

	/* group detail */
	String securityGroupDetailKey();
	String securityGroupDetailName();
	String securityGroupDetailTerritoryText();
	String securityGroupDetailTerritoryHint();
	String securityGroupImportWindowTitle();
	String securityGroupImportWindowButtonText();

	String securityGroupWarningCantImportFile();
	String securityGroupWarningStopEditingFirst();
	String securityGroupWarningOnlyOneGeometryAllowed();
	String securityGroupWarningNoGeometryToEdit();
	String securityGroupWarningNoGeometryToDelete();
	String securityGroupWarningNoChangesToSave();
	String securityGroupWarningCantZoomToGeometry();
	String securityGroupWarningExceptionDuringEditing(String exceptionMessage);

	/* roles text */
	String securityRoleAdministratorText();
	String securityRoleReadOnlyText();
	String securityRoleDeskManagerText();
	String securityRoleReadWriteText();

	/* groups */
	String groupSelectAssignedGroups();
	String groupSelectAssignedGroupsHelpText();
	String groupSelectAssignedGroupsTooltip();
	String groupSelectAvailableGroups();
	String groupsLoading();
	String groupGridActionsColumnRemoveTooltip();
	String groupGridRemoveConfirmQuestion(String name);

	/* users */
	String userSelectAssignedUsers();
	String userSelectAssignedUsersHelpText();
	String userSelectAssignedUsersTooltip();
	String userSelectAvailableUsers();
	String usersLoading();
	String userGridActionsColumnRemoveTooltip();
	String usersGridRemoveConfirmQuestion(String name);
	String securityUserDetailErrorEmailSyntax();
	String securityUserDetailErrorPasswordSyntax();
	String securityUserDetailErrorPasswordsDoNotMatch();
	String securityUserDetailActiveLabel();
	String securityUserDetailActiveTooltip();

	String deleteUserFailedWarnMessage();
	String layerSettingsEnableFeatureInfo();
	String layerSettingsEnableFeatureInfoTooltip();
	String layerSettingsFeatureInfoFormat();
	String layerSettingsFeatureInfoFormatTooltip();

	String adminAssignHelpText();
	String adminAssignAvailableUsers();
	String adminAssignAssignedUsers();
	String adminAssignAssignedUsersTooltip();
}
