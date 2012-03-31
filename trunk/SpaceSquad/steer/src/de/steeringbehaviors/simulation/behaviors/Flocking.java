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

import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Neighborhood;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;



/**
*   class Separation
*
*   Implements the Flocking behaviour
*/
public class Flocking extends Behavior
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Constructor */
	public Flocking()
	{  
		m_neighborhood = new Neighborhood();
		m_nearAreaRadius = 15;
		
		m_alignment = new Alignment(m_nearAreaRadius, 1);
		m_cohesion = new Cohesion(m_nearAreaRadius, 1);
		m_separation = new Separation(m_nearAreaRadius, 4);
		
		m_influence = 1;
		m_needsNeighborhood = true;
		
		m_behaviorName = "Flocking";
	}
	
	/** 
	* Constructor 
	*
	* @param nearAreaRadius   Radius of the area to be searched for relevant vehicles
	* @param influence        Influence of the behaviour
	*/
	public Flocking(int nearAreaRadius, int influence)
	{  
		m_neighborhood = new Neighborhood();
		m_nearVehicles = new Vector();
		m_nearAreaRadius = nearAreaRadius;
		
		m_alignment = new Alignment(m_nearAreaRadius, 1);
		m_cohesion = new Cohesion(m_nearAreaRadius, 1);
		m_separation = new Separation(m_nearAreaRadius, 4);

		m_influence = influence;
		m_needsNeighborhood = true;
		
		m_behaviorName = "Flocking";
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
			
			if (name.equals("neararearadius")) 	{ 	setNearAreaRadius((int) val);	}
			if (name.equals("cohesion"))		{	m_cohesion.setInfluence(val);	}
			if (name.equals("separation"))		{	m_separation.setInfluence(val);	}
			if (name.equals("alignment"))		{	m_alignment.setInfluence(val);	}
			
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
		
		m_cohesion.setNearAreaRadius(nearAreaRadius);
		m_separation.setNearAreaRadius(nearAreaRadius);
		m_alignment.setNearAreaRadius(nearAreaRadius);
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
		
		m_cohesion.setSpecialObject(objectDesc, specialObject);
		m_separation.setSpecialObject(objectDesc, specialObject);
		m_alignment.setSpecialObject(objectDesc, specialObject);
		
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
		Vector2d force = new Vector2d(0,0);	
		
		// combine the separation, cohesion and the alignment behavior 
		force=force.add(m_separation.calculate(v));
		force=force.add(m_cohesion.calculate(v));
		force=force.add(m_alignment.calculate(v));
				
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
	/** Separation behavior used in this behavior */
	protected Separation	m_separation;
	/** Alignment behavior used in this behavior */
	protected Alignment	m_alignment;
	/** Cohesion behavior used in this behavior */
	protected Cohesion	m_cohesion;

}