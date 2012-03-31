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

import java.awt.AWTEvent;
import java.util.TreeSet;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.w3c.dom.Node;
import org.w3c.dom.traversal.TreeWalker;

import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.xml.DescriptionGenerator;
import de.steeringbehaviors.simulation.xml.XMLReader;



/**
*   class SteeringTree
*
*   Implements a browseable tree of the steering model
*/
public class SteeringTree
extends JTree

{
		
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Constructor */
	public SteeringTree()
	{
		super();
	}

	/**
	* Constructor
	* @param root Root node of the tree
	*/    	
	public SteeringTree(DefaultMutableTreeNode root)
	{
		super(root);
		m_root=root;
	}

	/**
	* Constructor
	* @param root Root node of the subtree
	* @param treeWalker TreeWalker object
	*/    	
	public SteeringTree(DefaultMutableTreeNode root, TreeWalker treeWalker)
	{
		super(root);
		
		m_root=root;
		m_treeWalker=treeWalker;
		createTree();
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}


	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Return the scene node called "steering"
	* @return The scene node
	*/
	public DefaultMutableTreeNode getSceneNode()
	{
		return (DefaultMutableTreeNode) getRootNode().getFirstChild();
	}
	
	/**
	* Returns the selected tree node
	* @return The selected tree node
	*/
	public DefaultMutableTreeNode getSelectedNode()
	{
		// look which object is selected
		TreePath tp=getSelectionPath();
		DefaultMutableTreeNode treeNode=(DefaultMutableTreeNode) tp.getLastPathComponent();
		return treeNode;
	} 
	
	/**
	* Return the root node
	* @return The root node
	*/
	public DefaultMutableTreeNode getRootNode()
	{
		return m_root;
	}
	
	/**
	* Returns the selected tree node
	* @return The selected SteeringTreeNode
	*/
	public SteeringTreeNode getSelectedSteeringTreeNode()
	{
		return (SteeringTreeNode) getSelectedNode().getUserObject();
	}

	

	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** 
	* Sets a new object name and checks if the name is already available.
	* In that case, a number will be added to the object name
	* @param treenode TreeNode which name should be changed
	* @param name New name of the object
	*/
	public void setNewName(DefaultMutableTreeNode treenode, String name)
	{
		String newname=name;
		TreeSet set=new TreeSet();
		SteeringTreeNode stn=(SteeringTreeNode) treenode.getUserObject();
		
			DefaultMutableTreeNode node=(DefaultMutableTreeNode) getModel().getRoot();
			
			do
			{	
				try
				{
					SteeringTreeNode steeringtreenode=(SteeringTreeNode) node.getUserObject();
				
					if (((steeringtreenode.getType().equals("vehicle") ||
					steeringtreenode.getType().equals("obstacle") ) ) &&
					(steeringtreenode!=stn))
							set.add(steeringtreenode.getCaption());

				} catch (Exception e) {}
			}
			while ((node=node.getNextNode())!=null);
		
		// check if name is already available
		if (set.contains(newname))
		{	
			int i=0;
			do
			{	
				i++;
				newname = name + i;
			}
			while (set.contains(newname));
		}
		
		// set the name attribute and the caption
		stn.setAttribute("name",newname);
		stn.setCaption(newname);
		
		// update the treeview
		((DefaultTreeModel) getModel()).nodeChanged(treenode);
	} // setNewName
	
	/**
	* Removes all nodes
	*/
	public void empty()
	{
		// delete the tree
		m_root.removeAllChildren();
	}

	/** 
	* Creates a new tree
	* @param treeWalker The TreeWalker object used to build the new tree
	*/
	public void createNew(TreeWalker treeWalker)
	{
		m_treeWalker=treeWalker;
		// delete the tree
		empty();
		// create a new tree
		createTree();
	}
	
	/**
	* Creates the tree
	*/	
	private void createTree()
	{
		try
		{
			m_node=m_treeWalker.getCurrentNode();		  		
		
			// build the tree from the root ----------------
			buildTree(m_root);
		
			// set a custum cell-renderer -------------------
			setCellRenderer(new SteeringCellRenderer());
			setRootVisible(true);
	
		} //try
		catch (Exception e)
		{ 
			System.out.println("Error: CreateTree:"+e.getMessage());
			e.printStackTrace();
		}
		
		
		// select the first node
		if (m_root.getChildCount()>0)
		{
			DefaultTreeModel treeModel = (DefaultTreeModel) getModel();
			TreeNode[] path = treeModel.getPathToRoot(m_root.getFirstChild());
			setSelectionPath(new TreePath(path));
			expandPath(new TreePath(path));	
		}
	
	}

	/**
	* Build a subtree from the specified node
	* @param root Root node of the subtree
	*/
	private void buildTree(DefaultMutableTreeNode root)
	{		
		DefaultMutableTreeNode child;
	
		// get the first child of the node
		if (m_treeWalker.firstChild()!=null)
		{
			do
			{       m_node=m_treeWalker.getCurrentNode();
				//if (m_node.getNodeType()==Node.ELEMENT_NODE)
				
				// check if node contains description-infos for the renderer
				if (m_node.getNodeName().equals("description"))
				{
					
					// build the Description and add it to the root-node
					DescriptionGenerator descGen=new DescriptionGenerator(m_treeWalker);
					Geometrie description=descGen.buildDescription();
					((SteeringTreeNode) root.getUserObject()).getGeometrieObject().setObjectDescription(description);
				}
				else if (m_node.getNodeName().equals("hint"))
				{}
				else
				{
				
					String nodecaption=new String();

					// create a SteeringTreeNode-instance
					
					SteeringTreeNode stn=new SteeringTreeNode(m_node);
					
					// create a new child which contains the new SteeringTreeNode
					child = new DefaultMutableTreeNode(stn);

					// add child and build the subtree ------------------------
					root.add(child);
					
					// build the subtree of this node - (it's a recursive call) -------
					buildTree(child);
					// m_treeWalker.setCurrentNode(m_node);
			}
			}
			while(m_treeWalker.nextSibling()!=null);
			
			// go back to the parent -----------------
			m_treeWalker.parentNode();
		}
	
	} 

	/**
	* Selects a node from the tree
	* @param node Node to select
	*/
	public void selectNode(DefaultMutableTreeNode node)
	{
		DefaultTreeModel treemodel=(DefaultTreeModel) getModel();
		TreeNode[] path = treemodel.getPathToRoot(node);
		if (path!=null)	setSelectionPath(new TreePath(path));
	}
	
	
	/**
	* Clones a subtree
	* @param source Source node to clone
	* @param destination Node to add the cloned node
	*/
	public DefaultMutableTreeNode cloneSubTree(DefaultMutableTreeNode source, DefaultMutableTreeNode destination)
	{
		// get the source and the destination SteeringTreeNode
		SteeringTreeNode sourceStn = (SteeringTreeNode) source.getUserObject();
		SteeringTreeNode destStn = (SteeringTreeNode) sourceStn.clone();
		
		// create the new node
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(destStn);
		
		// choose a new name
		setNewName(newNode,  destStn.getCaption());
		
		// insert node into the tree	
		((DefaultTreeModel) getModel()).insertNodeInto(newNode, destination, destination.getChildCount() );
		
		// recursive call for each child
		if (source.getChildCount() > 0)
		{
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) source.getFirstChild();
			do
			{
				cloneSubTree(child, newNode);
			}
			while ((child = (DefaultMutableTreeNode) source.getChildAfter(child)) != null);
		}
		
		return newNode;
	} // cloneSubTree
	

	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////
	
	//* TreeWalker object used to create the tree */
	TreeWalker	m_treeWalker;
	//* Used to store the current position of the TreeWalker */
	Node		m_node;
	//* XMLReader used to load the xml file which contains the data */
	XMLReader	m_xmlReader;
	//* The root node of the JTree */
	DefaultMutableTreeNode	m_root;
} 