package org.hidetake.util.oauth.config;

import java.util.ArrayList;
import java.util.List;

import org.hidetake.util.oauth.model.OpenSocialApp;

public class AppRegistry
{

	private final List<OpenSocialApp> appList = new ArrayList<OpenSocialApp>();

	public AppRegistry()
	{
	}
	
	public void register(OpenSocialApp app)
	{
		appList.add(app);
	}

	public List<OpenSocialApp> getList()
	{
		return appList;
	}

}
