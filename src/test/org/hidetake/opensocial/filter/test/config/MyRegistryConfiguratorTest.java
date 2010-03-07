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
package org.hidetake.opensocial.filter.test.config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.logging.Logger;

import net.oauth.OAuthAccessor;
import net.oauth.signature.RSA_SHA1;

import org.hidetake.opensocial.filter.config.ConfigurationException;
import org.hidetake.opensocial.filter.config.RegistryConfigurator;
import org.hidetake.opensocial.filter.extension.AllowLocalhost;
import org.hidetake.opensocial.filter.extension.ValidationLogger;
import org.hidetake.opensocial.filter.extensionpoint.AccessControl;
import org.hidetake.opensocial.filter.extensionpoint.ExtensionPoint;
import org.hidetake.opensocial.filter.extensionpoint.FilterInitializing;
import org.hidetake.opensocial.filter.extensionpoint.RequestURL;
import org.hidetake.opensocial.filter.extensionpoint.Validation;
import org.hidetake.opensocial.filter.model.AppRegistry;
import org.hidetake.opensocial.filter.model.ExtensionRegistry;
import org.hidetake.opensocial.filter.model.OpenSocialApp;
import org.junit.Test;


public class MyRegistryConfiguratorTest
{

	private static final String cert =
		"-----BEGIN CERTIFICATE-----\n"
		+ "MIICdzCCAeCgAwIBAgIJAOi/chE0MhufMA0GCSqGSIb3DQEBBQUAMDIxCzAJBgNV\n"
		+ "BAYTAkpQMREwDwYDVQQKEwhtaXhpIEluYzEQMA4GA1UEAxMHbWl4aS5qcDAeFw0w\n"
		+ "OTA0MjgwNzAyMTVaFw0xMDA0MjgwNzAyMTVaMDIxCzAJBgNVBAYTAkpQMREwDwYD\n"
		+ "VQQKEwhtaXhpIEluYzEQMA4GA1UEAxMHbWl4aS5qcDCBnzANBgkqhkiG9w0BAQEF\n"
		+ "AAOBjQAwgYkCgYEAwEj53VlQcv1WHvfWlTP+T1lXUg91W+bgJSuHAD89PdVf9Ujn\n"
		+ "i92EkbjqaLDzA43+U5ULlK/05jROnGwFBVdISxULgevSpiTfgbfCcKbRW7hXrTSm\n"
		+ "jFREp7YOvflT3rr7qqNvjm+3XE157zcU33SXMIGvX1uQH/Y4fNpEE1pmX+UCAwEA\n"
		+ "AaOBlDCBkTAdBgNVHQ4EFgQUn2ewbtnBTjv6CpeT37jrBNF/h6gwYgYDVR0jBFsw\n"
		+ "WYAUn2ewbtnBTjv6CpeT37jrBNF/h6ihNqQ0MDIxCzAJBgNVBAYTAkpQMREwDwYD\n"
		+ "VQQKEwhtaXhpIEluYzEQMA4GA1UEAxMHbWl4aS5qcIIJAOi/chE0MhufMAwGA1Ud\n"
		+ "EwQFMAMBAf8wDQYJKoZIhvcNAQEFBQADgYEAR7v8eaCaiB5xFVf9k9jOYPjCSQIJ\n"
		+ "58nLY869OeNXWWIQ17Tkprcf8ipxsoHj0Z7hJl/nVkSWgGj/bJLTVT9DrcEd6gLa\n"
		+ "H5TbGftATZCAJ8QJa3X2omCdB29qqyjz4F6QyTi930qekawPBLlWXuiP3oRNbiow\n"
		+ "nOLWEi16qH9WuBs=\n"
		+ "-----END CERTIFICATE-----";

	@Test
	public void testConfigure() throws Exception
	{
		final RegistryConfigurator configurator = new RegistryConfigurator()
		{
			public void configure(AppRegistry registry) throws ConfigurationException
			{
				registry.register(new OpenSocialApp(
						"test",
						"http://www.example.com/apps/test",
						OpenSocialApp.createOAuthAccessorRSASHA1("mixi.jp", cert)));
			}

			public void configure(ExtensionRegistry registry) throws ConfigurationException
			{
				registry.register(new ValidationLogger());
				registry.register(new AllowLocalhost());
			}
		};
		
		final AppRegistry appRegistry = new AppRegistry();
		final ExtensionRegistry extensionRegistry = new ExtensionRegistry();

		//
		final Logger logger = Logger.getLogger(XmlRegistryConfiguratorTest.class.getName());
		long baseTime;
		
		baseTime = System.currentTimeMillis();
		configurator.configure(appRegistry);
		logger.info("configureAppRegistry: " + (System.currentTimeMillis() - baseTime) + " ms");
		
		baseTime = System.currentTimeMillis();
		configurator.configure(extensionRegistry);
		logger.info("configureExtensionRegistry: " + (System.currentTimeMillis() - baseTime) + " ms");

		// verify
		{
			final List<OpenSocialApp> list = appRegistry.getApps();
			assertThat(list.size(), is(1));
			
			final OpenSocialApp openSocialApp = list.get(0);
			assertThat(openSocialApp.getAppId(), is("test"));
			assertThat(openSocialApp.getAppUrl(), is("http://www.example.com/apps/test"));
			
			final OAuthAccessor oauthAccessor = openSocialApp.getOAuthAccessor();
			assertThat(oauthAccessor.consumer.consumerKey, is("mixi.jp"));
			assertThat((String) oauthAccessor.consumer.getProperty(RSA_SHA1.X509_CERTIFICATE), is(cert));
		}
		
		{
			assertThat(size(extensionRegistry.getExtensions(AccessControl.class)), is(1));
			assertThat(size(extensionRegistry.getExtensions(FilterInitializing.class)), is(1));
			assertThat(size(extensionRegistry.getExtensions(RequestURL.class)), is(0));
			assertThat(size(extensionRegistry.getExtensions(Validation.class)), is(1));
		}
	}

	private static int size(Iterable<? extends ExtensionPoint> iterable)
	{
		List<? extends ExtensionPoint> list = (List<? extends ExtensionPoint>) iterable;
		return list.size();
	}

}
