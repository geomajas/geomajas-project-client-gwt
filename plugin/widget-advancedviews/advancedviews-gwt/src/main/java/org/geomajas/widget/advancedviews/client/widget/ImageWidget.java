/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.advancedviews.client.widget;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.util.UrlBuilder;
import org.geomajas.widget.advancedviews.configuration.client.ImageInfo;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ImageStyle;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Clickable image of fixed size, opens associated link in separate window. This widget has an editor for desk manager.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ImageWidget extends VLayout {

	protected Img image;

	/**
	 * 
	 * @param imageInfo
	 */
	public ImageWidget(final ImageInfo imageInfo) {
		setShowEdges(true);
		setDefaultLayoutAlign(Alignment.CENTER);
		image = new Img();
		UrlBuilder url = new UrlBuilder(Geomajas.getDispatcherUrl() + imageInfo.getUrl());
		image.setSrc(url.toString());
		image.setAltText(imageInfo.getAlt());
		image.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				com.google.gwt.user.client.Window.open(imageInfo.getHref(), "_blank", "");
			}
		});

		// centralize
		image.setImageType(ImageStyle.CENTER);
		image.setWidth(imageInfo.getWidth());
		image.setHeight(imageInfo.getHeight());
		addMember(image);
	}
}
