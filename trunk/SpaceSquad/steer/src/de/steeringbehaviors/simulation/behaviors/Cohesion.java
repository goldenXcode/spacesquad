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
import java.util.*;

import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Neighborhood;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;


/**
*   class Separation
*
*   Implements the Cohesion behaviour
*/
public class Cohesion extends Behavior
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Constructor */
	public Cohesion()
	{  
		m_neighborhood = new Neighborhood();
		m_nearVehicles = new Vector();
		m_nearAreaRadius = 15;
		m_influence = 1;
		m_needsNeighborhood = true;
		m_behaviorName = "Cohesion";
	}
	
	/** 
	* Constructor 
	*
	* @param nearAreaRadius   Radius of the area to be searched for relevant vehicles
	* @param influence        Influence of the behaviour
	*/
	public Cohesion(int nearAreaRadius, int influence)
	{  
		m_neighborhood = new Neighborhood();
		m_nearVehicles = new Vector();
		m_nearAreaRadius = nearAreaRadius;
		m_influence = influence;
		m_needsNeighborhood = true;
		m_behaviorName = "Cohesion";
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
			
			if (name.equals("neararearadius")) 	{ 	m_nearAreaRadius= (int) val;	}
			
		}
		catch (Exception e) {}				
	}
	
	/** Sets the influence of the behaviour
	* @param influence New influence
	*/
	public void setInfluence(int influence) 
	{ 
		m_influence=influence; 
	}
	
	/** Sets the radius of the area to be searched for relevant vehicles
	* @param nearAreaRadius New area radius
	*/
	public void setNearAreaRadius(int nearAreaRadius) 
	{ 
		m_nearAreaRadius=nearAreaRadius; 
	}
	
	/** Sets the neighborhood object to be used
	* @param nhood New neighborhood
	*/
	public void setNeighborhood(Neighborhood nhood)
	{   
		m_neighborhood=nhood;
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
		
		// Recognize the Neighborhood class objects
		if (objectDesc.compareTo("Neighborhood") == 0)
		{
			m_neighborhood = (Neighborhood) specialObject;
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
	public Vector2d calculate(Vehicle v)
	{
		Iterator it = m_neighborhood.getNearVehicles(v, m_nearAreaRadius);
		Vehicle neighbor_vehicle;
		
		Vector2d vel_sum = new Vector2d(0,0);	
		
		double x=0;
		double y=0;
	
		int count=0;
		
		while (it.hasNext())
		{			
			count++;
			
			neighbor_vehicle = (Vehicle) it.next();
			
			x=x+neighbor_vehicle.getPos().getX();
			y=y+neighbor_vehicle.getPos().getY();
		}
		
		// calculate the average
		if (count!=0) 
		{ 
			x = x / count;
			y = y / count;
		}
		
		
		// now steer to the average-position
		Vector2d force=(new Point2d(x,y)).sub(v.getPos());
		
		force.scale(m_influence);
		
		return force;
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////  
		
	/** Neighboorhood object used for area searches */
	protected Neighborhood m_neighborhood;	
	/** Area of vehicles currently in the relevant area */
	protected Vector   m_nearVehicles;
	/** Radius of the area to be searched for relevant vehicles */
	protected int	   m_nearAreaRadius;

}