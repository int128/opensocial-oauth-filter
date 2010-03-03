/*
 * Copyright (C) 2009-2010 hidetake.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hidetake.util.oauth.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hidetake.util.oauth.extensionpoint.ExtensionPoint;

/**
 * Registry class for extensions.
 * 
 * @author hidetake.org
 *
 */
public class ExtensionRegistry
{

	private final Map<Class<?>, List<ExtensionPoint>> extensionPointMap =
		new HashMap<Class<?>, List<ExtensionPoint>>();

	private final List<ExtensionPoint> extensions = new ArrayList<ExtensionPoint>();

	// Constant value for getExtensions()
	private static final List<ExtensionPoint> emptyList =
		Collections.unmodifiableList(new ArrayList<ExtensionPoint>());
	
	/**
	 * Reset state of this registry.
	 */
	public void reset()
	{
		extensionPointMap.clear();
		extensions.clear();
	}

	/**
	 * Register an extension.
	 * Note that this method only check directly implemented interfaces.
	 * The interface hierarchy will be ignored.
	 * @param extension
	 */
	public void register(ExtensionPoint extension)
	{
		extensions.add(extension);
		
//		// fast implementation
//		if(extension instanceof AccessControl) {
//			register(AccessControl.class, extension);
//		}
//		if(extension instanceof FilterInitializing) {
//			register(FilterInitializing.class, extension);
//		}
//		if(extension instanceof RequestURL) {
//			register(RequestURL.class, extension);
//		}
//		if(extension instanceof Validation) {
//			register(Validation.class, extension);
//		}
		
		// find subclass of ExtensionPoint
		for(Class<?> kind : extension.getClass().getInterfaces()) {
			if(ExtensionPoint.class.isAssignableFrom(kind)) {
				register(kind, extension);
			}
		}
	}

	/**
	 * Register an extension.
	 * @param kind class of extension point interface
	 * @param extension extension object
	 */
	public void register(Class<?> kind, ExtensionPoint extension)
	{
		List<ExtensionPoint> list = extensionPointMap.get(kind);
		if(list == null) {
			// first time to register this kind of extension
			list = new ArrayList<ExtensionPoint>();
			extensionPointMap.put(kind, list);
		}
		list.add(extension);
	}
	
	/**
	 * Returns list of specified type extensions.
	 * @param <I> type of extension point interface
	 * @param kind
	 * @return read-only list
	 */
	@SuppressWarnings("unchecked")
	public <I extends ExtensionPoint> Iterable<I> getExtensions(Class<I> kind)
	{
		List<I> list = (List<I>) extensionPointMap.get(kind);
		if(list == null) {
			return (List<I>) emptyList;
		}
		return list;
	}

	/**
	 * Returns list of all extensions.
	 * @return read-only set object
	 */
	public List<ExtensionPoint> getExtensions()
	{
		return Collections.unmodifiableList(extensions);
	}

}
