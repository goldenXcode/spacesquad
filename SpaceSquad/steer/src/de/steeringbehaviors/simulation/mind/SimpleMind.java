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

package de.steeringbehaviors.simulation.mind;

import java.util.Iterator;
import java.util.Vector;

import de.steeringbehaviors.simulation.behaviors.Behavior;
import de.steeringbehaviors.simulation.behaviors.BehaviorInfo;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;



public class SimpleMind extends Mind
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructor 
	 */
	public SimpleMind()
	{
		m_behaviors = new Vector(0 , 2);
		m_forces = new Vector(2, 5);
	}
	
	/**
	 * Constructor 
	 * @param behaviors	Array with the behaviors to be used in the mind
	 */
	public SimpleMind(Vector behaviors)
	{
		BehaviorInfo info;
		Behavior behave;
		m_behaviors = behaviors;
		m_forces = new Vector(m_behaviors.size(), 5);
		
		Iterator it = m_behaviors.iterator();
		// Create a list with all 
		while (it.hasNext())
		{
			behave = (Behavior) it.next();
			info = new BehaviorInfo();
			info.m_behaviorName = behave.getBehaviorName();
			m_forces.add(info);
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
	
	
	
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
	 * Calculates the forces based on internal data 
	 * @return The force for this step of animation
	 */
	public Vector2d calculate(Vehicle v)
	{
		int i = 0;
		BehaviorInfo info;
		Vector2d force = new Vector2d(0, 0);
		Iterator it = m_behaviors.iterator();
		Behavior behave;
		
		if (m_trackBehaviors)
		{
			while (it.hasNext())
			{
				behave = (Behavior) it.next();
				info = (BehaviorInfo) m_forces.elementAt(i++);
				
				force = force.add(behave.calculate(v));
				
				// The force is stored as a percentage of the maximum force
				info.m_lastForce = force.length() / v.getMaxForce();
			}
		}
		else
		{
			while (it.hasNext())
			{
				behave = (Behavior) it.next();
				
				force = force.add(behave.calculate(v));				
			}
			
		}
		
		return force;	
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////
		
}