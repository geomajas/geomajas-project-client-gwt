package org.geomajas.widget.advancedviews.configuration.client;

import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * Configuration data for an image widget.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ImageInfo implements ClientWidgetInfo {

	/**
	 * Use this identifier in your configuration files (beans).
	 */
	public static final String IDENTIFIER = "ImageInfo";

	private static final long serialVersionUID = 100L;

	private String url;

	private String alt;

	private String href;

	private int width;

	private int height;

	/**
	 * Get the image url.
	 * 
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set the image url.
	 * 
	 * @param url the url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Get an alternative text for non-image capable browsers.
	 * 
	 * @return alternative text
	 */
	public String getAlt() {
		return alt;
	}

	/**
	 * Set an alternative text.
	 * 
	 * @param alt alternative text
	 */
	public void setAlt(String alt) {
		this.alt = alt;
	}

	/**
	 * Get the URL to open when the image is clicked upon.
	 * 
	 * @return the URL to open
	 */
	public String getHref() {
		return href;
	}

	/**
	 * Set the URL to open when the image is clicked upon.
	 * 
	 * @param href the URL to open
	 */
	public void setHref(String href) {
		this.href = href;
	}

	/**
	 * Get the width in pixels of the image.
	 * 
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the width in pixels.
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Get the height in pixels of the image.
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set the height in pixels.
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

}
