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

import de.steeringbehaviors.simulation.behaviors.Behavior;
import de.steeringbehaviors.simulation.mind.Mind;
import de.steeringbehaviors.simulation.renderer.Point2d;


/** 
 * Simulation step for enforcing the 'non-penetration' constraint. 
 * This means no vehicle is allowed to get into an invalid state, for 
 * example drive through an obstacle or another vehicle. 
 */
public class PathFindingSimulation extends Simulation
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Creates an empty simulation object. Add scene objects and 
	 * additional simulation steps afterwards.
	 */
	public PathFindingSimulation()
	{				
	    m_pathingVehicles = new Vector(0, 5);
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
		
    
    /** 
	 * Adds a new vehicle to the scene description. The vehicle is
	 * automatically added to all pre- and post-Simulations as well.
	 * @param newVehicle The vehicle object to add to the scene
	 */
	public void addVehicle(Vehicle newVehicle)
	{
		Iterator 	it;
		Simulation 	sim;
		Mind		mi;		
		Behavior	behave;
		
		// Register this class in all behaviors that need it
		mi = newVehicle.getMind();
		if (mi != null)
		{
			it = mi.getBehaviors();
			while (it.hasNext())
			{
				behave = (Behavior) it.next();
				if (behave.setSpecialObject("PathFindingSimulation", this))
				{
				    m_pathingVehicles.add(newVehicle);
				    
				    // For demo purposes: add a special path to the object
				    newVehicle.addWaypoint(new Point2d(250,180));
				    newVehicle.addWaypoint(new Point2d(150,190));
				    newVehicle.addWaypoint(new Point2d(150,350));
				    newVehicle.addWaypoint(new Point2d( 30,350));
				    newVehicle.addWaypoint(new Point2d( 30,480));
				}
			}
		}
		
		m_Vehicles.add(newVehicle);	
		m_Scene.add(newVehicle);
		
		// Add the vehicle to all additional pre-Simulations
		it = m_preSimulation.iterator();
		while (it.hasNext())
		{
			sim = (Simulation) it.next();	
			sim.addVehicle(newVehicle);
		}
		
		// Add the vehicle to all additional post-Simulations
		it = m_postSimulation.iterator();
		while (it.hasNext())
		{
			sim = (Simulation) it.next();	
			sim.addVehicle(newVehicle);
		}
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
		
		// First run all pre-simulation Simulations
		it = m_preSimulation.iterator();		
		while (it.hasNext())
		{
			sim = (Simulation) it.next();
			
			sim.runSimulation();
		}
		
		// The vehicle simulation step checks all vehicles
		// with pathfinding capabilities, if they need a new
		// path
		it = m_pathingVehicles.iterator();
		while (it.hasNext())
		{
			ve = (Vehicle) it.next();
			
			// Do the pathfinding, where necessary
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
	
	/** Array with all vehicles that have a pathfollowing behavior */
	Vector m_pathingVehicles = null;
}