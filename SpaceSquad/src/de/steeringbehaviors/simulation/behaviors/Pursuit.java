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
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;



/**
* Pursuit
*
* Pursuits a specified target object. This is not the same as
* the seek behavior! In Seek the current position of the target 
* is the destination point. The pursuit behavior tries to 
* anticipate the movement of the target and uses the estimated
* future position of the target as destination.
* 
*/
public class Pursuit extends Behavior
{

	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	  
	/** Constructor	 */
	public Pursuit() 
	{ 
		m_target = null; 
		m_influence = 1; 
		m_activeDistance = 2000; 
		
		m_behaviorName = "Pursuit";
	}
	
	/**
	 * Constructor
	 *
	 * @param target       Target object
	 * @param influence    Influence of the behaviour
	 */
	public Pursuit(Geometrie target, double influence) 
	{ 
		m_target = target; 
		m_influence = influence; 
		m_activeDistance = 2000;
		
		m_behaviorName = "Pursuit";
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
			
			if (name.equals("activedistance")) 	{ 	m_activeDistance = val; }			
			if (name.equals("estimatefactor")) 	{ 	m_estimateFactor = val; }			
			
		}
		catch (Exception e) {}
		
		try
		{
			if (name.equals("target"))	
			{	
			    m_target = (Geometrie) objectList.get(value);	
			    if (m_target != null) m_prevPos = new Point2d(m_target.getPos());			        
			}
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
		double  distance;
		double  estimate = 0.0;
		
		if (m_target == null) return new Vector2d(0, 0);
		
		Vector2d des_vel = m_target.getPos().sub(veh.getPos());		
		distance = des_vel.length();
		
		if (distance > 0)
		    estimate = distance / m_activeDistance;
		
		estimate *= m_estimateFactor;
		
		m_estimatePos.setX( m_target.getPos().getX() + (m_target.getPos().getX() - m_prevPos.getX()) * estimate);
		m_estimatePos.setY( m_target.getPos().getY() + (m_target.getPos().getY() - m_prevPos.getY()) * estimate);								
		
		m_prevPos.setX(m_target.getPos().getX());
		m_prevPos.setY(m_target.getPos().getY());
		
		// Do not steer if we are outside the active distance
		if (distance > m_activeDistance) return new Vector2d(0,0);  
		
		// Calc. the steering force if we are inside the active distance
		
		des_vel = m_estimatePos.sub(veh.getPos());
		distance = des_vel.length();
		
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
  
	/** The estimate factor is used to determine the future position of the quarry. 
	A factor of zero makes the pursuit behavior work the same way as the seek 
	behavior. */
	protected double    m_estimateFactor = 0.0;
	/** This is the estimated position of the target object */
	protected Point2d   m_estimatePos = new Point2d();
	/** The previous position of the target */
	protected Point2d   m_prevPos;
	/** Target object */
	protected Geometrie m_target = null; 	
	/** Maximum distance to trigger the behaviour */
	protected double    m_activeDistance;       	
}