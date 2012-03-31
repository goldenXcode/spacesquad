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
import java.util.Hashtable;

import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;



/** 
 * Base class for all steering behaviors
 */
public class SimplePathfollowing extends Behavior implements ObjectAttributes
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Constructor */
	public SimplePathfollowing() 
	{ 		
		m_behaviorName = "SimplePathFollowing";
	}
     	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
    
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
			
			// Instead of the distance, store the squared distance to speed up later calculations
			if (name.equals("arrivedistance"))		{	m_arriveDistance = val * val;	}				
		}
		catch (Exception e) {}				
	
	}
     	
	
	
	/** 
	 * Use this function if you need to pass a special object to 
	 * certain behaviors, but do not know which behavior really
	 * recognizes this object. If a behavior recognized the 
	 * objectDesc, it will return true and cast the object
	 * to the correct type internally. If the behavior does
	 * not know the objectDesc, it will return false. 
	 * @param objectDesc A string that represents a certain object type
	 * @param specialObject An instance of a special object that corresponds to the objectDesc
	 * @return True if the behvaior recognized the description, otherwise False
	 */
	public boolean setSpecialObject(String objectDesc, Object specialObject)
	{
	    boolean result = false;
		
		// Recognize the PathFindingSimulation class objects
		if (objectDesc.compareTo("PathFindingSimulation") == 0)
		{			
			result = true;
		}
			
		return result;
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
		Vector2d force = new Vector2d(0,0);
		double len;
		Point2d waypoint = null;
		Point2d target = null;
	
		if (veh.getPath().size() > 0)
		{
			waypoint = (Point2d) veh.getPath().getFirst();	    
			target = (Point2d) veh.getPath().getLast();
	
			veh.setPathState(veh.FOLLOWING_PATH);
			Vector2d dist = waypoint.sub(veh.getPos());
	     
			len = dist.lengthSquared();
	        
			if (len < m_arriveDistance)
			{
				// If we are at the last waypoint and have to stop there:
				if ((waypoint == target) && (m_stop == STOP_AT_TARGET))
				{
					// Try to make the vehicle stop
					force.setX(-veh.getVel().getX());
					force.setY(-veh.getVel().getY());

					len = force.length();

					// Force it below the maximum force
					if (len > veh.getMaxForce())
					{
						force.scale(veh.getMaxForce() / len);    
					}

					// If we use more force than velocity, scale it down
					if (len < veh.getVel().length())
					{
						force.scale(veh.getVel().length() / len);    
					}

					force.scale(m_influence);
				}
				else
				{
					// Just remove the last waypoint and be done with it
					veh.removeWaypoint();        
				}
			}
			else
			{
				// The steering force is the distance vector, 
				// scaled to maxForce * influence
				dist.scale(m_influence * veh.getMaxForce() / java.lang.Math.sqrt(len));        
				force.setX(dist.getX());
				force.setY(dist.getY());
			}
		}
		else
		{
			// Set the state to arrived 
			veh.setPathState(veh.FOLLOWING_PATH);
		}	

		return force; 
	}

	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////  	
	
	public static final int STOP_AT_TARGET = 0;
	public static final int KEEP_GOING = 1;    
	
	/** This is the minimum distance to the waypoint to say we arrived there */
	protected double m_arriveDistance = 0.0;
	/** Flag to show if, the vehicle should stop at the last waypoint*/
	protected int m_stop = STOP_AT_TARGET;
}