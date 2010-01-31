package org.hidetake.util.oauth.extensionpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hidetake.util.oauth.model.OpenSocialException;
import org.hidetake.util.oauth.model.OpenSocialRequest;

public interface Validation extends ExtensionPoint
{

	public void passed(HttpServletRequest request, HttpServletResponse response, OpenSocialRequest openSocialRequest);

	public void failed(HttpServletRequest request, HttpServletResponse response, OpenSocialException reason);

}
