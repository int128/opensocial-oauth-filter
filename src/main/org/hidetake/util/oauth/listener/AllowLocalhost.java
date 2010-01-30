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

import org.hidetake.util.oauth.ValidationEventListener;
import org.hidetake.util.oauth.config.InitContext;
import org.hidetake.util.oauth.model.OpenSocialException;

public class AllowLocalhost implements ValidationEventListener
{

	private static final Logger log = Logger.getLogger(AllowLocalhost.class.getName());
	
	public void init(InitContext context)
	throws ServletException
	{
	}
	
	public boolean isSkippingValidation(ServletRequest arg0, ServletResponse arg1)
	{
		if("127.0.0.1".equals(arg0.getRemoteAddr()) || "0:0:0:0:0:0:0:1".equals(arg0.getRemoteAddr())) {
			log.info("Validation disabled: " + arg0.getRemoteHost() + " [" + arg0.getRemoteAddr() + "]");
			return true;
		}
		return false;
	}

	public void manipulateURL(StringBuilder arg0, HttpServletRequest arg1)
	{
	}

	public boolean onOAuthException(HttpServletRequest arg0, HttpServletResponse arg1, OAuthException arg2)
	{
		return false;
	}

	public boolean onOpenSocialException(HttpServletRequest request, HttpServletResponse response, OpenSocialException e)
	{
		return false;
	}

	public void onValidationComplete(HttpServletResponse arg0)
	{
	}

}
