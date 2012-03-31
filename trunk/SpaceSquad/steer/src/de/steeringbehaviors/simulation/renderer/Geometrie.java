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

package de.steeringbehaviors.simulation.renderer;

import java.awt.*;
import java.util.*;

import de.steeringbehaviors.simulation.behaviors.ObjectAttributes;
import de.steeringbehaviors.simulation.mind.Mind;


/** class Geometrie Implements the base class for geometrie objects */

public class Geometrie implements ObjectAttributes
{
	
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
    /** Constructor */
    public Geometrie() {
        m_pos = new Point2d(0, 0);
        m_radius = 0;
        m_boundingbox = new Rect(
            new Point2d(0, 0),
            new Point2d(0, 0));
        m_localx = new Vector2d(1, 0);
        m_localy = new Vector2d(0, 1);
        m_objectName = "Geometrie";
    }

    /**
     * Constructor
     * @param pos Position of the object
     */
    public Geometrie(Point2d pos) {
        m_pos = pos;
        m_radius = 0;
        m_boundingbox = new Rect(
            new Point2d(0, 0),
            new Point2d(0, 0));
        m_localx = new Vector2d(1, 0);
        m_localy = new Vector2d(0, 1);
        m_objectName = "Geometrie";
    }

    /**
     * Constructor
     * @param pos Position of the object
     * @param radius Radius of the object
     * @param boundingbox Boundingbox of the object
     */
    public Geometrie(Point2d pos, double radius, Rect boundingbox) {
        m_pos = pos;
        m_radius = radius;
        m_boundingbox = boundingbox;
        m_localx = new Vector2d(1, 0);
        m_localy = new Vector2d(0, 1);
        m_objectName = "Geometrie";
    }

    /**
     * Constructor
     * @param objectName Name of the object
     * @param posX X position
     * @param posY Y position
     * @param radius Radius of the object
     * @param bminX Upper left boundingbox x position
     * @param bminY Upper left boundingbox y position
     * @param bmaxX Lower right boundingbox x position
     * @param bmaxY Lower right boundingbox y position
     */
    public Geometrie(String objectName, double posX, double posY, double radius, double bminX, double bminY,
        double bmaxX, double bmaxY) {
            m_objectName = objectName;
            m_pos = new Point2d(posX, posY);
            m_radius = radius;
            m_boundingbox = new Rect(bminX, bminY, bmaxX, bmaxY);
            m_localx = new Vector2d(1, 0);
            m_localy = new Vector2d(0, 1);
    }

	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////

	/**
     * Adds a new shape to the shape description array
     * @param info New shape to add
     */
    public void addShape(RenderInfo info)
    {    	
    	m_shapes.add(info);
        info.setParent(this);
        
        // Update the bounding box radius
        updateBoundingRadius();
    }		

	/** 
	 * Transforms the shapes attached to this object
	 * by the vector. The vector has to be in world
	 * coordinates (e.g. newPosition - oldPosition)
	 * @param relMove Vector used to move the points	 
	 */
	public void moveCenter(Vector2d relMove)
	{		
		Iterator shapes = m_shapes.iterator();	
		Vector2d vect = worldToLocal(relMove);
		RenderInfo info;
		
		while (shapes.hasNext())
		{
			info = (RenderInfo) shapes.next();
			
			info.moveCenter(vect);
		}
		
	}
	

	/** 
	 * Sets a attribute specified by the name
	 * @param name name of the attribute
	 * @param value value of the attribute
	 */
	public void setAttribute(String name, String value, Hashtable objectList)
	{	
		
		// set attribute with type double
		try
		{
			// convert string value to double
			Double temp = new Double(value);
			double val = temp.doubleValue();
		
			if (name.equals("x"))		{	m_pos.setX(val);	}
			if (name.equals("y"))		{	m_pos.setY(val);	}			
			if (name.equals("radius")) 	{ 	m_radius=val;		}
			if (name.equals("scalex"))	{	setScaleX(val);		}
			if (name.equals("scaley"))	{	setScaleY(val);		}
			if (name.equals("name"))	{	m_objectName = name;}
		}
		catch (Exception e) {}						
	}

    /**
     * Sets the local x axis to the specified vector
     * @param v New local x axis
     */
    public void setLocalx(Vector2d v) 
    {
        v.normalize();
        m_localx = v;
        m_localy.setX(-m_localx.getY());
        m_localy.setY(m_localx.getX());
    }
	
