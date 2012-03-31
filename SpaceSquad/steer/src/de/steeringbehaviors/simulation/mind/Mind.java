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

import java.util.*;

import de.steeringbehaviors.simulation.behaviors.Behavior;
import de.steeringbehaviors.simulation.behaviors.BehaviorInfo;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;


public class Mind
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructor 
	 */
	public Mind()
	{
		m_behaviors = new Vector(0 , 2);
		m_forces = new Vector(0, 2);
	}
	
	/**
	 * Constructor 
	 * @param behaviors	Array with the behaviors to be used in the mind
	 */
	public Mind(Vector behaviors)
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
	
	/**
	 * Replaces the array of behaviors with a new one
	 * @param behaviors Array with the new behaviors
	 */
	public void setBehaviors(Vector behaviors)
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
	
	/**
	 * Adds a new behaviors to the list of behaviors
	 * @param behave The new behavior
	 */
	public void addBehavior(Behavior behave)
	{	
		BehaviorInfo info = new BehaviorInfo();		
		
		m_behaviors.add(behave);	
		
		info.m_behaviorName = behave.getBehaviorName();
		m_forces.add(info);
	}
	
	/**
	 * Toggles the state of behavior tracking. If it is turned
	 * on, the calculated forces of the behaviors are stored in
	 * the forces array for every frame.
	 * @param trackBehaviors Turns the tracking of behavior forces on or off
	 */
	public void setTrackBehaviors(boolean trackBehaviors)
	{
		m_trackBehaviors = trackBehaviors;	
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * Gives access to the list of behaviors
	 * @return Iterator over the list of behaviors
	 */
	public Iterator getBehaviors()
	{
		return m_behaviors.iterator();	
	}
	
	/**
	 * Gives acces to the vector with the forces for each behavior in the last frame
	 * @return A Vector with the name of the behavior and the calculated force in the last frame
	 */
	public Vector getForces()
	{
		return m_forces;	
	}
	
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
		return new Vector2d(0, 0);	
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////
	
	/** List of behaviors used on this object */
	protected Vector m_behaviors = null;
	/** List of the behaviors with the associated forces for the last frame*/
	protected Vector m_forces = null;
	/** Use this to turn on the tracking of behavior forces */
	protected boolean m_trackBehaviors = false;
}