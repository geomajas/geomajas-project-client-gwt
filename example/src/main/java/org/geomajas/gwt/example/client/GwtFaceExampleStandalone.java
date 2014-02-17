/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.example.client;

import org.geomajas.gwt.client.util.Log;
import org.geomajas.gwt.client.widget.GraphicsWidget;
import org.geomajas.gwt.example.base.ExampleLayout;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.impl.DOMImpl;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.IMenuButton;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * <p>
 * The GWT test case sample application. Here here!
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GwtFaceExampleStandalone implements EntryPoint {
	
	public void onModuleLoad1() {  
        VLayout canvas = new VLayout(); 
        canvas.setBackgroundColor("green");
        canvas.addMember(new GraphicsWidget("myId"));
        
  
        final Img widget = new Img("person.png", 100, 100);  
        widget.setLeft(200);  
        widget.setTop(75);  
//        canvas.addChild(widget);  
  
        Menu sizeMenu = new Menu();  
        sizeMenu.setWidth(150);  
  
        MenuItem smallItem = new MenuItem("Small");  
        smallItem.setCheckIfCondition(new MenuItemIfFunction() {  
            public boolean execute(Canvas target, Menu menu, MenuItem item) {  
                return widget.getWidth() == 50;  
            }  
        });  
        smallItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {  
            public void onClick(MenuItemClickEvent event) {  
                widget.animateResize(50, 50);  
            }  
        });  
  
        MenuItem mediumItem = new MenuItem("Medium");  
        mediumItem.setCheckIfCondition(new MenuItemIfFunction() {  
            public boolean execute(Canvas target, Menu menu, MenuItem item) {  
                return widget.getWidth() == 100;  
            }  
        });  
        mediumItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {  
            public void onClick(MenuItemClickEvent event) {  
                widget.animateResize(100, 100);  
            }  
        });  
  
        MenuItem largeItem = new MenuItem("Large");  
        largeItem.setCheckIfCondition(new MenuItemIfFunction() {  
            public boolean execute(Canvas target, Menu menu, MenuItem item) {  
                return widget.getWidth() == 200;  
            }  
        });  
        largeItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {  
            public void onClick(MenuItemClickEvent event) {  
                widget.animateResize(200, 200);  
            }  
        });  
  
        sizeMenu.setItems(smallItem, mediumItem, largeItem);  
  
        Menu moveMenu = new Menu();  
        moveMenu.setWidth(150);  
  
        MenuItem upItem = new MenuItem("Up");  
        upItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {  
            public void onClick(MenuItemClickEvent event) {  
                widget.animateMove(widget.getOffsetX(), widget.getOffsetY() - 20);  
            }  
        });  
  
        MenuItem rightItem = new MenuItem("Right");  
        rightItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {  
            public void onClick(MenuItemClickEvent event) {  
                widget.animateMove(widget.getOffsetX() + 20, widget.getOffsetY());  
            }  
        });  
  
        MenuItem downItem = new MenuItem("Down");  
        downItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {  
            public void onClick(MenuItemClickEvent event) {  
                widget.animateMove(widget.getOffsetX(), widget.getOffsetY() + 20);  
            }  
        });  
  
        MenuItem leftItem = new MenuItem("Left");  
        leftItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {  
            public void onClick(MenuItemClickEvent event) {  
                widget.animateMove(widget.getOffsetX() - 20, widget.getOffsetY());  
            }  
        });  
  
        moveMenu.setData(upItem, rightItem, downItem, leftItem);  
  
        Menu mainMenu = new Menu();  
        mainMenu.setWidth(150);  
        widget.setContextMenu(mainMenu);  
  
        MenuItem visibleItem = new MenuItem();  
        visibleItem.setTitle("Visible");  
        visibleItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {  
            public void onClick(MenuItemClickEvent event) {  
                if (widget.isVisible()) {  
                    widget.animateHide(AnimationEffect.FADE);  
                } else {  
                    widget.animateShow(AnimationEffect.FADE);  
                }  
            }  
        });  
  
        MenuItemSeparator separator = new MenuItemSeparator();  
  
        MenuItem sizeItem = new MenuItem("Size");  
        sizeItem.setSubmenu(sizeMenu);  
  
        MenuItem moveItem = new MenuItem("Move");  
        moveItem.setSubmenu(moveMenu);  
  
        MenuItem resetItem = new MenuItem("Reset");  
        resetItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {  
            public void onClick(MenuItemClickEvent event) {  
                widget.animateRect(200, 75, 100, 100);  
                widget.animateShow(AnimationEffect.FADE);  
            }  
        });  
        resetItem.setIcon("person.png");  
        resetItem.setIconWidth(20);  
        resetItem.setIconHeight(20);  
  
        mainMenu.setItems(visibleItem, separator, sizeItem, moveItem, separator, resetItem);  
  
        IMenuButton mainMenuButton = new IMenuButton("Widget", mainMenu);  
        mainMenuButton.setWidth(150);  
        
        canvas.setContextMenu(mainMenu);
 
        canvas.draw();  
        
        Event.addNativePreviewHandler(new NativePreviewHandler() {
			
			@Override
			public void onPreviewNativeEvent(NativePreviewEvent event) {
				Log.logServer(Log.LEVEL_INFO, ""+event.getTypeInt());
				
			}
		});
    }  
  

	public void onModuleLoad() {
		DOMImpl impl = GWT.create(DOMImpl.class);
		Log.logServer(Log.LEVEL_INFO, impl.getClass().getName());

		ExampleLayout exampleLayout = new ExampleLayout();
		exampleLayout.buildUi();
		initEventSystem();
	}

	protected native void initEventSystem() /*-{
		$wnd.addEventListener(
						"contextmenu",
						function(evt) {
							evt.preventDefault();
						}, false);
	}-*/;

	public static void test(JavaScriptObject event) {
		Log.logServer(Log.LEVEL_INFO, "menu clicked");
	}

}
