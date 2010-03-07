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
/**
 * 
 */
package org.hidetake.opensocial.filter.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@SuppressWarnings("unchecked")
public class HttpServletRequestStub implements HttpServletRequest
{
	public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {}
	public void setAttribute(String arg0, Object arg1) {}
	public void removeAttribute(String arg0) {}
	public boolean isSecure() {return false;}
	public RequestDispatcher getRequestDispatcher(String arg0){return null;}
	public int getRemotePort(){return 0;}
	public int getServerPort() {return 0;}
	public String getServerName() {return null;}
	public String getScheme(){return null;}
	public String getRequestURI(){return null;}
	public String getHeader(String arg0){return null;}
	public String getRemoteHost(){return null;}
	public String getRemoteAddr(){return null;}
	public String getRealPath(String arg0){return null;}
	public BufferedReader getReader() throws IOException{return null;}
	public String getProtocol(){return null;}
	public String[] getParameterValues(String arg0){return null;}
	public Enumeration getParameterNames(){return null;}
	public Map getParameterMap(){return null;}
	public String getParameter(String arg0){return null;}
	public Enumeration getLocales(){return null;}
	public Locale getLocale(){return null;}
	public int getLocalPort(){return 0;}
	public String getLocalName(){return null;}
	public String getLocalAddr(){return null;}
	public ServletInputStream getInputStream() throws IOException{return null;}
	public String getContentType(){return null;}
	public int getContentLength(){return 0;}
	public String getCharacterEncoding(){return null;}
	public Enumeration getAttributeNames(){return null;}
	public Object getAttribute(String arg0){return null;}
	public boolean isUserInRole(String arg0){return false;}
	public boolean isRequestedSessionIdValid(){return false;}
	public boolean isRequestedSessionIdFromUrl(){return false;}
	public boolean isRequestedSessionIdFromURL(){return false;}
	public boolean isRequestedSessionIdFromCookie(){return false;}
	public Principal getUserPrincipal(){return null;}
	public HttpSession getSession(boolean arg0){return null;}
	public HttpSession getSession(){return null;}
	public String getServletPath(){return null;}
	public String getRequestedSessionId(){return null;}
	public StringBuffer getRequestURL(){return null;}
	public String getRemoteUser(){return null;}
	public String getQueryString(){return null;}
	public String getPathTranslated(){return null;}
	public String getPathInfo(){return null;}
	public String getMethod(){return null;}
	public int getIntHeader(String arg0){return 0;}
	public Enumeration getHeaders(String arg0){return null;}
	public Enumeration getHeaderNames(){return null;}
	public long getDateHeader(String arg0){return 0;}
	public Cookie[] getCookies(){return null;}
	public String getContextPath(){return null;}
	public String getAuthType(){return null;}
}