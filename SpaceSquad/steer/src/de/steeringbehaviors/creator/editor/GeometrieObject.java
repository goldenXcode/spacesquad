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

import java.util.*;
import java.awt.Color;

import de.steeringbehaviors.simulation.renderer.Circle;
import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.renderer.InfoBox;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.PolygonShape;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.renderer.VectorShape;



/** class GeometrieObject Implements all the functions for a viewable object in the editor canvas */
public class GeometrieObject
{

	
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Constructor
	* @param type Type of the object
	*/
	GeometrieObject(int type)
	{
		// vehicle or obstacle
		m_type=type;
		
		// init objects and variables
		init();
	} // GeometrieObject


	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////

	/**
	* Sets the description of the object
	* @param description Geometrie which describes the object
	*/
	public void setObjectDescription(Geometrie description)
	{
		m_objectDescription=description;
		
		// calculate the farthes x and y value
		m_farthestX = 0;
		m_farthestY = 0;
		
		Iterator shapeiter = m_objectDescription.getShapeIter();
		while (shapeiter.hasNext())
		{
			Object shape = shapeiter.next();
			if (shape.getClass().isInstance(new PolygonShape()))
			{	
				PolygonShape poly = (PolygonShape) shape;
				Iterator i = poly.getPoints();
				while (i.hasNext())
				{
					Point2d p = (Point2d) i.next();
					if (p.getX() > m_farthestX) m_farthestX = p.getX();
					if (p.getY() > m_farthestY) m_farthestY = p.getY();
				}
			}
		}
		
		// set the scale node components to the farthest point of the shape
		m_scaleNodeX=m_farthestX*m_scaleX;
		m_scaleNodeY=m_farthestY*m_scaleY;
		

		
	} // setObjectDescription
	
	/**
	* Sets the object selected
	* @param select Selection of the object
	*/
	public void setSelected(boolean select)
	{
		
		if (m_type == VEHICLE)
		{
			m_selected=select;
			if (select) 
			{	
				m_velocityVector.setVisible(true);
				m_selection.setVisible(true);
				m_velocityNode.setVisible(true);
				m_velocityVectorShape.setColor(Color.blue);
			}
			else 
			{	
				m_velocityVector.setVisible(true);
				m_selection.setVisible(false);
				m_velocityNode.setVisible(false);
				m_velocityVectorShape.setColor(Color.gray);
			}
		} // if vehicle
		
		
		if (m_type == OBSTACLE)
		{	m_selected=select;
			if (select)
			{
				m_velocityVector.setVisible(false);
				m_selection.setVisible(true);
				m_scaleNode.setVisible(true);
				m_rotationNode.setVisible(true);
				m_xAxis.setVisible(true);
				m_yAxis.setVisible(true);
				m_xAxisLabel.setVisible(true);
				m_yAxisLabel.setVisible(true);
			}
			else // false
			{
				m_velocityVector.setVisible(false);
				m_selection.setVisible(false);
				m_scaleNode.setVisible(false);
				m_rotationNode.setVisible(false);
				m_xAxis.setVisible(false);
				m_yAxis.setVisible(false);
				m_xAxisLabel.setVisible(false);
				m_yAxisLabel.setVisible(false);
				
			}
			
		} // if obstacle
		
	}
	
	/**
	* Sets an attribute
	* @param name Name of the attribute
	* @param value Value of the attribute
	*/
	
	public void setAttribute(String name, String value)
	{
		double val=0;
		
		// convert string to double
		try
		{
			val=(new Double(value)).doubleValue();
			
		} catch (Exception e) {}
		
		if (name.equals("x")) {	m_x=val;	}
		if (name.equals("y")) { m_y=val;	}
		if (name.equals("velx")) { m_velX=val;	}
		if (name.equals("vely")) { m_velY=val;	}
		
		
		// now, set the attributes		
		if (name.equals("angle"))  
		{ 
			m_localX=java.lang.Math.cos(val);
			m_localY=java.lang.Math.sin(val);
		
		}
		
		if (name.equals("scalex")) 
		{ 
			if (m_scaleX>=0) m_scaleX=val; 
			else if (m_scaleX<0) m_scaleX=-val;
			m_scaleNodeX=(val)*m_farthestX; 	
		}
		
		if (name.equals("scaley")) 
		{ 
			if (m_scaleY>=0) m_scaleY=val; 
			else if (m_scaleY<0) m_scaleY=-val;
			
			m_scaleNodeY=(val)*m_farthestY; 	
		}
	}
	

	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////

