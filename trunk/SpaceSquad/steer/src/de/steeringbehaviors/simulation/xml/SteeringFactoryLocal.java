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

import de.steeringbehaviors.simulation.behaviors.Alignment;
import de.steeringbehaviors.simulation.behaviors.Arrive;
import de.steeringbehaviors.simulation.behaviors.Cohesion;
import de.steeringbehaviors.simulation.behaviors.Containment;
import de.steeringbehaviors.simulation.behaviors.Evade;
import de.steeringbehaviors.simulation.behaviors.Flocking;
import de.steeringbehaviors.simulation.behaviors.ObjectAttributes;
import de.steeringbehaviors.simulation.behaviors.ObstacleAvoidance;
import de.steeringbehaviors.simulation.behaviors.Pursuit;
import de.steeringbehaviors.simulation.behaviors.Seek;
import de.steeringbehaviors.simulation.behaviors.Separation;
import de.steeringbehaviors.simulation.behaviors.SimplePathfollowing;
import de.steeringbehaviors.simulation.behaviors.Wander;
import de.steeringbehaviors.simulation.mind.Mind;
import de.steeringbehaviors.simulation.mind.SimpleMind;
import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.renderer.RenderInfo;
import de.steeringbehaviors.simulation.renderer.Tile;
import de.steeringbehaviors.simulation.simulationobjects.Neighborhood;
import de.steeringbehaviors.simulation.simulationobjects.Obstacle;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;


import java.util.*;
import java.io.*;
import java.net.URL;
import java.awt.Image;
import java.applet.Applet;
import javax.swing.ImageIcon;


/** class SteeringFactory 
* Loads a xml file an generates a SteeringBehaviors
* object model
*/
public class SteeringFactoryLocal
{
	
	
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Constructor */
	public SteeringFactoryLocal(Object parent)	
	{	m_vehicles = new Vector();
		m_obstacles = new Vector();
		m_background = null;
		m_sceneWidth = 0;
		m_sceneHeight = 0;
		m_parent = parent;
	}
	
	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Returns the background object
	* @return Background geometrie object
	*/
	public Geometrie getBackground()	{ return m_background; }
	
	/**
	* Returns a list of all vehicles of the scene 
	* @return List of vehicles
	*/
	public Vector getVehicles()	{ return m_vehicles;	}
	
	/**
	* Returns a list of all obstacles of the scene
	* @return List of obstacles
	*/
	public Vector getObstacles()	{ return m_obstacles;	}
	
	/**
	* Returns the scene width
	* @return The scene width
	*/
	public int getSceneWidth()	{ return m_sceneWidth; }
	
	/**
	* Returns the scene height
	* @return The scene height
	*/
	public int getSceneHeight()	{ return m_sceneHeight; }

	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Sets the list of vehicles
	* @param vehicles Vehicle list to use
	*/
	public void setVehicles(Vector vehicles)	{	m_vehicles=vehicles;	}
	
	/**
	* Sets the list of obtacles
	* @param obstacles Obstacle list to use
	*/
	public void setObstacles(Vector obstacles)	{	m_obstacles=obstacles;	}
	
	/**
	* Sets the Neighborhood
	* @param n The Neighborhood to use
	*/
	public void setNeighborhood(Neighborhood n)	{	m_neighborhood=n;	}
	
	/**
	* Sets the attributes stored in the NamedNodeMap to a object which implements the 
	* ObjectAttributes interface
	* @param obj Object which implements the ObjectAttributes interface
	* @param attr NamedNodeMap with attributes to set
	*/
	private void setObjectAttributes(ObjectAttributes obj, NamedNodeMap attr)
	{
		// set the attributes of the object
		for (int i=0; i<=attr.getLength()-1; i++)
		{
			Node node=attr.item(i);
			String name=node.getNodeName().toLowerCase();
			String value=node.getNodeValue();
			
			// set the attribute
			obj.setAttribute(name, value, m_steeringObjects);
		}
	} // setVehicleAttributes
	
	
	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////

	/**
	* Creates a scene as SteeringBehaviors model from the specified filename
	* @param filename Filename of the xml file using for the scene creation
	*/
	public void createScene(String filename)
	{
		try
 		{
  			File file=new File(".",filename);
			
  		
			// load the specified xml-file
			loadScene(new FileInputStream(file));
		}
  		catch (Exception e) 
  		{
  			System.out.println("Error opening file "+filename);
  		}
		
		m_treeWalker.firstChild();
		
		// check if xml-file is a steering model
		if (!m_treeWalker.getCurrentNode().getNodeName().equals("steering")) return;
		
		// first set global attributes
		createSteering();
		
		// first create a hashtable with all steeringobjects
		createSteeringObjectsHashtable();
		
		// now create to each object the model structure of the subtree
		m_treeWalker.firstChild();
		do
		{
			// get the type of the node
			String type=m_treeWalker.getCurrentNode().getNodeName();
			
			// create the object
			if (type.equals("vehicle")) createVehicle();
			if (type.equals("obstacle")) createObstacle();
			
		}
		while (m_treeWalker.nextSibling()!=null);
	} // createScene
	
