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
package de.steeringbehaviors.creator.editor;

import java.awt.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.TreeWalker;

import de.steeringbehaviors.simulation.xml.XMLReader;



/** Generates a SteeringBehavior object described in the standard objects.xml file  */
public class ObjectGenerator
{	
	
	private static final String OBJECT_XML = "de.steeringbehaviors.creator.config.objects";
	
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Constructor
	* @param parent Parent component
	*/
	public ObjectGenerator(Component parent)
	{
		m_parent=parent;
	
		// parse XML-file and create a TreeWalker
		XMLReader xmlReader=new XMLReader();
		
		ClassLoader cl = ClassLoader.getSystemClassLoader();

		String objectConfigFile = OBJECT_XML.replace(".",File.separator)+".xml";
		URL objectsURL = cl.getResource(objectConfigFile);

		
		
		InputStream iStream;
		try {
			iStream = objectsURL.openStream();
			xmlReader.readXMLFile(iStream);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		m_treeWalker=xmlReader.getTreeWalker();
		
		// store the root node
		m_root=m_treeWalker.getCurrentNode();    	
		
		// step over the the first child
		m_treeWalker.firstChild();
		m_treeWalker.firstChild();
		
		// search the root nodes of the object types
		do
		{	
			if (m_treeWalker.getCurrentNode().getNodeName().equals("vehicles"))
			{ 	m_vehicleRoot=m_treeWalker.getCurrentNode();  }
			
			if (m_treeWalker.getCurrentNode().getNodeName().equals("obstacles"))
			{	m_obstacleRoot=m_treeWalker.getCurrentNode(); }
			
			if (m_treeWalker.getCurrentNode().getNodeName().equals("minds"))
			{	m_mindRoot=m_treeWalker.getCurrentNode(); }
			
			if (m_treeWalker.getCurrentNode().getNodeName().equals("behaviors"))
			{	m_behaviorRoot=m_treeWalker.getCurrentNode(); }

		}
		while(m_treeWalker.nextSibling()!=null);

		createHints();

	} // objectGenerator
	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Adds a vehicle object into the tree
	* @param tree SteeringTree to which the vehicle should be added
	* @return New TreeNode
	*/
	public DefaultMutableTreeNode addVehicle(SteeringTree tree)
	{ 
		return addObject(tree, m_vehicleRoot); 
	}
	
	/** Adds a mind object into the tree
	* @param tree SteeringTree to which the mind should be added
	* @return New TreeNode
	*/
	public DefaultMutableTreeNode addMind(SteeringTree tree)
	{ 
		return addObject(tree, m_mindRoot); 
	}
	
	/** Adds a obstacle object into the tree
	* @param tree SteeringTree to which the obstacle should be added
	* @return New TreeNode
	*/
	public DefaultMutableTreeNode addObstacle(SteeringTree tree)
	{ 
		return addObject(tree, m_obstacleRoot); 
	}
	
	/** Adds a behavior object into the tree
	* @param tree SteeringTree to which the behavior should be added
	* @return New TreeNode
	*/
	public DefaultMutableTreeNode addBehavior(SteeringTree tree)
	{ 
		return addObject(tree, m_behaviorRoot); 
	}
	
	/** 
	* Adds a new object into the tree. If more objects are
	* available, a dialog asks which object to add
	* @tree SteeringTree to which the object should be added
	* @objectroot Root node of the subtree with objects
	* @return New TreeNode
	*/
	private DefaultMutableTreeNode addObject(SteeringTree tree, Node objectroot)
	{
		m_treeWalker.setCurrentNode(objectroot);
		
		// create a list of all objects
		Hashtable objectList = new Hashtable();
		
		m_treeWalker.firstChild();
		Node objectnode = m_treeWalker.getCurrentNode();
		do
		{	
			Node node=m_treeWalker.getCurrentNode();
			
			String key=node.getNodeName();
			
			// if element has a name use the name else use the nodename
			try { key = node.getAttributes().getNamedItem("name").getNodeValue();}
			catch (Exception e) { key = node.getNodeName();   }
			
			objectList.put(key, node);
		}
		while(m_treeWalker.nextSibling()!=null);
		
		// show input dialog only if number of objects>1
		if (objectList.size()>1)
		{
			Object[] entries = objectList.keySet().toArray();
			
			String ret = (String) JOptionPane.showInputDialog(
				m_parent, "Choose object to add", "Add object", JOptionPane.QUESTION_MESSAGE,
				null, entries, entries[0]);
			if (ret!=null)
			{
				objectnode=(Node) objectList.get(ret);
			}
			else return null;	
		}
		
		// now set the current treewalker-node to the selected one
		m_treeWalker.setCurrentNode(objectnode);
		
		DefaultMutableTreeNode treeNode=tree.getSelectedNode();
		
		// get the model
		DefaultTreeModel treeModel=(DefaultTreeModel) tree.getModel();
		
		SteeringTreeNode stn = new SteeringTreeNode(m_treeWalker.getCurrentNode());
		DefaultMutableTreeNode newNode=new DefaultMutableTreeNode(stn);
		
		SteeringTree newsubtree = new SteeringTree(newNode,m_treeWalker);
		
		//DefaultMutableTreeNode newNode=new DefaultMutableTreeNode(stn);
		treeModel.insertNodeInto(newNode, treeNode, treeNode.getChildCount() );
		
		// select the new node
		TreeNode[] path = treeModel.getPathToRoot(newNode);
		tree.setSelectionPath(new TreePath(path));
		
		return newNode;
	
	} // addObject	
	
	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Return the hint of an object
	*/
	public String getHint(String name)
	{
		return (String) m_hints.get(name);
	} // getHint
	
	
	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** 
	* Creates the hint to each object
	*/
	protected void createHints()
	{
		m_hints = new Hashtable();
		
		m_treeWalker.setCurrentNode(m_root);
		do
		{	
			if (m_treeWalker.getCurrentNode().getNodeName().equals("hint"))
			{	
				// store the treeWalker position
				Node temp = m_treeWalker.getCurrentNode();
				NodeList nodelist = temp.getChildNodes();
				
				String hint = new String();
				
				if (nodelist.getLength()>0)
				{
					if (nodelist.item(0).getNodeName().equals("#text"))
						hint = nodelist.item(0).getNodeValue();
				}
								
				// get the name of the parent node
				m_treeWalker.parentNode();
				String nodename = m_treeWalker.getCurrentNode().getNodeName();
				
				// set the old node
				m_treeWalker.setCurrentNode(temp);
				
				// put this hint into the hashtable
				m_hints.put(nodename, hint);
			}
		}
		while (m_treeWalker.nextNode() != null);
		
	} // createHints
	
	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////      

	/** Root node of the objects-file */
	Node	m_root;
	/** Root node of the obstacles subtree */
	Node	m_obstacleRoot;
	/** Root node of the minds subtree */
	Node	m_mindRoot;
	/** Root node of the behaviors subtree */
	Node	m_behaviorRoot;
	/** Root node of the vehicles subtree */
	Node	m_vehicleRoot;
	/** SteeringTree of standard objects */
	SteeringTree	m_objectTree;
	/** Parent component */
	Component	m_parent;
	/** The TreeWalker object */
	TreeWalker	m_treeWalker;
	/** Hashtable of the hints */
	Hashtable 	m_hints;
	
	

}