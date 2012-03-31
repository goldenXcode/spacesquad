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

import java.awt.Graphics;
import java.awt.Color;
import java.util.*;

import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Rect;
import de.steeringbehaviors.simulation.renderer.Vector2d;



/** Class defining vehicles */
public class Vehicle extends Geometrie 
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
    /** Constructor
     */
	public Vehicle() 
	{ 						
		init("Vehicle1", 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0);
	}
	
	/** Constructor
	 * @param pos The position
	 * @param mvel The velcocity
	 * @param radius The radius
	 * @param boundingbox The boundingbox
	 * @param maxVel The maximum velocity
	 * @param maxForce Tha maximum force
	 * @param mass The mass
	 */
	public Vehicle(Point2d pos, Vector2d mvel, double radius, Rect boundingbox,double maxVel, double maxForce, double mass) 
	{		    		
		m_boundingbox=boundingbox;
		m_mass = mass;										
		
		init("Vehicle1", pos.getX(), pos.getY(), mvel.getX(), mvel.getY(), radius, maxVel, maxForce);
	}
	
    /** Constructor
     * @param objectName The name
     * @param posX The x position
     * @param posY The y position
     * @param velX The x velocity
     * @param velY The y velocity
     * @param radius The radius
     * @param maxVel The maximum velicity
     * @param maxForce The maximum force
     */
	public Vehicle(String objectName, double posX, double posY, double velX, double velY, double radius, double maxVel, double maxForce)
	{
		init(objectName, posX, posY, velX, velY, radius, maxVel, maxForce);
	}
	
	protected void init(String objectName, double posX, double posY, double velX, double velY, double radius, double maxVel, double maxForce)
	{
	    m_boundingbox = new Rect(-7, -10, 7, 10);
	    
	    m_force = new Vector2d(0,0);
	    
	    m_mass = 1;
		
		m_maxForce = maxForce;
		
		m_localx = new Vector2d(1, 0);
		m_oldLocalx = new Vector2d(1, 0);
		setLocalx(m_localx);
		
		m_maxVel = maxVel;
	    
	    m_objectName = objectName;
	    
	    m_path = new LinkedList();
	    
		m_pos = new Point2d(posX, posY);
		m_oldPos = new Point2d(posX, posY);
		
		m_prevLocalX = new Vector2d(1, 0);
		m_radius = radius;
		
		m_vel = new Vector2d(velX, velY);
		m_oldVel = new Vector2d(velX, velY);
    }
	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
	
	public void addForce(Vector2d force)
	{
		
		double theLength = force.length();
		
		// Restrict the force to the maximum value if necessary
		if (theLength > m_maxForce) 
		{ 			
			force.scale(m_maxForce / theLength); 
		}				
				
		m_force.setX(force.getX());
		m_force.setY(force.getY());						
				
		m_vel = m_vel.add(force);
		
		// Restrict velocity to maximum if necessary
		theLength = m_vel.length();
		if (theLength > m_maxVel) 
		{ 			
			m_vel.scale(m_maxVel / theLength); 
		}		
		
		// Neue Position berechnen
		
		m_pos = m_pos.add(m_vel);
		
		// Update the local x axis only if the velcoity is greater zero
		if (theLength > 0.0)
		{		
			Vector2d temp = new Vector2d(m_vel);
			temp.normalize();
			
			// Die Richtung, in die das Fahrzeug schaut, hat Probleme wenn man 
			// sofort dem Geschwindigkeitsvektor anpasst. Dies fuehrt
			// bei bestimmten Verhaltenskombinationen (z.B. Obstacle Avoidance 
			// mit Seek) zu einem "Zittern" des Fahrzeuges. 
			Vector2d	lastLocalx = new Vector2d(m_localx);
			
			// Sampling ueber 2 Frames hinweg
			temp.setX((temp.getX() + m_prevLocalX.getX() + m_localx.getX()) / 3);
			temp.setY((temp.getY() + m_prevLocalX.getY() + m_localx.getY()) / 3);	
			
			m_prevLocalX.setX(lastLocalx.getX());
			m_prevLocalX.setY(lastLocalx.getY());
			
			temp.normalize();
			
			setLocalx(temp);
		}
	}
	
	/** 
	 * Adds a new waypoint to the list of current waypoints
	 * @param waypoint New waypoint to add to the list
	 */
	public void addWaypoint(Point2d waypoint)
	{
	    m_path.add(waypoint);
	}
	
	/**
	 * Removes the first waypoint from the list
	 */
	public void removeWaypoint()
	{
	    m_path.removeFirst();
	}
	
	/** 
     * Restores the position, the direction the vehicle is facing 
     * and the velocity back to the last saved state.
     */
	public void restoreState()
	{
		m_pos = m_oldPos;
		m_vel = m_oldVel;
		m_localx = m_oldLocalx;
	}
	
	/** 
     * Makes a  copy of the last position, the direction the vehicle is facing 
     * and the velocity. 
     */
	public void saveState()
	{
		m_oldPos = new Point2d(m_pos);
		m_oldVel = new Vector2d(m_vel);
		m_oldLocalx = new Vector2d(m_localx);
	}
	
	/** Sets a attribute specified by the name
	 * @param name name of the attribute
	 * @param value value of the attribute
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
		
			if (name.equals("x"))		{	m_pos.setX(val);	}
			if (name.equals("y"))		{	m_pos.setY(val);	}
			if (name.equals("maxvel")) 	{ 	m_maxVel=val;		}
			if (name.equals("maxforce")){ 	m_maxForce=val;		}
			if (name.equals("radius")) 	{ 	m_radius=val;		}
			if (name.equals("maxvel")) 	{ 	m_maxVel=val;		}
			if (name.equals("velx")) 	{ 	m_vel.setX(val);	}
			if (name.equals("vely")) 	{ 	m_vel.setY(val);	}
		}
		catch (Exception e) {}				
		
	}
			
			
	
	/** Sets the mass of the vehicle
	 * @param mass New mass
	 */
	public void setMass(double mass) 
	{ 
		m_mass = mass; 
	}
	 	 
	/** Sets the maximum force of the vehicle
	 * @param mass New mximum force
	 */
	public void setMaxForce(double maxForce) 
	{ 
		m_maxForce = maxForce; 
	}
	
	/** Sets the maximum velocity of the vehicle
	 * @param mass New mximum velocity
	 */
	public void setMaxVel(double maxVel) 
	{ 
		m_maxVel = maxVel; 
	}
	
	/** 
	 * Sets the current pathfollowing state for the vehicle
	 * @param state New pathfollowing state
	 */
	public void setPathState(int state)
	{
	    m_pathState = state;
	}
	    	    
	/** Returns the current velocity
	 * @return Current velocity
	 */
	public void setVel(Vector2d vel) 
	{
		m_vel = vel; 
	}
	
	/** Returns the x position
	 * @return X position
	 */
	public void setX(double x) 
	{ 
		m_pos.setX(x); 
	}
	
	/** Returns the y position
	 * @return Y position
	 */
	public void setY(double y) 
	{ 
		m_pos.setY(y); 
	}		
	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////		
	
	/** Returns the current maximum velocity
	 * @return Maximum velocity
	 */
	public double getMaxVel() 
	{ 
		return m_maxVel; 
	}
	
	/** Returns the current maximum force
	 * @return Maximum force
	 */
	public double getMaxForce() 
	{ 
		return m_maxForce; 
	}				
	
	/** Returns the position before the last simulation step
	 * @return Position before last simulation step
	 */
	public Point2d getOldPos()
	{
		return m_oldPos;	
	}
	
	/** Returns the velocity before the last simulation step
	 * @return Velocity before last simulation step
	 */
	public Vector2d getOldVel() 
	{
		return m_oldVel; 
	}
	
	/** 
	 * Give acces to the path for the vehicle. 
	 * @return List of waypoints for the vehicle
	 */
	public LinkedList getPath()
	{
	    return m_path;
	}
	
	/** 
	 * Give acces to the pathfollowing state of the vehicle. 
	 * @return The current state of the vehicle
	 */
	public int getPathState()
	{
	    return m_pathState;
	}
	
	/** Returns the current velocity
	 * @return Current velocity
	 */
	public Vector2d getVel() 
	{
		return m_vel; 
	}
	
	/** Returns the x position
	 * @return X position
	 */
	public double getX() 
	{ 
		return m_pos.getX(); 
	}
	
	/** Returns the y position
	 * @return Y position
	 */
	public double getY() 
	{ 
		return m_pos.getY(); 
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

    public static final int FOLLOWING_PATH = 0;
    public static final int ARRIVED_TARGET = 1;    

    /** Current force	*/
	private Vector2d 	m_force = null; 
	/** Mass of the vehicle	*/
	private double 		m_mass = 0.0;   						
	/** Maximum force allowed to be applied	*/
	private double 		m_maxForce = 0.0;	
	/** Maximum velocity of the vehicle	*/
	private double 		m_maxVel = 0.0; 					
	/** The local x axis of the object before the simulation step */
	private Vector2d 	m_oldLocalx = null;
	/** The position of the object before the simulation step */
	private Point2d 	m_oldPos = null;
	/** The velocity of the object before the simulation step */
	private Vector2d 	m_oldVel= null;
	/** List of all the waypoints for this vehicle */
	private LinkedList  m_path = null;
	/** Current state for the pathfollowing */
	private int         m_pathState = FOLLOWING_PATH;
	/** Copy of old local x axis for smoothing rotations */
	private Vector2d	m_prevLocalX = null;		
	/** Current velocity */
	private Vector2d 	m_vel = null;  	
}