	/**
	* Creates a hashtable with all objects of the scene, stored with
	* their name as hashkey
	*/
	private void createSteeringObjectsHashtable()
	{
		m_steeringObjects=new Hashtable();
		m_treeWalker.firstChild();
		do
		{
			// get the type of the node
			String type=m_treeWalker.getCurrentNode().getNodeName();
			
			// get the name of the object
			String name=m_treeWalker.getCurrentNode().getAttributes().getNamedItem("name").getNodeValue();
			
			// create the object and store it in the hashtable
			if (type.equals("vehicle")) 
			{
				
				Vehicle v=new Vehicle("test", 0, 0, 10, 10, 3, 10, 10);
				m_steeringObjects.put(name,v);
			}
			if (type.equals("obstacle"))
			{
				Obstacle o=new Obstacle();
				m_steeringObjects.put(name,o);
			}
		}
		while (m_treeWalker.nextSibling()!=null);
		
		// walk back
		m_treeWalker.parentNode();
	} // createSteeringHashtable
	
	/**
	* Sets global attributes, like scenesize or the background image
	*/
	public void createSteering()
	{
		System.out.println("Setting up scene...");
		
		// get the attributes
		NamedNodeMap attr=m_treeWalker.getCurrentNode().getAttributes();
		
		try
		{
			// set scene attributes
			m_sceneWidth = (int) new Double(attr.getNamedItem("width").getNodeValue()).doubleValue();
			m_sceneHeight = (int)new Double(attr.getNamedItem("height").getNodeValue()).doubleValue();
			
			System.out.println("Setting scene size to " + m_sceneWidth + "x" + m_sceneHeight);
		}
		catch (Exception e) { System.out.println("Error setting scene size"); }
		
		Image img = null;
		
		try
		{				
			
			String filename = attr.getNamedItem("image").getNodeValue();
			
			// if parent is a applet
			if (m_parent.getClass().getSuperclass().isInstance(new Applet()))
			{
				Applet applet = (Applet) m_parent;
			
				// create background image
				img = applet.getImage(applet.getCodeBase(), filename);
			}
			else
			{
				img = new ImageIcon(filename).getImage();
			}
			
			// if parent is an application
			// ********************************************************
			// >> here comes the code to load an image if parent is an 
			// >> application					   
			// ********************************************************
			
		
			if (img != null)
			{	
				m_background = new Geometrie();
				Tile t = new Tile(img);
				
				int temp;
				temp = new Integer(attr.getNamedItem("imgsizex").getNodeValue()).intValue();
				t.setWidth(temp);
				temp = new Integer(attr.getNamedItem("imgsizey").getNodeValue()).intValue();
				t.setHeight(temp);
				
				m_background.setPos(t.getWidth() / 2, t.getHeight() / 2);
				t.setZ(-20);
				m_background.addShape(t);
				
				
				
			}
			
			if (m_background != null)
				System.out.println("Created background image from file: " + filename);
		}
		catch (Exception e) { System.out.println("Error loading background image"); 
		e.printStackTrace(); }
		
	} // createSteering
	
	/**
	* Creates a vehicle
	*/
	public void createVehicle()
	{	System.out.println("creating vehicle...");
		// get the attributes
		NamedNodeMap attr=m_treeWalker.getCurrentNode().getAttributes();
		
		// get the vehicle from the hashtable
		Vehicle v = (Vehicle) m_steeringObjects.get(attr.getNamedItem("name").getNodeValue());
		
	
		// create the mind
		Mind mind=createMind();
		v.setMind(mind);
	
		// add description to vehilce
		Iterator i=createDescription().getShapeIter();
		while (i.hasNext())
		{
			v.addShape ((RenderInfo) i.next());
		}
		
		// set the attributes of the vehicle
		setObjectAttributes(v,attr);
		
		// add vehicle to the vehiclelist 
		m_vehicles.add(v);
	
	} // createVehicle

	/**
	* Creates a obstacle
	*/
	private void createObstacle()
	{	System.out.println("creating obstacle...");
		// get the attributes
		NamedNodeMap attr=m_treeWalker.getCurrentNode().getAttributes();
		
		// get the vehicle from the hashtable
		Obstacle o = (Obstacle) m_steeringObjects.get(attr.getNamedItem("name").getNodeValue());
		
		// add description to obstacle
		Iterator i=createDescription().getShapeIter();
		while (i.hasNext())
		{
			o.addShape ((RenderInfo) i.next());
		}

		// set the attributes of the vehicle
		setObjectAttributes(o,attr);
	
		// add obstacle to the obstaclelist
		m_obstacles.add(o);
		
		
	} // createObstacle

	/**
	* Returns the object description if available
	* @return Geometrie which describes the object
	*/
	private Geometrie createDescription()
	{	System.out.println("     creating description...");
		m_treeWalker.firstChild();
		
		Geometrie description=new Geometrie();
		
		do
		{
			if (m_treeWalker.getCurrentNode().getNodeName().equals("description"))
         		{
         			// build the Description and add it to the root-node
         			DescriptionGenerator descGen=new DescriptionGenerator(m_treeWalker);
         			description=descGen.buildDescription();
         		}
		}
		while (m_treeWalker.nextSibling()!=null);
		
		// walk back
		m_treeWalker.parentNode();
		
		return description;
	
	} // createDescription

