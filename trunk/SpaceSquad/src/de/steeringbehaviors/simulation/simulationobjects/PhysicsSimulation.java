/*
	Steering Behaviors Demo Applet
    
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
package de.steeringbehaviors.simulation.simulationobjects;

import java.util.Iterator;
import java.util.Vector;

import de.steeringbehaviors.simulation.mind.Mind;


/** 
 * Post simulation step for enforcing the 'non-penetration' constraint. 
 * This means no vehicle is allowed to get into an invalid state, for 
 * example drive through an obstacle or another vehicle. 
 */
public class PhysicsSimulation 
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Creates an empty simulation object. Add scene objects and 
	 * additional simulation steps afterwards.
	 */
	public PhysicsSimulation()
	{				
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
	 * runSimulation is called to run a simulation step on all the 
	 * vehicles and additional simulators. The idea of having a pre-
	 * and post simulation is, to allow additional simulation steps 
	 * to be created and seamlessly plugged into the system.
	 */
	public void runSimlation()
	{
		Iterator 		it;
		Mind			mi;
		Vehicle			ve;
		Simulation		sim;	
		
		
		
		// Then run the vehicle simulation step
		it = m_Vehicles.iterator();
		while (it.hasNext())
		{
			ve = (Vehicle) it.next();
			mi = ve.getMind();
			
			// If the vehicle has a mind, use it!
			if (mi != null)
			{
				mi.calculate(ve);	
			}	
		}
		
		// Last run all post simulation simulators
		it = m_postSimulation.iterator();
		while (it.hasNext())
		{
			sim = (Simulation) it.next();
			
			sim.runSimulation();		
		}
	}		
	

	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Array with all the objects in the scene */
	protected Vector m_Scene = null;
	/** Array with all the vehicles in the scene */
	protected Vector m_Vehicles = null;
	/** Array with all the obstacles in the scene */
	protected Vector m_Obstacle = null;
	/** Array with the simulation objects that have to run before the scene is recomputed */
	protected Vector m_preSimulation = null;
	/** Array with the simulation objects that have to run after the scene is recomputed */
	protected Vector m_postSimulation = null;
}