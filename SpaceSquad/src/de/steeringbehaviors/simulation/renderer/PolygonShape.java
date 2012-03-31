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

import java.awt.Color;
import java.awt.Polygon;
import java.util.*;

public class PolygonShape extends RenderInfo
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
    public PolygonShape()
    {
        m_points = new Vector(4, 2);
        m_filled = true;
        m_color = Color.black;
        m_type = POLYGONSHAPE;
    }

	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
	            
    /**
     * Adds a new point to the end of the polygon description. 
     * @param p Next point to add
     */
    public void addPoint(Point2d p)
    {        
        m_points.add(p);
    }

	/**
     * Clear the polygon description.      
     */
    public void clearPoints()
    {        
        m_points.clear();
    }	

	/** 
	 * Transforms the shape in local space.
	 * @param relMove Vector used to move the points	 
	 */
	public void moveCenter(Vector2d relMove)
	{		
		Iterator it = m_points.iterator();
		Point2d p;
		
		// Transform all points by the vector 
		for (int i = 0; i < m_points.size(); i++)
		{
			p = (Point2d) m_points.elementAt(i);	
			p = p.add(relMove);
			m_points.setElementAt(p, i);
		}		
	}

	

	public void setFilled(boolean filled) 
    { 
        m_filled = filled; 
    }

	/**     
     * @param index
     * @param p 
     */
    public void setPoint(int index, Point2d p)
    {
        m_points.set(index, p);
    }

    /**
     * 
     */
    public void setPolygon(Polygon p)
    {
        m_poly = p;
    }            
            
    //////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
    
    /** 
     * @return The color of the polygon
     */
    public Color getColor() 
    { 
        return m_color; 
    }
    
    /** 
	 * Returns the distance of the point on the shape that is farthest 
	 * away from the center of the geometrie.
     * @return The farthest distance from the center of the geometrie
     */
	public double getFarthestDistance()
	{	
		Iterator it = m_points.iterator();
		Point2d p;
		Vector2d v;
		double max = 0.0;
		double len;
		
		while (it.hasNext())
		{
			p = (Point2d) it.next();
			v = new Vector2d(p);		// Create a vector to the point
			v.setX(v.getX() * m_scaleX);
			v.setY(v.getY() * m_scaleY);
			
			len = v.length();
			if (len > max) max = len;
		}
		
		return max;
	}
    
    /** 
     * @return Filled or outlined drawing
     */
    public boolean getFilled() 
    { 
        return m_filled; 
    }
    
    public Iterator getPoints()
    {
        return m_points.iterator();
    }    
                
    /** 
     * @return The polygon used for drawing
     */
    public Polygon getPolygon() 
    { 
        return m_poly; 
    }
    
    //////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////
    
    /**
     * Tests if the point is inside the shape
     * @param x The x position in screen coordinates
     * @param y The y position in screen coordinates
     * @param zoom The zoom factor used in drawing the shape
     * @return True if the point is inside, False if not
    */
    public boolean isInside(double x, double y)
    {
        boolean inside = false;
        
        if (m_poly != null)
        {
            if (m_poly.contains((int) x, (int) y)) inside = true;
        }
                
        return inside;
    }
    
    //////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////
    
    /** Array with the single polygon points */
    protected Vector m_points;
    /** Switch between filled or outlined polygon */
    protected boolean m_filled;
    /** This is for speeding up the actual painting of the polygon */
    protected Polygon m_poly;
}
