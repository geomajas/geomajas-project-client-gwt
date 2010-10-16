/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.plugin.caching.step;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Step to invalidate caches for features which need to be updated during SaveOrUpdate.
 *
 * @author Joachim Van der Auwera
 */
public class UpdateFeatureInvalidateStep implements PipelineStep {

	private Logger log = LoggerFactory.getLogger(UpdateFeatureInvalidateStep.class);

	@Autowired
	private CacheManagerService cacheManager;

	@Autowired
	private TestRecorder recorder;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, Object result) throws GeomajasException {
		try {
			log.debug("UpdateFeatureInvalidateStep start");
			VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);

			// invalidate the area of the old feature
			InternalFeature oldFeature = context.getOptional(PipelineCode.OLD_FEATURE_KEY, InternalFeature.class);
			if (null != oldFeature) {
				// get original geometry from storage to assure not changed by transformation and available
				Object feature = layer.read(oldFeature.getId());
				context.put(PipelineCode.FEATURE_DATA_OBJECT_KEY, feature); // put in context to prevent getting twice
				FeatureModel featureModel = layer.getFeatureModel();
				Geometry oldGeometry = featureModel.getGeometry(feature);
				if (null != oldGeometry) {
					// invalidate
					recorder.record("layer", "Invalidate geometry for old version of feature");
					Envelope oldEnvelope = oldGeometry.getEnvelopeInternal();
					log.debug("invalidate old feature area {}", oldEnvelope);
					cacheManager.invalidate(layer, oldEnvelope);
				}
			}

			// invalidate area for new feature
			InternalFeature feature = context.get(PipelineCode.FEATURE_KEY, InternalFeature.class);
			Geometry geometry = feature.getGeometry();
			if (null != geometry) {
				recorder.record("layer", "Invalidate geometry for new feature");
				Envelope envelope = geometry.getEnvelopeInternal();
				log.debug("invalidate new feature area {}", envelope);
				cacheManager.invalidate(layer, envelope);
			}
		} catch (Throwable t) {
			// have to prevent caching code from making the pipeline fail, log and discard errors
			log.error("Error during caching step, only logged: " + t.getMessage(), t);
		}
	}
}
