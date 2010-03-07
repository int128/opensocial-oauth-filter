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
package org.hidetake.opensocial.filter.extensionpoint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hidetake.opensocial.filter.model.OpenSocialException;
import org.hidetake.opensocial.filter.model.OpenSocialRequest;

/**
 * OpenSocial request validation extension point.
 * 
 * @author hidetake.org
 *
 */
public interface Validation extends ExtensionPoint
{

	/**
	 * Called when the validation has been passed.
	 * @param request
	 * @param response
	 * @param openSocialRequest
	 * @throws ServletException
	 * @throws IOException
	 */
	public void passed(
		HttpServletRequest request,
		HttpServletResponse response,
		OpenSocialRequest openSocialRequest)
	throws ServletException, IOException;

	/**
	 * Called when the validation has been failed.
	 * @param request
	 * @param response
	 * @param reason
	 * @throws ServletException
	 * @throws IOException
	 */
	public void failed(
		HttpServletRequest request,
		HttpServletResponse response,
		OpenSocialException reason)
	throws ServletException, IOException;

	/**
	 * Called when the validation has been skipped (not passed).
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void skipped(
		HttpServletRequest request,
		HttpServletResponse response)
	throws ServletException, IOException;

}
