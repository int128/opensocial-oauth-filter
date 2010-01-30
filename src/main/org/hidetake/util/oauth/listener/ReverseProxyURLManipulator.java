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


import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthException;

import org.hidetake.util.oauth.ValidationEventListener;
import org.hidetake.util.oauth.config.InitContext;
import org.hidetake.util.oauth.model.OpenSocialException;

/**
 * 
 * URL manipulator for a reverse proxy which adds X-Forwarded-Host header.
 * 
 * @author hidetake.org
 *
 */
public class ReverseProxyURLManipulator implements ValidationEventListener
{

	public void init(InitContext context)
	throws ServletException
	{
	}
	
	public boolean isSkippingValidation(ServletRequest req, ServletResponse res)
	{
		return false;
	}

	public void manipulateURL(StringBuilder url, HttpServletRequest request)
	{
		// look up X-Forwarded-Host header
		String forwardedHost = request.getHeader("X-Forwarded-Host");
		if(forwardedHost == null) {
			// do nothing
		}
		else {
			// extract parts without hostname
			final int firstPartEndPos = url.indexOf("://") + "://".length();
			final String firstPart = url.substring(0, firstPartEndPos);
			
			final int lastPartStartPos = url.indexOf("/", firstPartEndPos);
			final String lastPart = url.substring(lastPartStartPos);
			
			// clear buffer
			url.setLength(0);
			
			url.append(firstPart);
			url.append(forwardedHost);
			url.append(lastPart);
		}
	}

	public boolean onOAuthException(HttpServletRequest request, HttpServletResponse response, OAuthException e)
	{
		return false;
	}

	public boolean onOpenSocialException(HttpServletRequest request, HttpServletResponse response, OpenSocialException e)
	{
		return false;
	}

	public void onValidationComplete(HttpServletResponse response)
	{
	}

}
