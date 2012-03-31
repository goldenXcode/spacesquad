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

import java.awt.Image;
import java.lang.String;

public class Tile extends RenderInfo
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
    /**
     * 
     */
    public Tile(Image texture)
    {
        m_texture = texture;
        m_width = m_texture.getWidth(null);
        m_height = m_texture.getHeight(null);
        m_type = TILE;
    }
    
    /**
     *
     */
    public Tile(Image texture, int width, int height)
    {
        m_texture = texture;
        m_width = width;
        m_height = height;
        m_type = TILE;
    }
    
    //////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////	
	
	/** 
     *
     */
    public void setZoomedWidth(int zoomedWidth)
    {
    	m_zoomedWidth = zoomedWidth;	
    }
    
    /** 
     *
     */
    public void setZoomedHeight(int zoomedHeight)
    {
    	m_zoomedHeight = zoomedHeight;	
    }
    
    
    /**
    *
    */
    public void setWidth(int width)
    {
    	m_width = width;
    }
    
    /**
    *
    */
    public void setHeight(int height)
    {
    	m_height = height;
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
		double len = 0.0;
		
		if (m_width * m_scaleX > m_height * m_scaleY)
		{
			len = (m_width / 2.0) * (m_width / 2.0) * m_scaleX;
		}
		else
		{
			len = (m_height / 2.0) * (m_height / 2.0) * m_scaleY;
		}
		
		return len;
	}
    
    /**
     * @return 
     */
    public Image getImage()
    {        
        return m_texture;        
    }
    
    /**
     * @return The width of the image
     */
    public int getWidth()
    {
        return m_width;
    }
    
    /**
     * @return The height of the image
     */
    public int getHeight()
    {
        return m_height;
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
     * @return True if the point is inside, False if not
    */
    public boolean isInside(double x, double y)
    {
        boolean inside = false;
        
        double relX = x - m_pos.getX();
        double relY = y - m_pos.getY();
        
        if (java.lang.Math.abs(relX) < (m_zoomedWidth  / 2.0)) inside = true;
        if (java.lang.Math.abs(relY) < (m_zoomedHeight / 2.0)) inside = true;
        
        return inside;
    }
    
    //////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////
	
    protected Image m_texture = null;
    protected int m_width = 0;
    protected int m_height = 0; 
    /** Width used for drawing on the screen */
    protected int m_zoomedWidth = 0;
    /** Height used for drawing on the screen */
    protected int m_zoomedHeight = 0;
}
