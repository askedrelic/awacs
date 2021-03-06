/**
* Copyright (c) 2010, Digg, Inc.
* All rights reserved.
* 
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
*
*    * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
*    * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
*    * Neither the name of the Digg, Inc. nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
* @author David Taylor <david@tinystatemachine.com>
*/

package controllers;

import java.util.*;

import controllers.CRUD;
import controllers.CRUD.ObjectType;

import lib.WidgetManager;
import lib.containers.*;
import models.*;


import play.*;
import play.db.jpa.JPASupport;
import play.mvc.*;
import widgets.InvalidWidgetException;

public class Displays extends CRUD {

    public static void display(String id, Boolean interactive) throws InvalidWidgetException {
    	String title = Setting.getSystemName();
   	
    	Dashboard d = null;
    	
    	
    	List<Display> displays = Display.find("byName", id).fetch();
    	
    	if(displays.size() > 0)
    		d = displays.get(0).dashboard;
    	else
    		try {
    			d = Dashboard.findById(Long.parseLong(id));
    		} catch (NumberFormatException e) {
    			notFound();
    		}
    	if(d == null)
    		notFound();
    	
    	WidgetManager wm = new WidgetManager();
    	
    	for(WidgetInstance w : d.allWidgets()) {
    		wm.add(w.provider);
    	}
    	
    	Set<String> cssIncludes = wm.getCSSIncludeList();
    	Set<String> jsIncludes = wm.getJSIncludeList();
    	
    	render(title, id, d, interactive, cssIncludes, jsIncludes);    	
    }
    
    public static void check(String id) {
    	
    	Dashboard d = null;
    	
    	List<Display> displays = Display.find("byName", id).fetch();
    	
    	if(displays.size() > 0)
    		d = displays.get(0).dashboard;
    	else
    		try {
    			d = Dashboard.findById(Long.parseLong(id));
    		} catch (NumberFormatException e) {
    			notFound();
    		}
    	
    	try { 
           	response.contentType = "text/plain";
        	renderJSON(d.status);
    	} catch (Exception e) {
      		renderJSON(new Dashboard.Status("-1", false));
    	}
    }
 
}
