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


/** Arrive - Behavior
 */
public class Arrive extends Behavior implements ObjectAttributes
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Constructor */
	public Arrive() 
	{ 
		m_target=new Geometrie(); 
		m_influence=1; 
		m_activeDistance=2000; 
		m_behaviorName = "Arrive";
	}
        
	/** Constructor with moving target specification
	 * @param target Target geometry
	 * @param steps Number of steps used to reach the target
	 * @param influence Influence of the behaviour
	 */
	public Arrive(Geometrie target, int steps, double influence) 
	{ 
		m_target=target; 
		m_influence=influence; 
		m_activeDistance=2000;
		m_steps = steps;
		m_behaviorName = "Arrive";
	}
	
	/** Constructor with static target specification
	 * @param x X position
	 * @param y Y position
	 * @param steps Number of steps used to reach the target
	 * @param influence Influence of the behaviour
	 */
	public Arrive(double x, double y, int steps, double influence) 
	{ 
		m_target=new Geometrie(new Point2d(x,y),0,new Rect(0,0,0,0)); 
		m_influence=influence; 
		m_activeDistance=2000;
		m_steps = steps;
		m_behaviorName = "Arrive";
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Sets a attribute specified by name
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
			
			if (name.equals("steps")) 			{ m_steps = val; }			
			if (name.equals("activeDistance")) 	{ m_activeDistance = val; }			
		}
		catch (Exception e) {}
		
		try
		{
			if (name.equals("target"))	
			{	
				m_target = (Geometrie) objectList.get(value);	
			}
		}
		catch (Exception e) {}
	
	}

	/** Sets the maximum distance from the target to trigger the behaviour
	 * @param adistance Active distance
	 */
	public void setActiveDistance(double adistance) 
	{ 
		m_activeDistance = adistance; 
	}

	/** Sets the moving target
	 * @param target The target geometry
	 */
	public void setTarget(Geometrie target) 
	{ 
		m_target = target; 
	}
	
	/** Sets the static target position
	 * @param x X position
	 * @param y Y position
	 */
	public void setTargetXY(double x, double y) 
	{ 
		m_target = new Geometrie(new Point2d(x,y),0,new Rect(0,0,0,0)); 
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
	 * @param veh The Vehicle
	 * @return Returns the resulting force
	 */
	public Vector2d calculate(Vehicle veh)
	{ 
		double distance;
		Point2d target = new Point2d(m_target.getPos());
		Point2d source = new Point2d(veh.getPos());
		
		Vector2d des_vel = target.sub(source);
		
		distance = des_vel.length();
		
		if (distance > m_activeDistance) return new Vector2d(0,0);  // Ausserhalb des Bereichs, keine Lenkung
		
		double speed = distance / m_steps;
		
		if (speed > veh.getMaxVel()) speed = veh.getMaxVel();
		
		// Innerhalb des Bereichs
		//des_vel.normalize();
		des_vel.scale(speed / distance);
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
	protected Geometrie       m_target;		
	/** Maximum distance from the target to trigger the behaviour */
	protected double          m_activeDistance;	
	/** Number of steps used to arrive at the target */
	protected double          m_steps;			
}