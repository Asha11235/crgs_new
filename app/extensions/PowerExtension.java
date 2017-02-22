/*
 * Copyright (C) 2012 mPower Social
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package extensions;

import java.util.Date;


import org.codehaus.groovy.runtime.NullObject;

import play.templates.JavaExtensions;

public class PowerExtension extends JavaExtensions {
	
	
	/**
	 * If the passed template Object is equal null 
	 * then render the passed val 
	 * or render the original value
	 *
	 * @param var the variable
	 * @param val the value
	 * @return the string
	 */
	// TODO - Fix this broken function
	public static String defaultVal(Object var, String val) {
		
		if(var instanceof NullObject) {
			return val;
		} else {
			return var.toString();
		}
	}
	
}
