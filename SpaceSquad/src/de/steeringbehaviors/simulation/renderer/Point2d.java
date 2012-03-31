/*
	Steering Behaviors Demo Applet
    
    Copyright (C) 2001	Thomas Feilkas 			
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
		Thomas@Pulse3d.com		
	
*/
package de.steeringbehaviors.simulation.renderer;

/**
* class Point2d
*
* Implements a point in 2d space
*/
public class Point2d
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
  	/** Constructor */
  	public Point2d() { m_x=0; m_y=0;}
  
  	/** 
 	 * Constructor 
 	 *
 	 * @param x    X position
 	 * @param y    Y position
 	 */
  	public Point2d(double x, double y) { m_x=x; m_y=y; }

  	/** 
 	 * Constructor 
 	 *
 	 * @param v    Copyconstructor
  	 */
  	public Point2d(Point2d v) { m_x=v.getX(); m_y=v.getY(); }

	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////

	/** Sets the current x position
	 * @param x New x position
	 */
  	public void setX(double x) { m_x=x; }

  	/** Sets the current y position
	 * @param y New y position
	 */
  	public void setY(double y) { m_y=y; }

	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Returns the current x position
	 * @return Current x position
	 */
	public double getX() { return m_x; }
  
	/** Returns the current y position
	 * @return Current y position
	 */
  	public double getY() { return m_y; } 

	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Adds a vector to this point and returns the resulting point
	 * @param v Vector
	 * @return Resulting point
 	 */
	public Point2d add(Vector2d v) 
	{ 
		return (new Point2d(m_x + v.getX(), m_y + v.getY())); 
	}
  
  	/** Subtracts a vector from this point and returns the resulting point
	 * @param p Vector
	 * @return Resulting point
	 */
	public Vector2d sub(Point2d p) 
	{ 
		return (new Vector2d(m_x - p.getX(), m_y - p.getY())); 
	}
  
	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////

	/** X position */
	protected double m_x;
	/** Y position */
	protected double m_y;

}