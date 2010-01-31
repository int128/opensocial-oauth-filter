package org.hidetake.util.oauth.extensionpoint;

import javax.servlet.http.HttpServletRequest;

public interface RequestURL extends ExtensionPoint
{
	
	public void postprocess(StringBuilder url, HttpServletRequest request);

}
