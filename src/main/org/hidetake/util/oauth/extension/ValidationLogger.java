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
package org.hidetake.util.oauth.extension;

import java.util.logging.Logger;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hidetake.util.oauth.config.AppRegistry;
import org.hidetake.util.oauth.config.ExtensionRegistryManager;
import org.hidetake.util.oauth.extensionpoint.ExtensionPoint;
import org.hidetake.util.oauth.extensionpoint.FilterInitializing;
import org.hidetake.util.oauth.extensionpoint.Validation;
import org.hidetake.util.oauth.model.OpenSocialApp;
import org.hidetake.util.oauth.model.OpenSocialException;
import org.hidetake.util.oauth.model.OpenSocialRequest;

/**
 * 
 * Logger for validation events.
 * 
 * @author hidetake.org
 *
 */
public class ValidationLogger implements FilterInitializing, Validation
{
	private static final Logger log = Logger.getLogger(ValidationLogger.class.getName());

	public void init(FilterConfig config, AppRegistry appRegistry) throws ServletException
	{
		for(OpenSocialApp app : appRegistry.getList()) {
			log.info("Registered opensocial-app: id=" + app.getAppId() + ", url=" + app.getAppUrl());
			log.info("Registered opensocial-container: " + app.getOAuthAccessor().consumer.consumerKey);
		}
		
		for(ExtensionPoint extensionPoint : ExtensionRegistryManager.get().getAllExtensions()) {
			log.info("Registered extension: " + extensionPoint.getClass().getName());
		}
	}

	public void failed(HttpServletRequest request, HttpServletResponse response, OpenSocialException reason)
	{
		log.severe("Validation failed: " + reason.getLocalizedMessage());
	}

	public void passed(HttpServletRequest request, HttpServletResponse response, OpenSocialRequest openSocialRequest)
	{
		log.info("Validation passed: viewer=" + openSocialRequest.getViewerId() + ", owner=" + openSocialRequest.getOwnerId());
	}

}