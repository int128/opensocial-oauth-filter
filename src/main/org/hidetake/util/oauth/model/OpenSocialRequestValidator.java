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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import net.oauth.OAuth;
import net.oauth.OAuthException;
import net.oauth.OAuthValidator;
import net.oauth.SimpleOAuthValidator;

public class OpenSocialRequestValidator
{

	private final OpenSocialAccessor openSocialAccessor;
	private final OAuthValidator oauthValidator = new SimpleOAuthValidator();

	public OpenSocialRequestValidator(OpenSocialAccessor anOpenSocialAccessor)
	{
		openSocialAccessor = anOpenSocialAccessor;
	}

	public final OpenSocialAccessor getOpenSocialAccessor()
	{
		return openSocialAccessor;
	}
	
	public void validate(OpenSocialRequest openSocialRequest)
	throws OAuthException, OpenSocialException, IOException
	{
		// validate signature
		try {
			oauthValidator.validateMessage(
				openSocialRequest.getOAuthMessage(), openSocialAccessor.getOAuthAccessor());
		}
		catch (URISyntaxException e) {
			throw new OAuthException(e);
		}
		
		// validate application id
		if(openSocialRequest.getAppId() == null) {
			throw new OpenSocialException("Parameter " + OpenSocialRequest.OPENSOCIAL_APP_ID + " not exist");
		}
		else if(openSocialRequest.getAppId().equals(openSocialAccessor.getAppId())) {
		}
		else {
			throw new OpenSocialException("Invalid appId: " + openSocialRequest.getAppId());
		}
		
		// validate application URL
		if(openSocialRequest.getAppUrl() == null) {
			throw new OpenSocialException("Parameter " + OpenSocialRequest.OPENSOCIAL_APP_URL + " not exist");
		}
		if(openSocialRequest.getAppUrl().equals(openSocialAccessor.getAppUrl())) {
		}
		else {
			throw new OpenSocialException("Invalid appUrl: " + openSocialRequest.getAppUrl());
		}
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

}
