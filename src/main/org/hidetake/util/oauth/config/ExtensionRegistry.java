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
package org.hidetake.util.oauth.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hidetake.util.oauth.extensionpoint.ExtensionPoint;

public class ExtensionRegistry
{

	private final Map<Class<? extends ExtensionPoint>, List<ExtensionPoint>> map =
		new HashMap<Class<? extends ExtensionPoint>, List<ExtensionPoint>>();

	private static final Iterable<ExtensionPoint> emptyList = new ArrayList<ExtensionPoint>();

	protected ExtensionRegistry()
	{
	}
	
	public void reset()
	{
		map.clear();
	}

	public <I extends ExtensionPoint> void register(I extension)
	{
		for(Class<I> c : extension.getClass().getInterfaces()) {
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

	@SuppressWarnings("unchecked")
	public <I extends ExtensionPoint> Iterable<I> getExtensions(Class<I> c)
	{
		List<I> list = (List<I>) map.get(c);
		if(list == null) {
			return (Iterable<I>) emptyList;
		}
		return list;
	}

	public Set<ExtensionPoint> getAllExtensions()
	{
		Set<ExtensionPoint> result = new HashSet<ExtensionPoint>();
		for(List<? extends ExtensionPoint> list : map.values()) {
			result.addAll(list);
		}
		return result;
	}

}
