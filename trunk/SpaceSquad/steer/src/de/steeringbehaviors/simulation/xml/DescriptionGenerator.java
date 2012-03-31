/*
	Steering Behaviors

    Copyright (C) 2001	
    
    	Thomas Feilkas 			
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
		CSchnell@Gmx.de
		TFeilkas@Gmx.de
	
*/
package de.steeringbehaviors.simulation.xml;

import org.apache.xerces.parsers.*;
import org.w3c.dom.*;
import org.w3c.dom.traversal.*;

import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.PolygonShape;



/** Class DescriptionGenerator Generates a object description from a xml file */
public class DescriptionGenerator
{		
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////

	/**
     	* Constructor
     	* @param treeWalker TreeWalkerObject to navigate in the DOM
     	*/
	public DescriptionGenerator(TreeWalker treeWalker)
	{
		m_treeWalker=treeWalker;
	}
	
	
	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Returns the Geometrie object described in the DOM
	* @return Geometrie object
	*/
	public Geometrie buildDescription()
	{	
		Geometrie description=new Geometrie();
		
		if (m_treeWalker.firstChild()!=null)
		{
			do
			{
				m_node=m_treeWalker.getCurrentNode();
				
				// what type of description?
				if (m_node.getNodeName().equals("polygonshape"))
         			{
         				description=buildPolygonShape();
         			}
				
			}
			while (m_treeWalker.nextSibling()!=null);
		}
		
		// go back to the parent -----------------
		m_treeWalker.parentNode();
		
		return description;
		
	} // buildDescription

	/*
	* Returns the Geometrie object consisting of a polygon shape which
	* is described in the xml file
	* @return Geometrie object
	*/
	private Geometrie buildPolygonShape()
	{
		// create a geometrie
		Geometrie description=new Geometrie();
		PolygonShape ps = new PolygonShape();
                ps.setZ(1);
                description.addShape(ps);
                
                // assign the geometrie
                //stn.getGeometrieObject().setObjectDescription(description);
                
		if (m_treeWalker.firstChild()!=null)
		{
			do
			{
				m_node=m_treeWalker.getCurrentNode();
				
				if (m_node.getNodeName().equals("point2d"))
         			{
         					// get all points
						do
						{
							m_node=m_treeWalker.getCurrentNode();
							NamedNodeMap attr;
         	
         						if (m_node.hasAttributes())
         						{
         					 		attr=m_node.getAttributes();
         							double x=(new Double(attr.getNamedItem("x").getNodeValue())).doubleValue();
         							double y=(new Double(attr.getNamedItem("y").getNodeValue())).doubleValue();
         					
         							// add this point to the polygonshape
         							ps.addPoint(new Point2d(x,y));
         						}
							
						}
						while (m_treeWalker.nextSibling()!=null);				
         			}
				
			}
			while (m_treeWalker.nextSibling()!=null);
		}
		
		// go back to the parent -----------------
		m_treeWalker.parentNode();
		
		return description;
		
	} // buildPolygonShape


	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////

	/** TreeWalker object used to build the description of a Geometrie object*/
	protected TreeWalker		m_treeWalker;

	/** Used to store the current position of the TreeWalker */	
	protected Node			m_node;

} // class