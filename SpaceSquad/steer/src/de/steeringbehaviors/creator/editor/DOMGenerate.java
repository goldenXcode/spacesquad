/*
Steering Behaviors Scene Creator

Copyright (c) 2001	Thomas Feilkas
Christian Schnellhammer

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

For further questions contact us at:
cschnell@gmx.de
tfeilkas@gmx.de

*/
package de.steeringbehaviors.creator.editor;

import  javax.swing.tree.DefaultTreeModel;
import  javax.swing.tree.DefaultMutableTreeNode;
import  java.util.*;
import  org.w3c.dom.*;
import  org.apache.xerces.dom.DocumentImpl;
import  org.apache.xerces.dom.DOMImplementationImpl;
import  org.w3c.dom.Document;
import  org.apache.xml.serialize.OutputFormat;
import  org.apache.xml.serialize.Serializer;
import  org.apache.xml.serialize.SerializerFactory;
import  org.apache.xml.serialize.XMLSerializer;

import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.PolygonShape;


import  java.io.*;



/** Class DOMGenerate Generates a DOM Document of the SteeringBehaviors object model */
public class DOMGenerate 
{

	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////

	/**
	* Creates a DOM from the specified TreeModel
	* @param treeModel TreeModel of the object model
	* @return DOM Document
	*/
	public Document createDOM(DefaultTreeModel treeModel)
	{
		m_treeModel=treeModel;
		m_document= new DocumentImpl();

		// create root element
		DefaultMutableTreeNode m_node;
		m_node=(DefaultMutableTreeNode)((DefaultMutableTreeNode)treeModel.getRoot()).getFirstChild();
		m_root = m_document.createElement("steering"); 

		m_stn=(SteeringTreeNode) m_node.getUserObject();

		setAttributes(m_root, m_stn);


		buildDOM(m_root, m_node);
		m_document.appendChild(m_root);

		return m_document;
	} // createDOM	

	/**
	* Builds the DOM from the specified node of the tree
	* @param root Root Element to add the elements
	* @param treeroot Root node of the object tree
	*/
	private void buildDOM(Element root, DefaultMutableTreeNode treeroot)
	{		
		
		Element child;
		DefaultMutableTreeNode m_node;
		// get the first child of the node

		if (treeroot.getChildCount()>0)
		{
			
			m_node= (DefaultMutableTreeNode) treeroot.getFirstChild();
		
			// get the first child of the node
			
			do
			{     
			
				m_stn=(SteeringTreeNode) m_node.getUserObject();
				
				child=m_document.createElement(m_stn.getName());
				
						
				buildDescription(child, m_node, m_stn);
									
				setAttributes(child, m_stn);
				
				// add child and build the subtree ------------------------
				root.appendChild(child);
				
				// build the subtree of this node - (it's a recursive call) -------
				buildDOM(child, m_node);
				// m_treeWalker.setCurrentNode(m_node);
			
				m_node=(DefaultMutableTreeNode) treeroot.getChildAfter(m_node);

			}
			while(m_node!=null);
		}
	} // buildDOM
	
	/**
	* Creates a DOM of an object description
	* @param root Root Element to add the DOM elements
	* @param treeroot Node of the object tree
	* @param stn SteeringTreeNode which contains the object description
	*/
	private void buildDescription(Element root, DefaultMutableTreeNode treeroot, SteeringTreeNode stn)
	{
		
		
		if ((stn.getGeometrieObject())!=null)
		{
			Geometrie objectDescription=stn.getGeometrieObject().getObjectDescription();	
			Element description=m_document.createElement("description");
			
			Iterator i=objectDescription.getShapeIter();
			if (i.hasNext())
			{
				do
				{
					Object obj = i.next();
					if (obj.getClass().isInstance(new PolygonShape()))
					{
						// object is a polygonshape
						buildPolygonShape(description, (PolygonShape) obj);
					}
					
					
				}	
				while (i.hasNext());
			}
			
			
			
			root.appendChild(description);
		}
	} // buildDescription
	
	/** 
	* Creates a description of a polygonshape
	* @param root Element to add the DOM description
	* @param ps PolygonShape from which the DOM description should be created
	*/
	private void buildPolygonShape(Element root, PolygonShape ps)
	{
		
		Element polygonshape=m_document.createElement("polygonshape");
		
		Element child;
		
		Iterator i=ps.getPoints();
		if (i.hasNext())
		{
			do
			{
				Point2d p=(Point2d) i.next();
				child=m_document.createElement("point2d");
				
				child.setAttribute("x",new Double(p.getX()).toString());
				child.setAttribute("y",new Double(p.getY()).toString());		
				
				// add this point to polygonshape
				polygonshape.appendChild(child);
			}
			while (i.hasNext());
		}
		
		root.appendChild(polygonshape);
	} // polygonshape

	/**
	* Sets the attributes of an element
	* @param element Element to set the attributes
	* @param stn SteeringTreeNode with the attributes to set
	*/
	private void setAttributes(Element element, SteeringTreeNode stn)
	{
		TreeMap attr=stn.getAttributes();
				
		if (attr.size()>0)
		{
			Iterator it=attr.keySet().iterator();
			while(it.hasNext())
			{
				String name=(String) it.next();
				String value=(String) attr.get(name);
				element.setAttribute(name,value);
			}
		}
	}


	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////        

	/** Document object */
	protected Document		m_document;
	
	/** Element object */
	protected Element		m_item;
	
	/** Root Element */
	protected Element		m_root;
	
	/** Actual SteeringTreeNode */
	protected SteeringTreeNode 	m_stn;
	
	/** TreeModel of the JTree */
	protected DefaultTreeModel	m_treeModel;

} // class DOMGenerate