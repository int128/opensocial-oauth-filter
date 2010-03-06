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
package org.hidetake.util.oauth.util;

import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathVariableResolver;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XPath wrapper class.
 * 
 * @author hidetake.org
 *
 */
public class XPathEvaluator
{

	private final Node targetNode;
	private final XPath xpath;

	/**
	 * Create a XPath object.
	 * @return xpath
	 */
	public static final XPath createXPath()
	{
		return XPathFactory.newInstance().newXPath();
	}
	
	/**
	 * Constructor.
	 * @param targetNode XML node
	 */
	public XPathEvaluator(Node targetNode)
	{
		this(targetNode, createXPath());
	}
	
	/**
	 * Constructor.
	 * @param targetNode XML node
	 * @param xpath XPath object
	 */
	public XPathEvaluator(Node targetNode, XPath xpath)
	{
		this.targetNode = targetNode;
		this.xpath = xpath;
	}

	/**
	 * Returns nodes which the expression matches.
	 * @param expression XPath expression
	 * @return nodes
	 * @throws NoSuchNodeException
	 */
	public Iterable<Node> getNodeList(String expression) throws NoSuchNodeException
	{
		try {
			final NodeList nodeList = (NodeList) xpath.evaluate(expression, targetNode, XPathConstants.NODESET);
			if(nodeList.getLength() == 0) {
				throw new NoSuchNodeException(expression);
			}
			
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

	/**
	 * Returns node values which the expression matches.
	 * @param expression XPath expression
	 * @return node values
	 * @throws NoSuchNodeException
	 */
	public Iterable<String> getNodeValueList(String expression) throws NoSuchNodeException
	{
		try {
			final NodeList nodeList = (NodeList) xpath.evaluate(expression, targetNode, XPathConstants.NODESET);
			if(nodeList.getLength() == 0) {
				throw new NoSuchNodeException(expression);
			}
			
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

	/**
	 * Returns an node which the expression matches.
	 * @param expression
	 * @return XML node object
	 * @throws NoSuchNodeException
	 */
	public Node getNode(String expression) throws NoSuchNodeException
	{
		try {
			Node node = (Node) xpath.evaluate(expression, targetNode, XPathConstants.NODE);
			if(node == null) {
				throw new NoSuchNodeException(expression);
			}
			return node;
		}
		catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns an node value which the expression matches.
	 * @param expression
	 * @return node value string
	 * @throws NoSuchNodeException
	 */
	public String getString(String expression) throws NoSuchNodeException
	{
		return getNode(expression).getNodeValue();
	}

	/**
	 * Set variable map. Variables are given in expression, for example <code>$id</code>.
	 * @param variableMap map of variable name, actual value
	 */
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
