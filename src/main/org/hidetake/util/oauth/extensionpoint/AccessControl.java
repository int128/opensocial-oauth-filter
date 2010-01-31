package org.hidetake.util.oauth.extensionpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface AccessControl extends ExtensionPoint
{

	public boolean skipValidation(HttpServletRequest request, HttpServletResponse response);

}
