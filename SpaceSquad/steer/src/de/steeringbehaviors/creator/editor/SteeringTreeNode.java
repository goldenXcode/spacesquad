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

import java.util.Iterator;
import java.util.TreeMap;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import de.steeringbehaviors.simulation.renderer.Geometrie;




/** Class SteeringTreeNode implements a user object of the tree */
public class SteeringTreeNode
{
		
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Constructor */
	SteeringTreeNode()
	{
	}
	
	/**
	* Constructor
	* @param caption Caption of the node
	* @param type Type of the node
	* @param name Name of the node
	*/
	SteeringTreeNode(String caption, String type, String name)
	{ 
		m_attr=new TreeMap();
		m_caption=caption; 
		m_type=type; 
		m_name=name; 
		//m_geometrieObject=new GeometrieObject();
	}
	
	/**
	* Constructor
	* @param node Node of which attributes can be extracted
	*/
	SteeringTreeNode(Node node)
	{
	
		// if parent node is the behaviors-node, then type is a behavior
		// else type is the name of the node
		m_attr=new TreeMap();
		
		//m_geometrieObject=new GeometrieObject();
		
		setType(getTypeByName(node.getNodeName()));
		
		m_geometrieObject=null;
		if (getType().equals("vehicle") )
		{
			m_geometrieObject=new GeometrieObject(GeometrieObject.VEHICLE);			
		}
		else if (getType().equals("obstacle") )
		{
			m_geometrieObject=new GeometrieObject(GeometrieObject.OBSTACLE);
		}
		
				
		setCaption(node.getNodeName());

		if (node.hasAttributes()) setAttributes(node.getAttributes());
		
		String name=node.getNodeName();
		setName(name);
			
		// get the name of the item to show it as caption
		try 
		{	
			if (m_attr.get("name")!=null) setCaption((String)m_attr.get("name"));
			else setCaption(name);
		}
		catch(Exception e){  }
	

	} 


	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////

	/** 
	* Sets the caption of the node
	* @param s Caption name
	*/
	public void setCaption(String s)  		{ m_caption=s; }
	
	/**
	* Sets the node type
	* @param t Type of the node 
	*/
	public void setType(String t)			{ m_type=t; }
	
	/**
	* Sets the attributes listed in the NamedNodeMap
	* @param NamedNodeMap of attributes to set
	*/
	public void setAttributes(NamedNodeMap attr) 	
	{ 
		m_attr=new TreeMap();
		
		for (int i=0; i<=attr.getLength()-1 ; i++)
		{
			setAttribute(	attr.item(i).getNodeName(),
					attr.item(i).getNodeValue());
		}
	} 

	/**
	* Sets a single attribute
	* @param name Name of the attribute to set
	* @param value Value of the attribute to set
	*/
	public void setAttribute(String name, String value)
	{
		m_attr.put(name,value);

		// change the description of the object
		if (m_geometrieObject!=null) m_geometrieObject.setAttribute(name,value);
	} 

	/**
	* Sets a GeometrieObject
	* @param geo GeometrieObject to set
	*/
	public void setGeometrieObject(GeometrieObject geo)	
	{ 
		m_geometrieObject = geo;	
	}

	/**
	* Sets the name of the node
	* @param n Name to set
	*/
	public void setName(String n)
	{ 
		m_name=n; 
	}



	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
	
	
	/**
	* Returns the caption of the node
	* @return Caption of the node
	*/
	public String getCaption()			
	{ 
		return m_caption; 
	}
	
	/**
	* Returns the node type
	* @return The type of the node
	*/
	public String getType()
	{ 
		return m_type; 
	}
	
	/**
	* Returns the name of the node
	* @return Name of the node
	*/
	public String getName()				
	{ 
		return m_name; 
	}
	
	/**
	* Returns the description of the assigned GeometrieObject
	* @return Description of the assigned GeometrieObject
	*/
	public Geometrie getObjectDescription()		
	{ 
		return m_geometrieObject.getObjectDescription(); 
	}
	
	/**
	* Returns the assigned GeometrieObject
	* @return Assigned GeometrieObject
	*/
	public GeometrieObject getGeometrieObject()	
	{ 
		return m_geometrieObject;	
	}
	
	
	/**
	* Returns a TreeMap of all atttributes
	* @return TreeMap of all attributes
	*/  	
	public TreeMap getAttributes()			
	{ 
		return m_attr; 
	}
	
	
	/**
	* Returns a singe attribute
	* @param name Name of the attribute to return
	* @return The value of the specified attribute
	*/
	public String getAttribute(String name)
	{
		return (String) m_attr.get(name);
	}
	
	
	/**
	* Returns the caption. Used for the JTree entries
	* @return Caption of the node
	*/
	public String toString()
	{ 
		return m_caption; 
	}
	  	
	
	
	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////
	
	
	/**
	* Clones the SteeringTreeNode object
	* @return The cloned SteeringTreeNode
	*/
	public Object clone()
	{
		SteeringTreeNode newstn = new SteeringTreeNode(m_caption, m_type, m_name);
		
		if (m_geometrieObject != null)
			newstn.setGeometrieObject((GeometrieObject) m_geometrieObject.clone());
		
		// set all attributes. The parameter in GeometrieObject is set too
		Iterator i = m_attr.keySet().iterator();
		while (i.hasNext())
		{
			String key = (String) i.next();
			((SteeringTreeNode) newstn).setAttribute(key, (String) m_attr.get(key));	
		}
		
		return newstn;
	}
	
	/**
	* Removes a specified attribute
	* @param name Name of the attribute to remove
	*/
	void removeAttribute(String name)
	{
		m_attr.remove(name);
	}

	/**
	* Returns the type depending on the specified node name
	* @param name Name of the node 
	* @return Type of the node
	*/
	public String getTypeByName(String name)
	{
		String ret = name;
		if (name.equals("steering")) ret = new String("steering");
		if (name.equals("vehicle")) ret = new String("vehicle");
		if (name.equals("obstacle")) ret = new String("obstacle");
		if (name.equals("simplemind")) ret = new String("mind");
		if (name.equals("seek")) ret = new String("behavior");
		if (name.equals("obstacleavoidance")) ret = new String("behavior");
		if (name.equals("separation")) ret = new String("behavior");
		if (name.equals("alignment")) ret = new String("behavior");
		if (name.equals("cohesion")) ret = new String("behavior");
		if (name.equals("flocking")) ret = new String("behavior");
		if (name.equals("flee")) ret = new String("behavior");
		if (name.equals("arrive")) ret = new String("behavior");
		if (name.equals("wander")) ret = new String("behavior");
		if (name.equals("containment")) ret = new String("behavior");
		if (name.equals("simplepathfollowing")) ret = new String("behavior");
		if (name.equals("pursuit")) ret = new String("behavior");
		if (name.equals("evade")) ret = new String("behavior");
		
		
		return ret;
	}
	


	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////


	/** The caption of the node. It is shown in the JTree */
	private String		m_caption;
	
	/** The type of the node, specified as string */
	private String		m_type;
	
	/** The name of the node */
	private String		m_name;
	
	/** TreeMap which contains the attributes of this node */
	private TreeMap		m_attr;
	
	/** GeometrieObject of the node */
	private GeometrieObject m_geometrieObject;

}