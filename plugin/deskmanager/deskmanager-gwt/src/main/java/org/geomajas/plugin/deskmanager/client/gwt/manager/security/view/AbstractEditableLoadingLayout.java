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
package org.geomajas.plugin.deskmanager.client.gwt.manager.security.view;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.Geomajas;

/**
 * Abstract class for a panel with three buttons: edit, cancel, save.
 *
 * @author Jan Venstermans
 */
public abstract class AbstractEditableLoadingLayout extends AbstractButtonsLayout implements EditableLoadingView {

	protected Label loadingLabel;

	protected VLayout loadingLayout;

	public AbstractEditableLoadingLayout() {
		super();
		createLoadingWidget();
		addChild(loadingLayout);
	}

	@Override
	public void setLoading() {
		loadingLabel.setContents("<B><i>" + MESSAGES.blueprintDetailLoadingConfig() + "</i> " + "<img src='"
				+ Geomajas.getIsomorphicDir() + "/images/circle.gif' style='height: 1em' /></B>");
		loadingLayout.animateShow(AnimationEffect.FADE);
	}

	@Override
	public void setLoaded() {
		loadingLayout.animateHide(AnimationEffect.FADE);
		loadingLabel.setContents("");
	}

	//---------------------------------------------------------------------
	// private methods
	//---------------------------------------------------------------------

	private void createLoadingWidget() {
		loadingLayout = new VLayout();
		loadingLayout.setWidth100();
		loadingLayout.setHeight100();
		loadingLayout.setOpacity(70);
		loadingLayout.setBackgroundColor("ffffff");

		loadingLabel = new Label();
		loadingLabel.setTop("30%");
		loadingLabel.setAlign(Alignment.CENTER);

		loadingLayout.addMember(loadingLabel);
		loadingLayout.hide();
	}
}
