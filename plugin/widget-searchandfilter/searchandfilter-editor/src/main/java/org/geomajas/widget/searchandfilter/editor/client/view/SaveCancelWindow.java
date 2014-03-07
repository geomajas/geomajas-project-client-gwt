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
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.widget.searchandfilter.editor.client.i18n.SearchAndFilterEditorMessages;
import org.geomajas.widget.searchandfilter.editor.client.presenter.EditorPresenter;

/**
 * Wrapper around a layout, resulting in a window with cancel and save button.
 *
 * @author Jan Venstermans
 */
public class SaveCancelWindow extends Window implements EditorPresenter.SaveView {

	private final SearchAndFilterEditorMessages messages =
			GWT.create(SearchAndFilterEditorMessages.class);

	private Canvas content;

	private EditorPresenter.SaveHandler handler;

	/**
	 * @param content the view to be wrapped
	 */
	public SaveCancelWindow(Canvas content) {
		this.content = content;
		layout();
	}

	/**
	 * @param content the view to be wrapped
	 * @param handler
	 */
	public SaveCancelWindow(Canvas content, EditorPresenter.SaveHandler handler) {
		this(content);
		setSaveHandler(handler);
	}

	/**
	 *
	 */
	private void layout() {
		setAutoSize(true);
		setCanDragReposition(true);
		setCanDragResize(false);
		setKeepInParentRect(true);
		setOverflow(Overflow.HIDDEN);
		setAutoCenter(true);
		setShowCloseButton(false);
		setShowMinimizeButton(false);
		setShowMaximizeButton(false);
		setIsModal(true);
		setShowModalMask(true);

		// buttons //

		HLayout buttons = new HLayout(10);
		IButton save = new IButton(messages.saveButtonText());
		save.setIcon(WidgetLayout.iconSave);
		save.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				saved();
			}
		});
		IButton cancel = new IButton(messages.cancelButtonText());
		cancel.setIcon(WidgetLayout.iconCancel);
		cancel.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				cancelled();
			}
		});
		buttons.addMember(save);
		buttons.addMember(cancel);

		// layout structure //
		VLayout layout = new VLayout(10);
		layout.addMember(content);
		layout.addMember(buttons);
		addItem(layout);
	}

	public void show() {
		super.show();
	}

	@Override
	public void setSaveHandler(EditorPresenter.SaveHandler handler) {
		this.handler = handler;
	}

	private void cancelled() {
		hide();
	}

	private void saved() {
		handler.onSave();
	}
}
