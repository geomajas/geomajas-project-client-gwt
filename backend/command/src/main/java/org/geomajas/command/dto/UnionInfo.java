/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.dto;

import org.geomajas.annotation.Api;

import java.io.Serializable;

/**
 * DTO object that holds additional information needed to perform a union operation.
 * 
 * @author Jan De Moerloose
 * @since 1.11.0
 * 
 */
@Api(allMethods = true)
public class UnionInfo implements Serializable {

	private static final long serialVersionUID = 1110L;

	private int precision = -1;

	private boolean usePrecisionAsBuffer;

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public boolean isUsePrecisionAsBuffer() {
		return usePrecisionAsBuffer;
	}

	public void setUsePrecisionAsBuffer(boolean usePrecisionAsBuffer) {
		this.usePrecisionAsBuffer = usePrecisionAsBuffer;
	}

	@Override
	public String toString() {
		return "UnionInfo{" +
				"precision=" + precision +
				", usePrecisionAsBuffer=" + usePrecisionAsBuffer +
				'}';
	}
}
