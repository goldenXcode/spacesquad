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

import de.steeringbehaviors.simulation.behaviors.Behavior;
import de.steeringbehaviors.simulation.mind.Mind;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;



/** 
 * The Neighborhood class implements a spatial scene lookup table. 
 * Normally used as a pre-simulation simulator, it calculates the 
 * distances between the vehicles in the scene. It can be used to
 * querry for all vehicles or obstacles within a defined radius of 
 * another vehicle.
 */
public class Neighborhood extends Simulation
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Constructor */
	public Neighborhood()
	{ 				
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
				behave.setSpecialObject("Neighborhood", this);
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
  
	/** 
	 * Initializes the neighborhood object distance matrix
	 */
	public void init()
	{   	
		// Anzahl der Elemente ermitteln
		m_nElements = m_Vehicles.size();
		
		// Distanzmatrix erzeugen
		m_distMatrix = new double[m_nElements][m_nElements];
		
		// Distanz-Matrix mit -1 vorbelegen 	
		for (int i = 0 ; i <= m_nElements-1 ; i++)
		{
			for (int j = 0 ; j <= m_nElements-1 ; j++)
			{
				m_distMatrix[i][j]=-1;
			}
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
		
		// Reset the element count to force a re-initialization of the distance matrix
		m_nElements = 0;
	}
	
	/**
	 * Replaces the list of vehicles with a new list
	 *
	 * @param vehicleList Array of vehicles
	 */
	public void setVehicleList(Vector vehicleList) 
	{ 
		m_Vehicles=vehicleList; 
		init();
	}            
	
	/** 
	 * Updates the current neighborhood state and recalculates
	 * the distance informations. If the number of vehicles has
	 * changed, the distance matrix is completely regenerated.
	 * No pre- or post-simualtion steps are run on this simulator!
	 */
	public void runSimulation()
	{  
		// Bei Änderung der Elemente neu initialisieren
		
		if (m_Vehicles.size()!=m_nElements) init();
		
		for (int i=0 ; i <= m_nElements-1 ; i++)
		{	
			for (int j=i+1 ; j <= m_nElements-1 ; j++)  
			{
				// Variablen
				Vehicle tmp_v1,tmp_v2;
				Point2d tmp_p1,tmp_p2;
				Vector2d tmp_vect;
				
				// Vehiclepaar
				tmp_v1=(Vehicle) m_Vehicles.elementAt(i);
				tmp_v2=(Vehicle) m_Vehicles.elementAt(j);
				
				// Positionen d. Vehicle
				tmp_p1=tmp_v1.getPos();
				tmp_p2=tmp_v2.getPos();
				
				// Entfernung berechnen
				tmp_vect=tmp_p1.sub(tmp_p2);
				
				// Qadratische Entfernung
				double lenSqr=tmp_vect.lengthSquared();
				m_distMatrix[i][j]=lenSqr;
				m_distMatrix[j][i]=lenSqr;
			}
		}
	}

	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////

	/** 
	 * Returns the number of elements in the distance matrix
	 * @return Number of elements in the distance matrix
	 */
	public int getCount() 
	{ 
		return m_nElements; 
	}
	
	/** 
	 * Returns an array of vehicles whose distance from the vehicle
	 * is less than the specified distance
	 * @param v Vehicle used as center for search
	 * @param distance maximum distance from vehicle
	 * @return Array of vehicles
	 */
	public Iterator getNearObstacles(Vehicle v, double distance)
	{
		m_nearObstacles.clear();
		
		boolean intersects = true;
		
		Iterator it = m_Obstacle.iterator();
		Obstacle test;
		
		double test_minX = v.getPos().getX() - distance;
		double test_minY = v.getPos().getY() - distance;
		
		double test_maxX = v.getPos().getX() + distance;
		double test_maxY = v.getPos().getY() + distance;				
		
		double posX, posY, radius;
		
		while (it.hasNext())
		{
			test = (Obstacle) it.next();
			
			intersects = true;
				
			posX = test.getPos().getX();	
			posY = test.getPos().getY();
			radius = test.getRadius();
			
			// Check first, if the boundingcircle of the obstacle intersects the testing-
			// rectangle along the x axis
			if ( (posX + radius <= test_minX) && (posX - radius <= test_minX) )
				intersects = false;
			if ( (posX + radius >= test_maxX) && (posX - radius >= test_maxX) )
				intersects = false;
			if ( (posY + radius <= test_minY) && (posY - radius <= test_minY) )
				intersects = false;
			if ( (posY + radius >= test_maxY) && (posY - radius >= test_maxY) )
				intersects = false;
			
			// Add the obstacle to the resultset
			if (intersects) m_nearObstacles.add(test);												
		}				
		            
		return m_nearObstacles.iterator();	
	}
	
	/** 
	 * Returns an array of vehicles whose distance from the vehicle
	 * is less than the specified distance
	 * @param v Vehicle used as center for search
	 * @param distance maximum distance from vehicle
	 * @return Array of vehicles
	 */
	public Iterator getNearVehicles(Vehicle v, double distance)
	{
	    m_nearVehicles.clear();
	    
		// Index des Vehicles festellen
		int vehicleIndex=m_Vehicles.indexOf(v);
		
		// Quadratische Entfernung
		double distSquared=distance*distance;
		
		// Entfernungsmatrix durchsuchen und nahe Vehicles in Liste einfügen
		for (int i=0 ; i <= m_nElements-1 ; i++)
		{
			if ((i!=vehicleIndex) && (m_distMatrix[vehicleIndex][i]<=distSquared))
				m_nearVehicles.addElement(m_Vehicles.elementAt(i));
		}	
		            
		return m_nearVehicles.iterator();	
	}

	/** 
	 * Gives access to the underlying array of vehicles
	 * @return The array of vehicles
	 */
	public Vector getVehicleList() 
	{ 
		return m_Vehicles; 
	}	
    
	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////	        	                       		
			
	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////  
  		
	/** Distance matrix */
	protected double[][] m_distMatrix;
	
	protected Vector m_nearObstacles =  new Vector();
	protected Vector m_nearVehicles =  new Vector();
	/** Number of elements in the distance matrix */
	protected int m_nElements = 0;
  
}