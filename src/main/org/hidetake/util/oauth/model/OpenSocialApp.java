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
package org.hidetake.util.oauth.model;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.signature.RSA_SHA1;

/**
 * Represents an OpenSocial application.
 * 
 * @author hidetake.org
 *
 */
public class OpenSocialApp
{

	private final String appId;
	private final String appUrl;
	private final OAuthAccessor oauthAccessor;

	/**
	 * Create an accessor object which uses RSA-SHA1 signature.
	 * @param consumerKey
	 * @param certificate
	 * @return
	 */
	public static OAuthAccessor createOAuthAccessorRSASHA1(String consumerKey, String certificate)
	{
		OAuthConsumer consumer = new OAuthConsumer(null, consumerKey, null, null);
		consumer.setProperty(RSA_SHA1.X509_CERTIFICATE, certificate);
		OAuthAccessor oauthAccessor = new OAuthAccessor(consumer);
		return oauthAccessor;
	}
	
	/**
	 * Constructor.
	 * @param appId application ID
	 * @param appUrl application XML URL
	 * @param oauthAccessor OpenSocial container
	 */
	public OpenSocialApp(String appId, String appUrl, OAuthAccessor oauthAccessor)
	{
		this.appId = appId;
		this.appUrl = appUrl;
		this.oauthAccessor = oauthAccessor;
	}

	/**
	 * Returns the application ID.
	 * @return
	 */
	public final String getAppId()
	{
		return appId;
	}
	
	/**
	 * Returns the application XML URL.
	 * @return
	 */
	public final String getAppUrl()
	{
		return appUrl;
	}

	/**
	 * Returns the accessor object which represents OpenSocial container.
	 * @return
	 */
	public OAuthAccessor getOAuthAccessor()
	{
		return oauthAccessor;
	}

}
