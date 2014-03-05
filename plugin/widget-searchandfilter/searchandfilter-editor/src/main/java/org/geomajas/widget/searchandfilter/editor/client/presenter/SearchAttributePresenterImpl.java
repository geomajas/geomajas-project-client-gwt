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
package org.geomajas.widget.searchandfilter.editor.client.presenter;

import org.geomajas.widget.searchandfilter.editor.client.SearchAndFilterEditor;
import org.geomajas.widget.searchandfilter.editor.client.configuration.SearchAttribute;

/**
 * Default implementation of {@link SearchAttributePresenter}.
 *
 * @author Jan Venstermans
 */
public class SearchAttributePresenterImpl implements SearchAttributePresenter,
		SearchAttributePresenter.Handler {

	private View view;

	public SearchAttributePresenterImpl() {
		this.view = SearchAndFilterEditor.getViewManager().getSearchAttributeView();
		view.setHandler(this);
		view.setSearchAttribute(getSelectedSearchAttribute());
		bind();
	}

	private void bind() {

	}

	private SearchAttribute getSelectedSearchAttribute() {
		return SearchAndFilterEditor.getSearchesStatus().getSelectedSearchAttribute();
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public void editSelectedAttribute() {
		getView().show(getSelectedSearchAttribute());
	}

	@Override
	public void createSearchAttribute() {
		getView().show(new SearchAttribute());
	}

	@Override
	public void onSave() {
		if (view.validate()) {
			SearchAttribute attribute = view.getSearchAttribute();
			view.hide();
		}
	}

	@Override
	public void onSelect(SearchAttribute config) {

	}

	@Override
	public void onEdit(SearchAttribute config) {

	}
}
