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
package org.hidetake.util.oauth.test.config;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.hidetake.util.oauth.config.ExtensionRegistry;
import org.hidetake.util.oauth.config.ExtensionRegistryFactory;
import org.hidetake.util.oauth.extension.AllowLocalhost;
import org.hidetake.util.oauth.extension.ValidationLogger;
import org.hidetake.util.oauth.extensionpoint.ExtensionPoint;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;


public class ExtensionRegistryFactoryTest
{
		
	@Test
	public void test1() throws Exception
	{
		final InputStream stream = ExtensionRegistryFactoryTest.class.getResourceAsStream("config1.xml");
		final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		final Document xml = df.newDocumentBuilder().parse(stream);
		
		ExtensionRegistryFactory factory = new ExtensionRegistryFactory();
		ExtensionRegistry extensionRegistry = factory.create(xml);
		
		final Set<String> expects = new HashSet<String>();
		expects.add(AllowLocalhost.class.getName());
		expects.add(ValidationLogger.class.getName());
		
		final Set<String> actuals = new HashSet<String>();
		for(ExtensionPoint extensionPoint : extensionRegistry.getAllExtensions()) {
			actuals.add(extensionPoint.getClass().getName());
		}
		
		Assert.assertEquals(expects, actuals);
	}

}
