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

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.RenderInfo;
import de.steeringbehaviors.simulation.renderer.SteeringRenderer;
import de.steeringbehaviors.simulation.renderer.Tile;


import java.awt.Color;


import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;


import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

/** Implements the canvas in the editor */
public class EditorCanvas extends JComponent
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	 /**
	* Constructor
	* @param h Hashtable of all objects
	* @param tree The SteeringTree
	*/
	public EditorCanvas(Hashtable h, SteeringTree tree)
	{ 
		super();
		
		m_steeringTree=tree;
		
		m_geometrieObjects=h;
		m_Renderer = new SteeringRenderer();
		m_DoubleBuffer = createImage(10,10); 
		
		init();
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
	
	

	/**
	* Sets the background image
	* @param filename The Image object to set as background
	*/
	public void setBackgroundImage(Image background)
	{
		if (background != null)
		{
			m_visibleObjects.remove(m_backgroundImage);
			
			m_backgroundImage = new Geometrie();
			Tile t = new Tile(background);
			m_backgroundImage.setPos(t.getWidth() / 2, t.getHeight() / 2);
			t.setZ(-20);
			m_backgroundImage.addShape(t);
						
			m_visibleObjects.add(m_backgroundImage);
		}
		else m_backgroundImage = null;
	
	} // setBackgroundImage

	/**
	* Sets the width of the canvas
	* @param width Width to set
	*/
	public void setWidth(int width)
	{
		m_width=width;
	}

	/**
	* Sets the height of the canvas
	* @param height Height to set
	*/
	public void setHeight(int height)
	{
		m_height=height;
	}

	/**
	* Sets the AttributeEditor
	* @param aeditor The AttributeEditor
	*/
	public void setAttributeEditor(AttributeEditor aeditor)
	{
		m_attributeEditor=aeditor;
	}
	
	/**
	* Sets the menu manager object
	* @param mm The SteeringMenuManager object for menu-generating
	*/
	public void setMenuManager(SteeringMenuManager mm)
	{
		m_menuManager=mm;
	}

	/**
	* Sets the Hashtable with all geometrie objects
	* @param h Hashtable with all geometrie objects
	*/  	
	public void setGeometrieObjects(Hashtable h)
	{
		m_geometrieObjects=h;
	}

	/**
	* Sets the active object
	* @param obj Geometrie object which should be the active object
	*/  	
	public void setActiveObject(Geometrie obj)
	{	
		// clear all selections
		m_selection.clear();
		deselectObjects();	
		selectObject(obj);
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////

	/**
	* Returns the Renderer
	* @return The used Renderer
	*/
	public SteeringRenderer getRenderer() { return m_Renderer; }
	
	/**
	* Returns the tile object with the background image
	* @return The tile object
	*/
	public Tile getBackgroundImageTile() 
	{ 
		
		if (m_backgroundImage == null) return null;
		
		Iterator i = m_backgroundImage.getShapeIter();
		Tile t = null;
		if (i.hasNext())
		{
			t = (Tile) i.next();
		}
		

		return t;
	}
	
	/**
	* Returns the geometrie object with the background image tile
	* @return The geometrie object
	*/
	public Geometrie getBackgroundImageGeometrie() { return m_backgroundImage; }
	
	
	
	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////

	/**
	* Loads a image and sets it as background
	* @param filename The filename of the image to load
	*/
	public void loadBackgroundImage(String filename)
	{
		Image background = null;
		try
		{
			background = (new ImageIcon(filename)).getImage();
		}
		catch (Exception e) 
		{
			System.out.println("Can't load image " + filename);
		}

		setBackgroundImage(background);
		
	} // loadBackgroundImage
	
	
	
	/** Initializes all member variables, objects and the mouse listener */
	private void init()
	{
		m_geometrieObjects=new Hashtable();	
		m_visibleObjects=new Vector();
		m_selection=new Vector();
		m_velocityNode=new Geometrie();
	
		MouseListener ml = new MouseAdapter() 
		{
			public void mousePressed(MouseEvent event)
			{
				// left mouse button
				//if (event.getModifiers() == event.BUTTON1_MASK)
				if (true)
				{
					Vector clicked = getRenderer().getVisibleObjects(event.getX(), event.getY());
					m_clickedObject=null;
					deselectObjects();
					
					
					if (clicked!=null)
					{
						Iterator it = clicked.iterator();

					
						// reset
						m_mouseOffsetX=m_mouseOffsetY=0;
						
						if (!it.hasNext())
						{
							// if no object was clicked, select the node called "steering"
							m_steeringTree.selectNode(m_steeringTree.getSceneNode());
							repaint();
						}
								
						while (it.hasNext())
						{
							
							RenderInfo r = (RenderInfo) it.next();
							Geometrie geo=r.getParent();
						
							// set the clicked Object
							m_clickedObject=geo;
							
							if ((geo == m_backgroundImage) && (m_backgroundImage != null))
							{
								// select the node called "steering"
							m_steeringTree.selectNode(m_steeringTree.getSceneNode());
							}
							else
							{
					
								// get the difference between the position of the object and the clicked position
								// (in world coordinates)
								m_mouseOffsetX = m_Renderer.screenToWorldX(event.getX()) - geo.getPos().getX();
								m_mouseOffsetY = m_Renderer.screenToWorldY(event.getY()) - geo.getPos().getY();
					
								// select the node in the steeringtree
								m_steeringTree.selectNode((DefaultMutableTreeNode)m_geometrieObjects.get(geo));
				
								// select the Geometrie-Object
								setActiveObject(r.getParent());
							}
							// repaint the components
							repaint();
						}
						
						
						clicked.clear();
					}
					
							
				}
			}
			
			public void mouseReleased(MouseEvent event)
			{
				
				
			
				// if button was a popup trigger
				if (event.isPopupTrigger())
				{
					int selRow = m_steeringTree.getRowForLocation(event.getX(), event.getY());
				

					if(event.getClickCount() == 1) 
					{		
						// show context menu if a menu manager is available
						if (m_menuManager!=null)
						{
							// get the selected node
							//SteeringTreeNode stn=(SteeringTreeNode) ((DefaultMutableTreeNode) treepath.getLastPathComponent()).getUserObject();
							SteeringTreeNode stn= (SteeringTreeNode) m_steeringTree.getSelectedNode().getUserObject();
								JPopupMenu popup=m_menuManager.getTreeContextMenu(stn.getType());
						
							// show a context menu
							popup.show(event.getComponent(),event.getX(),event.getY());
						}
					}
				}
				// update values in the attribute editor e.g. after a movement
				// of a object
				else m_attributeEditor.updateValueView(); 
			}
		};
		addMouseListener(ml); 		
		
		MouseMotionListener mml = new MouseMotionAdapter()
		{
			public void mouseMoved(MouseEvent event)
			{
				// write mouse coordinates info
				((CenterPanel) getParent()).setLabelXY((int) m_Renderer.screenToWorldX(event.getX()), (int) m_Renderer.screenToWorldY(event.getY()));
			}
			
			public void mouseDragged(MouseEvent event)
			{
				if (event.getModifiers() == event.BUTTON1_MASK)   
				{
					// the current selected node
					SteeringTreeNode stn=m_steeringTree.getSelectedSteeringTreeNode();
					
					if (m_clickedObject!=null)
					{
						if((m_clickedObject==stn.getGeometrieObject().getObjectDescription()) ||
						   (m_clickedObject==stn.getGeometrieObject().getVelocityVector()) ||
						   (m_clickedObject==stn.getGeometrieObject().getSelection()))
						{
							stn.setAttribute("x",(new Double( m_Renderer.screenToWorldX(event.getX()) - m_mouseOffsetX )).toString());
							stn.setAttribute("y",(new Double( m_Renderer.screenToWorldY(event.getY()) - m_mouseOffsetY )).toString());
						}
						if(m_clickedObject==stn.getGeometrieObject().getVelocityNode())
						{
							double dx = m_Renderer.screenToWorldX(event.getX()) - stn.getGeometrieObject().getObjectDescription().getPos().getX();
							double dy =  m_Renderer.screenToWorldY(event.getY()) - stn.getGeometrieObject().getObjectDescription().getPos().getY();
						
							stn.setAttribute("velx",(new Double(dx)).toString());
							stn.setAttribute("vely",(new Double(dy)).toString());
						}
						if(m_clickedObject==stn.getGeometrieObject().getRotationNode())
						{
							// calculate the angle
							double dx = m_Renderer.screenToWorldX(event.getX()) - stn.getGeometrieObject().getObjectDescription().getPos().getX();
							double dy = m_Renderer.screenToWorldY(event.getY()) - stn.getGeometrieObject().getObjectDescription().getPos().getY();
							
							double angle=java.lang.Math.atan2( dy, dx );
							
							angle=java.lang.Math.round(angle*1000000000)/1000000000.0;
							
							stn.setAttribute("angle",(new Double(angle)).toString());
							
						}
						if (m_clickedObject==stn.getGeometrieObject().getScaleNode())
						{
							Point2d point=stn.getObjectDescription().worldToLocal(new Point2d(m_Renderer.screenToWorldX(event.getX()), m_Renderer.screenToWorldY(event.getY())));
							
							if (point.getX()>0)
							stn.setAttribute("scalex",new Double(  point.getX()/stn.getGeometrieObject().getFarthestX() ).toString());
							else stn.setAttribute("scalex","0.1");
							
							if (point.getY()>0)
							stn.setAttribute("scaley",new Double(  point.getY()/stn.getGeometrieObject().getFarthestY() ).toString());
							else stn.setAttribute("scaley","0.1");
						}

						// write the object coordinates info
						((CenterPanel) getParent()).setLabelXY(m_clickedObject.getPos().getX(), m_clickedObject.getPos().getY());
						
					}
					// update the geometrieObject
					if (stn.getGeometrieObject()!=null) stn.getGeometrieObject().update();
					repaint();
					
				}
				
				
			}
		};
		addMouseMotionListener(mml);
		
		
		// set special attributes, like background image, scene size ...
		
		// select the steering node
		DefaultMutableTreeNode stnnode = m_steeringTree.getSceneNode();
		SteeringTreeNode stn = (SteeringTreeNode) stnnode.getUserObject();
		
		// set scene size
		try
		{	
			setWidth((int)new Double(stn.getAttribute("width")).doubleValue());
			setHeight((int)new Double(stn.getAttribute("height")).doubleValue());
			resize();
		} catch (Exception e) {}
		
		// set background image
		if (stn.getAttribute("image") != null)
			loadBackgroundImage(stn.getAttribute("image"));
			
		// set grid on/off
		
		if (stn.getAttribute("showgrid").equals("true")) m_Renderer.setShowGrid(true);
		else m_Renderer.setShowGrid(false);
		
		
		
	} // init
	
	/**
	* Resizes the Canvas and the Renderer to the size specified with
	* the methods setWidth() and setHeight()
	*/
	public void resize()
	{	
		if ((getSize().getWidth()!=m_width) || (getSize().getHeight()!=m_height))
		{
			// update dimension
			this.setPreferredSize(new Dimension(m_width, m_height));
			
			if (m_width != m_Renderer.getSceneWidth()) 
			{
				m_Renderer.setSceneWidth(m_width);
				//m_Renderer.setScreenWidth(m_width);
			}
			
			if (m_height != m_Renderer.getSceneHeight()) 
			{
				m_Renderer.setSceneHeight(m_height);
				//m_Renderer.setScreenHeight(m_height);
			}
			
			// validate the component
			revalidate();	
			
			getParent().repaint();	
		}
	}
	
	/** 
	* Paints the canvas 
	* @param g The Graphic context
	*/
	public void paint(Graphics g)
	{	
		// set Background color
		g.setColor(Color.white);
		g.fillRect(0,0,(int)(m_Renderer.getSceneWidth() * m_Renderer.getZoom()),(int)(m_Renderer.getSceneHeight() * m_Renderer.getZoom()));
		
		Iterator i=m_visibleObjects.iterator();	
		m_Renderer.renderScene(i, g);
	}
	
	/**
	* Generates all descriptions of the objects from the spzified SteeringTree
	* @param steeringtree SteeringTree from which the descriptions should be generated
	*/
	public void updateDescriptions(SteeringTree steeringtree)
	{
		m_geometrieObjects.clear();
		m_visibleObjects.clear();
		
		DefaultMutableTreeNode node=(DefaultMutableTreeNode)steeringtree.getModel().getRoot();
		
		do
		{	
			try
			{
				SteeringTreeNode steeringtreenode=(SteeringTreeNode) node.getUserObject();
				steeringtreenode.getGeometrieObject().update();
				
				// put the visible objects in the hashtable to find out which
				// nodes belongs to the geometriobj
				Iterator it=steeringtreenode.getGeometrieObject().getVisibleDescriptions().iterator();
				
				while (it.hasNext())
				{ m_geometrieObjects.put(it.next(),node); }
				
				
				m_visibleObjects.addAll(steeringtreenode.getGeometrieObject().getVisibleDescriptions());

			} catch (Exception e) {  }
		}
		while ((node=node.getNextNode())!=null);
		
		// add backgroundImage to the visible Objects
		if (m_backgroundImage != null) m_visibleObjects.add(m_backgroundImage);
		
	} // updateDescriptions

	/** Deselects all objects */
	public void deselectObjects()
	{
		Iterator it=m_geometrieObjects.keySet().iterator();
		while (it.hasNext())
		{
			// deselect this GeometrieObject
			((SteeringTreeNode)((DefaultMutableTreeNode)m_geometrieObjects.get((Geometrie) it.next())).getUserObject()).getGeometrieObject().setSelected(false);
		}
	}


	/**
	* Selects a Geometrie object
	* @param obj Geometrie to select
	*/
	public void selectObject(Geometrie obj)
	{
		if (obj != null)
		{
			try
			{
				// get SteeringTreeNode
				SteeringTreeNode stn=(SteeringTreeNode)((DefaultMutableTreeNode)m_geometrieObjects.get(obj)).getUserObject();
		
				stn.getGeometrieObject().setSelected(true);
				updateDescriptions(m_steeringTree);
				stn.getGeometrieObject().update();
				repaint();
			} 
			catch (Exception e) {}
		}
	} // selectObject  	
	
	
	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////    
	
	/** Hashtable with all objects */
	protected Hashtable 		m_geometrieObjects;
	
	/** List of all selected objects */
	protected Vector		m_selectedObjects;
	
	/** List of all visible objects */
	protected Vector		m_visibleObjects;
	
	/** The menu manager object if necessary */
	protected SteeringMenuManager	m_menuManager;
	
	/** List of descriptions of all selection objects */
	protected Vector		m_selection;
	
	/** Geometrie which contains a tile with the background image */
	protected Geometrie		m_backgroundImage = null;
	
	/** Geometrie which describes the current selected object */
	protected Geometrie		m_currentObject;
	
	/** Geometrie which describes the velocity node */
	protected Geometrie		m_velocityNode;
	
	/** Geometrie which describes the last clicked object */
	protected Geometrie		m_clickedObject;
	
	/** The SteeringTree */
	protected SteeringTree		m_steeringTree;
	
	/** The AttributeEditor */
	protected AttributeEditor	m_attributeEditor;
	
	/** The SteeringRenderer */
	protected SteeringRenderer	m_Renderer;
	
	/** Image used for double buffering */
	protected Image			m_DoubleBuffer;
	
	/** Graphic context */
	protected Graphics		m_dbGraphics;
	
	/** X disctance of the mouse pointer to the object position */
	private double			m_mouseOffsetX;
	
	/** Y distance of the mouse pointer to the object position */
	private double			m_mouseOffsetY;  	

	/** Width of the canvas */
	private int			m_width=640;
	
	/** Height of the canvas */
	private int			m_height=480;
}