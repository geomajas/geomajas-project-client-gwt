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
package org.geomajas.widget.searchandfilter.search.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Dto object for a search configuration on a vector layer.
 *
 * @author Jan Venstermans
 */
public class ConfiguredSearch implements Criterion {

	private static final long serialVersionUID = 100L;

	private String title; // consider as id

	private String description, titleInWindow;

	private String iconUrl;

	private List<ConfiguredSearchAttribute> attributes = new ArrayList<ConfiguredSearchAttribute>();

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

	public List<ConfiguredSearchAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<ConfiguredSearchAttribute> attributes) {
		this.attributes = attributes;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public void serverLayerIdVisitor(Set<String> layerIds) {
		// do nothing?
	}

	@Override
	public List<Criterion> getCriteria() {
		List<Criterion> criteria = new ArrayList<Criterion>();
		for (ConfiguredSearchAttribute attribute : attributes) {
			criteria.add(attribute);
		}
		return criteria;
	}

	@Override
	public String getDisplayText() {
		return "ConfiguredSearch";  // should not be used
	}
}
