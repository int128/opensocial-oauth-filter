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

import javax.xml.parsers.DocumentBuilderFactory;

import net.oauth.OAuthAccessor;
import net.oauth.signature.RSA_SHA1;

import org.hidetake.util.oauth.config.AppRegistry;
import org.hidetake.util.oauth.config.AppRegistryFactory;
import org.hidetake.util.oauth.config.ConfigurationException;
import org.hidetake.util.oauth.model.OpenSocialApp;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;


public class AppRegistryFactoryTest
{

	@Test
	public void testApp1() throws Exception
	{
		final InputStream stream = AppRegistryFactoryTest.class.getResourceAsStream("config1.xml");
		final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		final Document xml = df.newDocumentBuilder().parse(stream);

		final AppRegistryFactory factory = new AppRegistryFactory();
		final AppRegistry registry = factory.create(xml);
		
		final OpenSocialApp openSocialApp = registry.getList().get(0);
		Assert.assertEquals("test", openSocialApp.getAppId());
		Assert.assertEquals("http://www.example.com/apps/test", openSocialApp.getAppUrl());
		
		final OAuthAccessor oauthAccessor = openSocialApp.getOAuthAccessor();
		Assert.assertEquals("mixi.jp", oauthAccessor.consumer.consumerKey);
		System.out.println(oauthAccessor.consumer.getProperty(RSA_SHA1.X509_CERTIFICATE));
	}

	@Test(expected = ConfigurationException.class)
	public void testApp2() throws Exception
	{
		final InputStream stream = AppRegistryFactoryTest.class.getResourceAsStream("config2.xml");
		final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		final Document xml = df.newDocumentBuilder().parse(stream);

		final AppRegistryFactory factory = new AppRegistryFactory();
		
		factory.create(xml);
	}

	@Test(expected = ConfigurationException.class)
	public void testApp3() throws Exception
	{
		final InputStream stream = AppRegistryFactoryTest.class.getResourceAsStream("config3.xml");
		final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		final Document xml = df.newDocumentBuilder().parse(stream);

		final AppRegistryFactory factory = new AppRegistryFactory();
		
		factory.create(xml);
	}

}