	/** 
	* Returns the x value of the farthest x point of the shape
	* @return The farthest x value
	*/
	public double getFarthestX()		{ return m_farthestX;		}
	
	/** 
	* Returns the y value of the farthest y point of the shape
	* @return The farthest y value
	*/
	public double getFarthestY()		{ return m_farthestY;		}
	
	
	/**
	* Returns the selection geometrie
	* @return Geometrie of the selection object
	*/
	public Geometrie getSelection()		{ return m_selection;		}
	
	/**
	* Returns the description of the object
	* @return Geometrie which describes the main object
	*/
	public Geometrie getObjectDescription()	{ return m_objectDescription;	}	
	
	/**
	* Returns the description of the velocity node
	* @return Geometrie which describes the velocity node
	*/
	public Geometrie getVelocityNode()	{ return m_velocityNode;	}
	
	/**
	* Returns the description of the velocity vector
	* @return Geometrie which describes the velocity vector
	*/
	public Geometrie getVelocityVector()	{ return m_velocityVector;	}
	
	/**
	* Returns the description of the scale node
	* @return Geometrie which describes the scale node
	*/
	public Geometrie getScaleNode()		{ return m_scaleNode;		}
	
	/**
	* Returns the description of the rotation node
	* @return Geometrie which describes the rotation node
	*/
	public Geometrie getRotationNode()	{ return m_rotationNode;	}
	
	
	/**
	* Returns all actually visible descriptions
	* @return Vector with all descriptions of the visible objects
	*/
	public Vector getVisibleDescriptions()
	{
		Vector v=new Vector();
		v.add(m_objectDescription);
		v.add(m_selection);
		v.add(m_velocityNode);
		v.add(m_velocityVector);
		v.add(m_scaleNode);
		v.add(m_rotationNode);
		v.add(m_xAxis);
		v.add(m_yAxis);
		v.add(m_xAxisLabel);
		v.add(m_yAxisLabel);

		return v;
	}



	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////

	/**
	* Clones the GeometrieObject object
	* @return The cloned GeometrieObject
	*/
	public Object clone()
	{
		Object gobj = new GeometrieObject(m_type);
		
		
		// crate a new geometrie
		Geometrie geo = new Geometrie();
				
		// clone all Points
		Iterator shapeiter = m_objectDescription.getShapeIter();
		while (shapeiter.hasNext())
		{
			Object shape = shapeiter.next();
			if (shape.getClass().isInstance(new PolygonShape()))
			{	
				PolygonShape oldPoly = (PolygonShape) shape;
				PolygonShape poly = new PolygonShape();
				Iterator i = ((PolygonShape) shape).getPoints();
				while (i.hasNext())
				{
					Point2d p = (Point2d) i.next();
					poly.addPoint( new Point2d(p.getX(),p.getY()) );
				}
				poly.setZ(oldPoly.getZ());
				poly.setFilled(oldPoly.getFilled());
				geo.addShape(poly);
			}
		}
		
		((GeometrieObject)gobj).setObjectDescription(geo);
		
		return gobj;
	}

