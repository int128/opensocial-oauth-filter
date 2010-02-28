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
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.oauth.OAuthAccessor;
import net.oauth.signature.RSA_SHA1;

import org.hidetake.util.oauth.config.RegistryConfigurator;
import org.hidetake.util.oauth.config.XmlRegistryConfigurator;
import org.hidetake.util.oauth.config.ConfigurationException;
import org.hidetake.util.oauth.extension.AllowLocalhost;
import org.hidetake.util.oauth.extension.ValidationLogger;
import org.hidetake.util.oauth.extensionpoint.AccessControl;
import org.hidetake.util.oauth.extensionpoint.ExtensionPoint;
import org.hidetake.util.oauth.extensionpoint.FilterInitializing;
import org.hidetake.util.oauth.extensionpoint.RequestURL;
import org.hidetake.util.oauth.extensionpoint.Validation;
import org.hidetake.util.oauth.model.AppRegistry;
import org.hidetake.util.oauth.model.ExtensionRegistry;
import org.hidetake.util.oauth.model.OpenSocialApp;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;


public class XmlRegistryConfiguratorTest
{

	@Test
	public void testApp1() throws Exception
	{
		// do
		final Document xml = setupXML("config1.xml");
		final AppRegistry registry = new AppRegistry();
		final RegistryConfigurator configurator = new XmlRegistryConfigurator(xml);
		configurator.configure(registry);
		
		// verify
		final List<OpenSocialApp> list = registry.getApps();
		assertThat(list.size(), is(1));
		
		final OpenSocialApp openSocialApp = list.get(0);
		assertThat(openSocialApp.getAppId(), is("test"));
		assertThat(openSocialApp.getAppUrl(), is("http://www.example.com/apps/test"));
		
		final OAuthAccessor oauthAccessor = openSocialApp.getOAuthAccessor();
		assertThat(oauthAccessor.consumer.consumerKey, is("mixi.jp"));
		assertThat((String) oauthAccessor.consumer.getProperty(RSA_SHA1.X509_CERTIFICATE),
			is("-----BEGIN CERTIFICATE-----\n" +
				"MIICdzCCAeCgAwIBAgIJAOi/chE0MhufMA0GCSqGSIb3DQEBBQUAMDIxCzAJBgNV\n" +
				"BAYTAkpQMREwDwYDVQQKEwhtaXhpIEluYzEQMA4GA1UEAxMHbWl4aS5qcDAeFw0w\n" +
				"OTA0MjgwNzAyMTVaFw0xMDA0MjgwNzAyMTVaMDIxCzAJBgNVBAYTAkpQMREwDwYD\n" +
				"VQQKEwhtaXhpIEluYzEQMA4GA1UEAxMHbWl4aS5qcDCBnzANBgkqhkiG9w0BAQEF\n" +
				"AAOBjQAwgYkCgYEAwEj53VlQcv1WHvfWlTP+T1lXUg91W+bgJSuHAD89PdVf9Ujn\n" +
				"i92EkbjqaLDzA43+U5ULlK/05jROnGwFBVdISxULgevSpiTfgbfCcKbRW7hXrTSm\n" +
				"jFREp7YOvflT3rr7qqNvjm+3XE157zcU33SXMIGvX1uQH/Y4fNpEE1pmX+UCAwEA\n" +
				"AaOBlDCBkTAdBgNVHQ4EFgQUn2ewbtnBTjv6CpeT37jrBNF/h6gwYgYDVR0jBFsw\n" +
				"WYAUn2ewbtnBTjv6CpeT37jrBNF/h6ihNqQ0MDIxCzAJBgNVBAYTAkpQMREwDwYD\n" +
				"VQQKEwhtaXhpIEluYzEQMA4GA1UEAxMHbWl4aS5qcIIJAOi/chE0MhufMAwGA1Ud\n" +
				"EwQFMAMBAf8wDQYJKoZIhvcNAQEFBQADgYEAR7v8eaCaiB5xFVf9k9jOYPjCSQIJ\n" +
				"58nLY869OeNXWWIQ17Tkprcf8ipxsoHj0Z7hJl/nVkSWgGj/bJLTVT9DrcEd6gLa\n" +
				"h5TbGftATZCAJ8QJa3X2omCdB29qqyjz4F6QyTi930qekawPBLlWXuiP3oRNbiow\n" +
				"nOLWEi16qH9WuBs=\n" +
				"-----END CERTIFICATE-----"));
	}

