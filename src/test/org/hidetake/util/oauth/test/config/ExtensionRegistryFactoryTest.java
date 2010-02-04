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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hidetake.util.oauth.config.ConfigurationException;
import org.hidetake.util.oauth.config.ExtensionRegistry;
import org.hidetake.util.oauth.config.ExtensionRegistryFactory;
import org.hidetake.util.oauth.extension.AllowLocalhost;
import org.hidetake.util.oauth.extension.ValidationLogger;
import org.hidetake.util.oauth.extensionpoint.AccessControl;
import org.hidetake.util.oauth.extensionpoint.ExtensionPoint;
import org.hidetake.util.oauth.extensionpoint.FilterInitializing;
import org.hidetake.util.oauth.extensionpoint.RequestURL;
import org.hidetake.util.oauth.extensionpoint.Validation;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class ExtensionRegistryFactoryTest
{

	@Test
	public void testGetAllExtensions1() throws Exception
	{
		ExtensionRegistry extensionRegistry = setupExtensionRegistry("config1.xml");
		Set<ExtensionPoint> allExtensions = extensionRegistry.getAllExtensions();
		
		Assert.assertEquals(2, allExtensions.size());
		
		final Set<String> expected = new HashSet<String>();
		expected.add(AllowLocalhost.class.getName());
		expected.add(ValidationLogger.class.getName());
		
		final Set<String> actual = new HashSet<String>();
		for(ExtensionPoint extensionPoint : allExtensions) {
			actual.add(extensionPoint.getClass().getName());
		}
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetAllExtensions2() throws Exception
	{
		ExtensionRegistry extensionRegistry = setupExtensionRegistry("config2.xml");
		Set<ExtensionPoint> allExtensions = extensionRegistry.getAllExtensions();
		
		Assert.assertEquals(1, allExtensions.size());
		
		Assert.assertEquals(
			AllowLocalhost.class.getName(),
			allExtensions.toArray()[0].getClass().getName());
	}
	
	@Test
	public void testGetAllExtensions3() throws Exception
	{
		ExtensionRegistry extensionRegistry = setupExtensionRegistry("config3.xml");
		Set<ExtensionPoint> allExtensions = extensionRegistry.getAllExtensions();
		
		Assert.assertTrue(allExtensions.isEmpty());
	}
	
	@Test
	public void testGetExtensions1() throws Exception
	{
		ExtensionRegistry extensionRegistry = setupExtensionRegistry("config1.xml");
		
		Assert.assertTrue(extensionRegistry.getExtensions(AccessControl.class).iterator().hasNext());
		Assert.assertTrue(extensionRegistry.getExtensions(FilterInitializing.class).iterator().hasNext());
		Assert.assertFalse(extensionRegistry.getExtensions(RequestURL.class).iterator().hasNext());
		Assert.assertTrue(extensionRegistry.getExtensions(Validation.class).iterator().hasNext());
	}
	
	@Test
	public void testGetExtensions2() throws Exception
	{
		ExtensionRegistry extensionRegistry = setupExtensionRegistry("config2.xml");
		
		Assert.assertTrue(extensionRegistry.getExtensions(AccessControl.class).iterator().hasNext());
		Assert.assertFalse(extensionRegistry.getExtensions(FilterInitializing.class).iterator().hasNext());
		Assert.assertFalse(extensionRegistry.getExtensions(RequestURL.class).iterator().hasNext());
		Assert.assertFalse(extensionRegistry.getExtensions(Validation.class).iterator().hasNext());
	}
	
	@Test
	public void testGetExtensions3() throws Exception
	{
		ExtensionRegistry extensionRegistry = setupExtensionRegistry("config3.xml");
		
		Assert.assertFalse(extensionRegistry.getExtensions(AccessControl.class).iterator().hasNext());
		Assert.assertFalse(extensionRegistry.getExtensions(FilterInitializing.class).iterator().hasNext());
		Assert.assertFalse(extensionRegistry.getExtensions(RequestURL.class).iterator().hasNext());
		Assert.assertFalse(extensionRegistry.getExtensions(Validation.class).iterator().hasNext());
	}

	private ExtensionRegistry setupExtensionRegistry(String path)
	throws SAXException, IOException, ParserConfigurationException, ConfigurationException
	{
		final InputStream stream = ExtensionRegistryFactoryTest.class.getResourceAsStream(path);
		final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		final Document xml = df.newDocumentBuilder().parse(stream);
		
		ExtensionRegistryFactory factory = new ExtensionRegistryFactory();
		ExtensionRegistry extensionRegistry = factory.create(xml);
		return extensionRegistry;
	}

}
