package org.hidetake.util.oauth.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathVariableResolver;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XPath wrapper class.
 * 
 * @author hidetake.org
 *
 */
public class XPathEvaluator
{

	private final Document document;
	private final XPath xpath;

	public XPathEvaluator(InputStream stream)
	throws SAXException, IOException, ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//factory.setNamespaceAware(true);
		document = factory.newDocumentBuilder().parse(stream);
		xpath = XPathFactory.newInstance().newXPath();
	}

	public Iterable<Node> getNodeList(String expression)
	{
		try {
			final NodeList nodeList = (NodeList) xpath.evaluate(expression, document, XPathConstants.NODESET);
			return new Iterable<Node>()
			{
				public Iterator<Node> iterator()
				{
					return new Iterator<Node>()
					{
						private int index = 0;

						public boolean hasNext()
						{
							return index < nodeList.getLength();
						}

						public Node next()
						{
							return nodeList.item(index++);
						}

						public void remove()
						{
							throw new UnsupportedOperationException("NodeList is read-only");
						}
					};
				}
			};
		}
		catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	public Iterable<String> getNodeValueList(String expression)
	{
		try {
			final NodeList nodeList = (NodeList) xpath.evaluate(expression, document, XPathConstants.NODESET);
			return new Iterable<String>()
			{
				public Iterator<String> iterator()
				{
					return new Iterator<String>()
					{
						private int index = 0;

						public boolean hasNext()
						{
							return index < nodeList.getLength();
						}

						public String next()
						{
							return nodeList.item(index++).getNodeValue();
						}

						public void remove()
						{
							throw new UnsupportedOperationException("NodeList is read-only");
						}
					};
				}
			};
		}
		catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	public String getString(String expression)
	{
		try {
			return (String) xpath.evaluate(expression, document, XPathConstants.STRING);
		}
		catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	public void setVariable(final Map<QName, Object> variableMap)
	{
		xpath.setXPathVariableResolver(new XPathVariableResolver()
		{
			public Object resolveVariable(QName variableName)
			{
				return variableMap.get(variableName);
			}
		});

	}
	
}
