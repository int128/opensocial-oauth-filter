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

import org.hidetake.util.oauth.extensionpoint.ExtensionPoint;
import org.w3c.dom.Document;

public class ExtensionRegistryFactory
{

	public ExtensionRegistry create()
	{
		return new ExtensionRegistry();
	}
	
	public ExtensionRegistry create(Document xml) throws ConfigurationException
	{
		ExtensionRegistry registry = new ExtensionRegistry();
		
		try {
			XPathEvaluator rootEvaluator = new XPathEvaluator(xml);
			
			for(String id : rootEvaluator.getNodeValueList("/config/extensions/extension/@id")) {
				ExtensionPoint extension = (ExtensionPoint) Class.forName(id).newInstance();
				registry.register(extension);
			}
			
			return registry;
		}
		catch (NoSuchNodeException e) {
			// returns empty list
			return registry;
		}
		catch (InstantiationException e) {
			throw new ConfigurationException(e);
		}
		catch (IllegalAccessException e) {
			throw new ConfigurationException(e);
		}
		catch (ClassNotFoundException e) {
			throw new ConfigurationException(e);
		}
	}

}
