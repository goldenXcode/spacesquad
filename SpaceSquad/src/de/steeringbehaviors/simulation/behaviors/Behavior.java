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

import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Neighborhood;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;



/** 
 * Base class for all steering behaviors
 */
public class Behavior implements ObjectAttributes
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Constructor */
	public Behavior() 
	{ 
		m_influence = 0; 
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
	
		// set attribute with type double
		try
		{
			// convert string value to double
			Double temp = new Double(value);
			double val = temp.doubleValue();
						
			if (name.equals("influence")) 	
			{ 	
				m_influence=val;	
			}
		}
		catch (Exception e) {}				
	
	}
     	
	/** 
	 * Sets the influence of the behaviour
	 * @param influence Influence of the behaviour
	 */
	public void setInfluence(double influence) 
	{ 
		m_influence=influence; 
	}
	
	public void setNeighborhood(Neighborhood nh)
	{
		m_neighborhood = nh;	
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
		return false;
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @return The name of the behavior class
	 */
	public String getBehaviorName()
	{
		return m_behaviorName;	
	}
	
	/** 
	 * Returns the influence of the behaviour on the overall steering force
	 * @return Influence of the behaviour
	 */
	public double getInfluence() 
	{ 
		return m_influence; 
	}
	
	/** 
	 * This function is used to signal if the behavior needs a 
	 * Neighborhood simulation object to function correctly
	 * @return True if a Neighborhood object is needed
	 */
	public boolean needsNeighborhood()
	{
		return m_needsNeighborhood;	
	}
	
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
		return new Vector2d(0,0); 
	}

	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////  	
	
	/** The name of the behavior. Each class based on behavior overrides this with its own name. */
	protected String m_behaviorName = "Behavior";
	/** Influence of the behavior on the overall force */
	protected double m_influence;	
	/** Signals that the behavior needs access to a neighborhood simulation to function correctly */
	protected boolean m_needsNeighborhood = false;
	/** The neighborhood simulation to use for specific behaviors*/
	protected Neighborhood m_neighborhood = null;
}