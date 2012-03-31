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
import java.awt.Color;
import java.util.*;

import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Neighborhood;
import de.steeringbehaviors.simulation.simulationobjects.Obstacle;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;



/**
* ObstacleAvoidance
*
* Implements the ObstacleAvoidance behavior. This behavior is used to steer
* an object around obstacles in its way. 
*/
public class ObstacleAvoidance extends Behavior
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Constructor */
	public ObstacleAvoidance() 
	{   	  			
		init();
	}
	
	/** 
	 * Constructor 
	 *
	 * @param    radius      Radius of testing cylinder
	 * @param    vlength     Length of the testing cylinder
	 * @param    influence   influence of the behaviour
	 */
	public ObstacleAvoidance(double radius, double vlength, double influence) 
	{ 
		init();
		
		m_length = vlength;
		m_radius = radius;
		m_influence=influence;   	  	
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Inits the object */
	public void init()
	{
		m_behaviorName = "ObstacleAvoidance";
		
		m_testlength = 0;
		m_result = new Vector2d();	
		m_test = new Vector2d();
		m_influence=1;   
		m_length = 20;	  	
		m_radius = 1;
		m_obstacles = null;
	}
	
	/** Sets a attribute specified by the name
	* @param name name of the attribute
	* @param value value of the attribute
	* @param objectList hashtable of all steeringobjects
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
			
			if (name.equals("radius")) { m_radius=val; }
			if (name.equals("length")) { m_length=val; }
		}
		catch (Exception e) {}					
	}
	
	/** 
	 * Sets the array of obstacle to test for collisions each frame
	 * @param obstacles Array of obstacles
	 */
	public void setObstacles(Vector obstacles) 
	{ 
		m_obstacles = obstacles; 
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
		boolean result = false;
		
		// Recognize the Neighborhood class objects
		if (objectDesc.compareTo("Neighborhood") == 0)
		{
			m_neighborhood = (Neighborhood) specialObject;
			result = true;
		}
			
		return result;
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
	 * Calculates the resulting force vector for this frame
	 * @param v The vehicle
	 * @return Returns the resulting force
	 */
	public Vector2d calculate(Vehicle v)
	{     
		Vector2d	normal = new Vector2d();		// Ausweichvektor in lokalen Koordinaten
		Point2d		otherPos = new Point2d();		// Position des Hindernisses
		
		Iterator    it = null;
		
		Vector2d	delta = new Vector2d();			// Differenz zwischen den Objektpositionen
		Point2d		n_pos = new Point2d();			// Position des nahesten Objekts
		double		n_radius = 0;					// Radius des Hindernisses
		double		n_distance = Double.MAX_VALUE;	// Distanz des nahestens Objekts
		double		distFact = 0;
		double		intersect = -1;				
		
		m_result= normal;		
		m_nearest = null;
		m_obstacles = null;
		
		// Erzeuge Vector zum Testen im Bereich vor dem Fahrzeug  
		m_testlength = v.getVel().length() * m_length;	  	
		
		m_test.setX(v.getLocalX().getX());
		m_test.setY(v.getLocalX().getY());  	    	  	
		m_test.scale(m_testlength);  	  	  	  	
		
		// Test for a neighborhood object
		if (m_neighborhood != null)
		{
			it = m_neighborhood.getNearObstacles(v, m_radius * 40.0);
		}
				
		if (it == null) return m_result;

		while (it.hasNext())
		{
			m_tempObstacle = (Obstacle) it.next();
			
			// Objekte mit m_collision == false uebergehen...
			if (m_tempObstacle.getCollide() == false) continue;
			
			// Die Position des anderen Objekts in unser Koordinatensystem umwandeln
			otherPos.setX(m_tempObstacle.getPos().getX());
			otherPos.setY(m_tempObstacle.getPos().getY());								
			
			otherPos = v.worldToLocal(otherPos);
			
			intersect = -1;
																		
			// First test the boundingcircle for the nearest intersection			
			intersect = xaxis_circle_intersect(otherPos, m_tempObstacle.getRadius(), normal);						
			
			
			/**	Dieser Teil muss überarbeitet werden!
			// If the boundingcircle is intersected, test the polygon itself
			if (m_tempObstacle.getName() ==  "RectObstacle")
			{
				Point2d p1 = m_tempObstacle.localToWorld(m_tempObstacle.getBoundingBox().getp1());
				Point2d p2 = m_tempObstacle.localToWorld(new Point2d(m_tempObstacle.getBoundingBox().getp2().getX(), m_tempObstacle.getBoundingBox().getp1().getY())); 
				Point2d p3 = m_tempObstacle.localToWorld(m_tempObstacle.getBoundingBox().getp2()); 
				Point2d p4 = m_tempObstacle.localToWorld(new Point2d(m_tempObstacle.getBoundingBox().getp1().getX(), m_tempObstacle.getBoundingBox().getp2().getY())); 
				
				intersect = xaxis_rect_intersect(v, n_pos, p1, p2, p3, p4, normal);  								
			}							
			*/ 
			
			// If the intersection is nearer than the last one, make the
			// more detailed test
			if ((intersect > 0) && (intersect < n_distance))
			{				
				Point2d p = null;
				double tempIntersect = intersect + 1.0;
				
				m_polyPoints.clear();
				
				Iterator tObstacle = m_tempObstacle.getPoints().iterator();				
				while (tObstacle.hasNext())
				{
						p = (Point2d) tObstacle.next();												
						
						m_polyPoints.add(p);
				}

				// get the nearest distance, point of intersection, and the normal vector there
				tempIntersect = xaxis_poly_intersect(v, normal);
				if (tempIntersect <= intersect)	intersect = tempIntersect;
			}
			
			// The object with the nearest intersection along the x axis is chosen for avoiding
			if ((intersect > 0) && (intersect < n_distance))
			{
				n_distance = intersect;
				m_nearest = m_tempObstacle;
				n_pos.setX(otherPos.getX());
				n_pos.setY(otherPos.getY());  		  			  			  			
				m_result = normal;
			}
		}
		
		// In case of no nearest obstacle, just return a force of zero
		if ((m_nearest == null) || (n_distance >= m_testlength) )
		{
			m_result.setX(0);
			m_result.setY(0);
			return m_result;
		}
		
		// Calculate the force for the nearest obstacle
		m_theNormal = m_result;                		
		
		m_result.normalize();				
		
		// The force is based on the distance of the intersection and the 
		// center of the vehicle		
		if (m_testlength > 0)
			distFact = 1 - (n_distance / m_testlength);
		else
		distFact = 0;	
		
		double force = v.getMaxForce() * distFact;
		
		if (force > v.getMaxForce()) force = v.getMaxForce();
		
		m_result.scale(force * m_influence);
		
		//m_result.setX(-force);                
		
		// Steering Vektor in Weltkoordinaten umrechnen
		m_result = v.localToWorld(m_result);		
		
		return m_result;  	  	  	  	
	}
	
	/** 
	 * xaxis_circle_intersect
	 *
	 * Test a circle for intersection with the local x axis
	 * of the vehicle. Returns -1 if there is no intersection
	 * else returns the x position of the intersection. The normal
	 * parameter is set to either point to +1 of -1 , depending of
	 * the position of the object.
	 * @param o_pos Position of the object
	 * @param radius Radius of the object
	 * @param normal Resulting steering force in local coordinates
	 * @return -1 if no intersection, minimum x position if intersection
	 */
	protected double xaxis_circle_intersect(Point2d o_pos,double radius, Vector2d normal) 
	{
		if (Math.abs(o_pos.getY()) > radius)
			return -1;
		
		// Ausweichvektor erzeugen
		normal.setX(0);
	// Falls wir direkt auf ein Hinderniss zusteuern, weichen wir nach rechts aus
	if (o_pos.getY() < 0)
		normal.setY(1);
	else
		normal.setY(-1);
				
		return (o_pos.getX() - Math.sqrt(radius * radius - o_pos.getY() * o_pos.getY()));
	}
	
	/** 
	 * xaxis_poly_intersect
	 *
	 * Test a polygon for intersection with the local x axis
	 * of the vehicle. Returns -1 if there is no intersection
	 * else returns the x position of the intersection point. The 
	 * normal parameter is set to the nromal vector of the line segment
	 * at the intersection point in vehicle coordinates. 
	 * @param v Our vehicle	 
	 * @param normal [Out ]Resulting steering force in local coordinates
	 * @return -1 if no intersection, minimum x position if intersection
	 */
	protected double xaxis_poly_intersect(Vehicle v, Vector2d normal)
	{
		Point2d startPoint= null, nextPoint = null;
		Point2d firstPoint = null;
		Iterator it;
		double x = Double.MAX_VALUE;
		double intersect;
		
		it = m_polyPoints.iterator();
		if (it.hasNext())
		{
			firstPoint = (Point2d) it.next();
			
			firstPoint = v.worldToLocal(firstPoint);
			startPoint = firstPoint;
		}
		
		while (it.hasNext())
		{
			nextPoint = (Point2d) it.next();
			
			nextPoint = v.worldToLocal(nextPoint);
			// The line segment goes from startPoint to nextPoint
			
			// If the two Y values are of different signs, the x axis is intersected
			if (startPoint.getY() * nextPoint.getY() < 0.0)
			{
				// Der Schnittpunkt mit der x-Achse wird wie folgt berechnet:
			// y = a*(x - x1) + y1
			// a = (y2 - y1) / (x2 - x1)
			// => x = (-y1 * (x2 - x1)) / (y2 - y1) + x1
			intersect = (-startPoint.getY() * (nextPoint.getX()  - startPoint.getX())) /
						(  nextPoint.getY() - startPoint.getY()) + startPoint.getX();
			
			// If this is the nearest intersection point...
			if ((intersect > 0.0)  && (x > intersect))
			{
				x = intersect;
				// Calculate the normal vector
				normal = new Vector2d();
				normal.setX(nextPoint.getY() - startPoint.getY());
					normal.setY(-(nextPoint.getX() - startPoint.getX()));        			
					
					normal.normalize();
			}
			
			}						
			
			// Update startpoint 
			startPoint = nextPoint;
		}
		
		// Test the last line segment, too
		if (startPoint != firstPoint)
		{
			nextPoint = firstPoint;
			if (startPoint.getY() * nextPoint.getY() < 0.0)
			{
				// Make the same test as above!
				intersect = (-startPoint.getY() * (nextPoint.getX()  - startPoint.getX())) /
						(  nextPoint.getY() - startPoint.getY()) + startPoint.getX();
			
			// If this is the nearest intersection point...
			if ((intersect > 0.0)  && (x > intersect))
			{
				x = intersect;
				// Calculate the normal vector
				normal = new Vector2d();
				normal.setX(nextPoint.getY() - startPoint.getY());
					normal.setY(-(nextPoint.getX() - startPoint.getX()));  
					
					normal.normalize();
			}
		}
		}
		
		if (x == Double.MAX_VALUE) x = -1.0;
		
		return x;
	}
	
	/** 
	 * xaxis_rect_intersect
	 *
	 * Tests a rectangle for intersection with the local x axis.
	 * Returns -1 if there is no intersection, otherwise the
	 * x positon of the intersection is returned and the normal
	 * parameter is set to the surface normal at the intersection.
	 * @param v Our vehicle
	 * @param o_pos Position of the obstacle
	 * @param p1 Rectangle point: left up
	 * @param p2 Rectangle point: right up
	 * @param p3 Rectangle point: right down
	 * @param p4 Rectangle point: left down
	 * @param normal Returns the normal vector used for steering
	 * @return -1 if no intersection, minimum x position if intersection
	 */
	protected double xaxis_rect_intersect(Vehicle v, Point2d o_pos,Point2d p1, Point2d p2, Point2d p3, Point2d p4, Vector2d normal)
	{
		double minX = Double.MAX_VALUE;			  
		double x;
		Vector2d delta;
		
		// Koordinaten von Weltkoordinaten in lokale Koordinaten umrechnen
		p1 = v.worldToLocal(p1);
		p2 = v.worldToLocal(p2);
		p3 = v.worldToLocal(p3);
		p4 = v.worldToLocal(p4);
		
		// Ueberpruefe, ob das Rechteck die x-Achse schneidet
				
		// Da wir in Vehicle-Koordinaten rechnen, muessen die zwei Punkte
		// einer Linie auf gegenueberliegenden Seiten der x-Achse liegen.
		// => y1 * y2 < 0 bedeudet die Linie schneidet die x-Achse
                
	// Der Schnittpunkt mit der x-Achse wird wie folgt berechnet:
	// y = a*(x - x1) + y1
	// a = (y2 - y1) / (x2 - x1)
	// => x = (-y1 * (x2 - x1)) / (y2 - y1) + x1
	
	// Der Normalenvektor ergibt sich aus:
	// 

		if (p1.getY() * p2.getY() < 0)
		{
			x = p1.getX() + ((-p1.getY() * (p2.getX() - p1.getX())) / (p2.getY() - p1.getY()));
			if ((x < minX) && (x > 0)) 			
			{
				minX = x;	
				normal.setX(p2.getY() - p1.getY());
				normal.setY(-(p2.getX() - p1.getX()));
			}
		}
		
		if (p2.getY() * p3.getY() < 0)
		{
			x = p2.getX() + ((-p2.getY() * (p3.getX() - p2.getX())) / (p3.getY() - p2.getY()));
			if ((x < minX) && (x > 0))
			{
				minX = x;
				normal.setX(p3.getY() - p2.getY());
				normal.setY(-(p3.getX() - p2.getX()));
			}
				
		}
		
		if (p3.getY() * p4.getY() < 0)
		{
			x = p3.getX() + ((-p3.getY() * (p4.getX() - p3.getX())) / (p4.getY() - p3.getY()));
			if ((x < minX) && (x > 0)) 			
			{
				minX = x;
				normal.setX(p4.getY() - p3.getY());
				normal.setY(-(p4.getX() - p3.getX()));
			}
		}
		
		if (p4.getY() * p1.getY() < 0)
		{
			x = p4.getX() + ((-p4.getY() * (p1.getX() - p4.getX())) / (p1.getY() - p4.getY()));
			if ((x < minX) && (x > 0))			
			{
				minX = x;											
				normal.setX(p1.getY() - p4.getY());
				normal.setY(-(p1.getX() - p4.getX()));
			}
		}								
		
		normal.normalize();		
		
		if (minX == Double.MAX_VALUE) minX = -1;		
		
		return minX;  		
	}

	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////
 
	/** Resulting steering force */
	protected Vector2d	m_result;
	/** Vector used for testing for possible collsions */
	protected Vector2d	m_test;					
	/** Radius of the testing cylinder */
	protected double 	m_radius; 	
	/** Length of the testing cylinder */
	protected double 	m_length;	
	/** Array of obstacles to test for collsions */
	protected Vector	m_obstacles;			
	/** Array used to store the points of a polygon for intersection - testing */
	protected Vector	m_polyPoints = new Vector(3, 5);
	/** Current length of the testing cyclinder */
	protected double	m_testlength;		 	
	/** Object nearest to our vehicle */
	protected Obstacle	m_nearest = null;
	/** A neighborhood object used for spatial queries */
	protected Neighborhood m_neighborhood = null;
	/** Current object to be tested */
	protected Obstacle	m_tempObstacle = null;	
	/** Resulting normal vector */
	protected Vector2d  m_theNormal;	
}