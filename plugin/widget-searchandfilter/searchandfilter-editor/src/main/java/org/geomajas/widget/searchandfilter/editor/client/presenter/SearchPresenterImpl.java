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

import com.smartgwt.client.widgets.Canvas;
import org.geomajas.widget.searchandfilter.editor.client.SearchAndFilterEditor;
import org.geomajas.widget.searchandfilter.editor.client.configuration.SearchAttribute;
import org.geomajas.widget.searchandfilter.editor.client.configuration.SearchConfig;
import org.geomajas.widget.searchandfilter.editor.client.event.SearchesInfoChangedEvent;

/**
 * Default implementation of {@link org.geomajas.widget.searchandfilter.editor.client.presenter.SearchesPresenter}.
 *
 * @author Jan Venstermans
 */
public class SearchPresenterImpl implements SearchPresenter, SearchPresenter.Handler,
		SearchesInfoChangedEvent.Handler {

	private View view;

	private SearchAttributePresenter searchAttributePresenter;

	public SearchPresenterImpl() {
		this.view = SearchAndFilterEditor.getViewManager().getSearchView();
		this.searchAttributePresenter = new SearchAttributePresenterImpl();
		view.setHandler(this);
		view.setSearchConfig(getSelectedSearchConfig());
		bind();
	}

	private void bind() {
		SearchAndFilterEditor.addSearchesInfoChangedHandler(this);
	}

	private SearchConfig getSelectedSearchConfig() {
		return SearchAndFilterEditor.getSearchesStatus().getSelectedSearchConfig();
	}

	@Override
	public void onAddAttribute() {
		searchAttributePresenter.createSearchAttribute();
	}

	@Override
	public void onSelect(SearchAttribute attribute) {
		SearchAndFilterEditor.getSearchesStatus().setSelectedSearchAttribute(attribute);
	}

	@Override
	public void onEdit(SearchAttribute attribute) {
		onSelect(attribute);
		searchAttributePresenter.editSelectedAttribute();
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public void editSelectedSearch() {
		getView().show(getSelectedSearchConfig());
	}

	@Override
	public void createSearch() {
		getView().show(new SearchConfig());
	}

	@Override
	public void onSave() {
		if (view.validate()) {
			SearchConfig searchConfig = view.getSearchConfig();
			SearchAndFilterEditor.getSearchesStatus().saveSearch(searchConfig);
			view.hide();
		}
	}

	@Override
	public void onSearchInfoChanged(SearchesInfoChangedEvent event) {
	   	view.update();
	}
}
