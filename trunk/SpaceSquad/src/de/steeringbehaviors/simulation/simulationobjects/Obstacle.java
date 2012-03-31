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
package de.steeringbehaviors.simulation.simulationobjects;

import java.awt.Graphics;
import java.awt.Color;
import java.util.*;

import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Rect;
import de.steeringbehaviors.simulation.renderer.Vector2d;



/**
* class Obstacle
*
* Implements the base class for obstacle objects
*/
public class Obstacle extends Geometrie
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
        /** Constructor */
	public Obstacle()
	{
		m_objectName = "Obstacle";
		m_pos=new Point2d(0, 0);
		m_radius=1;
		m_boundingbox=new Rect(0, 0, 0, 0);
		m_localx=new Vector2d(1,0);
		m_localy=new Vector2d(0,1);
	}
	
        /** 
        * Constructor 
        *
        * @param objectName     Name of the obstacle
        * @param posX           x position of the obstacle
        * @param posY           Y position of the obstacle
        * @param radius         Radius of the obstacle
        * @param bminX          Minimum x position of the boundingbox (local coordinates)
        * @param bminY          Minimum y position of the boundingbox (local coordinates)
        * @param bmaxX          Maximum x position of the boundingbox (local coordinates)
        * @param bmaxY          Maximum y position of the boundingbox (local coordinates)
        */
	public Obstacle(String objectName, double posX, double posY, double radius, double bminX, double bminY, double bmaxX, double bmaxY)
	{
		double temp;
		m_objectName = objectName;
		m_pos=new Point2d(posX, posY);
		m_radius=radius;		
		if (bminX > bmaxX)
		{
			temp = bminX;
			bminX = bmaxX;
			bmaxX = temp;
		}
		if (bminY > bmaxY)
		{
			temp = bminY;
			bminY = bmaxY;
			bmaxY = temp;
		}
		m_boundingbox=new Rect(bminX, bminY, bmaxX, bmaxY);
		m_localx=new Vector2d(1,0);
		m_localy=new Vector2d(0,1);
		
	}	
	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Sets the visibility of the object
	 * @param visible True / False
	 */
	public void setVisible(boolean visible) 
	{ 
		m_visible = visible; 
	}
	
	/** Turns collision detection on / off
	  * @param collide True / False
	  */
	public void setCollide(boolean collide) 
	{ 
		m_collide = collide; 
	}
	
	/** 
	 * Sets the color of the object 
	 *
	 * @param    c   Color of the object
	 */
	public void setColor(Color c) 
	{
		m_color = c; 
	}
	
	/** 
	 * Sets the repelling force of the object 
	 *
	 * @param    rForce      Repelling force
	 */
	public void setRepellForce(double rForce) 
	{ 
		m_rForce = rForce; 
	}
	
	/** 
	 * Sets the behaviours stored in the obstacle 
	 *
	 * @param    behaviors   Array of behaviours
	 */
	public void setBehaviors(Vector behaviors) 
	{ 
		m_behaviors=behaviors; 
	}    
	    
	 /** Sets a attribute specified by the name
	 * @param name name of the attribute
	 * @param value value of the attribute
	 */
	public void setAttribute(String name, String value, Hashtable objectList)
	{	
		
		super.setAttribute(name, value, objectList);
		
		// set attribute with type double
		try
		{
			// convert string value to double
			Double temp = new Double(value);
			double val = temp.doubleValue();
		
			
			if (name.equals("angle")) 	
			{ 	
				setLocalx(new Vector2d(java.lang.Math.cos(val),java.lang.Math.sin(val)));
		     	}
			
		}
		catch (Exception e) {}					
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Returns collsion detection status
	 * @return Collision status
	 */
	public boolean getCollide() 
	{ 
		return m_collide; 
	}
	
	/** Returns the type of the object
	 * @return Type of the object
	 */
	public String getName() 
	{
		return "Obstacle";
	}
	
	/** Returns the repelling force
	 * @return The repelling force
	 */
	public double getRepellForce() 
	{ 
		return m_rForce; 
	}	        
	
	/** Returns visibility status
	 * @return Visibility status
	 */
	public boolean getVisible() 
	{ 
		return m_visible; 
	}				

	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////	        	                       		
			
	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Visibility status */
	protected boolean	m_visible = true;
	/** Collsion detection status */
	protected boolean	m_collide = true;	
	/** Repelling force */
	protected double	m_rForce = 2;	
	/** Array of behaviours */
	protected Vector  m_behaviors;	
	/** Color of the obstacle */
	protected Color	m_color = new Color(0xC5,0xCE,0xE4);

 }