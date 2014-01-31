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
package org.geomajas.plugin.editing.jsapi.gwt.client.gfx;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.event.InfoDragLineChangedEvent;
import org.geomajas.plugin.editing.gwt.client.handler.InfoDragLineHandler;
import org.geomajas.plugin.editing.jsapi.gwt.client.JsGeometryEditor;
import org.geomajas.plugin.editing.jsapi.gwt.client.listener.InfoChangedListener;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * JavaScript wrapper of {@link org.geomajas.plugin.editing.gwt.client.handler.InfoDragLineHandler}.
 * 
 * @author Jan De Moerloose
 * @author Jan Venstermans
 * @since 1.0.0
 * 
 */
@Export("InfoHandler")
@ExportPackage("org.geomajas.plugin.editing.gfx")
@Api(allMethods = true)
public class JsInfoHandler implements Exportable, InfoDragLineChangedEvent.Handler {

	private InfoDragLineHandler delegate;

	private GeometryEditor editor;

	private InfoChangedListener infoLineCallback;

	private EventBus eventBus;

	/**
	 * Needed for exporter.
	 */
	public JsInfoHandler() {
	}

	/**
	 * JavaScript constructor.
	 * 
	 * @param jsEditor the editor service
	 */
	@Export
	public JsInfoHandler(JsGeometryEditor jsEditor) {
		editor = jsEditor.getDelegate();
		eventBus = new SimpleEventBus();
		eventBus.addHandler(InfoDragLineChangedEvent.getType(), this);
		delegate = new InfoDragLineHandler(editor.getMapWidget(), editor.getEditService(), eventBus);
	}

	/**
	 * Register this handler for editor events.
	 */
	public void register() {
		delegate.register();
	}

	/**
	 * Unregister this handler from editor events.
	 */
	public void unregister() {
		delegate.unregister();
	}

	/**
	 * Return whether this handler is registered with the editor.
	 * 
	 * @return true if registered
	 */
	public boolean isRegistered() {
		return delegate.isRegistered();
	}

	/**
	 * Set the callback closure to get the HTML content.
	 *
	 * @param infoLineCallback the info of
	 */
	public void addInfoListener(InfoChangedListener infoLineCallback) {
		this.infoLineCallback = infoLineCallback;
	}

	@Override
	public void onChanged(InfoDragLineChangedEvent event) {
		infoLineCallback.onInfoChanged(event.getGeometry(), event.getDragPoint(),
				event.getStartA(), event.getStartB());
	}

}
