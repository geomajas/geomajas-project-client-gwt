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

	/* General */
	String detailTabSearches();
	String searchesGroupTitle();
	String saveButtonText();
	String cancelButtonText();

	/* Searches panel */
	String searchesGridColumnSearchName();
	String searchesGridColumnSearchDescription();
	String searchesGridColumnActions();
	String searchesGridColumnActionsTooltip();
	String searchesAddSearchConfigButtonTooltip();

	/* Search window */
	String searchDetailsWindowTitle();
	String searchDetailsWindowFieldTitleLabel();
	String searchDetailsWindowFieldDescriptionLabel();
	String searchDetailsWindowFieldTitleInWindowLabel();
	String searchDetailsWindowFieldIconUrlLabel();
	String searchDetailsWindowGridColumnAttributeName();
	String searchDetailsWindowGridColumnLabel();
	String searchDetailsWindowGridColumnOperation();
	String searchDetailsWindowGridColumnInputType();
	String searchDetailsWindowGridColumnActions();
	String searchDetailsWindowGridColumnActionsTooltip();
	String searchDetailsWindowAddSearchAttributeButtonTooltip();

	/* ConfiguredSearchAttribute window */
	String searchAttributeWindowTitle();
	String searchAttributeWindowAttributeNameLabel();
	String searchAttributeWindowLabelLabel();
	String searchAttributeWindowOperationLabel();
	String searchAttributeWindowInputTypeLabel();
	String searchAttributeWindowInputDropDownButtonTooltip();
	String searchAttributeWindowGridValue();

}