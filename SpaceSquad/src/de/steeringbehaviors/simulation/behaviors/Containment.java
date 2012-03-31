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

import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Neighborhood;
import de.steeringbehaviors.simulation.simulationobjects.Obstacle;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;


/**
* ObstacleAvoidance
*
* Implements the Containment behavior. This behavior is used to steer
* an object around obstacles in its way by using three testpoints. 
*/
public class Containment extends Behavior
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Constructor */
	public Containment() 
	{   	  	
		m_behaviorName = "Containment";
		init();
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Inits the object */
	public void init()
	{
		m_frontTest = new Vector2d(45,0);
		m_leftTest = new Vector2d(15,15);
		m_rightTest = new Vector2d(15,-15);
		m_influence=1;   
		
		m_frontIntersectionPoint1 = new Point2d();
		m_frontIntersectionPoint2 = new Point2d();
		
		m_leftIntersectionPoint1 = new Point2d();
		m_leftIntersectionPoint2 = new Point2d();
		
		m_rightIntersectionPoint1 = new Point2d();
		m_rightIntersectionPoint2 = new Point2d();

		m_behaviorName = "Containment";
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
			
	//		if (name.equals("radius")) { m_radius=val; }
	//		if (name.equals("length")) { m_length=val; }
			if (name.equals("frontdistance")) { m_frontTest=new Vector2d(val,0); }
			if (name.equals("sidex")) { m_leftTest.setX(val); m_rightTest.setX(val); }
			if (name.equals("sidey")) { m_leftTest.setY(-val); m_rightTest.setY(val); }
		}
		catch (Exception e) {}					
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
	* Returns the angle and the distance to the collided object.
	* Returns null, if no collision is detected.
	* The first element of the double[] array is the distance, the
	* second one is the angle
	* @param v The vehicle
	* @param o The obstacle
	* @param testvector Testvector for collision detection
	* @return A double array. The first element represents the distance
	* to the object, the second one the angle. 
	* Null is returned, if no collision is detected
	*/
	protected Double[] getCollisionDetails(Vehicle v, Obstacle o, Vector2d testvector, Point2d retPoint1, Point2d retPoint2)
	{

		// 1. search the edge which crosses the testvector
		// 2. calculate the distance / angle to the intersected line segment
		double nearestIntersection = 300000;
		Point2d nearestEdgePoint1 = new Point2d();
		Point2d nearestEdgePoint2 = new Point2d();

		Point2d firstPoint = new Point2d();
		Point2d p1 = new Point2d();
		Point2d p2 = new Point2d();
		
		Point2d local_p1 = new Point2d();
		Point2d local_p2 = new Point2d();		

		Iterator pointIter= o.getPoints().iterator();
		
		// create a dummy object which is directed by the testvector
		Geometrie dummyObject=new Geometrie(new Point2d(0,0));
		dummyObject.setLocalx(new Vector2d(testvector));

		// store the testvector-length for faster access
		double testvector_length = testvector.length();

	
		// calculating the x/y-koordinates of the testpoint
		Vector2d temp = v.localToWorld(testvector);
		double testX = v.getPos().getX() + temp.getX();
		double testY = v.getPos().getY() + temp.getY();
		
		// intersection true/false
		boolean intersects=false;
		

		// get the first point
		if (pointIter.hasNext()) firstPoint=p2=(Point2d) pointIter.next();
		
		// get the other points
		while (true)
		{
			
			if (pointIter.hasNext())
			{
				// the next line-segment
				p1=p2;
				p2=(Point2d) pointIter.next();
			}
			else if (firstPoint==p2) 
			{	
				// the last line-segment is already done, break
				break;
			}
			else
			{
				// the last line-segment
				p1=p2;
				p2=firstPoint;
			}
	
			
			// 1st exclusion test (before localtoworld)
			if (v.getLocalX().getX()>0)
			{
				if ((p1.getX() > testX) && (p2.getX() > testX)) continue;
				if ((p1.getX() < v.getPos().getX()) && (p2.getX() < v.getPos().getX() )) continue;
			}
			
			if (v.getLocalX().getX()<0)
			{
				if ((p1.getX() < testX) && (p2.getX() < testX)) continue;
				if ((p1.getX() > v.getPos().getX()) && (p2.getX() > v.getPos().getX() )) continue;
			}
		
			if (v.getLocalX().getY()>0)
			{
				if ((p1.getY() > testY) && (p2.getY() > testY)) continue;
				if ((p1.getY() < v.getPos().getY()) && (p2.getY() < v.getPos().getY() )) continue;
			}
			
		/*	
			if (v.getLocalX().getY()<0)
			{
				if ((p1.getY() < testY) && (p2.getY() < testY)) continue;
				if ((p1.getY() > v.getPos().getY()) && (p2.getY() > v.getPos().getY() )) continue;
			}*/
		
			// into local koordinates
			local_p1 = dummyObject.worldToLocal(v.worldToLocal(p1));
			local_p2 = dummyObject.worldToLocal(v.worldToLocal(p2));
					
			// 2nd exclusion test:
			// if both point are over or under the x-axis -> break
			if ( ((local_p1.getY() > 0) && (local_p2.getY() > 0)) ||
			     ((local_p1.getY() < 0) && (local_p2.getY() < 0)) ) continue;
			
			// 3rd exclusion test;
			// if p1 is under and p2 over the x-axis, the linesegment is
			// backfaced
			if ((local_p1.getY() > 0) && (local_p2.getY() < 0)) continue;
			
			
			// 4th exclusion test:
			if ( (local_p1.getX() > testvector_length) &&
			     (local_p2.getX() > testvector_length)) continue;
			
			if ( (local_p1.getX() < 0) &&
			     (local_p2.getX() < 0)) continue;
			
			
			// now, test if the edge intersects the x-axis 
			// within the testvector_length
			
			// y = a*(x - x1) + y1
			// a = (y2 - y1) / (x2 - x1)
			// => x = (-y1 * (x2 - x1)) / (y2 - y1) + x1
			
			double intersection = 	(-local_p1.getY() *  ( local_p2.getX() - local_p1.getX() )) / 
						(local_p2.getY() - local_p1.getY()) + local_p1.getX();
			
			//System.out.println("intersection-value: "+intersection);
			// 5th exclusion test:
			// intersection within the testvector_length
			if (intersection > testvector_length) continue;
			
			
			if ((intersection > 0) && (intersection < nearestIntersection))
			{
				intersects=true;
				nearestIntersection=intersection;
				nearestEdgePoint1 = local_p1;
				nearestEdgePoint2 = local_p2;
				
				
				retPoint1.setX(p1.getX());
				retPoint1.setY(p1.getY());
			
				retPoint2.setX(p2.getX());
				retPoint2.setY(p2.getY());
			}	
		} 		
		
		if (intersects)
		{
			Double[] ret = new Double[2];
			
			ret[0] = new Double(nearestIntersection);
			
			// calculate the angle between the testvector and the intersected
			// line segment
			ret[1] = new Double(java.lang.Math.atan2( -nearestEdgePoint1.getY() , (nearestIntersection-nearestEdgePoint1.getX()) ));
			
			//System.out.println("intersection detected. dist / angle : "+ret[0]+ " / "+ret[1]);
			
			return ret;
		}

		// if no continue occured, return null because there
		// is no intersection with the obstacle 		
		return null;
	}
	
	/** 
	 * Calculates the resulting force vector for this frame
	 * @param v The vehicle
	 * @return Returns the resulting force
	 */
	public Vector2d calculate(Vehicle v)
	{
		 
	//System.out.println("calculate");	
		
		// Front test
		if (m_neighborhood == null) System.out.println("Neighborhood null");
		
		Iterator it = m_neighborhood.getNearObstacles(v, m_frontTest.length());

	
	Point2d p1=new Point2d();
	Point2d p2= new Point2d();
	
	Point2d mp1 = new Point2d();
	Point2d mp2 = new Point2d();
		
		m_frontIntersection = false;
		m_frontDistance = 30000;
		
		m_leftIntersection = false;
		m_leftDistance = 30000;
		
		m_rightIntersection = false;
		m_rightDistance = 30000;
		
			
		while (it.hasNext())
		{     
			m_info=getCollisionDetails(v, (Obstacle) it.next(), m_frontTest, m_frontIntersectionPoint1 , m_frontIntersectionPoint2);
			
			// if intersection is detected
			if (m_info!=null)
			{    
				// check if obstacle is nearer than the previous ones
				if (m_info[0].doubleValue() < m_frontDistance)
				{ 
					m_frontIntersection = true;
					m_frontDistance = m_info[0].doubleValue();
					m_frontAngle = m_info[1].doubleValue();
				}
			}
		}
		  
	
		// Left/Right test
		it = m_neighborhood.getNearObstacles(v, m_leftTest.length());
	
		while (it.hasNext())
		{
			Obstacle tempObstacle= (Obstacle) it.next();
			m_info = getCollisionDetails(v, tempObstacle, m_leftTest, m_leftIntersectionPoint1, m_leftIntersectionPoint2);
			if (m_info != null)
			{	
				if (m_info[0].doubleValue() < m_leftDistance)
				{ 
					m_leftIntersection = true;
					m_leftDistance = m_info[0].doubleValue();
					m_leftAngle = m_info[1].doubleValue();
				}
			}
			
		
			m_info = getCollisionDetails(v, tempObstacle, m_rightTest, m_rightIntersectionPoint1, m_rightIntersectionPoint2);
			if (m_info != null)
			{
				if (m_info[0].doubleValue() < m_rightDistance)
				{ 
					m_rightIntersection = true;
					m_rightDistance = m_info[0].doubleValue();
					m_rightAngle = m_info[1].doubleValue();
				}
			}
			
		}
	
		
		// now, generate the steering vector which depends on the 
		// attributes front/right/left-distance/angle
		
		Vector2d force = new Vector2d(0,0);
		
		if (m_frontIntersection)
		{
			
			double factor = java.lang.Math.sin(m_frontAngle)*m_frontDistance;
			
			double dy = m_frontIntersectionPoint2.getY() - m_frontIntersectionPoint1.getY();
			double dx = m_frontIntersectionPoint2.getX() - m_frontIntersectionPoint1.getX();
			
			Vector2d norm = new Vector2d(-dy,dx);
			norm.normalize();
			norm.scale(factor);
			
			//System.out.println("factor: "+factor);
			//System.out.println("norm: "+norm.getX()+" / "+norm.getY());
			//System.out.println("p1: "+mp1.getX()+ " / " +mp1.getY());
			
			force = force.add(norm);
			
			/*Vector2d temp=new Vector2d(-v.getLocalX().getY(),v.getLocalX().getX());
				temp.normalize();
				temp.scale(factor);
			return temp;*/
			
			
			
			/*if (front_angle>0)
			{
				if (front_angle<1.57)
				{ 	Vector2d temp=new Vector2d(v.getLocalX().getY(),-v.getLocalX().getX());
						temp.normalize();
						temp.scale(0.5);
					return temp;
				}
				else
				{ 	Vector2d temp=new Vector2d(-v.getLocalX().getY(),-v.getLocalX().getX());
					temp.normalize();
					temp.scale(0.5);
					return temp;
				
					
				}
			}*/
		}
		
		if (m_leftIntersection)
		{
			
			double factor = java.lang.Math.sin(m_leftAngle)*m_leftDistance;
			
			double dy = m_leftIntersectionPoint2.getY() - m_leftIntersectionPoint1.getY();
			double dx = m_leftIntersectionPoint2.getX() - m_leftIntersectionPoint1.getX();
			
			Vector2d norm = new Vector2d(-dy,dx);
			norm.normalize();
			norm.scale(factor);
			
			force = force.add(norm);
		}
	
		if (m_rightIntersection)
		{
			
			double factor = java.lang.Math.sin(m_rightAngle)*m_rightDistance;
			
			double dy = m_rightIntersectionPoint2.getY() - m_rightIntersectionPoint1.getY();
			double dx = m_rightIntersectionPoint2.getX() - m_rightIntersectionPoint1.getX();
			
			Vector2d norm = new Vector2d(-dy,dx);
			norm.normalize();
			norm.scale(factor);
			
			force = force.add(norm);
		}
		
		if (m_frontIntersection || m_leftIntersection || m_rightIntersection) return force;
	
		return new Vector2d(0,0);     
		
	} // calculate(Vehicle v)
	
	
	//////////////////////////////////////////////////////////////////////
	//
	// VARIABLES
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Vector used for testing the front for possible collisions */
	protected Vector2d m_frontTest;
	/** Vector used for testing the left side for possible collisions */
	protected Vector2d m_leftTest;
	/** Vector used for testing the right side for possible collisions */
	protected Vector2d m_rightTest;
	/** A neighborhood object used for spatial queries */
	protected Neighborhood m_neighborhood = null;
	
	/** Predicates, whether the front test vector intersects an obsacle or not */
	protected boolean m_frontIntersection = false;
	/** Predicates, whether the left test vector intersects an obsacle or not */
	protected boolean m_leftIntersection = false;
	/** Predicates, whether the right test vector intersects an obsacle or not */
	protected boolean m_rightIntersection = false;
		
	/** Used as return value */
	protected Double[] m_info = null;
	
	/** The distance to the obstacle in front of the vehicle*/
	protected double m_frontDistance = 30000;
	/** The angle to the obstacle in front of the vehicle */
	protected double m_frontAngle = 0;
	/** The first point of an intersected line-segment in front of the vehicle */
	protected Point2d m_frontIntersectionPoint1;
	/** The second point of an intersected line-segment in front of the vehicle */
	protected Point2d m_frontIntersectionPoint2;
	
	/** The distance to the obstacle on the left side of the vehicle */
	protected double m_leftDistance = 30000;
	/** The angle to the obstacle on the left side of the vehicle */
	protected double m_leftAngle = 0;
	/** The first point of an intersected line-segment on the left side of the vehicle */
	protected Point2d m_leftIntersectionPoint1;
	/** The second point of an intersected line-segment on the left side of the vehicle */
	protected Point2d m_leftIntersectionPoint2;
	
	/** The distance to the obstacle on the right side of the vehicle */
	protected double m_rightDistance = 30000;
	/** The angle to the obstacle on the right side  of the vehicle */
	protected double m_rightAngle = 0;
	/** The first point of an intersected line-segment on the right side of the vehicle */
	protected Point2d m_rightIntersectionPoint1;
	/** The second point of an intersected line-segment on the right side of the vehicle */
	protected Point2d m_rightIntersectionPoint2;
}