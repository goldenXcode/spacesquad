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

public class InfoBox extends RenderInfo
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
    public InfoBox(Point2d relPos)
    {
        m_relPos = new Point2d(relPos);
        m_type = INFOBOX;
    }
    
    public InfoBox(Point2d relPos, String text)
    {
        m_relPos = new Point2d(relPos);
        m_text = text;
        m_type = INFOBOX;
    }
    
    public InfoBox(Point2d relPos, String text, int fontSize)
    {
        m_relPos = new Point2d(relPos);
        m_text = text;
        m_fontSize = fontSize;
        m_type = INFOBOX;
    }
    
    //////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////

	/**
     * Scales the complete object according to the factor. Works only
     * if the factor is greater zero.
     * @param factor Scale to apply to the object
     */
	public void scale(double factor)
	{
		m_fontSize *= factor;
		
		return;
	}
    
    public void setFontMetrics(FontMetrics theFont)
    {
        m_fontMetrics = theFont;
    }
    
    public void setFontSize(int fontSize)
    {
        m_fontSize = fontSize;
    }
    
    public void setRelPos(Point2d relPos)
    {
        m_relPos = new Point2d(relPos);
    }
    
    public void setText(String text)
    {
        m_text = text;
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
		double width = 0;
		
		if (m_fontMetrics != null)
		{
			width = m_fontMetrics.stringWidth(m_text);			
		}
		else
		{
			// Estimate the width of the text
			width = m_text.length() * 12;
		}
		return width;
	}
    
    public int getFontSize()
    {
        return m_fontSize;
    }
    
    public Point2d getRelPos()
    {
        return m_relPos;
    }
    
    public String getText()
    {
        return m_text;
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
        
        if (m_fontMetrics != null)
        {               
            int relX = (int) (x - m_pos.getX());
            int relY = (int) (y - m_pos.getY());
        
            int width = m_fontMetrics.stringWidth(m_text);
            int height = -m_fontMetrics.getHeight();
        
            if ((relX >= 0) && (relX <= width))
            {
                if ((relY <= 0) && (relY >= height))
                {
                    inside = true;
                }
            }
        }
        else
    	{
    		// Estimate the inside of the object
    		int relX = (int) (x - m_pos.getX());
            int relY = (int) (y - m_pos.getY());
        
            int width = m_text.length() * 12;
            int height = 12;
        
            if ((relX >= 0) && (relX <= width))
            {
                if ((relY <= 0) && (relY >= height))
                {
                    inside = true;
                }
            }
    	}
        return inside;
    }
    
    //////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////
    
    protected FontMetrics  m_fontMetrics = null;
    protected Point2d m_relPos = null;
    protected int m_fontSize = 12;
    protected String m_text;
}
