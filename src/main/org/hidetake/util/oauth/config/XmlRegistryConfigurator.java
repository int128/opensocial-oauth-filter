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
package org.hidetake.util.oauth.config;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;

import net.oauth.OAuthAccessor;

import org.hidetake.util.oauth.extensionpoint.ExtensionPoint;
import org.hidetake.util.oauth.model.AppRegistry;
import org.hidetake.util.oauth.model.ExtensionRegistry;
import org.hidetake.util.oauth.model.OpenSocialApp;
import org.hidetake.util.oauth.util.NoSuchNodeException;
import org.hidetake.util.oauth.util.XPathEvaluator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Configuration XML file parser.
 * 
 * @author hidetake.org
 *
 */
public class XmlRegistryConfigurator implements RegistryConfigurator
{

	private final XPathEvaluator rootEvaluator;
	private final XPath xpath;
	
	public XmlRegistryConfigurator(Document sourceXML)
	{
		this.xpath = XPathEvaluator.createXPath();
		this.rootEvaluator = new XPathEvaluator(sourceXML, xpath);
	}
	
	public void configure(AppRegistry registry) throws ConfigurationException
	{
		try {
			for(Node appNode : rootEvaluator.getNodeList("/config/opensocial-apps/opensocial-app")) {
				XPathEvaluator appNodeEvaluator = new XPathEvaluator(appNode, xpath);
				
				// get application data
				String appId = appNodeEvaluator.getString("./@app-id");
				String appUrl = appNodeEvaluator.getString("./@app-url");
				String containerId = appNodeEvaluator.getString("./@container-id");
				
				// get oauth data
				OAuthAccessor oauthAccessor = getOAuthAccessor(containerId);
				
				// register
				OpenSocialApp app = new OpenSocialApp(appId, appUrl, oauthAccessor);
				registry.register(app);
			}
		}
		catch (NoSuchNodeException e) {
			throw new ConfigurationException(e);
		}
	}
	
	public void configure(ExtensionRegistry registry) throws ConfigurationException
	{
		try {
			for(String id : rootEvaluator.getNodeValueList("/config/extensions/extension/@id")) {
				ExtensionPoint extension = (ExtensionPoint) Class.forName(id).newInstance();
				registry.register(extension);
			}
		}
		catch (NoSuchNodeException e) {
			// no extension found
			return;
		}
		catch (InstantiationException e) {
			throw new ConfigurationException(e);
		}
		catch (IllegalAccessException e) {
			throw new ConfigurationException(e);
		}
		catch (ClassNotFoundException e) {
			throw new ConfigurationException(e);
		}
	}

	private OAuthAccessor getOAuthAccessor(String containerId) throws ConfigurationException
	{
		// get container node
		Map<QName, Object> variableMap = new HashMap<QName, Object>();
		variableMap.put(new QName("id"), containerId);
		rootEvaluator.setVariable(variableMap);
		
		Node containerNode;
		try {
			containerNode = rootEvaluator.getNode(
				"/config/opensocial-containers/opensocial-container[@id=$id]");
		}
		catch (NoSuchNodeException e) {
			throw new ConfigurationException("No such opensocial-container found, id=" + containerId);
		}
		
		// get container parameters
		String signatureMethod;
		try {
			XPathEvaluator containerEvaluator = new XPathEvaluator(containerNode, xpath);
			signatureMethod = containerEvaluator.getString("./oauth/@signature-method");
		}
		catch (NoSuchNodeException e) {
			throw new ConfigurationException("No signature method found");
		}
		
		// initialize accessor
		if("RSA-SHA1".equals(signatureMethod)) {
			try {
				XPathEvaluator containerEvaluator = new XPathEvaluator(containerNode, xpath);
				
				String consumerKey = containerEvaluator.getString("./oauth/@consumer-key");
				String cert = containerEvaluator.getString("./oauth/certificate/text()").trim();
				
				return OpenSocialApp.createOAuthAccessorRSASHA1(consumerKey, cert);
			}
			catch (NoSuchNodeException e) {
				throw new ConfigurationException("No key or certificate found");
			}
		}
		else {
			throw new ConfigurationException("Not implemented yet: " + signatureMethod);
		}
	}

}
