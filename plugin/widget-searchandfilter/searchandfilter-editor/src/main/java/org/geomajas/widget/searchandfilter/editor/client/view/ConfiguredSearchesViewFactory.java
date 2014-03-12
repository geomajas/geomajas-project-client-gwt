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
package org.geomajas.widget.searchandfilter.editor.client.view;

import org.geomajas.widget.searchandfilter.editor.client.presenter.ConfiguredSearchesPresenter;

/**
 * Default factory for  {@link org.geomajas.widget.searchandfilter.editor.client
 * .presenter.ConfiguredSearchesPresenter.View}.
 *
 * @author Jan Venstermans
 *
 */
public class ConfiguredSearchesViewFactory {

	public ConfiguredSearchesPresenter.View createSearchesView() {
		 return new ConfiguredSearchesView();
	}

}