	@Test(expected = ConfigurationException.class)
	public void testApp2() throws Exception
	{
		final Document xml = setupXML("config2.xml");
		final AppRegistry registry = new AppRegistry();
		final RegistryConfigurator configurator = new XmlRegistryConfigurator(xml);
		configurator.configure(registry);
	}

	@Test(expected = ConfigurationException.class)
	public void testApp3() throws Exception
	{
		final Document xml = setupXML("config3.xml");
		final AppRegistry registry = new AppRegistry();
		final RegistryConfigurator configurator = new XmlRegistryConfigurator(xml);
		configurator.configure(registry);
	}

	@Test
	public void testExtensions1() throws Exception
	{
		final ExtensionRegistry extensionRegistry = setupExtensionRegistry("config1.xml");
		
		assertThat(size(extensionRegistry.getExtensions(AccessControl.class)), is(1));
		assertThat(size(extensionRegistry.getExtensions(FilterInitializing.class)), is(1));
		assertThat(size(extensionRegistry.getExtensions(RequestURL.class)), is(0));
		assertThat(size(extensionRegistry.getExtensions(Validation.class)), is(1));
	}

	@Test
	public void testExtensions2() throws Exception
	{
		final ExtensionRegistry extensionRegistry = setupExtensionRegistry("config2.xml");
		
		assertThat(size(extensionRegistry.getExtensions(AccessControl.class)), is(1));
		assertThat(size(extensionRegistry.getExtensions(FilterInitializing.class)), is(0));
		assertThat(size(extensionRegistry.getExtensions(RequestURL.class)), is(0));
		assertThat(size(extensionRegistry.getExtensions(Validation.class)), is(0));
	}

	@Test
	public void testExtensions3() throws Exception
	{
		final ExtensionRegistry extensionRegistry = setupExtensionRegistry("config3.xml");
		
		assertThat(size(extensionRegistry.getExtensions(AccessControl.class)), is(0));
		assertThat(size(extensionRegistry.getExtensions(FilterInitializing.class)), is(0));
		assertThat(size(extensionRegistry.getExtensions(RequestURL.class)), is(0));
		assertThat(size(extensionRegistry.getExtensions(Validation.class)), is(0));
	}

	@Test
	public void testExtensionsAll1() throws Exception
	{
		final ExtensionRegistry extensionRegistry = setupExtensionRegistry("config1.xml");
		
		final List<ExtensionPoint> allExtensions = extensionRegistry.getExtensions();
		assertThat(allExtensions.size(), is(2));
		
		// check items
		final Set<String> actual = new HashSet<String>();
		for(ExtensionPoint extensionPoint : allExtensions) {
			actual.add(extensionPoint.getClass().getName());
		}
		
		assertThat(actual.size(), is(2));
		assertThat(actual, hasItems(AllowLocalhost.class.getName(), ValidationLogger.class.getName()));
	}

	@Test
	public void testExtensionsAll2() throws Exception
	{
		final ExtensionRegistry extensionRegistry = setupExtensionRegistry("config2.xml");
		
		final List<ExtensionPoint> allExtensions = extensionRegistry.getExtensions();
		assertThat(allExtensions.size(), is(1));
		
		final ExtensionPoint actual = allExtensions.iterator().next();
		assertThat(actual.getClass().getName(), is(AllowLocalhost.class.getName()));
	}

	@Test
	public void testExtensionsAll3() throws Exception
	{
		final ExtensionRegistry extensionRegistry = setupExtensionRegistry("config3.xml");
		
		final List<ExtensionPoint> allExtensions = extensionRegistry.getExtensions();
		assertThat(allExtensions.isEmpty(), is(true));
	}

	private static Document setupXML(String path)
	throws SAXException, IOException, ParserConfigurationException
	{
		final InputStream stream = XmlRegistryConfiguratorTest.class.getResourceAsStream(path);
		final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		return df.newDocumentBuilder().parse(stream);
	}

	private static ExtensionRegistry setupExtensionRegistry(String path)
	throws SAXException, IOException, ParserConfigurationException, ConfigurationException
	{
		final Document xml = setupXML(path);
		final ExtensionRegistry extensionRegistry = new ExtensionRegistry();
		final RegistryConfigurator configurator = new XmlRegistryConfigurator(xml);
		configurator.configure(extensionRegistry);
		
		return extensionRegistry;
	}

	private static int size(Iterable<? extends ExtensionPoint> iterable)
	{
		List<? extends ExtensionPoint> list = (List<? extends ExtensionPoint>) iterable;
		return list.size();
	}

}
