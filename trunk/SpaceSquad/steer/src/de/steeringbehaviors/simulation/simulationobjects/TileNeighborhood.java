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

import java.awt.Polygon;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import de.steeringbehaviors.simulation.behaviors.Behavior;
import de.steeringbehaviors.simulation.mind.Mind;
import de.steeringbehaviors.simulation.renderer.Point2d;


/** 
 * The Neighborhood class implements a spatial scene lookup table. 
 * Normally used as a pre-simulation simulator, it calculates the 
 * distances between the vehicles in the scene. It can be used to
 * querry for all vehicles or obstacles within a defined radius of 
 * another vehicle.
 */
public class TileNeighborhood extends Neighborhood
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Constructor */
	public TileNeighborhood()
	{ 				
	    init();
	}	
  
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////      	   	
  
    /** 
	 * Adds a new obstacle to the scene description. The obstacle is
	 * automatically added to all pre- and post-Simulations as well.
	 * @param newObstacle The obstacle object to add to the scene
	 */
	public void addObstacle(Obstacle newObstacle)
	{
		Iterator 	it;
		Simulation 	sim;
		Vector		points = new Vector(10, 10);		
		Point2d		p;
		
		m_Obstacle.add(newObstacle);
		m_Scene.add(newObstacle);	
		
		int tileX = (int )(newObstacle.getPos().getX() / m_tilesizeX);
		int tileY = (int )(newObstacle.getPos().getY() / m_tilesizeY);
		
		// Add all Points to the list (they are already in world space)
		it = newObstacle.getPoints().iterator();
		while (it.hasNext())
		{
			p = new Point2d((Point2d) it.next());			
			points.add(p);
		}
		
		// Scan convert the obstacle into tile space
		scanConvertObstacle(points, newObstacle);
		
		// Add the obstacle to all additional pre-Simulations
		it = m_preSimulation.iterator();
		while (it.hasNext())
		{
			sim = (Simulation) it.next();	
			sim.addObstacle(newObstacle);
		}
		
		// Add the obstacle to all additional post-Simulations
		it = m_postSimulation.iterator();
		while (it.hasNext())
		{
			sim = (Simulation) it.next();	
			sim.addObstacle(newObstacle);
		}
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
		Mind		mi;		
		Behavior	behave;
		int         tileX, tileY;
		
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
		
		tileX = (int )(newVehicle.getPos().getX() / m_tilesizeX);
		tileY = (int )(newVehicle.getPos().getY() / m_tilesizeY);
		
		// if the vehicle is within the tile informastion space,
		// add it to the list of vehicles of the calculated tile
		if ((tileX < m_tilesX) && (tileY < m_tilesY))
		{
		    TileInformation tinfo = (TileInformation) m_tiles.elementAt(tileX * tileY);
		    tinfo.m_vehicles.add(newVehicle);
		    m_tiles.add(tileX * tileY, tinfo);
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
	 * Initializes the tile matrix. In the initial state, all tiles 
	 * are considered accessible and do not have any vehicles or obstacles 
	 * associated with it.
	 */
	public void init()
	{   
	    TileInformation tinfo;
		
		m_tiles = new Vector(m_tilesX * m_tilesY, 5);
		
		for (int i = 0; i < m_tilesX * m_tilesY; i++)
		{
		    tinfo = new TileInformation();
		    m_tiles.add(i, tinfo);    
		}				
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
	 * the tile informations. 
	 * No pre- or post-simulation steps are run on this simulator!
	 */
	public void runSimulation()
	{  
		// Update the tile information for all vehicles
		Iterator it = m_Vehicles.iterator();
		int tileX, tileY;
		Vehicle v;
		TileInformation info;
				
		while (it.hasNext())
		{
		    v = (Vehicle) it.next();
		    
		    // Remove the vehicle from the tile at the previous position
		    tileX = (int) (v.getOldPos().getX() / m_tilesizeX);		    
		    tileY = (int) (v.getOldPos().getY() / m_tilesizeY);
		    
		    if ((tileX < 0) || (tileX >= m_tilesX)) continue;
		    if ((tileY < 0) || (tileY >= m_tilesY)) continue;
		    
		    info = (TileInformation) m_tiles.elementAt(tileY * m_tilesX + tileX);
		    info.m_vehicles.remove(v);
		    
		    // Add the vehicles to the tile at the new position
		    tileX = (int) (v.getPos().getX() / m_tilesizeX);
		    tileY = (int) (v.getPos().getY() / m_tilesizeY);
		    
		    if ((tileX < 0) || (tileX >= m_tilesX)) continue;
		    if ((tileY < 0) || (tileY >= m_tilesY)) continue;
		    
		    info = (TileInformation) m_tiles.elementAt(tileY * m_tilesX + tileX);
		    info.m_vehicles.add(v);		    
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
	 * @return Array of obstacles
	 */
	public Iterator getNearObstacles(Vehicle v, double distance)
	{
		TileInformation info;
		Iterator obstacles;		
		
		m_nearObstacles.clear();		
		
		int anzTilesX = (int) (distance / m_tilesizeX);
		int anzTilesY = (int) (distance / m_tilesizeY);						
		
		int tileX = (int) (v.getPos().getX() / m_tilesizeX);
		int tileY = (int) (v.getPos().getY() / m_tilesizeY);
		
		int minX = tileX - anzTilesX;
		int maxX = tileX + anzTilesX;
		int minY = tileY - anzTilesY;
		int maxY = tileY + anzTilesY;
		
		//
		// Reject the search, if the parameters are outside the tilespace range
		//
		if (minX < 0)
		{
			minX = 0;
			if (maxX < 0) return m_nearObstacles.iterator();
		}
		
		if (maxX >= m_tilesX)
		{
			maxX = m_tilesX - 1;
			if (minX >= m_tilesX) return m_nearObstacles.iterator();
		}
		
		if (minY < 0)
		{
			minY = 0;
			if (maxY < 0) return m_nearObstacles.iterator();
		}
		
		if (maxY >= m_tilesY)
		{
			maxY = m_tilesY - 1;
			if (minY >= m_tilesY) return m_nearObstacles.iterator();
		}
		
		//
		// Return all obstacles in the search space
		//
		for (int y = minY; y <= maxY; y++)
		{
			for (int x = minX; x <= maxX; x++)
			{
				info = (TileInformation) m_tiles.elementAt(y * m_tilesX + x);
				// Add all found obstacles to the array
				obstacles = info.m_obstacles.iterator();
				while (obstacles.hasNext())
				{					
					m_nearObstacles.add((Obstacle) obstacles.next());
				}
			}
		}
		
		return m_nearObstacles.iterator();	
	}
	
	/** 
	 * Returns an array of vehicles where the distance from the vehicle
	 * is less than the specified distance
	 * @param v Vehicle used as center for search
	 * @param distance Maximum distance from vehicle
	 * @return Array of vehicles
	 */
	public Iterator getNearVehicles(Vehicle v, double distance)
	{	    	    		
		Iterator it;	    
		TileInformation info;
		Iterator vehicles;		
		
		m_nearVehicles.clear();
		
		int anzTilesX = (int) (distance / m_tilesizeX);
		int anzTilesY = (int) (distance / m_tilesizeY);						
		
		int tileX = (int) (v.getPos().getX() / m_tilesizeX);
		int tileY = (int) (v.getPos().getY() / m_tilesizeY);
		
		int minX = tileX - anzTilesX;
		int maxX = tileX + anzTilesX;
		int minY = tileY - anzTilesY;
		int maxY = tileY + anzTilesY;
		
		//
		// Reject the search, if the parameters are outside the tilespace range
		//
		if (minX < 0)
		{
			minX = 0;
			if (maxX < 0) return m_nearVehicles.iterator();
		}
		
		if (maxX >= m_tilesX)
		{
			maxX = m_tilesX - 1;
			if (minX >= m_tilesX) return m_nearVehicles.iterator();
		}
		
		if (minY < 0)
		{
			minY = 0;
			if (maxY < 0) return m_nearVehicles.iterator();
		}
		
		if (maxY >= m_tilesY)
		{
			maxY = m_tilesY - 1;
			if (minY >= m_tilesY) return m_nearVehicles.iterator();
		}
		
		//
		// Return all vehicles in the search space
		//
		for (int y = minY; y <= maxY; y++)
		{
			for (int x = minX; x <= maxX; x++)
			{
				info = (TileInformation) m_tiles.elementAt(y * m_tilesX + x);
				// Add all found obstacles to the array
				vehicles = info.m_vehicles.iterator();
				while (vehicles.hasNext())
				{					
					m_nearVehicles.add(vehicles.next());
				}
			}
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
	
	/**
	 * Sets the accessible flag for all tiles that the obstacle covers. The
	 * tiles are chosen by scan converting the polygon into tile space.
	 * @param points Array of points in world space
	 */
	protected void scanConvertObstacle(Vector points, Obstacle newObstacle)
	{
		Iterator it = points.iterator();
		Point2d	p = null;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		
		int testTile;
		
		int testX, testY;
		
		Polygon	poly = new Polygon();
		
		TileInformation info;
		
		// Convert the incoming polygon defined by points using double values
		// into a Java standard polygon using integer values. Also find the 
		// maximum and minimum x and y values of the points.
		while (it.hasNext())
		{
			p = (Point2d) it.next();
			
			if (p.getX() > maxX) maxX = (int) p.getX();
			if (p.getX() < minX) minX = (int) p.getX();
			if (p.getY() > maxY) maxY = (int) p.getY();
			if (p.getY() < minY) minY = (int) p.getY();
			
			poly.addPoint((int) p.getX(), (int) p.getY());
		}
		
		System.out.println("Max: " + maxX + ", " + maxY);
		System.out.println("Min: " + minX + ", " + minY);
		
		//
		// Now do a quick reject of the obstacles that fall outside the range of the tile-space
		//
		if (maxX > (m_tilesX * m_tilesizeX))
		{
			maxX = (int) (m_tilesX * m_tilesizeX);
			if (minX > m_tilesX * m_tilesizeX) 
				return;
		} 
		if (minX < 0)
		{
			minX = 0;
			if (maxX < 0) return;
		}
		
		if (maxY > m_tilesY * m_tilesizeY)
		{
			maxY = (int) (m_tilesY * m_tilesizeY);
			if (minY > (m_tilesY * m_tilesizeY) )
				return;
		} 
		if (minY < 0)
		{
			minY = 0;
			if (maxY < 0) return;
		}
		
		//
		// Convert the max / min positions into tile space
		//
		maxX /= m_tilesizeX;
		minX /= m_tilesizeX;
		maxY /= m_tilesizeY;
		minY /= m_tilesizeY;
		
		if (maxX >= m_tilesX) maxX = m_tilesX - 1;
		if (maxY >= m_tilesY) maxY = m_tilesY - 1;
		
		System.out.println("Scan converting Obstacle: " + newObstacle.getObjectName());
		
		//
		// Four different cases: 
		//	
		if (maxX == minX)
		{
			// The obstacle is completely inside of one tile
			if (maxY == minY)
			{
				System.out.println("Obstacle completely inside one tile");
				
				info = (TileInformation) m_tiles.elementAt(maxY * m_tilesX + maxX);
				info.m_accessible = false;
				info.m_obstacles.add(newObstacle);
			}
			// The obstacle covers on line of tiles along the y axis, but does not cross any borders
			else
			{				
				System.out.println("Obstacle covers a line along the y axis at x=" + maxX);
				for (int i = minY; i <= maxY; i++)
				{
					testTile = i * m_tilesX + maxX;	
					info = (TileInformation) m_tiles.elementAt(testTile);
					info.m_accessible = false;
					info.m_obstacles.add(newObstacle);
				}
			}
		}
		// The obstacle covers a line of tiles along the x axis
		else if (maxY == minY)
		{
			System.out.println("Obstacle covers a line along the x axis at y=" + maxY);
			
			for (int i = minX; i <= maxX; i++)
			{
				testTile = minY * m_tilesX + i;	
				info = (TileInformation) m_tiles.elementAt(testTile);
				info.m_accessible = false;
				info.m_obstacles.add(newObstacle);
			}
		}
		// The obstacle covers a complete block of tiles
		else
		{
			System.out.println("Obstacle covers a block from " + minX + ", " + minY + " to " + maxX + ", " + maxY);
			
			for (int y = minY; y <= maxY; y++)
			{
				for (int x = minX; x <= maxX; x++)
				{
					// Test the four cornerpoints of the tile for intersection
					// with the created polygon. If one corner is inside the
					// polygon, the tile is flagged as inaccessible
					testX = (int) (x * m_tilesizeX);
					testY = (int) (y * m_tilesizeY);
					
					if (poly.contains(testX, testY)) 
					{
						info = (TileInformation) m_tiles.elementAt(y * m_tilesX + x);
						info.m_accessible = false;
						info.m_obstacles.add(newObstacle);
						continue;
					}
					
					testX += m_tilesizeX;
					if (poly.contains(testX, testY)) 
					{
						info = (TileInformation) m_tiles.elementAt(y * m_tilesX + x);
						info.m_accessible = false;
						info.m_obstacles.add(newObstacle);
						continue;
					}
					
					testY += m_tilesizeY;
					if (poly.contains(testX, testY)) 
					{
						info = (TileInformation) m_tiles.elementAt(y * m_tilesX + x);
						info.m_accessible = false;
						info.m_obstacles.add(newObstacle);
						continue;
					}
					
					testX = (int) (x * m_tilesizeX);
					if (poly.contains(testX, testY)) 
					{
						info = (TileInformation) m_tiles.elementAt(y * m_tilesX + x);
						info.m_accessible = false;
						info.m_obstacles.add(newObstacle);
						continue;
					}
					
					// Narrow down the search by using every second point
					testX += 2;
					testY = (int) (y * m_tilesizeY) + 2;
					boolean found = false;
					while (testY < (int) (y * m_tilesizeY + 1))
					{
						while (testX < (int) (x * m_tilesizeX + 1))
						{
							if (poly.contains(testX, testY)) 
							{
								info = (TileInformation) m_tiles.elementAt(y * m_tilesX + x);
								info.m_accessible = false;
								info.m_obstacles.add(newObstacle);
								found = true;
								break;
							}
							if (found) break;
							testX += 2;
						}
						testY += 2;
					}
				}
			}
		}												
		
		System.out.println("Finished scan converting Obstacle: " + newObstacle.getObjectName());
	}
				
	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////    	  	
  	
  	/**
  	 * The TileInformation class holds special informations for each tile.
  	 * First it shows, if the tile is accessible for vehicles, or if it is
  	 * blocked by one or more obstacles. The list of vehicles contains all
  	 * the vehicles where the centerpoint is inside the tile. The list of 
  	 * obstacles contains all obstacles that cross or cover the tile.
  	 */
  	public class TileInformation
  	{
  	    /** Accessible shows, if a vehicle is allowed to enter this tile */
  	    public boolean m_accessible = true;
  	    /** This linked list holds all vehicles that are inside the tile, e.g. their center position is inside the tile */
  	    public LinkedList m_vehicles = new LinkedList();
  	    /** This array holds all obstacles that are inside the tile, or cross the tile in any way */
  	    public Vector m_obstacles = new Vector(0, 2);
  	}
  	  	
  	/** An array of tiles. Each tile holds information about accessibility, vehicles and obstacles */
	public Vector    m_tiles = null;
	/** The size of a tile along the x axis */
	public double    m_tilesizeX = 10.0;
	/** The size of a tile along the y axis */
	public double    m_tilesizeY = 10.0;
	/** Number of tiles along the x axis */
	public int       m_tilesX = 64;
	/** Number of tiles along the y axis */
	public int       m_tilesY = 48;
}