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
package org.geomajas.plugin.deskmanager.client.gwt.manager.validation;

import javax.validation.Validator;

import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

/**
 * @author Jan De Moerloose
 */
public final class ManagerValidatorFactory extends AbstractGwtValidatorFactory {

	/**
	 * @author Jan De Moerloose
	 */
	  @GwtValidation({TerritoryDto.class, UserDto.class })
	  public interface GwtValidator extends Validator {
	  }

	  @Override
	  public AbstractGwtValidator createValidator() {
		  return GWT.create(GwtValidator.class);
	  }
}