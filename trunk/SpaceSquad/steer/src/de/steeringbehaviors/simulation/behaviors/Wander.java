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

package de.steeringbehaviors.simulation.behaviors;

import java.awt.Graphics;
import java.awt.Color;
import java.util.Hashtable;
import java.util.Random;

import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;




/** The wander behaviour to steer a vehicle in a random way */
public class Wander extends Behavior
{

	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Constructor */
	public Wander() 	
	{ 
		m_pos = new Point2d(30,0); 
		m_r = 20; 
		m_influence = 1; 
		m_seekPoint = new Point2d(0,0);
		m_rate = 10;
		init();
	}
	
	/** Constructor
	* @param p Center point of circle (local coordinates)
	* @param r Radius of circle
	* @param influence The influence
	*/
	public Wander(Point2d p, double r, double influence) 
	{ 
		m_pos = p; 
		m_r = r; 
		m_influence = influence; 
		m_seekPoint = new Point2d(0,0);
		m_rate = 10;
		
		init();
	}
	
	/** Constructor
	* @param x X position of center point of circle
	* @param y Y position of center point of circle
	* @param r Radius of circle
	* @param influence The influence
	*/
	public Wander(double x, double y, double r, double influence)
	{ 
		m_pos = new Point2d(x,y); 
		m_r = r; 
		m_influence = influence; 
		m_seekPoint = new Point2d(0,0);
		m_rate = 10;
		
		init();
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Initialization function	*/
	public void init()
	{
		m_behaviorName = "Wander";
		
		// Punkt auf dem Kreis festlegen
		Random rand = new Random();
		
		Vector2d vect = new Vector2d(rand.nextInt(),rand.nextInt());
		
		// evtl. negative Werte
		
		if (rand.nextInt() > 0.5) vect.setX(vect.getX() * (-1));
		if (rand.nextInt() > 0.5) vect.setY(vect.getY() * (-1));
		
		// Punkt auf dem Kreis festlegen durch Normalisierung u. Skalierung
		vect.normalize(); vect.scale(m_r);
		m_seekPoint = new Point2d(m_pos);
		m_seekPoint = m_seekPoint.add(vect); 
	}				
	
	/** Sets a attribute specified by the name
	 * @param name name of the attribute
	 * @param value value of the attribute
	 * @param objectList hashtable of all steeringobjects
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
			
			if (name.equals("x"))		{ m_pos.setX(val); }
			if (name.equals("y"))		{ m_pos.setY(val); }
			if (name.equals("radius")) 	{ m_r=val; }		
		
		}
		catch (Exception e) {}
	
	}
	
	public void setRate(double rate) 
	{ 
		m_rate = rate; 
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
	
	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** 
	 * Calculates the resulting force vector for this frame
	 * @param veh The vehicle
	 * @return Returns the resulting force
	 */
	public Vector2d calculate(Vehicle v)
	{     
		// Zufallsvektor berechnen
		Random rand = new Random();
		Vector2d vect = new Vector2d(rand.nextInt(),rand.nextInt());
		
		// evtl. negative Werte
		if (rand.nextInt() > 0.5) vect.setX(vect.getX()*(-1));
		if (rand.nextInt() > 0.5) vect.setY(vect.getY()*(-1));
		
		// Vektor nur innerhalb der Variations-Stärke zulassen
		if (vect.length() > m_rate) { vect.normalize(); vect.scale(m_rate); }			
		
		// Zufallsvektor zum Seek-Point addieren
		m_seekPoint=m_seekPoint.add(vect);
		
		// Neuen Punkt auf Kreis projezieren
		Vector2d temp = new Vector2d(m_seekPoint.getX()-m_pos.getX(), m_seekPoint.getY()-m_pos.getY());
		
		temp.normalize(); 
		temp.scale(m_r);
		m_seekPoint = m_pos.add(temp);
		
		// Umrechnen auf World-Koordinaten
		Point2d seekpointworld = new Point2d(v.localToWorld(m_seekPoint));
		
		// Auf seekPoint zusteuern (seek-Behavior)
		Seek seekBehavior = new Seek(new Geometrie(seekpointworld), m_influence);
		    
		return seekBehavior.calculate(v);  	  	
	}
	
	
	
	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	////////////////////////////////////////////////////////////////////// 
	
	/** Position of the circle in local coordinates */
	protected Point2d     m_pos; 	       
	/** Resulting seek direction */
	protected Point2d     m_seekPoint;
	/** Radius of the inhibiting circle */
	protected double      m_r;	    		
	/** Maximum rate of variation */
	protected double      m_rate;  	
	/** Random number generator */
	protected Random      rand = new Random();		
}