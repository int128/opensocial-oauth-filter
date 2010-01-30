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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;

import org.hidetake.util.oauth.ValidationEventListener;
import org.hidetake.util.oauth.model.OpenSocialApp;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Configuration XML file parser.
 * 
 * @author hidetake.org
 *
 */
public class AppRegistryFactory
{

	private final XPath xpath = XPathEvaluator.createXPath();
	
	public AppRegistryFactory()
	{
	}

	public AppRegistry create(String path) throws ConfigurationException, FileNotFoundException
	{
		InputStream configStream = new FileInputStream(path);
		return create(configStream);
	}
	
	public AppRegistry create(InputStream configStream) throws ConfigurationException
	{
		XPathEvaluator rootEvaluator;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			//factory.setNamespaceAware(true);
			Document xml = factory.newDocumentBuilder().parse(configStream);
			
			rootEvaluator = new XPathEvaluator(xml, xpath);
		}
		catch (SAXException e) {
			throw new ConfigurationException(e);
		}
		catch (IOException e) {
			throw new ConfigurationException(e);
		}
		catch (ParserConfigurationException e) {
			throw new ConfigurationException(e);
		}
		
		try {
			AppRegistry registry = new AppRegistry();
			
			for(Node appNode : rootEvaluator.getNodeList("/config/opensocial-apps/opensocial-app")) {
				XPathEvaluator appNodeEvaluator = new XPathEvaluator(appNode, xpath);
				
				// get application data
				String appId = appNodeEvaluator.getString("./@app-id");
				String appUrl = appNodeEvaluator.getString("./@app-url");
				String containerId = appNodeEvaluator.getString("./@container-id");
				
				// get oauth data
				OAuthAccessor oauthAccessor = getOAuthAccessor(rootEvaluator, containerId);
				
				// register
				OpenSocialApp app = new OpenSocialApp(appId, appUrl, oauthAccessor);
				registry.register(app);
			}
			
			return registry;
		}
		catch (NoSuchNodeException e) {
			throw new ConfigurationException(e);
		}
	}

	private OAuthAccessor getOAuthAccessor(XPathEvaluator rootEvaluator, String containerId)
	throws ConfigurationException
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
		XPathEvaluator containerEvaluator = new XPathEvaluator(containerNode, xpath);
		
		String consumerKey;
		String signatureMethod;
		String cert;
		try {
			consumerKey = containerEvaluator.getString("./oauth/@consumer-key");
			signatureMethod = containerEvaluator.getString("./oauth/@signature-method");
			cert = containerEvaluator.getString("./oauth/certificate/text()").trim();
		}
		catch (NoSuchNodeException e) {
			throw new ConfigurationException(e);
		}
		
		// initialize accessor
		OAuthConsumer consumer = new OAuthConsumer(null, consumerKey, null, null);
		consumer.setProperty(signatureMethod + ".X509Certificate", cert);
		OAuthAccessor oauthAccessor = new OAuthAccessor(consumer);
		return oauthAccessor;
	}

	//TODO: separate
	@Deprecated
	public List<ValidationEventListener> getValidationEventListeners(String path)
	throws ConfigurationException, FileNotFoundException
	{
		InputStream configStream = new FileInputStream(path);
		return getValidationEventListeners(configStream);
	}
	
	//TODO: separate
	@Deprecated
	public List<ValidationEventListener> getValidationEventListeners(InputStream configStream)
	throws ConfigurationException
	{
		XPathEvaluator rootEvaluator;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			//factory.setNamespaceAware(true);
			Document xml = factory.newDocumentBuilder().parse(configStream);
			
			rootEvaluator = new XPathEvaluator(xml, xpath);
		}
		catch (SAXException e) {
			throw new ConfigurationException(e);
		}
		catch (IOException e) {
			throw new ConfigurationException(e);
		}
		catch (ParserConfigurationException e) {
			throw new ConfigurationException(e);
		}
		
		try {
			List<ValidationEventListener> result = new ArrayList<ValidationEventListener>();
			
			for(String id : rootEvaluator.getNodeValueList("/config/features/feature/@id")) {
				try {
					ValidationEventListener eventListener = (ValidationEventListener) Class.forName(id).newInstance();
					result.add(eventListener);
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
			
			return result;
		}
		catch (NoSuchNodeException e) {
			throw new ConfigurationException(e);
		}
	}

}
