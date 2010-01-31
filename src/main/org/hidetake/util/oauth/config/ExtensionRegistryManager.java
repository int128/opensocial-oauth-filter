package org.hidetake.util.oauth.config;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hidetake.util.oauth.extensionpoint.ExtensionPoint;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ExtensionRegistryManager
{

	private final static ExtensionRegistry instance = new ExtensionRegistry();

	public final static ExtensionRegistry get()
	{
		return instance;
	}
	
	public static void register(InputStream configStream) throws ConfigurationException
	{
		XPathEvaluator rootEvaluator;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			//factory.setNamespaceAware(true);
			Document xml = factory.newDocumentBuilder().parse(configStream);
			
			rootEvaluator = new XPathEvaluator(xml);
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
			for(String id : rootEvaluator.getNodeValueList("/config/extensions/extension/@id")) {
				try {
					ExtensionPoint extension = (ExtensionPoint) Class.forName(id).newInstance();
					ExtensionRegistryManager.get().register(extension);
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
		}
		catch (NoSuchNodeException e) {
			throw new ConfigurationException(e);
		}
	}

}
