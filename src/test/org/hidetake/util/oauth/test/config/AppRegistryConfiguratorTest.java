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
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import net.oauth.OAuthAccessor;
import net.oauth.signature.RSA_SHA1;

import org.hidetake.util.oauth.config.AppRegistry;
import org.hidetake.util.oauth.config.RegistryConfigurator;
import org.hidetake.util.oauth.config.XmlRegistryConfigurator;
import org.hidetake.util.oauth.config.ConfigurationException;
import org.hidetake.util.oauth.model.OpenSocialApp;
import org.junit.Test;
import org.w3c.dom.Document;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


public class AppRegistryConfiguratorTest
{

	@Test
	public void testXML1() throws Exception
	{
		// do
		final InputStream stream = AppRegistryConfiguratorTest.class.getResourceAsStream("config1.xml");
		final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		final Document xml = df.newDocumentBuilder().parse(stream);

		final AppRegistry registry = new AppRegistry();
		final RegistryConfigurator configurator = new XmlRegistryConfigurator(xml);
		configurator.configure(registry);
		
		// verify
		final List<OpenSocialApp> list = registry.getList();
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
	public void testXML2() throws Exception
	{
		final InputStream stream = AppRegistryConfiguratorTest.class.getResourceAsStream("config2.xml");
		final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		final Document xml = df.newDocumentBuilder().parse(stream);

		final AppRegistry registry = new AppRegistry();
		final RegistryConfigurator configurator = new XmlRegistryConfigurator(xml);
		configurator.configure(registry);
	}

	@Test(expected = ConfigurationException.class)
	public void testXML3() throws Exception
	{
		final InputStream stream = AppRegistryConfiguratorTest.class.getResourceAsStream("config3.xml");
		final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		final Document xml = df.newDocumentBuilder().parse(stream);

		final AppRegistry registry = new AppRegistry();
		final RegistryConfigurator configurator = new XmlRegistryConfigurator(xml);
		configurator.configure(registry);
	}

}
