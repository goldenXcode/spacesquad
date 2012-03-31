
package de.steeringbehaviors.simulation.behaviors;

/**
 * The BehaviorInfo class is used to store some data about the
 * currently acive behaviors of a vehicle. This data could be
 * used to display what is happening inside the mind of a vehicle.
 */
public class BehaviorInfo
{
	/** The name of the behavior, for example 'Arrive' */
	public String   m_behaviorName;
	/** The last force for this behavior. The value should be in the range 0.0 to 1.0 (to represent a percentage) */
	public double   m_lastForce = 0.0;
}