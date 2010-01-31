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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.oauth.OAuth;
import net.oauth.OAuthMessage;

import org.hidetake.util.oauth.config.ExtensionRegistryManager;
import org.hidetake.util.oauth.extensionpoint.RequestURL;

public class OpenSocialRequest
{

	public static final String OPENSOCIAL_APP_URL = "opensocial_app_url";
	public static final String OPENSOCIAL_APP_ID = "opensocial_app_id";
	public static final String OPENSOCIAL_VIEWER_ID = "opensocial_viewer_id";
	public static final String OPENSOCIAL_OWNER_ID = "opensocial_owner_id";

	private final OAuthMessage oauthMessage;
	private final String appId;
	private final String appUrl;
	private final String viewerId;
	private final String ownerId;
	
	protected OpenSocialRequest(OAuthMessage oauthMessage, String appId, String appUrl, String viewerId, String ownerId)
	{
		this.oauthMessage = oauthMessage;
		this.appId = appId;
		this.appUrl = appUrl;
		this.viewerId = viewerId;
		this.ownerId = ownerId;
	}

	public static OpenSocialRequest create(HttpServletRequest request)
	throws OpenSocialException
	{
		StringBuilder url = parseRequestUrl(request);
		
		for(RequestURL extension : ExtensionRegistryManager.get().getExtensions(RequestURL.class)) {
			extension.postprocess(url, request);
		}
		
		OAuthMessage message =
			new OAuthMessage(request.getMethod(), url.toString(), parseRequestParameters(request));
		
		return create(message, request);
	}

	public static OpenSocialRequest create(OAuthMessage message, HttpServletRequest request)
	throws OpenSocialException
	{
		String appId = request.getParameter(OPENSOCIAL_APP_ID);
		String appUrl = request.getParameter(OPENSOCIAL_APP_URL);
		String viewerId = request.getParameter(OPENSOCIAL_VIEWER_ID);
		String ownerId = request.getParameter(OPENSOCIAL_OWNER_ID);
		
		// check null
		if(appId == null) {
			throw new OpenSocialException(
				"Parameter " + OpenSocialRequest.OPENSOCIAL_APP_ID + " not exist");
		}
		if(appUrl == null) {
			throw new OpenSocialException(
				"Parameter " + OpenSocialRequest.OPENSOCIAL_APP_URL + " not exist");
		}
		if(viewerId == null) {
			throw new OpenSocialException(
				"Parameter " + OpenSocialRequest.OPENSOCIAL_VIEWER_ID + " not exist");
		}
		if(ownerId == null) {
			throw new OpenSocialException(
				"Parameter " + OpenSocialRequest.OPENSOCIAL_OWNER_ID + " not exist");
		}
		
		return new OpenSocialRequest(message, appId, appUrl, viewerId, ownerId);
	}

	/**
	* Constructs and returns a List of OAuth.Parameter objects, one per
	* parameter in the passed request.
	* 
	* @param  request Servlet request object with methods for retrieving the
	*         full set of parameters passed with the request
	* @see    http://wiki.opensocial.org/index.php?title=Validating_Signed_Requests
	*/
	public static final List<OAuth.Parameter> parseRequestParameters(HttpServletRequest request)
	{
		List<OAuth.Parameter> parameters = new ArrayList<OAuth.Parameter>();
	
		for (Object e : request.getParameterMap().entrySet()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) e;
	
			for (String value : entry.getValue()) {
				parameters.add(new OAuth.Parameter(entry.getKey(), value));
			}
		}
	
		return parameters;
	}

	/**
	 * Constructs and returns the full URL associated with the passed request
	 * object.
	 * 
	 * @param  request Servlet request object with methods for retrieving the
	 *         various components of the request URL
	 * @see    http://wiki.opensocial.org/index.php?title=Validating_Signed_Requests
	 */
	public static final StringBuilder parseRequestUrl(HttpServletRequest request)
	{
		StringBuilder url = new StringBuilder();
	
		String scheme = request.getScheme();
		url.append(scheme);
		url.append("://");
		url.append(request.getServerName());
		
		int port = request.getLocalPort();
		if (port == 0) {
			//nothing
		}
		else if ((scheme.equals("http") && port != 80)||(scheme.equals("https") && port != 443)) {
			url.append(":");
			url.append(port);
		}
		url.append(request.getRequestURI());
		
		return url;
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
	
	public final String getViewerId()
	{
		return viewerId;
	}
	
	public final String getOwnerId()
	{
		return ownerId;
	}

}
