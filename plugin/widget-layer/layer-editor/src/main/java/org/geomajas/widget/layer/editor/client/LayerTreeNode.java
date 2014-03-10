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
package org.geomajas.widget.layer.editor.client;

import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;
import org.geomajas.widget.layer.configuration.client.ClientAbstractNodeInfo;
import org.geomajas.widget.layer.configuration.client.ClientBranchNodeInfo;
import org.geomajas.widget.layer.configuration.client.ClientLayerNodeInfo;

import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LayerTreeNode extends TreeNode {

	public static final String FLD_NAME = "fld_name";

	public static final String FLD_PUBLIC = "fld_public";

	private ClientAbstractNodeInfo node;

	private LayerDto layer;

	public LayerTreeNode(ClientBranchNodeInfo node) {
		this(node, null);
	}
	
	public LayerTreeNode(ClientAbstractNodeInfo node, LayerDto layer) {
		if (node == null) {
			throw new IllegalArgumentException("Was expecting a node!");
		}
		this.node = node;
		this.layer = layer;
		if (node instanceof ClientBranchNodeInfo) {
			setName(((ClientBranchNodeInfo) node).getLabel());
			setAttribute(FLD_NAME, ((ClientBranchNodeInfo) node).getLabel());
			setAttribute(LayerTreeNode.FLD_PUBLIC, true); // no such thing as non-public folders
			setIsFolder(true);
		} else if (node instanceof ClientLayerNodeInfo) {
			setID(layer.getClientLayerIdReference());
			setName(layer.getClientLayerIdReference());
			if (layer != null) {
				if (layer.getClientLayerInfo() != null) {
					setAttribute(FLD_NAME, layer.getClientLayerInfo().getLabel());
				} else if (layer.getReferencedLayerInfo() != null) {
					setAttribute(FLD_NAME, layer.getReferencedLayerInfo().getLabel());
				} else if (layer.getLayerModel() != null) {
					setAttribute(FLD_NAME, layer.getLayerModel().getName());
				}
			}
			setAttribute(LayerTreeNode.FLD_PUBLIC, layer.getLayerModel().isPublic());
			setIsFolder(false);
		}
	}

	public ClientAbstractNodeInfo getNode() {
		return node;
	}

	/**
	 * Get client layer info, might return null if node isn't a leaf.
	 * 
	 * @return the clientLayerInfo
	 */
	public LayerDto getLayer() {
		return layer;
	}
}