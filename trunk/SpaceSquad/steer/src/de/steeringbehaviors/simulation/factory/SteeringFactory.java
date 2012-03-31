package de.steeringbehaviors.simulation.factory;

import org.apache.xerces.parsers.*;
import org.w3c.dom.*;
import org.w3c.dom.traversal.*;

import de.steeringbehaviors.simulation.xml.XMLReader;



import java.util.Vector;
import java.io.*;

class SteeringFactory
{
	Vector		m_vehicles;
	Vector		m_obstacles;
	TreeWalker 	m_treeWalker;
	
	
	public void setVehicles(Vector vehicles)
	{
		m_vehicles=vehicles;
	}

	public void setObstacles(Vector obstacles)
	{
		m_obstacles=obstacles;
	}
	
	public void createScene(String filename)
	{
		// load the specified xml-file
		loadScene(filename);
		
		m_treeWalker.firstChild();
		
		// check if xml-file is a steering model
		if (!m_treeWalker.getCurrentNode().getNodeName().equals("steering")) return;
		
		m_treeWalker.firstChild();
		do
		{
			System.out.println("Creating a " + m_treeWalker.getCurrentNode().getNodeName());
			if (m_treeWalker.getCurrentNode().getNodeName().equals("vehicle")) createVehicle();
			
		}
		while (m_treeWalker.nextSibling()!=null);
	
	} // createScene
	
	public void createVehicle()
	{
		// get the attributes
		NamedNodeMap attr=m_treeWalker.getCurrentNode().getAttributes();
		
		// create a vehicle set its attributes
		// Vehicle v=new Vehicle();
		setVehicleAttributes(attr);
		
		
		m_treeWalker.firstChild();
		// later check here the type of the mind
		
		//Vector bahaviors=createBehaviors();
	
		
	} // createVehicle

	
	private void createBehaviors()
	{
		m_treeWalker.firstChild();
		Vector behaviors=new Vector();
		do
		{
			System.out.println("            - Adding behavior " + m_treeWalker.getCurrentNode().getNodeName());
			
			NamedNodeMap attr=m_treeWalker.getCurrentNode().getAttributes();
			String name=m_treeWalker.getCurrentNode().getNodeName();
			
			//if (name.equals("seek")) { Seek s=new Seek(); setSeekAttributes(s, attr); }
			// ... etc.
		//	setAttributes(attr);
			
		}
		while (m_treeWalker.nextSibling()!=null);
	}


	private void setVehicleAttributes(NamedNodeMap attr)
	{
		for (int i=0; i<=attr.getLength()-1; i++)
		{
			Node node=attr.item(i);
			String name=node.getNodeName();
			String value=node.getNodeValue();
			
			// if (name.equals("name")) v.setName(value);
			// .... 
			// etc.
			
			System.out.println("     " + name +" = "+value);
		
		}	
		
		
	} // setVehicleAttributes

	private void loadScene(String filename)
	{
		XMLReader xmlreader = new XMLReader();
		xmlreader.readXMLFile("",filename);
		m_treeWalker=xmlreader.getTreeWalker();
	} // loadScene


}