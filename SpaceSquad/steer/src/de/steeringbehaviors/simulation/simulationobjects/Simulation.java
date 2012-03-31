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

import java.util.Iterator;
import java.util.Vector;

import de.steeringbehaviors.simulation.mind.Mind;
import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;



/** Simulation base class */
public class Simulation 
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
	public Simulation()
	{		
		m_Vehicles = new Vector(5, 5);	
		m_Obstacle = new Vector(5, 5);			
		m_preSimulation = new Vector(0, 2);	
		m_postSimulation = new Vector(0, 2);
		m_Scene = new Vector(5, 10);
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Adds a new background object to the scene description
	* @param newBackground The background object to add to the scene
	*/
	public void addBackground(Geometrie newBackground)
	{
		if (newBackground != null)
		{
			m_background = newBackground;
			m_Scene.add(newBackground);
		}
	}
	
	/**
	* Adds a new background object to the scene description
	* @param newBackground The background object to add to the scene
	*/
	public void addGeometrie(Geometrie geom)
	{
		// Add it to the scene
		m_Scene.add(geom);			
	}
	
	/** 
	 * Adds a new obstacle to the scene description. The obstacle is
	 * automatically added to all pre- and post-Simulations as well.
	 * @param newObstacle The obstacle object to add to the scene
	 */
	public void addObstacle(Obstacle newObstacle)
	{
		Iterator 	it;
		Simulation 	sim;
		
		m_Obstacle.add(newObstacle);
		m_Scene.add(newObstacle);	
		
		// Add the vehicle to all additional pre-Simulations
		it = m_preSimulation.iterator();
		while (it.hasNext())
		{
			sim = (Simulation) it.next();	
			sim.addObstacle(newObstacle);
		}
		
		// Add the vehicle to all additional post-Simulations
		it = m_postSimulation.iterator();
		while (it.hasNext())
		{
			sim = (Simulation) it.next();	
			sim.addObstacle(newObstacle);
		}
	}
	
	/** 
	 * Adds a new post simulation step to the Simulation. The
	 * Simulation gets acces to the obstacles and vehicles
	 * already present in this Simulation automatically.
	 * @param postSimulation The simulation object to add 
	 */
	public void addPostSimulation(Simulation postSimulation)
	{
		Iterator 	it;
		Vehicle 	ve;
		Obstacle	ob;
		
		// Add all vehicles to the post-simulation step
		it = m_Vehicles.iterator();		
		while (it.hasNext())
		{
			ve = (Vehicle) it.next();			
			postSimulation.addVehicle(ve);			
		}
		
		// Add all obstacles to the post-simulation step
		it = m_Obstacle.iterator();		
		while (it.hasNext())
		{
			ob = (Obstacle) it.next();			
			postSimulation.addObstacle(ob);			
		}
		
		postSimulation.setParent(this);
		
		m_postSimulation.add(postSimulation);	
	}
	
	/** 
	 * Adds a new pre-simulation step to the Simulation. The
	 * Simulation gets acces to the obstacles and vehicles
	 * already present in this Simulation automatically.
	 * @param preSimulation The simulation object to add 
	 */
	public void addPreSimulation(Simulation preSimulation)
	{
		Iterator 	it;
		Vehicle 	ve;
		Obstacle	ob;
		
		// Add all vehicles to the post-simulation step
		it = m_Vehicles.iterator();		
		while (it.hasNext())
		{
			ve = (Vehicle) it.next();			
			preSimulation.addVehicle(ve);			
		}
		
		// Add all obstacles to the post-simulation step
		it = m_Obstacle.iterator();		
		while (it.hasNext())
		{
			ob = (Obstacle) it.next();			
			preSimulation.addObstacle(ob);			
		}
		
		preSimulation.setParent(this);
		
		m_preSimulation.add(preSimulation);	
	}
	
	/** 
	 * Adds a new vehicle to the scene description. The vehicle is
	 * automatically added to all pre- and post-Simulations as well.
	 * @param newVehicle The vehicle object to add to the scene
	 */
	public void addVehicle(Vehicle newVehicle)
	{
		Iterator 	it;
		Simulation 	sim;
		
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
	
	/** 
	 * Removes all objects from the scene description.
	 * The pre- and post-simulations are also cleared of all objects	 
	 */
	public void removeAll()
	{
		m_Scene.clear();
		m_Obstacle.clear();
		m_Vehicles.clear();
		
		Iterator 	it;
		Simulation 	sim;
		
		// Clear all pre-simulations
		it = m_preSimulation.iterator();
		while (it.hasNext())
		{
			sim = (Simulation) it.next();	
			sim.removeAll();
		}
		
		// Clear all post-simulations
		it = m_postSimulation.iterator();
		while (it.hasNext())
		{
			sim = (Simulation) it.next();	
			sim.removeAll();
		}
	}
	
	/** 
	 * Removes a Geometrie object from the scene description.
	 * @param geom The object to remove from the scene
	 */
	public void removeGeometrie(Geometrie geom)
	{
		m_Scene.remove(geom);		
	}
		
	/** 
	 * Removes an Obstacle object from the scene description.
	 * The object will also be removed from all pre- and 
	 * post-simulations.
	 * @param obst The object to remove from the scene
	 */
	public void removeObstacle(Obstacle obst)
	{
		Iterator 	it;
		Simulation 	sim;
		
		m_Obstacle.remove(obst);
		m_Scene.remove(obst);
		
		// Remove it from all pre-simulations
		it = m_preSimulation.iterator();
		while (it.hasNext())
		{
			sim = (Simulation) it.next();	
			sim.removeObstacle(obst);
		}
		
		// Remove it from all post-simulations
		it = m_postSimulation.iterator();
		while (it.hasNext())
		{
			sim = (Simulation) it.next();	
			sim.removeObstacle(obst);
		}
	}

	/** 
	 * Removes an Obstacle object from the scene description.
	 * The object will also be removed from all pre- and 
	 * post-simulations.
	 * @param obst The object to remove from the scene
	 */
	public void removeVehicle(Vehicle veh)
	{
		Iterator 	it;
		Simulation 	sim;
		
		m_Vehicles.remove(veh);
		m_Scene.remove(veh);
		
		// Remove it from all pre-simulations
		it = m_preSimulation.iterator();
		while (it.hasNext())
		{
			sim = (Simulation) it.next();	
			sim.removeVehicle(veh);
		}
		
		// Remove it from all post-simulations
		it = m_postSimulation.iterator();
		while (it.hasNext())
		{
			sim = (Simulation) it.next();	
			sim.removeVehicle(veh);
		}
	}
		
	/** 
	 * Sets the parent simulation object. 	 
	 * @param parent The parent simulation
	 */
	protected void setParent(Simulation parent)
	{
		m_parent = parent;	
	}

	/**
	 * Sets the height of the scene in world coordinates. Negative
	 * values are ignored and leave the height unchanged.
	 * @param height The height of the scene
	 */
	public void setSceneHeight(double height)
	{
		if (height > 0.0)
			m_sceneHeight = height;	
	}

	/**
	 * Sets the width of the scene in world coordinates. Negative
	 * values are ignored and leave the width unchanged.
	 * @param width The width of the scene
	 */
	public void setSceneWidth(double width)
	{
		if (width > 0.0)
			m_sceneWidth = width;	
	}
	
	/**
	 * Use this to turn the scene constraints on or off. The constraints
	 * are based on the width and height of the scene. Use the <code>setWrapAround</code>
	 * function to toggle between constraining inside the rectangle, or 
	 * wrapping around at the edges.
	 * @param useConstraints Turns constraints on or off
	 */
	public void setUseSceneConstraints(boolean useConstraints)
	{
		m_useSceneConstraints = useConstraints;	
	}
	
	/**
	 * Toggles between constraining the vehicles inside the scene rectangle
	 * (wrapAround = false) and wrapping the vehicles 
	 * @param wrapAround Turns wrapping around the scen on or off
	 */
	public void setWrapAround(boolean wrapAround)
	{
		m_wrapAround = wrapAround;
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
	
	
	/** 
	 * Gives acces to the obstacles in the scene description
	 * @return The obstacles used in this simulation
	 */
	public Vector getObstacle()
	{
		return m_Obstacle;	
	}
	
	/** 
	 * Returns the parent simulation object. For the root simulation this 
	 * equals to null.
	 * @return The parent simulation
	 */
	public Simulation getParent()
	{
		return m_parent;	
	}
	
	/** 
	 * Gives acces to the complete scene description
	 * @return The current scene
	 */
	public Vector getScene()
	{
		return m_Scene;
	}
	
	/** 
	 * Returns the time in milliseconds used for the last simulation step
	 * @return The time for the last simulation step in ms
	 */
	public long getSimulationTime()
	{
		return m_simulationTime;
	}
	
	/** 
	 * Gives acces to the vehicles in the scene description
	 * @return The vehicles used in this simulation
	 */
	public Vector getVehicles()
	{
		return m_Vehicles;	
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
		Mind			mi;
		Vehicle			ve;
		Simulation		sim;	
		Vector2d		force = new Vector2d();
		long			theTime = System.currentTimeMillis();
		
		// First run all pre-simulation Simulations
		it = m_preSimulation.iterator();		
		while (it.hasNext())
		{
			sim = (Simulation) it.next();
			
			sim.runSimulation();
		}
		
		// Then run the vehicle simulation step
		it = m_Vehicles.iterator();
		while (it.hasNext())
		{			
			ve = (Vehicle) it.next();
			
			// Save the current position
			ve.saveState();
			
			mi = ve.getMind();
			
			// If the vehicle has a mind, use it!
			if (mi != null)
			{
				force = mi.calculate(ve);					
			}	
			else
			{
				force = new Vector2d(0.0, 0.0);	
			}
			
			ve.addForce(force);
			
			// If sceneConstraints is turned on, check if the 
			// resulting values are valid, or need some 
			// additional attention...
			if (m_useSceneConstraints)
			{
				Point2d pos = ve.getPos();
				if (m_wrapAround)
				{
					if (pos.getX() > m_sceneWidth)
						pos.setX(pos.getX() - m_sceneWidth);
					if (pos.getX() < 0.0)
						pos.setX(pos.getX() + m_sceneWidth);
						
					if (pos.getY() > m_sceneHeight)
						pos.setY(pos.getY() - m_sceneHeight);
					if (pos.getY() < 0.0)
						pos.setY(pos.getY() + m_sceneHeight);
				}
				else
				{
					if (pos.getX() > m_sceneWidth)
						pos.setX(m_sceneWidth);
					if (pos.getX() < 0.0)
						pos.setX(0.0);
						
					if (pos.getY() > m_sceneHeight)
						pos.setY(m_sceneHeight);
					if (pos.getY() < 0.0)
						pos.setY(0.0);											
				}
				ve.setPos(pos.getX(), pos.getY());
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
	
	/** The background object */
	protected Geometrie m_background = null;	
	/** Array with all the obstacles in the scene */
	protected Vector m_Obstacle = null;
	/** Reference to the parent simulation.  Is null for the root simulation */
	protected Simulation m_parent = null;
	/** Array with the simulation objects that have to run before the scene is recomputed */
	protected Vector m_preSimulation = null;
	/** Array with the simulation objects that have to run after the scene is recomputed */
	protected Vector m_postSimulation = null;
	/** Array with all the objects in the scene */
	protected Vector m_Scene = null;
	/** The width of a scene in world coordinates */
	protected double m_sceneWidth = 640.0;
	/** The height of a scene in world coordinates */
	protected double m_sceneHeight = 480.0;
	/** Contains the amount of time used for one simulation step in ms */
	protected long  m_simulationTime = 0;			
	/** Use this to test the vehicles for violation of scene constraints. For example if
	    it is outside the scene boundary. */
	protected boolean m_useSceneConstraints = true;
	/** Array with all the vehicles in the scene */
	protected Vector m_Vehicles = null;
	/** This flag is used to determine is vehicles start out on the other side of the
	    scene, if they cross the boundary. */
	protected boolean m_wrapAround = true;
}