/*
	Steering Behaviors Demo Applet
    
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

import java.lang.Math;


/** Implements a simple 2d vector class
 */
public class Vector2d
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
		
	/** Construct zero length vector */
	public Vector2d() 
	{ 
		m_x=0; 
		m_y=0;
	}
	
	/** Construct vector from x and y info
	 * @param x X member
	 * @param y Y member
	 */
	public Vector2d(double x,double y) 
	{ 
		m_x=x; 
		m_y=y; 
	}
	
	/** Copyconstructor
	 * @param v Vector to copy
	 */
	public Vector2d(Vector2d v) 
	{ 
		m_x=v.getX(); 
		m_y=v.getY(); 
	}
	
	/** Construct vector from point
	 * @param p The point
	 */
	public Vector2d(Point2d p) 
	{ 
		m_x = p.getX(); 
		m_y = p.getY(); 
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Sets the x member
     * @param x The new x member value
     */
	public void setX(double x) 
	{ 
		m_x=x; 
	}
    
    /** Sets the y member
     * @param y The new y member value
     */
	public void setY(double y) { m_y=y; }
	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Returns the x member
     * @return X member
     */
	public double getX() 
	{ 
		return m_x; 
	}
     
    /** Returns the y member
     * @return Y member
     */
	public double getY() 
	{ 
		return m_y; 
	}
     
	
	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////
	
    /** Add another vector to this vector
     * @param v The other vector
     * @return New vector
     */
	public Vector2d add(Vector2d v) 
	{ 
		return (new Vector2d(m_x+v.getX(), m_y+v.getY()));  
	}

    /** Subtract a vector from this vector
     * @param v The other vector
     * @return The new vector
     */
	public Vector2d sub(Vector2d v) 
	{ 
		return (new Vector2d(m_x-v.getX(), m_y-v.getY()));  
	}
	
    /** Returns the length of the vector
     * @return The length
     */
	public double length() 
	{ 
		double result;
		try
		{
			result=  Math.sqrt(m_x*m_x+m_y*m_y); 
		}
		catch (java.lang.ArithmeticException e)
		{
			result = 0;
		}
		return result;
	}
	
    /** Calculates the langth of the vector in squared space
     * @return Squared length of vector
     */
	public double lengthSquared() 
	{ 
		return (m_x*m_x+m_y*m_y); 
	}
	
    /** Scales the vector
     * @param a New scale
     */
	public void scale(double a) 
	{ 
		m_x*=a; 
		m_y*=a; 
	}
	
    /** Normalizes the vector */
	public void normalize() 
	{ 
		try
		{
			scale(1/length()); 
		}
		catch (java.lang.ArithmeticException e)
		{
			m_x = 0;
			m_y = 0;
		}
	}

    /** Calculates the dot product of this vector with the other vector
     * @param v The other vector
     * @return Dot product
     */
	public double dot(Vector2d v) 
	{ 
		return m_x * v.getX() + m_y * v.getY(); 
	}

	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////
	
	/** X member of vector */
	protected double m_x;
    /** Y member of vector */
	protected double m_y;
}