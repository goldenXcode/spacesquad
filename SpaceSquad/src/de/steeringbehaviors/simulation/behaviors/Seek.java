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

import java.util.Hashtable;

import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Rect;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;



/**
* class Seek
*
* Seeks a specified target object
*/
public class Seek extends Behavior
{

	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	  
	/** Constructor	 */
	public Seek() 
	{ 
		m_target=new Geometrie(); 
		m_influence=1; 
		m_activeDistance=2000; 
		
		m_behaviorName = "Seek";
	}
	
	/**
	 * Constructor
	 *
	 * @param target       Target object
	 * @param influence    Influence of the behaviour
	 */
	public Seek(Geometrie target, double influence) 
	{ 
		m_target=target; 
		m_influence=influence; 
		m_activeDistance=2000;
		
		m_behaviorName = "Seek";
	}
	
	/**
	 * Constructor
	 *
	 * @param x            X target position
	 * @param y            Y target position
	 * @param influence    Influence of the behaviour
	 */
	public Seek(double x, double y, double influence) 
	{ 
		m_target=new Geometrie(new Point2d(x,y),0,new Rect(0,0,0,0)); 
		m_influence=influence; 
		m_activeDistance=2000;
	
		m_behaviorName = "Seek";
	}
  
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
  
	/** Sets the maximum distance to the target to trigger the behaviour
	 * @param adistance The active distance
	 */
	public void setActiveDistance(double adistance) 
	{ 
		m_activeDistance=adistance; 
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
			
			if (name.equals("activedistance")) 	{ 	m_activeDistance=val; }
			if (name.equals("posX")) 			{ 	m_target.getPos().setX(val); }
			if (name.equals("posY")) 			{ 	m_target.getPos().setY(val); }			
		}
		catch (Exception e) {}
		
		try
		{
			if (name.equals("target"))	{	m_target=(Geometrie) objectList.get(value);	}
		}
		catch (Exception e) {}
	
	}

	/** Sets the current target
	 * @param target The new target
	 */
	public void setTarget(Geometrie target) 
	{ 
		m_target=target; 
	}
	
	/** Sets the current target to a new fixed position
	 * @param x X position of target
	 * @param y Y position of target
	 */
	public void setTargetXY(double x, double y) 
	{ 
		m_target=new Geometrie(new Point2d(x,y),0,new Rect(0,0,0,0)); 
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
	public Vector2d calculate(Vehicle veh)
	{ 
		double distance;
		
		if (m_target == null) return new Vector2d(0.0, 0.0);
		
		Point2d target = new Point2d(m_target.getPos());
		Point2d source = new Point2d(veh.getPos());
		
		Vector2d des_vel = target.sub(source);
		
		distance = des_vel.length();
		
		if (distance > m_activeDistance) return new Vector2d(0,0);  // Ausserhalb des Bereichs, keine Lenkung
		
		// Innerhal des Bereichs
		// des_vel.normalize();
		des_vel.scale(veh.getMaxVel() / distance);
		des_vel = des_vel.sub(veh.getVel());
		des_vel.scale(m_influence);
		    	
		return des_vel;
		
	}  	
  
	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////  
  
	/** Target object */
	protected Geometrie m_target; 
	
	/** Maximum distance to trigger the behaviour */
	protected double m_activeDistance;       
}