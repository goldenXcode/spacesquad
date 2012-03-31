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
import java.util.*;

public class Circle extends RenderInfo
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
    public Circle(double radius)
    {
        m_radius = radius;
        m_type = CIRCLE;
    }

    public Circle(double radius, boolean filled)
    {
        m_radius = radius;
        m_filled = filled;
        m_type = CIRCLE;
    }

    public Circle(double radius, boolean filled, Color c)
    {
        m_radius = radius;
        m_filled = filled;
        m_color = c;
        m_type = CIRCLE;
    }

	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////	

    /** 
     * Sets the fill status of the circle
     * @param filled The new fill status of the circle 
     */
    public void setFilled(boolean filled) 
    { 
    	m_filled = filled;
    }
    
    /** 
     * Sets the radius of the circle
     * @param radius The new radius of the circle 
     */
    public void setRadius(double radius) 
    { 
    	m_radius = radius; 
    }
    
    /** 
     * Sets the radius of the circle when drawn on the screen
     * @param radius The drawing radius of the circle 
     */
    public void setZoomedRadius(double zoomedRadius) 
    { 
    	m_zoomedRadius = zoomedRadius; 
    }
    
    //////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
        
	/** 
	 * Returns the distance of the point on the shape that is farthest 
	 * away from the center of the geometrie.
     * @return The farthest distance from the center of the geometrie
     */
	public double getFarthestDistance()
	{		
		return m_radius * m_scaleX;
	}
	        
    /** 
     * Returns the fill status
     * @return The fill status of the circle 
     */
    public boolean getFilled() 
    { 
    	return m_filled; 
    }

	/** 
     * Returns the radius
     * @return The radius of the circle 
     */
    public double getRadius() 
    { 
    	return m_radius; 
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
        
        double relX = x - m_pos.getX();
        double relY = y - m_pos.getY();
        double distance = java.lang.Math.sqrt(relX * relX + relY * relY);
                
        if (distance < m_zoomedRadius) inside = true;                    
                
        return inside;
    }

	
        
    //////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////       
        
    /** The radius of the circle */
    protected double m_radius = 0.0;
    /** The radius of the circle when drawn on the screen */
    protected double m_zoomedRadius = 0.0;
    /** Filled or unfilled circle */
    protected boolean m_filled = true;        
}
