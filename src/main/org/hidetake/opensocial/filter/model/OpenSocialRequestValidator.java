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
package org.hidetake.opensocial.filter.model;

import java.io.IOException;
import java.net.URISyntaxException;




import net.oauth.OAuthException;
import net.oauth.OAuthValidator;
import net.oauth.SimpleOAuthValidator;

public class OpenSocialRequestValidator
{

	private final AppRegistry appRegistry;
	private final OAuthValidator oauthValidator = new SimpleOAuthValidator();

	public OpenSocialRequestValidator(AppRegistry appRegistry)
	{
		if(appRegistry.getApps().size() == 0) {
			throw new IllegalArgumentException("No registered opensocial-app found");
		}
		this.appRegistry = appRegistry;
	}

	public final AppRegistry getAppRegistry()
	{
		return appRegistry;
	}
	
	public void validate(OpenSocialRequest openSocialRequest) throws OpenSocialException
	{
		if(appRegistry.getApps().size() == 1) {
			validateSingle(openSocialRequest, appRegistry.getApps().get(0));
		}
		else {
			for(OpenSocialApp app : appRegistry.getApps()) {
				try {
					validateSingle(openSocialRequest, app);
				}
				catch(OpenSocialException e) {
					// try next candidate
					continue;
				}
				
				// success
				return;
			}
			
			throw new OpenSocialException("No suitable opensocial-app found");
		}
	}

	private void validateSingle(OpenSocialRequest openSocialRequest, OpenSocialApp app)
	throws OpenSocialException
	{
		if(app.getAppId() == null) {
			// ignore opensocial_app_id
		}
		else if(openSocialRequest.getAppId().equals(app.getAppId())) {
			// validation passed
		}
		else {
			// mismatch
			throw new OpenSocialException(
				"Invalid " + OpenSocialRequest.OPENSOCIAL_APP_ID + ": " + openSocialRequest.getAppId());
		}
		
		if(app.getAppUrl() == null) {
			// ignore opensocial_app_url
		}
		else if(openSocialRequest.getAppUrl().equals(app.getAppUrl())) {
			// validation passed
		}
		else {
			// mismatch
			throw new OpenSocialException(
				"Invalid " + OpenSocialRequest.OPENSOCIAL_APP_URL + ": " + openSocialRequest.getAppUrl());
		}
		
		// validate signature
		try {
			oauthValidator.validateMessage(
				openSocialRequest.getOAuthMessage(), app.getOAuthAccessor());
		}
		catch (URISyntaxException e) {
			throw new OpenSocialException(e);
		}
		catch (OAuthException e) {
			throw new OpenSocialException(e);
		}
		catch (IOException e) {
			throw new OpenSocialException(e);
		}
	}

}
