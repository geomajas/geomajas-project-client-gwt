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

package org.geomajas.widget.searchandfilter.editor.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * <p>
 * Localization messages for the GWT test samples.
 * </p>
 *
 * @author Jan Venstermans
 */
public interface SearchAndFilterEditorMessages extends Messages {

	String detailTabSearches();
	String searchesGroupTitle();

	String searchesGridColumnSearchName();
	String searchesGridColumnSearchNameTooltip();
	String searchesGridColumnSearchDescription();
	String searchesGridColumnSearchDescriptionTooltip();
	String searchesGridColumnActions();
	String searchesGridColumnActionsTooltip();

	String searchDetailsWindowTitle();
	String searchDetailsWindowFieldTitleLabel();
	String searchDetailsWindowFieldTitleTooltip();
	String searchDetailsWindowFieldDescriptionLabel();
	String searchDetailsWindowFieldDescriptionTooltip();
	String searchDetailsWindowFieldTitleInWindowLabel();
	String searchDetailsWindowFieldTitleInWindowTooltip();
	String searchDetailsWindowFieldIconUrlLabel();
	String searchDetailsWindowFieldIconUrlTooltip();

	String searchAttributeWindowAttributeNameLabel();
	String searchAttributeWindowLabelLabel();
	String searchAttributeWindowOperationLabel();
	String searchAttributeWindowInputTypeLabel();

	String saveButtonText();
	String cancelButtonText();

	/* SearchAttribute */
	String searchAttributeOperationEqualToString();
	String searchAttributeOperationEqualToInteger();
	String searchAttributeOperationSmallerThanString();
	String searchAttributeOperationSmallerThanInteger();
	String searchAttributeOperationLargerThanString();
	String searchAttributeOperationLargerThanInteger();

	String searchAttributeInputTypeFreeTextToString();
	String searchAttributeInputTypeFreeNrToString();
	String searchAttributeInputTypeDropDownToString();
}