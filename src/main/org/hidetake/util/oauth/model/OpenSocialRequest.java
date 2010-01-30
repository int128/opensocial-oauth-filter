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

import javax.servlet.http.HttpServletRequest;

import net.oauth.OAuthMessage;

public class OpenSocialRequest
{

	public static final String OPENSOCIAL_APP_URL = "opensocial_app_url";
	public static final String OPENSOCIAL_APP_ID = "opensocial_app_id";
	
	private final OAuthMessage oauthMessage;
	private final String appId;
	private final String appUrl;

	public OpenSocialRequest(OAuthMessage message, HttpServletRequest request)
	{
		oauthMessage = message;
		appId = request.getParameter(OPENSOCIAL_APP_ID);
		appUrl = request.getParameter(OPENSOCIAL_APP_URL);
	}

	public final OAuthMessage getOAuthMessage()
	{
		return oauthMessage;
	}

	public final String getAppId()
	{
		return appId;
	}

	public final String getAppUrl()
	{
		return appUrl;
	}
	
}
