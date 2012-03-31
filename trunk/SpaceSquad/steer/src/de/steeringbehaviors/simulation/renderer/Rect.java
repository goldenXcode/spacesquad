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

/**
* class Rectangle   
*
* Implements a simple rectangle based on Point2d objects
*/
public class Rect
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Constructor */
	public Rect() 
	{ 
		m_p1=new Point2d(0,0); 
		m_p2=new Point2d(0,0); 
	}
	
	/** 
	 * Constructor 
	 *
	 * @param p1   Upper left point
	 * @param p2   Lower right point
	 */
	public Rect(Point2d p1, Point2d p2) 
	{ 
		m_p1=p1; 
		m_p2=p2; 
	}
	
	/** 
	 * Constructor 
	 *
	 * @param x1   Upper left point x position
	 * @param y1   Upper left point y position
	 * @param x2   Lower right point x position
	 * @param y2   Lower right point y position
	 */
	public Rect(double x1,double y1,double x2,double y2) 
	{ 
		m_p1=new Point2d(x1,y1); 
		m_p2=new Point2d(x2,y2); 
	}
	
	/** 
	 * Constructor 
	 *
	 * @param p        Upper left point 
	 * @param width    Width of the rectangle
	 * @param height   Height of the rectangle
	 */
	public Rect(Point2d p, double width, double height) 
	{ 
		m_p1=p; 
		m_p2=new Point2d(p.getX()+width, p.getY()+height); 
	}

  	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Sets the upper left point of the rectangle
 	 * @param p1 Upper left point
 	 */
  	public void setp1(Point2d p1) { m_p1=p1; }
  
  	/** Sets the lower right point of the rectangle
 	 * @param p2 Lower right point
 	 */
  	public void setp2(Point2d p2) { m_p2=p2; }
  	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
	
  	/** Returns the upper left point of the rectangle
 	 * @return Upper left point
 	 */
  	public Point2d getp1()  { return m_p1; }
  
   	/** Returns the lower right point of the rectangle
 	 * @return Lower right point
 	 */
  	public Point2d getp2()  { return m_p2; }
  
  	/** Returns the width of the rectangle
 	 * @return The width
 	 */
  	public double getWidth() { return m_p2.getX()-m_p1.getX(); }
  
  	/** Returns the height of the rectangle
 	 * @return The height
 	 */
  	public double getHeight() { return m_p2.getY()-m_p1.getY(); }
  
  	

	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////

  	/** Tests,  if a point is inside the rectangle
 	 * @param p Point to test
 	 * @return True / False
 	 */
  	public boolean inRect(Point2d p)
  	{
  		return ((p.getX() >= m_p1.getX()) && (p.getX() <= m_p2.getX()) &&
  			(p.getY() >= m_p1.getY()) && (p.getY() <= m_p2.getY()));
  	}

	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////
	
  /** Upper left point of the rectangle */
  protected Point2d m_p1;
  /** Lower right point of the rectangle */
  protected Point2d m_p2;
}