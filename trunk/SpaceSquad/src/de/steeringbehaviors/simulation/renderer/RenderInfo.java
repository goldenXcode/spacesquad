package de.steeringbehaviors.simulation.renderer;

import java.lang.Comparable;
import java.awt.Color;
import java.util.*;

/** Base class for all shapes used by the renderer
 */
public class RenderInfo implements Comparable, Intersection
{   
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	 
    /** Constructs a simple shape
     */
    public RenderInfo()
    {
        m_z = 0;
        m_pos = new Point2d(0, 0);
    }

    /** Constructs a shape including the z position
     * @param z The z position
     */
    public RenderInfo(int z)
    {
        m_z = z;
        m_pos = new Point2d(0, 0);
    }
    
    /** Constructs a new shape
     * @param z The z position
     * @param parent The parent Geometrie
     */
    public RenderInfo(int z, Geometrie parent)
    {
        m_z = z;
        m_parent = parent;
        m_pos = new Point2d(0, 0);
    }

	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////

	/** 
	 * Transforms the shape in local space.
	 * @param relMove Vector used to move the points	 
	 */
	public void moveCenter(Vector2d relMove)
	{		
		
	}

	/** 
     * Sets the color of the shape
     * @param c The new color of the shape
     */
    public void setColor(Color c) 
    { 
        m_color = c; 
    }	    

    /** Sets the parent Geometrie object
     * @param parent The parent Geometrie object
     */
    public void setParent(Geometrie parent)
    {
        m_parent = parent;
    }
    
    /** Sets the position
     * @param pos The new position
     */
    public void setPos(Point2d pos) 
    {
	m_pos = pos;
	}	
	
	/**
     * Scales the complete object along the x axis according to the factor. Works only
     * if the factor is greater zero.
     * @param factor Scale to apply to the object
     */
	public void setScaleX(double scaleX)
	{
		m_scaleX = scaleX;
		
		return;
	}
	
	/**
     * Scales the complete object along the Y axis according to the factor. Works only
     * if the factor is greater zero.
     * @param factor Scale to apply to the object
     */
	public void setScaleY(double scaleY)
	{
		m_scaleY = scaleY;
		
		return;
	}

    /** Sets the z position
     * @param z The z position
     */
    public void setZ(int z)
    {
        m_z = z;
    }

	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////

	/** 
     * Returns the color of the shape
     * @return The color of the shape 
     */
    public Color getColor() 
    { 
        return m_color; 
    }

	/** 
	 * Returns the distance of the point on the shape that is farthest 
	 * away from the center of the geometrie.
     * @return The farthest distance from the center of the geometrie
     */
	public double getFarthestDistance()
	{
		return 0.0;
	}

	/** Returns the parent Geometrie object
     * @return The parent Geometrie object
     */
    public Geometrie getParent()
    {
        return m_parent;
    }
	/** 
	 * Returns the position
     * @return The position
     */
    public Point2d getPos() 
    { 
        return m_pos; 
    }
    
	/** Return the type of the object     
     * @return A type of shape defined in this class
     */
    public int getType()
    {
        return m_type;
    }

    /** 
     * Returns the z position
     * @return The z position
     */
    public int getZ()
    {
        return m_z;
    }

	/** 
     * Returns the scaling of the shape along the x axis
     * @return The scaling along the x axis
     */
	public double getScaleX()
	{
		return m_scaleX;	
	}
	
	/** 
     * Returns the scaling of the shape along the y axis
     * @return The scaling along the y axis
     */
	public double getScaleY()
	{
		return m_scaleY;	
	}
    
    //////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////
    
    /** Function used in comparable sets to compare two RenderInfo objects
     * @param o The other object to compare to
     * @return 0 = Objects equal, -1 = other object > this object, 1 = other object < this object
     */
    public int compareTo(Object o)
    {
        RenderInfo info = (RenderInfo) o;
        if (info.getZ() < m_z) return 1;
        if (info.getZ() > m_z) return -1;

        return 0;
    }        
    
    /**
     * Tests if the point is inside the shape
     * @param x The x position in screen coordinates
     * @param y The y position in screen coordinates
     * @param zoom The zoom factor used in drawing the shape
     * @return True if the point is inside, False if not
    */
    public boolean isInside(double x, double y)
    {
        return false;
    }        

	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////

    public static final int RENDERINFO = 0;
    public static final int CIRCLE = 1;
    public static final int VECTORSHAPE = 2;
    public static final int POLYGONSHAPE = 3;
    public static final int TILE = 4;
    public static final int INFOBOX = 5;
                
    /** The color of the shape */
    protected Color m_color = Color.black;
    /** This is the parent Geometrie object */
    protected Geometrie m_parent =null;    
    /** Position of the shape after transformation into view space */
    protected Point2d m_pos = null;
    /** The scale of the shape along the x axis */
    protected double m_scaleX = 1.0;    
    /** The scale of the shape along the y axis */
    protected double m_scaleY = 1.0;    
    /** The type of the shape. Also see the type constants defined in this class*/
    protected int m_type = RENDERINFO;
    /** Z position of the shape. This value is used to determine the order of drawing (eg. back to front) */
    protected int m_z = 0;
}
