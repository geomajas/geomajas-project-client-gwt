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
package org.geomajas.widget.searchandfilter.editor.client.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Search Dto object.
 *
 * @author Jan Venstermans
 */
public class SearchConfig implements Serializable {

	private static final long serialVersionUID = 100L;

	private String title, description, titleInWindow;

	private String iconUrl;

	private List<SearchAttribute> attributes = new ArrayList<SearchAttribute>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitleInWindow() {
		return titleInWindow;
	}

	public void setTitleInWindow(String titleInWindow) {
		this.titleInWindow = titleInWindow;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public List<SearchAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<SearchAttribute> attributes) {
		this.attributes = attributes;
	}
}
