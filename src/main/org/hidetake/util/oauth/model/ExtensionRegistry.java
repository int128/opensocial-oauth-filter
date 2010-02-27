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

	private final Map<Class<? extends ExtensionPoint>, List<ExtensionPoint>> map =
		new HashMap<Class<? extends ExtensionPoint>, List<ExtensionPoint>>();

	private final List<ExtensionPoint> allExtensions = new ArrayList<ExtensionPoint>();

	// Constant value for getExtensions()
	private static final List<ExtensionPoint> emptyList =
		Collections.unmodifiableList(new ArrayList<ExtensionPoint>());
	
	/**
	 * Reset state of this registry.
	 */
	public void reset()
	{
		map.clear();
		allExtensions.clear();
	}

	/**
	 * Register an extension.
	 * @param extension
	 */
	public void register(ExtensionPoint extension)
	{
		allExtensions.add(extension);
		
		// TODO: consider interface hierarchy
		for(Class<? extends ExtensionPoint> c : extension.getClass().getInterfaces()) {
			// find subclass of ExtensionPoint
			if(ExtensionPoint.class.isAssignableFrom(c)) {
				List<ExtensionPoint> list = map.get(c);
				
				if(list == null) {
					// first time to register this kind of extension
					list = new ArrayList<ExtensionPoint>();
					map.put(c, list);
				}
				
				list.add(extension);
			}
		}
	}

	/**
	 * Returns list of specified type extensions.
	 * @param <I> extension point interface
	 * @param c
	 * @return read-only list
	 */
	@SuppressWarnings("unchecked")
	public <I extends ExtensionPoint> Iterable<I> getExtensions(Class<I> c)
	{
		List<I> list = (List<I>) map.get(c);
		if(list == null) {
			return (List<I>) emptyList;
		}
		return list;
	}

	/**
	 * Returns set of all extensions.
	 * @return read-only set object
	 */
	public List<ExtensionPoint> getAllExtensions()
	{
		return Collections.unmodifiableList(allExtensions);
	}

}
