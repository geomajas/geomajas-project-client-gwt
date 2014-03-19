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
package org.geomajas.widget.searchandfilter.editor.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditorFactoryRegistry;
import org.geomajas.widget.searchandfilter.editor.client.event.SearchesInfoChangedEvent;
import org.geomajas.widget.searchandfilter.editor.client.event.VectorLayerInfoChangedEvent;
import org.geomajas.widget.searchandfilter.editor.client.view.ViewManager;
import org.geomajas.widget.searchandfilter.editor.client.view.ViewManagerImpl;

/**
 * Entry Point and central class for the SearchAndFilterEditor plugin.
 * @author Jan Venstermans
 *
 */
public final class SearchAndFilterEditor implements EntryPoint {

	private static EventBus eventBus = new SimpleEventBus();

	private static ViewManager viewManager = new ViewManagerImpl();

	private SearchAndFilterEditor() {
	}

	@Override
	public void onModuleLoad() {
		WidgetEditorFactoryRegistry.getMapRegistry().register(new ConfiguredSearchEditorFactory());
		WidgetEditorFactoryRegistry.getApplicationRegistry().register(new ConfiguredSearchEditorFactory());
	}

	public static ConfiguredSearchesStatus getConfiguredSearchesStatus() {
		return ConfiguredSearchesStatusImpl.getInstance();
	}

	public static ViewManager getViewManager() {
		return viewManager;
	}

	public static void setViewManager(ViewManager viewManager) {
		SearchAndFilterEditor.viewManager = viewManager;
	}

	public static HandlerRegistration addSearchesInfoChangedHandler(SearchesInfoChangedEvent.Handler handler) {
		return eventBus.addHandler(SearchesInfoChangedEvent.getType(), handler);
	}

	public static void fireSearchesInfoChangedEvent(SearchesInfoChangedEvent event) {
		eventBus.fireEvent(event);
	}

	public static HandlerRegistration addVectorLayerInfoChangedHandler(VectorLayerInfoChangedEvent.Handler handler) {
		return eventBus.addHandler(VectorLayerInfoChangedEvent.getType(), handler);
	}

	public static void fireVectorLayerInfoChangedEvent() {
		eventBus.fireEvent(new VectorLayerInfoChangedEvent());
	}

	public static ConfiguredSearchAttributeService getSearchAttributeService() {
		return ConfiguredSearchAttributeServiceImpl.getInstance();
	}

}