	/** Sets the mind that controlls this vehicle
	 * @param theMind The new mind
	 */
	public void setMind(Mind theMind) 
	{ 
		m_mind = theMind; 
	}
	
	/**
     * Sets the name of the object
     * @param objectName New object name
     */
    public void setObjectName(String objectName) 
    { 
    	m_objectName = objectName;
    }
    
	/**
     * Sets the position of the object in world coordinates
     * @param x X position
     * @param y Y position
     */
    public void setPos(double x, double y) 
    { 
    	m_pos.setX(x); 
    	m_pos.setY(y); 
    }

    /**
     * Sets the radius of the object
     * @param radius New radius of the object
     */
    public void setRadius(double radius) 
    { 
    	m_radius = radius; 
    }                    
	
	/**
     * Scales the complete object along the x axis according to the factor. Works only
     * if the factor is greater zero.
     * @param factor Scale to apply to the object
     */
	public void setScaleX(double scaleX)
	{
		RenderInfo info;
		Iterator it = m_shapes.iterator();
		
		if (scaleX <= 0.0) return;		
		
		while (it.hasNext())
		{
			info = (RenderInfo) it.next();
			info.setScaleX(scaleX);	
		}
		
		// Update the bounding circle radius
		updateBoundingRadius();
		
		m_scaleX = scaleX;
	}
	
	/**
     * Scales the complete object along the y axis according to the factor. Works only
     * if the factor is greater zero.
     * @param factor Scale to apply to the object
     */
	public void setScaleY(double scaleY)
	{
		RenderInfo info;
		Iterator it = m_shapes.iterator();
		
		if (scaleY <= 0.0) return;		
		
		while (it.hasNext())
		{
			info = (RenderInfo) it.next();
			info.setScaleY(scaleY);	
		}
		
		// Update the bounding circle radius
		updateBoundingRadius();
		
		m_scaleY = scaleY;
	}
    
    /**
	 * Sets the visiblity status of the geometry object
	 * @visible Visiblity of the geometry
	 */
	public void setVisible(boolean visible) 
	{ 
		m_visible = visible; 
	}		    
	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////

	/**
     * Returns the boundingbox of the object
     * @return The bounding box
     */
    public Rect getBoundingBox() 
    { 
    	return m_boundingbox; 
    }
    
	/**
     * Returns the local x axis
     * @return Local x axis
     */
    public Vector2d getLocalX() 
    { 
    	return m_localx; 
    }

    /**
     * Returns the local y axis
     * @return Local y axis
     */
    public Vector2d getLocalY() 
    { 
    	return m_localy;
    }	 

	/**
     * Returns the mind object used for steering this geometrie
     * @return The mind of the object
     */
	public Mind getMind()
	{
		return m_mind;	
	}

	/**
     * Returns the name of the object
     * @return The object name
     */
    public String getObjectName() 
    { 
    	return m_objectName; 
    }

	/**
     * Returns all the points of the polygon used as a shape.
     * The points are all in world space, not in object space.
     * @return Array with all points in world space.
     */
	public Vector getPoints()
	{
		Vector points = new Vector(3, 5);
		Iterator shapes = m_shapes.iterator();
		Iterator thePoints;
		
		RenderInfo info;
		PolygonShape ps;
		Point2d p;
		
		while (shapes.hasNext())
		{
			info = (RenderInfo) shapes.next();
			
			if (info.getType() == RenderInfo.POLYGONSHAPE)
			{
				ps = (PolygonShape) info;
				thePoints = ps.getPoints();
				while (thePoints.hasNext())
				{
					// Transform the point from local to world space and add it to the result
					p = new Point2d((Point2d) thePoints.next());
					p.setX(p.getX() * m_scaleX);
					p.setY(p.getY() * m_scaleY);
					p = localToWorld(p);
					points.add(p);	
				}
			}	
		}
		
		return points;
	}

    /**
     * Returns the position of the object
     * @return Object position
     */
    public Point2d getPos() 
    { 
    	return m_pos; 
    }

    /**
     * Returns the radius of the object
     * @return The radius
     */
    public double getRadius() 
    { 
    	return m_radius; 
    }
    
    /**
     * Returns the scale of the object along the x axis
     * @return The x axis scale
     */
    public double getScaleX()
    {
    	return m_scaleX;
    }
    
    /**
     * Returns the scale of the object along the y axis
     * @return The y axis scale
     */
    public double getScaleY()
    {
    	return m_scaleY;
    }
    
