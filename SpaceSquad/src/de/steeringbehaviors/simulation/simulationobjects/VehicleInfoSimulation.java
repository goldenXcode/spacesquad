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
package de.steeringbehaviors.simulation.simulationobjects;

import java.util.*;
import java.awt.Color;

import de.steeringbehaviors.simulation.behaviors.BehaviorInfo;
import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.renderer.InfoBox;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.PolygonShape;


/** 
 * This Simulation class is used to display information about the forces
 * genereated by the behaviors on a specified vehicle.
 */
public class VehicleInfoSimulation extends Simulation
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** 
	 * Creates an empty simulation object. Add scene objects and 
	 * additional simulation steps afterwards.
	 */
	public VehicleInfoSimulation()
	{		
		m_Vehicles = new Vector(0, 5);	
		m_Obstacle = new Vector(0, 5);	
		m_preSimulation = new Vector(0, 2);	
		m_postSimulation = new Vector(0, 2);
		m_Scene = new Vector(0, 10);
		m_forcebars = new Vector(0, 5);
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
	
	public void setTrackedVehicle(Vehicle trackedVehicle)
	{
		BehaviorInfo	info;		// The info about a behavior
		Geometrie		name;		// Geometrie object for the name of the behavior
		InfoBox			bName;		// The shape for the name of the behavior 
		Geometrie		force;		// Geometrie object for the last calculated force
		PolygonShape	bForce;		// The shape for the force
		
		double			posX, posY;	// The position of the geometrie object
		
		// Turn tracking of for the previous vehicle
		if (m_trackedVehicle != null)
			m_trackedVehicle.getMind().setTrackBehaviors(false);
			
		m_trackedVehicle = trackedVehicle;
		
		// Reset the scene information
		m_Scene.clear();
		m_forcebars.clear();
		
		if (trackedVehicle == null)
			return;
			
		// Turn tracking on for the new vehicle
		m_trackedVehicle.getMind().setTrackBehaviors(true);
		
		
		
		// 
		// The scene consists of two geometrie objects per behavior,
		// one for the name of the vehicle and one for the position.
		// The behavior geometries are one InfoBox for the name of the 
		// behavior, e.g. "Wander", and a blue bar in the background of
		// the name to represent the calculated force in the last frame.
		// 
		
		posX = 5;
		posY = 20;
		
		Vector forces = m_trackedVehicle.getMind().getForces();				
		
		Iterator it = forces.iterator();
		
		while (it.hasNext())
		{
			info = (BehaviorInfo) it.next();
			
			name = new Geometrie(new Point2d(posX, posY));
			bName = new InfoBox(new Point2d(), info.m_behaviorName);
			bName.setZ(10);
			name.addShape(bName);
			
			m_Scene.add(name);
			
			posY += 5;
			
			force = new Geometrie(new Point2d(posX, posY));
			bForce = new PolygonShape();
			bForce.addPoint(new Point2d(0, 0));
			bForce.addPoint(new Point2d(25, 0));
			bForce.addPoint(new Point2d(25, 10));
			bForce.addPoint(new Point2d(0, 10));	
			bForce.setColor(Color.blue);			
			force.addShape(bForce);
			
			m_forcebars.add(bForce);
			
			m_Scene.add(force);
			
			posY += 25;
			 	
		}
		
		// Still need to add the name and the position
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////			
	
	/** 
	 * Gives acces to the complete scene description
	 * @return The current scene
	 */
	public Vector getScene()
	{
		return m_Scene;
	}		
	
	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * runSimulation is called to run a simulation step on all the 
	 * vehicles and additional Simulations. The idea of having a pre-
	 * and post simulation is, to allow additional simulation steps 
	 * to be created and seamlessly plugged into the system.
	 */
	public void runSimulation()
	{
		Iterator 		it;		
		Vehicle			ve;
		Simulation		sim;	
		Vector			forces;
		BehaviorInfo	info;
		int				i;
		PolygonShape	ps;
		long			theTime = System.currentTimeMillis();
		
		// First run all pre-simulation Simulations
		it = m_preSimulation.iterator();		
		while (it.hasNext())
		{
			sim = (Simulation) it.next();
			
			sim.runSimulation();
		}
		
		// Then run the vehicle simulation step
		if (m_trackedVehicle != null)
		{
			forces = m_trackedVehicle.getMind().getForces();	
			it = forces.iterator();
			i = 0;
			while (it.hasNext())
			{
				info = (BehaviorInfo) it.next();
				ps = (PolygonShape) m_forcebars.elementAt(i++);
				ps.setScaleX(info.m_lastForce);
			}
		}
		
		// Last run all post-simulation Simulations
		it = m_postSimulation.iterator();
		while (it.hasNext())
		{
			sim = (Simulation) it.next();
			
			sim.runSimulation();		
		}
		
		m_simulationTime = System.currentTimeMillis() - theTime;
	}		
	

	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////
	
	/** A shortcut to the shapes representing the behavior forces */
	protected Vector m_forcebars = null;
	/** The currently tracked vehicle */
	protected Vehicle m_trackedVehicle = null;
	
}