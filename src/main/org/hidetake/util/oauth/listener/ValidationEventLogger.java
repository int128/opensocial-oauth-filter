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
package org.hidetake.util.oauth.listener;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthException;
import net.oauth.signature.RSA_SHA1;

import org.hidetake.util.oauth.ValidationEventListener;
import org.hidetake.util.oauth.config.InitContext;
import org.hidetake.util.oauth.model.OpenSocialApp;
import org.hidetake.util.oauth.model.OpenSocialException;

/**
 * 
 * Logger for validation events.
 * 
 * @author hidetake.org
 *
 */
public class ValidationEventLogger implements ValidationEventListener
{
	private static final Logger log = Logger.getLogger(ValidationEventLogger.class.getName());

	public void init(InitContext context) throws ServletException
	{
		OpenSocialApp accessor = context.getOpenSocialAccessor();
		log.info("App: " + accessor.getAppId() + ": " + accessor.getAppUrl());
		log.info("Container: "
			+ RSA_SHA1.X509_CERTIFICATE
			+ ": "
			+ accessor.getOAuthAccessor().consumer.getProperty(RSA_SHA1.X509_CERTIFICATE));
		
		for(ValidationEventListener listener : context.getValidationEventListeners()) {
			log.info("Registered event listener: " + listener.getClass().getName());
		}
	}
	
	public void manipulateURL(StringBuilder url, HttpServletRequest request)
	{
		log.fine("OAuth message URL: " + url);
	}

	public boolean onOAuthException(HttpServletRequest request, HttpServletResponse response, OAuthException e)
	{
		log.severe("Validation failed: " + e.getLocalizedMessage());
		//e.printStackTrace();
		return false;
	}

	public boolean onOpenSocialException(HttpServletRequest request, HttpServletResponse response, OpenSocialException e)
	{
		log.severe("Validation failed: " + e.getLocalizedMessage());
		return false;
	}

	public void onValidationComplete(HttpServletResponse response)
	{
		log.info("Validation complete");
	}

	public boolean isSkippingValidation(ServletRequest req, ServletResponse res)
	{
		return false;
	}

}
