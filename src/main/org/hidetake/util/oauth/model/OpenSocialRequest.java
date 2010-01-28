package org.hidetake.util.oauth.model;

import javax.servlet.http.HttpServletRequest;

import net.oauth.OAuthMessage;

public class OpenSocialRequest
{

	public static final String OPENSOCIAL_APP_URL = "opensocial_app_url";
	public static final String OPENSOCIAL_APP_ID = "opensocial_app_id";
	
	private final OAuthMessage oauthMessage;
	private final String appId;
	private final String appUrl;

	public OpenSocialRequest(OAuthMessage message, HttpServletRequest request)
	{
		oauthMessage = message;
		appId = request.getParameter(OPENSOCIAL_APP_ID);
		appUrl = request.getParameter(OPENSOCIAL_APP_URL);
	}

	public final OAuthMessage getOAuthMessage()
	{
		return oauthMessage;
	}

	public final String getAppId()
	{
		return appId;
	}

	public final String getAppUrl()
	{
		return appUrl;
	}
	
}