	/**
     * Gets an iterator for the shapes describing the geometrie
     * @return An iterator over all shapes of type RenderInfo
     */
    public Iterator getShapeIter() { return m_shapes.iterator(); }    

	/**
	 * Returns the visiblity status of the geometry object
	 * @return Visiblity of the geometry
	 */
	public boolean getVisible() { return m_visible; }
	
	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////

	/**
     * Simple collsion event handler
     * @param otherObj Geometry object collided with
     */
    public void onCollide(Geometrie otherObj) { }

    /**
     * Transforms a point from the world coordinate system to local coordinates
     * @param p Point in world coordinates
     * @return Point in local coordinates
     */
    public Point2d localToWorld(Point2d p) 
    {
        Point2d temp = new Point2d();
        
        temp.setX(m_localx.getX() * p.getX() + m_localy.getX() * p.getY());
        temp.setY(m_localx.getY() * p.getX() + m_localy.getY() * p.getY());
        
        temp.setX(temp.getX() + m_pos.getX());
        temp.setY(temp.getY() + m_pos.getY());
        
        return temp;
    }
    
    /**
     * Transforms a vector from the world coordinate system to local coordinates
     * @param p Vector in world coordinates
     * @return Vector in local coordinates
     */
    public Vector2d localToWorld(Vector2d p) 
    {
        Vector2d temp = new Vector2d();
        
        temp.setX(m_localx.getX() * p.getX() + m_localy.getX() * p.getY());
        temp.setY(m_localx.getY() * p.getX() + m_localy.getY() * p.getY());
        
        return temp;
    }
   
	/**
     * Updates the bounding box radius automaitcally     
     */
	protected void updateBoundingRadius()
	{
		Iterator it = m_shapes.iterator();
		RenderInfo info;
		double distance = 0.0;
		double max = 0.0;
		
		while (it.hasNext())
		{
			info = (RenderInfo) it.next();
			distance = info.getFarthestDistance();
			if (distance > max) max = distance;	
		}
		
		m_radius = max;
	}   
   
    /**
     * Transforms a point from the local coordinate system to world coordinates
     * @param p Point in local coordinates
     * @return Point in world coordinates
     */
    public Point2d worldToLocal(Point2d p) {
        Point2d temp = new Point2d();
        double posX, posY;
        posX = p.getX() - m_pos.getX();
        posY = p.getY() - m_pos.getY();
        // Umwandeln der Weltkoordinaten in lokale Koordinaten
        // durch multiplizieren mit der inversen lokalen Matrix   	   	   	   	   	
        temp.setX(m_localy.getY() * posX - m_localy.getX() * posY);
        temp.setY(-m_localx.getY() * posX + m_localx.getX() * posY);
        return temp;
    }

    /**
     * Transforms a vector from the local coordinate system to world coordinates
     * @param p Vector in local coordinates
     * @return Vector in world coordinates
     */
    public Vector2d worldToLocal(Vector2d p) {
        Vector2d temp = new Vector2d();
        double posX, posY;
        posX = p.getX();
        posY = p.getY();
        // Umwandeln der Weltkoordinaten in lokale Koordinaten
        // durch multiplizieren mit der inversen lokalen Matrix   	   	   	   	   	
        temp.setX(m_localy.getY() * posX - m_localy.getX() * posY);
        temp.setY(-m_localx.getY() * posX + m_localx.getX() * posY);
        return temp;
    }                   

    //////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////        

    /** Bounding box rectangle */
    protected Rect m_boundingbox;
    /** Local x axis */
    protected Vector2d m_localx;
    /** Local y axis */
    protected Vector2d m_localy;    
    /** Name of the object */
    protected String m_objectName;
	/** Current object position in world coordinates */
    protected Point2d m_pos;
	/** Radius of the bounding sphere */
    protected double m_radius;
    /** Array with all the Shapes of type RenderInfo describing the geometrie*/
    protected Vector m_shapes = new Vector(2, 2);        
    /** Flag to set the visibility of the whole object */
    protected boolean m_visible = true;    
    /** The mind of the geometry, in case it needs one :) */
    protected Mind m_mind = null;
    /** The scale factor along the x axis for all shapes of this object */
    protected double m_scaleX = 1.0;
    /** The scale factor along the y axis for all shapes of this object */
    protected double m_scaleY = 1.0;
}