	/**
	* Initialize all member variables and objects
	*/
	private void init()
	{
		m_velX=0;
		m_velY=0;
		m_x=0;
		m_y=0;
		m_selected=false;
		m_localX=0;
		m_localY=0;
		m_scaleX=1;
		m_scaleY=1;
				
		m_scaleNodeX=20;
		m_scaleNodeY=20;
		
		m_objectDescription=new Geometrie();

		// create the velocity node
		m_velocityNode=new Geometrie();
		PolygonShape ps = new PolygonShape();
		ps.addPoint(new Point2d(-4.0, -4.0));
		ps.addPoint(new Point2d(4.0, -4.0));
		ps.addPoint(new Point2d(4.0, 4.0));
		ps.addPoint(new Point2d(-4.0, 4.0));
		ps.setFilled(false);
		ps.setColor(Color.red);
		m_velocityNode.addShape(ps);
		m_velocityNode.setVisible(false);
		ps.setZ(2);
				
		// create the scale node
		m_scaleNode=new Geometrie();
		ps = new PolygonShape();
		ps.addPoint(new Point2d(-4.0, -4.0));
		ps.addPoint(new Point2d(4.0, -4.0));
		ps.addPoint(new Point2d(4.0, 4.0));
		ps.addPoint(new Point2d(-4.0, 4.0));
		ps.setFilled(false);
		ps.setColor(Color.blue);
		m_scaleNode.addShape(ps);
		m_scaleNode.setVisible(false);
		ps.setZ(200);
		
		// create the rotation node
		m_rotationNode=new Geometrie();
		Circle c = new Circle(5,false);
		c.setColor(Color.red);
		ps=new PolygonShape();
		ps.addPoint(new Point2d(-2,-9));
		ps.addPoint(new Point2d(4,-5));
		ps.addPoint(new Point2d(-2,2));
		ps.setFilled(true);
		ps.setColor(Color.red);

		m_rotationNode.addShape(c);
		m_rotationNode.addShape(ps);
		m_rotationNode.setVisible(false);
		c.setZ(200);
		ps.setZ(2);
				
		// create the velocity vector
		m_velocityVector=new Geometrie();
		m_velocityVectorShape=new VectorShape();
		m_velocityVectorShape.setColor(Color.gray);
		m_velocityVectorShape.setZ(0);
		m_velocityVector.addShape(m_velocityVectorShape);
		
		
		// create the x-axis
		m_xAxis = new Geometrie();
		
		m_xAxisShape = new VectorShape(new Vector2d(1,0));
		m_xAxisShape.setColor(Color.gray);
		m_xAxisShape.setZ(100);
		m_xAxis.addShape(m_xAxisShape);
		
		// create the y-axis
		m_yAxis = new Geometrie();
		m_yAxisShape = new VectorShape(new Vector2d(0,1));
		m_yAxisShape.setColor(Color.gray);
		m_yAxisShape.setZ(100);
		m_yAxis.addShape(m_yAxisShape);
		
		// create the x-axis label
		m_xAxisLabel = new Geometrie();
		m_xAxisLabelShape = new InfoBox(new Point2d(0,0), "x");
		m_xAxisLabelShape.setColor(Color.gray);
		m_xAxisLabelShape.setZ(100);
		m_xAxisLabel.addShape(m_xAxisLabelShape);
		
		
		// create the y-axis label
		m_yAxisLabel = new Geometrie();
		m_yAxisLabelShape = new InfoBox(new Point2d(0,0), "y");
		m_yAxisLabelShape.setColor(Color.gray);
		m_yAxisLabelShape.setZ(100);
		m_yAxisLabel.addShape(m_yAxisLabelShape);
		
		// create a selection
		m_selection = new Geometrie();		
		ps = new PolygonShape();
		// a rectangle
		ps.addPoint(new Point2d(-1.0, -1.0));
		ps.addPoint(new Point2d(1.0, -1.0));
		ps.addPoint(new Point2d(1.0, 1.0));
		ps.addPoint(new Point2d(-1.0, 1.0));
		ps.setFilled(false);
		ps.setZ(0);
		m_selection.addShape(ps);
		m_selection.setVisible(false);
	} // init


