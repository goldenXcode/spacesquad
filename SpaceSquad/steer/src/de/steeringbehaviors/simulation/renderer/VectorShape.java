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


/**
 *
 * @author  tom
 * @version 
 */
public class VectorShape extends RenderInfo 
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
    /** Creates new VectorShape. The vector is initialized to (1.0, 0.0)
     * and a length of zero.
     */
    public VectorShape() 
    {
        m_vector = new Vector2d(0.0, 0.0);        
        m_type = VECTORSHAPE;
    }
    
    /** Creates new VectorShape */
    public VectorShape(Vector2d vector) 
    {
        m_vector = new Vector2d(vector);        
        m_type = VECTORSHAPE;
    }
    
    /** Creates new VectorShape */
    public VectorShape(Vector2d vector, double length) 
    {
        m_vector = new Vector2d(vector);        
        m_length = length;
        m_type = VECTORSHAPE;
    }
    
    //////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////	
	
	public void setDrawVector(Vector2d vect)
    {
        m_drawVector = vect;
    }
	
	/** 
	 * Sets a new vector to be used for displaying. 
	 * The vector will be normalized automatically.	 
     * @param vector The new vector
     */
    public void setVector(Vector2d vector)
    {
        m_vector = vector;      
    }
    
    /** 
     * @param length The new length of the vector
     */
    public void setLength(double length)
    {
        m_length = length;
    }
    
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
	
	public Vector2d getDrawVector()
    {
        return m_drawVector;
    }
	
	/** 
	 * Returns the distance of the point on the shape that is farthest 
	 * away from the center of the geometrie.
     * @return The farthest distance from the center of the geometrie
     */
	public double getFarthestDistance()
	{	
		double len = 0.0;
		Vector2d v = new Vector2d(m_vector);
		v.setX(m_vector.getX() * m_scaleX);
		v.setY(m_vector.getY() * m_scaleY);
		
		v.scale(m_length);
		
		len = v.length();
		
		return len;
	}
	
	/** 
     * @return The length of the vector
     */
    public double getLength()
    {
        return m_length;
    }
    
    /** 
     * @return The Vector
     */
    public Vector2d getVector()
    {
        return m_vector;
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
        
        double dln = m_drawVector.length();
        double dl = new Vector2d(x - m_pos.getX(), y - m_pos.getY()).length();
        double dr = new Vector2d((m_pos.getX() + m_drawVector.getX()) - x, 
        				(m_pos.getY() + m_drawVector.getY()) - y).length();
        
        double df = dln - dl - dr;                
        
        if (java.lang.Math.abs(df) < dln / 100)
        	inside = true;        	        
        
        return inside;
    }                
    
    //////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////
    
    /** The vector */
    protected Vector2d    m_vector = null;
    /** Length of the vector*/
    protected double      m_length = 1;    
    /** The Vector as drawn on the screen */
    protected Vector2d  m_drawVector = new Vector2d();    
}