	/**
	* Returns the Mind object
	* @return Mind object
	*/
	private Mind createMind()
	{	
		Mind mind=new Mind();
		
		m_treeWalker.firstChild();
		
		do
		{
			String type=m_treeWalker.getCurrentNode().getNodeName();
			if (type.equals("simplemind"))
			{	System.out.println("     creating simplemind...");
				mind=new SimpleMind();
			}		
		}
		while (m_treeWalker.nextSibling()!=null);
		
		// create the behaviorlist
		Vector behaviors=createBehaviors();
		
		// add the behaviors to the mind
		mind.setBehaviors(behaviors);
				
		// walk back
		m_treeWalker.parentNode();
		
		return mind;
	}
	
	/** 
	* Returns a Vector of behaviors
	* @return Vector of behaviors
	*/
	private Vector createBehaviors()
	{	System.out.println("          creating behaviors");
		
		Vector behaviors=new Vector();
		if (m_treeWalker.firstChild()!=null)
		{
			do
			{
				NamedNodeMap attr=m_treeWalker.getCurrentNode().getAttributes();
				String type=m_treeWalker.getCurrentNode().getNodeName();
			
				// create behavior and add to the behaviorlist
				if (type.equals("separation")) 
				{ 	System.out.println("            - separation");
					Separation b=new Separation();  
					setObjectAttributes(b, attr);
					behaviors.add(b);
				}
				
				if (type.equals("alignment")) 
				{ 	System.out.println("            - alignment");
					Alignment b=new Alignment();  
					setObjectAttributes(b, attr);
					behaviors.add(b);
				}
				
				if (type.equals("cohesion")) 
				{ 	System.out.println("            - cohesion");
					Cohesion b=new Cohesion();  
					setObjectAttributes(b, attr);
					behaviors.add(b);
				}
				
				if (type.equals("flocking")) 
				{ 	System.out.println("            - flocking");
					Flocking b=new Flocking();  
					setObjectAttributes(b, attr);
					behaviors.add(b);
				}
				
				if (type.equals("wander")) 
				{ 	System.out.println("            - wander");
					Wander b=new Wander();  
					setObjectAttributes(b, attr); 
					behaviors.add(b);
				}
				if (type.equals("arrive")) 
				{ 	System.out.println("            - arrive");
					Arrive b=new Arrive();  
					setObjectAttributes(b, attr); 
					behaviors.add(b);
				}
				if (type.equals("seek")) 
				{ 	System.out.println("            - seek");
					Seek b=new Seek();  
					setObjectAttributes(b, attr); 
					behaviors.add(b);
				}
				
				if (type.equals("pursuit")) 
				{ 	System.out.println("            - pursuit");
					Pursuit b=new Pursuit();  
					setObjectAttributes(b, attr); 
					behaviors.add(b);
				}
				
				if (type.equals("evade")) 
				{ 	System.out.println("            - evade");
					Evade b=new Evade();  
					setObjectAttributes(b, attr); 
					behaviors.add(b);
				}
				
				if (type.equals("simplepathfollowing")) 
				{ 	System.out.println("            - simplepathfollowing");
					SimplePathfollowing b=new SimplePathfollowing();  
					setObjectAttributes(b, attr);					
					behaviors.add(b);
				}
				
				if (type.equals("obstacleavoidance")) 
				{ 	System.out.println("            - obstacleavoidance");
					ObstacleAvoidance b=new ObstacleAvoidance();  
					setObjectAttributes(b, attr); 
					behaviors.add(b);
				}
			
				if (type.equals("containment")) 
				{ 	System.out.println("            - containment");
					Containment b=new Containment();  
					setObjectAttributes(b, attr); 
					behaviors.add(b);
				}
			
				// ... etc.
			}
			while (m_treeWalker.nextSibling()!=null);
		
			// walk back
			m_treeWalker.parentNode();
		}
		
		// return the behaviorlist
		return behaviors;
	} // createBehaviors

	/**
	* Loads and parses the InputStream and sets the treeWalker
	* @param in InputStream to parse
	*/
	private void loadScene(InputStream in)
	{     
		XMLReader xmlreader = new XMLReader();
		xmlreader.readXMLFile(in);
		m_treeWalker=xmlreader.getTreeWalker();
		
	} // loadScene

    	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////        
	
	/** The parent object */
	Object		m_parent;
	
	/** The neighborhood object */
	Neighborhood	m_neighborhood;
	
	/** The scene width */
	int		m_sceneWidth;
	
	/** The scene height */
	int		m_sceneHeight;
	
	/** The background object */
	Geometrie	m_background;
	
	/** List of all vehicles in the scene */
	Vector		m_vehicles;
	
	/** List of all obstacles in the scene */
	Vector		m_obstacles;

	/** Hashtable of all objects in the scene. Stored with the object name as key */
	Hashtable	m_steeringObjects;
	
	/** The TreeWalker object */
	TreeWalker 	m_treeWalker;

}