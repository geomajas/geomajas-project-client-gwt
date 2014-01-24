package org.geomajas.gwt.client.controller;


public interface MeasureDistanceInfoHandler {
	
	public void onStart();

	public void onDistance(double totalDistance, double lastSegment);
	
	public void onStop();

}
