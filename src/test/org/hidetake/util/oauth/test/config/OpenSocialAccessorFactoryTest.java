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
import java.util.List;
import java.util.Set;

import net.oauth.OAuthAccessor;
import net.oauth.signature.RSA_SHA1;

import org.hidetake.util.oauth.ValidationEventListener;
import org.hidetake.util.oauth.config.OpenSocialAccessorFactory;
import org.hidetake.util.oauth.model.OpenSocialAccessor;
import org.junit.Assert;
import org.junit.Test;


public class OpenSocialAccessorFactoryTest
{

	private static final String config1 = "config1.xml";
	
	@Test
	public void testListeners1() throws Exception
	{
		final InputStream stream = OpenSocialAccessorFactoryTest.class.getResourceAsStream(config1);
		final OpenSocialAccessorFactory config = new OpenSocialAccessorFactory(stream);
		
		final Set<String> expects = new HashSet<String>();
		expects.add("org.hidetake.util.oauth.listener.ValidationEventLogger");
		expects.add("org.hidetake.util.oauth.listener.AllowLocalhost");
		
		final Set<String> actuals = new HashSet<String>();
		final List<ValidationEventListener> validationEventListeners = config.getValidationEventListeners();
		for(ValidationEventListener listener : validationEventListeners) {
			actuals.add(listener.getClass().getName());
		}
		
		Assert.assertEquals(expects, actuals);
	}

	@Test
	public void testApp1() throws Exception
	{
		final InputStream stream = OpenSocialAccessorFactoryTest.class.getResourceAsStream(config1);
		final OpenSocialAccessorFactory config = new OpenSocialAccessorFactory(stream);
		
		final OpenSocialAccessor openSocialAccessor = config.getOpenSocialAccessor();
		Assert.assertEquals("test", openSocialAccessor.getAppId());
		Assert.assertEquals("http://www.example.com/apps/test", openSocialAccessor.getAppUrl());
		
		final OAuthAccessor oauthAccessor = openSocialAccessor.getOAuthAccessor();
		Assert.assertEquals("mixi.jp", oauthAccessor.consumer.consumerKey);
		System.out.println(oauthAccessor.consumer.getProperty(RSA_SHA1.X509_CERTIFICATE));
	}

}
