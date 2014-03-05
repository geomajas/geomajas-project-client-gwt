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

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.impl.DeskmanagerIcon;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.editor.client.configuration.SearchConfig;
import org.geomajas.widget.searchandfilter.editor.client.configuration.SearchesStatus;
import org.geomajas.widget.searchandfilter.editor.client.presenter.SearchesPresenter;

/**
 * Factory for  {@link org.geomajas.widget.searchandfilter.editor.client.presenter.SearchesPresenter.View}.
 *
 * @author Jan Venstermans
 *
 */
public class SearchesViewFactory {

	public SearchesPresenter.View createSearchesView() {
		 return new SearchesView();
	}

}
