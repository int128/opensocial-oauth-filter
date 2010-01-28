package org.hidetake.util.oauth.model;

import net.oauth.OAuthAccessor;

public class OpenSocialAccessor
{

	private final String appId;
	private final String appUrl;
	private final OAuthAccessor oauthAccessor;
	
	public OpenSocialAccessor(String appId, String appUrl, OAuthAccessor oauthAccessor)
	{
		this.appId = appId;
		this.appUrl = appUrl;
		this.oauthAccessor = oauthAccessor;
	}

	public final String getAppId()
	{
		return appId;
	}
	
	public final String getAppUrl()
	{
		return appUrl;
	}

	public OAuthAccessor getOAuthAccessor()
	{
		return oauthAccessor;
	}

}
