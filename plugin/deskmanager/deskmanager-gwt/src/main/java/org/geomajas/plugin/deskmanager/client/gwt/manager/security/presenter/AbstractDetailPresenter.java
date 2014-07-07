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
package org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter;

import com.google.gwt.core.client.Callback;
import org.geomajas.global.ExceptionDto;
import org.geomajas.plugin.deskmanager.client.gwt.manager.security.view.DetailView;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;

/**
 * Abstract implementation of
 * {@link org.geomajas.plugin.deskmanager.client.gwt.manager.security.presenter.DetailHandler}.
 * @author Jan De Moerloose
 * @author Jan Venstermans
 * @param <T> The object whose details will be managed by the presenter.
 */
public abstract class AbstractDetailPresenter<T> extends AbstractEditablePresenter implements DetailHandler<T> {

	private DetailView<T> view;

	private T currentObject;

	private ObjectsTabHandler<T> objectsTabHandler;

	public AbstractDetailPresenter(DetailView<T> view) {
		super(view);
		this.view = view;
		view.setHandler(this);
	}

	//---------------------------------------------
	// presenter methods
	//---------------------------------------------

	@Override
	public void setObjectsTabHandler(ObjectsTabHandler<T> groupsHandler) {
		this.objectsTabHandler = groupsHandler;
	}

	@Override
	public void loadObject(T object) {
		view.setEnabled(false);
		if (object != null) {
			view.setLoading();
			getObjectFromService(object, new DataCallback<T>() {

				@Override
				public void execute(T objectFromDb) {
					setCurrentObject(objectFromDb);
					view.setLoaded();
					view.setEnabled(true);
				}
			});
		} else {
			setCurrentObject(null);
		}
	}

	protected abstract void getObjectFromService(T object, DataCallback<T> onFinish);

	@Override
	public void createNewObject() {
		setCurrentObject(createEmptyObject());
		setEditable(true);
		view.prepareForNewObjectInput();
	}

	protected abstract T createEmptyObject();

	//---------------------------------------------
	// handler methods
	//---------------------------------------------

	@Override
	public void onCancel() {
		super.onCancel();
		setCurrentObject(currentObject);
	}

	protected void loadAllAndShowObject(final T object) {
		objectsTabHandler.loadAll(new Callback<Boolean, ExceptionDto>() {
			@Override
			public void onFailure(ExceptionDto reason) {
				// do nothing
			}

			@Override
			public void onSuccess(Boolean result) {
				 if (result) {
					 objectsTabHandler.onSelect(object);
				 }
			}
		});
	}

	private void setCurrentObject(T object) {
		this.currentObject = object;
		view.clearValues();
		if (object != null) {
			view.setObject(object);
			view.setEnabled(true);
		} else {
			setEditable(false);
			view.setEnabled(false);
		}
	}
}
