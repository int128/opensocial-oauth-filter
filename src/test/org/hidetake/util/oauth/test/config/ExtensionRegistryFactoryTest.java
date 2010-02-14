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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class ExtensionRegistryFactoryTest
{

	@Test
	public void testGetAllExtensions1() throws Exception
	{
		final ExtensionRegistry extensionRegistry = setupExtensionRegistry("config1.xml");
		
		final Set<ExtensionPoint> allExtensions = extensionRegistry.getAllExtensions();
		assertThat(allExtensions.size(), is(2));
		
		// check items
		final Set<String> expected = new HashSet<String>();
		expected.add(AllowLocalhost.class.getName());
		expected.add(ValidationLogger.class.getName());
		
		final Set<String> actual = new HashSet<String>();
		for(ExtensionPoint extensionPoint : allExtensions) {
			actual.add(extensionPoint.getClass().getName());
		}
		
		assertThat(actual.size(), is(2));
		assertThat(expected.size(), is(2));
		assertThat(actual, is(expected));
	}
	
	@Test
	public void testGetAllExtensions2() throws Exception
	{
		final ExtensionRegistry extensionRegistry = setupExtensionRegistry("config2.xml");
		
		final Set<ExtensionPoint> allExtensions = extensionRegistry.getAllExtensions();
		assertThat(allExtensions.size(), is(1));
		
		final ExtensionPoint actual = allExtensions.iterator().next();
		assertThat(actual.getClass().getName(), is(AllowLocalhost.class.getName()));
	}
	
	@Test
	public void testGetAllExtensions3() throws Exception
	{
		final ExtensionRegistry extensionRegistry = setupExtensionRegistry("config3.xml");
		
		final Set<ExtensionPoint> allExtensions = extensionRegistry.getAllExtensions();
		assertThat(allExtensions.isEmpty(), is(true));
	}
	
	@Test
	public void testGetExtensions1() throws Exception
	{
		final ExtensionRegistry extensionRegistry = setupExtensionRegistry("config1.xml");
		
		assertThat(extensionRegistry.getExtensions(AccessControl.class).iterator().hasNext(), is(true));
		assertThat(extensionRegistry.getExtensions(FilterInitializing.class).iterator().hasNext(), is(true));
		assertThat(extensionRegistry.getExtensions(RequestURL.class).iterator().hasNext(), is(false));
		assertThat(extensionRegistry.getExtensions(Validation.class).iterator().hasNext(), is(true));
	}
	
	@Test
	public void testGetExtensions2() throws Exception
	{
		final ExtensionRegistry extensionRegistry = setupExtensionRegistry("config2.xml");
		
		assertThat(extensionRegistry.getExtensions(AccessControl.class).iterator().hasNext(), is(true));
		assertThat(extensionRegistry.getExtensions(FilterInitializing.class).iterator().hasNext(), is(false));
		assertThat(extensionRegistry.getExtensions(RequestURL.class).iterator().hasNext(), is(false));
		assertThat(extensionRegistry.getExtensions(Validation.class).iterator().hasNext(), is(false));
	}
	
	@Test
	public void testGetExtensions3() throws Exception
	{
		final ExtensionRegistry extensionRegistry = setupExtensionRegistry("config3.xml");
		
		assertThat(extensionRegistry.getExtensions(AccessControl.class).iterator().hasNext(), is(false));
		assertThat(extensionRegistry.getExtensions(FilterInitializing.class).iterator().hasNext(), is(false));
		assertThat(extensionRegistry.getExtensions(RequestURL.class).iterator().hasNext(), is(false));
		assertThat(extensionRegistry.getExtensions(Validation.class).iterator().hasNext(), is(false));
	}

	private ExtensionRegistry setupExtensionRegistry(String path)
	throws SAXException, IOException, ParserConfigurationException, ConfigurationException
	{
		final InputStream stream = ExtensionRegistryFactoryTest.class.getResourceAsStream(path);
		final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		final Document xml = df.newDocumentBuilder().parse(stream);
		
		final ExtensionRegistryFactory factory = new ExtensionRegistryFactory();
		final ExtensionRegistry extensionRegistry = factory.create(xml);
		assertThat(extensionRegistry, is(notNullValue()));
		
		return extensionRegistry;
	}

}