	/**
	* Updates all objects, its descriptions and attributes
	*/
	public void update()
	{
		m_objectDescription.setPos(m_x,m_y);
		m_selection.setPos(m_x,m_y);
		
		
		if (m_type == VEHICLE)
		{
		
			m_objectDescription.setLocalx(new Vector2d(m_velX, m_velY));
			
			m_velocityNode.setPos(m_x + m_velX, m_y + m_velY);
		
			// velocity vector
			m_velocityVectorShape.setVector(new Vector2d(m_velX,m_velY));
			m_velocityVector.setPos(m_x,m_y);
		}
		
		if (m_type == OBSTACLE)
		{
			int x=(int)(m_x+m_localX*30);
			int y=(int)(m_y+m_localY*30);
			m_rotationNode.setPos(x,y);
			
			
			
			//
			//Point2d scaleNodePos=new Point2d(m_x+m_scaleNodeX,m_y+m_scaleNodeY);
			Point2d scaleNodePos=new Point2d(m_scaleNodeX,m_scaleNodeY);
			scaleNodePos=m_objectDescription.localToWorld(scaleNodePos);
			m_scaleNode.setPos(scaleNodePos.getX(), scaleNodePos.getY());
			
			// scale
			//Point2d scale=m_scaleNode.getPos();
			//m_objectDescription.worldToLocal(scale);
			
			m_objectDescription.setScaleX(m_scaleX);
			m_objectDescription.setScaleY(m_scaleY);
			
			//m_selection.add(m_objectDescription.getBoundingBox());
			// scale selection (later getOutline... if implemented)
			//m_selection.setScaleX(m_objectDescription.getRadius());			
			
			
			// update the axis geometrie
			Vector2d newLocalx = new Vector2d(m_localX, m_localY);
			m_objectDescription.setLocalx(newLocalx);
			m_xAxis.setLocalx(newLocalx);
			m_yAxis.setLocalx(newLocalx);
			m_xAxisLabel.setLocalx(newLocalx);
			m_yAxisLabel.setLocalx(newLocalx);
			
			m_xAxisShape.setLength(m_farthestX + (m_scaleX * m_farthestX) + 10);
			m_yAxisShape.setLength(m_farthestY + (m_scaleY * m_farthestY) + 10);	
			m_xAxis.setPos(m_x,m_y);
			m_yAxis.setPos(m_x,m_y);
			m_xAxisLabel.setPos(m_x,m_y);
			m_yAxisLabel.setPos(m_x,m_y);
			m_xAxisLabelShape.setRelPos(new Point2d(m_farthestX + (m_scaleX * m_farthestX) + 17,2));
			m_yAxisLabelShape.setRelPos(new Point2d(-2,m_farthestY + (m_scaleY * m_farthestY) + 17));
			
		}
	}


	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////        
	
	/** Geometrie which describes the object */
	protected Geometrie 	m_objectDescription;
	
	/** Geometrie which describes the selection box */
	protected Geometrie	m_selection;
	
	/** x-position of the object */
	protected double	m_x;
	
	/** y-position of the object */
	protected double	m_y;
	
	/** Flag to set the object selected */
	protected boolean	m_selected;
	
	/** Type of the object */
	protected int		m_type;
	
	/** Geometrie which describes the velocity vector */
	protected Geometrie	m_velocityVector;
	
	/** A shape of type RenderInfo which describes the vector */
	protected VectorShape	m_velocityVectorShape;
	
	/** Geometrie which describes a node to change the velocity with the mouse. */
	protected Geometrie	m_velocityNode;
	
	/** If type is vehicle, the x component of the velocity */	
	protected double 	m_velX;
	
	/** If type is vehicle, the y component of the velocity */
	protected double	m_velY;
	
	/** Geometrie which describes the scale node */
	protected Geometrie	m_scaleNode;
	
	/** Geometrie which describes the rotation node */
	protected Geometrie	m_rotationNode;
	
	/** The x component of the local x-axis */
	protected double	m_localX;
	
	/** The y component of the local x-axis */
	protected double	m_localY;
	
	/** The rotation-angle of the object */
	protected double	m_angle;
	
	/** The scale factor in x-direction */
	protected double	m_scaleX;
	
	/** The scale factor in y-direction */
	protected double	m_scaleY;
	
	/** Value of the farthest x point of the shape */
	protected double	m_farthestX = 20;
	
	/** Value of the farthest y point of the shape */
	protected double	m_farthestY = 20;
	
	/** The x position of the scale node */
	protected double	m_scaleNodeX;
	
	/** The y position of the scale node */
	protected double	m_scaleNodeY;
	
	/** The x axis */
	protected Geometrie 	m_xAxis;
	
	/** The y axis */
	protected Geometrie 	m_yAxis;
	
	/** The x axis vector shape */
	protected VectorShape	m_xAxisShape;
	
	/** The y axis vector shape */
	protected VectorShape	m_yAxisShape;

	/** The x axis label */
	protected Geometrie	m_xAxisLabel;
	
	/** The y axis label */
	protected Geometrie	m_yAxisLabel;
	
	/** The InfoBox object for the x axis label */
	protected InfoBox	m_xAxisLabelShape;
	
	/** The InfoBox object for the y axis label */
	protected InfoBox	m_yAxisLabelShape;

	public static final int VEHICLE = 1;
	public static final int OBSTACLE = 2;
	
} // class GeometrieObject