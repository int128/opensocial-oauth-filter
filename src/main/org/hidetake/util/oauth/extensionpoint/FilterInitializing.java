package org.hidetake.util.oauth.extensionpoint;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.hidetake.util.oauth.config.AppRegistry;

public interface FilterInitializing extends ExtensionPoint
{

	public void init(FilterConfig config, AppRegistry appRegistry) throws ServletException;

}
