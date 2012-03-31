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
package de.steeringbehaviors.simulation.renderer;


import java.util.*;
import java.awt.*;

/** This represents a simple 2d rendering system. It supports panning and zooming over the complete scene.
 * The shapes (based on RenderInfo) are first clipped against the viewpoint. The drawing is based on the z position and done fr´om back to front.
 */
public class SteeringRenderer
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
    /** Contructs an instance of the Renderer
     */
    public SteeringRenderer()
    {
        init(640, 480, 0, 0);
    }

    /** Constructs an instance of the renderer
     * @param width The width of the viewing rectangle
     * @param height The height of the viewing rectangle
     */
    public SteeringRenderer(int width, int height)
    {
        init(width, height, 0, 0);
    }

	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////

    private void init(int width, int height, int relX, int relY)
    {
        m_screenWidth = width;
        m_screenHeight = height;
        m_relX = relX;
        m_relY = relY;

        m_visibleInfo = new ArrayList();
        m_clickedShapes = new Vector(2, 2);
    }
	
	/**
     * Allow panning to use negative values
     * @param freePann New state of free panning
     */    
	public void setAllowFreePann(boolean freePann)
	{
   		m_allowFreePann = freePann;
	}
	
	/**
	 * Sets the color of underlying grid
	 * @param gridColor New color of the grid
	 */
	public void setGridColor(Color gridColor)
	{
		m_gridColor = gridColor;
	}

	/**
	 * Sets the spacing of underlying grid in the y-axis
	 * @param gridWidth New y-axis spacing of the grid
	 */
	public void setGridHeight(double gridHeight)
	{
		m_gridHeight = gridHeight;
	}

	/**
	 * Sets the spacing of underlying grid in the x-axis
	 * @param gridWidth New x-axis spacing of the grid
	 */
	public void setGridWidth(double gridWidth)
	{
		m_gridWidth = gridWidth;
	}

	/**
	 * Sets the ingnoreVisiblity status of the renderer
	 * @visible Ignore visibility information 
	 */
    public void setIgnoreVisiblity(boolean ignoreVisibility ) 
    { 
    	m_ignoreVisiblity = ignoreVisibility ; 
    }        	        

    /** Sets the relative x position of the viewing rectangle
     * @param relX Relative x position
     */
    public void setRelX(int relX)
    {
		// Free panning allows to use negative relative positions
		if (m_allowFreePann)
		{
			m_relX = relX;
		}
		else
		{
			if (relX >= 0) 
			{
			    if (relX < m_sceneWidth - (int) (m_screenWidth / m_zoom))
			        m_relX = relX;
			    else
			        m_relX = m_sceneWidth - (int) (m_screenWidth / m_zoom);
			}
			else
			{
			    m_relX = 0;
			}        
		}
    }

    /**
     * Sets the relative y position of the viewing rectangle
     * @param relY Relative y position
     */
    public void setRelY(int relY)
    {
    	// Free panning allows to use negative relative positions
		if (m_allowFreePann)
		{
			m_relY = relY;
		}
		else
		{
	        if (relY >= 0) 
	        {
	            if (relY < m_sceneHeight - (int) (m_screenHeight / m_zoom))
	                m_relY = relY;
	            else
	                m_relY = m_sceneHeight - (int) (m_screenHeight  / m_zoom);
	        }
	        else
	        {
	            m_relY = 0;
	        }
        }
    }

	/** 
	 * Sets the height of the scene in pixel
     * @param height Height in pixel
     */
	public void setSceneHeight(int height)
	{
		m_sceneHeight = height;
	}
	
	/** 
	 * Sets the width of the scene in pixel
     * @param width Width in pixel
     */
	public void setSceneWidth(int width)
	{
		m_sceneWidth = width;
	}

	/** Sets the width of the viewing rectangle
     * @param width Width in pixel
     */
    public void setScreenWidth(int width)
    {
        if (width > 0) m_screenWidth = width;
    }

    /**
     * Sets the height of the viewing rectangle
     * @param height Height in pixel
     */
    public void setScreenHeight(int height)
    {
        if (height > 0) m_screenHeight = height;
    }

	/**
     * Sets the state of the background grid
     * @param showGrid Flag to turn the grid on or off
     */
	public void setShowGrid(boolean showGrid)
	{
		m_showGrid = showGrid;	
	}

    /** Sets the new zoom factor
     * @param zoom New zoom factor
     */
    public void setZoom(double zoom)
    {
        if (zoom > 0.0)
            m_zoom = zoom;
    }
    
    //////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
        
	/**
     * Returns true if panning is allowed to used negative values
     * @return State of free panning
     */    
	public boolean allowFreePann()
	{
   		return m_allowFreePann;
	}
        
	/**
	 * Sets the color of underlying grid
	 * @param gridColor New color of the grid
	 */
	public Color getGridColor()
	{
		return m_gridColor;
	}        
        
    /**
     * Returns the tim ein ms used for rendering the last frame
     * @return Time used for rendering the last frame
     */    
    public long getFrameTime()
    {
        return m_frameTime;
    }            
    
    /**
	 * Returns the ingnoreVisiblity status of the renderer
	 * @return Ignore visibility information 
	 */
    public boolean getIgnoreVisiblity() 
    { 
    	return m_ignoreVisiblity; 
    }
    
    /** Access the relative x position
     * @return The relative x position of the viewing rectangle
     */
    public int getRelX()
    {
        return m_relX;
    }

    /** Access the relative y position
     * @return The relative y position of the viewing rectangle
     */
    public int getRelY()
    {
        return m_relY;
    }
    
    /**
     * Width of the scene in pixels
     * @return Width of the scene 
     */
    public int getSceneWidth()
    {
        return m_sceneWidth;
    }
    
    /**
     * Height of the scene in pixels
     * @return Height of the scene 
     */
    public int getSceneHeight()
    {
        return m_sceneHeight;
    }
    
    /**
     * The state of the background grid
     * @return The state of showing the background grid
     */
    public boolean getShowGrid()
    {
    	return m_showGrid;	
    }
    
    /** Get the number of visible shapes
     * @return The number of visible shapes
     */
    public int getVisibleCount()
    {
        return m_visibleInfo.size();
    }

    /** Get the visible shapes at the specified position
     * @param x The x position
     * @param y The y position
     * @return The list of visible shape at the x, y position. Can be empty!
     */
    public Vector getVisibleObjects(int x, int y)
    {
        Iterator it = m_visibleInfo.iterator();
        RenderInfo object = null;
                
        m_clickedShapes.clear();
                
        while (it.hasNext())
        {
            object = (RenderInfo) it.next();            
            if (object.isInside(x, y)) 
            {
                // Check, if only foreground shapes are requested
                if ((m_returnBackgroundShapes) || (object.getZ() >= 0))
                    m_clickedShapes.add(object);
            }
        }
        
        return m_clickedShapes;
    }
    
    /** Access the zoom factor
     * @return The current zoom factor
     */
    public double getZoom()
    {
        return m_zoom;
    }
    
    
    //////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////

	/** Draws a grid on the graphics context using the defined
	 * grid spacing. The grid is automatically adjusted to 
	 * relative screen position and zoomfactor.     
     * @param g Graphics context to render into
     */
	protected void drawGrid(Graphics g)
	{
		int zoomedWidth, zoomedHeight;
		int zoomedGridX, zoomedGridY;
		
		int startPosX, startPosY;			
        
        double relX, relY;                
        
        zoomedGridX = (int) ( m_zoom * m_gridWidth);
        zoomedGridY = (int) ( m_zoom * m_gridHeight);
        
        // Do not draw the grid if the spacing is too small
        if ( (zoomedGridX <= 2) || (zoomedGridY <= 2)) return;
        
        // Calculate the start position of the grid 
        if (m_relX >= 0)        
        {	
        	relX = 1.0 - (m_relX / m_gridWidth) - java.lang.Math.ceil(m_relX / m_gridWidth);
        }
        else
        {
        	relX =  -(m_relX / m_gridWidth) + java.lang.Math.ceil(m_relX / m_gridWidth);        	
        }
        if (m_relY >= 0)
        {
        	relY = 1.0 - (m_relY / m_gridHeight) - java.lang.Math.ceil(m_relY / m_gridHeight);
        }
        else
        {
        	relY =  -(m_relY / m_gridHeight) + java.lang.Math.ceil(m_relY / m_gridHeight);        	
        }
        
		startPosX = (int) (m_zoom * relX * m_gridWidth);
		startPosY = (int) (m_zoom * relY * m_gridHeight);			
	
		// Set the specific grid color
		g.setColor(m_gridColor);
		
		// Draw the vertical grid lines
		int x = startPosX;		
		while (x < m_screenWidth)
		{
			g.drawLine(x, 0, x, m_screenHeight);
			x += zoomedGridX;	
		}
		
		// Draw the horizontal grid lines
		int y = startPosY;		
		while (y < m_screenHeight)
		{
			g.drawLine(0, y, m_screenWidth, y);
			y += zoomedGridY;	
		}
		
	}

    /** Renders the scene into the graphics object
     * @param it Iterator over the Geometrie objects in the scene
     * @param g Graphics context to render into
     */
    public void renderScene(Iterator it, Graphics g)
    {
        Geometrie geom;
        Iterator shapes;
        RenderInfo	info;

        int minX, minY;
        int maxX, maxY;                
        
        double radius;
        
        int zoomedWidth, zoomedHeight;

        boolean visible = false;

        long oldTime = System.currentTimeMillis();
        
        // Clean visible shapes tree
        m_visibleInfo.clear();

        // 
        zoomedWidth = (int) (m_screenWidth / m_zoom);
        zoomedHeight = (int) (m_screenHeight / m_zoom);
                
        // Test all geometrie objects for visibility
        // This is just a rough test to see, if one point of the box
        // around the bounding circle is inside the view.
        while(it.hasNext())
        {
            visible = true;
            geom = (Geometrie) it.next();                        
            
            if ((!m_ignoreVisiblity) && (!geom.getVisible())) continue;            
            
            radius = geom.getRadius();

            minX = (int) geom.getPos().getX();
            minY = (int) geom.getPos().getY();

            maxX = (int) (minX + radius * geom.getScaleX() - m_relX);
            minX = (int) (minX - radius * geom.getScaleX() - m_relX);

            maxY = (int) (minY + radius * geom.getScaleY() - m_relY);
            minY = (int) (minY - radius * geom.getScaleY() - m_relY);

			// Discard all shapes that are not inside, do not cover, or cross the
			// viewing rectangle
			if ((minX < 0.0) && (maxX < 0.0)) visible = false;
			if ((minX > zoomedWidth) && (maxX > zoomedWidth)) visible = false;

            if ((minY < 0.0) && (maxY < 0.0)) visible = false;
			if ((minY > zoomedHeight) && (maxY > zoomedHeight)) visible = false;

            // If the geometrie is visible, enter all the RenderInfo's of this item
            // into the list of visible objects
            if (visible == true)
            {
                shapes = geom.getShapeIter();
                while (shapes.hasNext())
                {
                    info = (RenderInfo) shapes.next();

                    // Adjust the position info, so it is inside the viewing rectangle
                    info.getPos().setX(geom.getPos().getX() - m_relX);
                    info.getPos().setY(geom.getPos().getY() - m_relY);

                    m_visibleInfo.add(info);
                }
            }
        }

        // Draw only the visible objects
        renderVisible(g);
        
        m_frameTime = oldTime - System.currentTimeMillis();
    }

    /**
     * Renders only the shapes of the visible Geometrie objects
     * @param g Graphics context to render to
     */
    private void renderVisible(Graphics g)
    {
        Point2d		pos;
        Point2d         relPos;
        RenderInfo	info;
        int             posX;
        int             posY;
        
        // Variables used by special Circle rendering code
        Circle          drawCircle;
        int             radius;
        
        // Variables used by special VectorShape rendering code
        VectorShape     drawVectorShape;
        int             endX;
        int             endY;
        Point2d         drawPoint;
        Vector2d        vect;
        
        // Variables used by special Tile rendering code
        Tile            drawTile;
        int             width;
        int             height;
        
        // Variables used by special PolygonShape rendering code
        PolygonShape    drawPolygon;
        Iterator        pointIter;
        Geometrie       parent;
        Point2d         actPoint;
        double          relX;
        double          relY;
        Polygon         p;
        
        // Variables used by special InfoBox rendering code
        InfoBox         drawInfoBox;                
        
        if (m_showGrid) drawGrid(g);
        
        // Sort the array according to z info
        Collections.sort(m_visibleInfo);
        
        Iterator it = m_visibleInfo.iterator();

        while (it.hasNext())
        {
            info = (RenderInfo) it.next();

            switch (info.getType())            
            {                      
            case RenderInfo.CIRCLE:
                drawCircle = (Circle) info;
                pos = drawCircle.getPos();

                pos.setX(m_zoom * pos.getX());
                pos.setY(m_zoom * pos.getY());
                
                radius = (int) (m_zoom * info.getScaleX() * drawCircle.getRadius());
                posX = (int) pos.getX();
                posY = (int) pos.getY();

				drawCircle.setZoomedRadius(radius);

                g.setColor(drawCircle.getColor());

                if (drawCircle.getFilled() == true)
                {
                    g.fillOval( posX - radius, posY - radius, 2 * radius,2 * radius);
                }
                else
                {
                    g.drawOval( posX - radius, posY - radius, 2 * radius,2 * radius);
                }
                
                break;
                
            case RenderInfo.VECTORSHAPE:
                drawVectorShape = (VectorShape) info;
                
                // Copy the vector, so the original does not get messed up...
                vect = drawVectorShape.getDrawVector();
                vect.setX(drawVectorShape.getVector().getX() * info.getScaleX());
                vect.setY(drawVectorShape.getVector().getY() * info.getScaleY());                                
                
                vect = drawVectorShape.getParent().localToWorld(vect);
                
                vect.scale(drawVectorShape.getLength());
                
                g.setColor(drawVectorShape.getColor());                                
                
                // Adjust the position and length to the window and zoom factor
                pos = drawVectorShape.getPos();
                
                pos.setX(m_zoom * pos.getX());
                pos.setY(m_zoom * pos.getY());
                
                posX = (int) pos.getX();
                posY = (int) pos.getY();
                
                vect.setX(m_zoom * vect.getX());
                vect.setY(m_zoom * vect.getY());
                
                drawVectorShape.setDrawVector(vect);
                
                endX = posX + (int) vect.getX();
                endY = posY + (int) vect.getY();
                                                                
                // Draw the line
                g.drawLine(posX, posY, endX, endY);                                
            
                break;
                
            case RenderInfo.TILE:
                
                drawTile = (Tile) info;                
                
                pos = drawTile.getPos();
                
                pos.setX(m_zoom * pos.getX());
                pos.setY(m_zoom * pos.getY());
                
                posX = (int) pos.getX();
                posY = (int) pos.getY();
                
                width = (int) (drawTile.getWidth() * m_zoom * info.getScaleX());
                height = (int) (drawTile.getHeight() * m_zoom * info.getScaleY());
                
                posX -= width / 2;
                posY -= height / 2;
                
                drawTile.setZoomedWidth(width);
                drawTile.setZoomedHeight(height);
                
                g.drawImage(drawTile.getImage(), posX, posY, width, height,null);
            
                break;
                
            case RenderInfo.POLYGONSHAPE:                
                drawPolygon = (PolygonShape) info;
                pointIter = drawPolygon.getPoints();
                parent = drawPolygon.getParent();                
                
                p = new Polygon();                
                
                // Use this to move the points to the correct drawing position
                relX = (drawPolygon.getPos().getX() - parent.getPos().getX());
                relY = (drawPolygon.getPos().getY() - parent.getPos().getY());
                
                while (pointIter.hasNext())
                {
                    actPoint = new Point2d((Point2d) pointIter.next());
                    
                    // Adjust the point according to the scaling
                    actPoint.setX(actPoint.getX() * info.getScaleX());
                    actPoint.setY(actPoint.getY() * info.getScaleY());
                     
                    drawPoint = parent.localToWorld(actPoint);
                    p.addPoint( (int) (m_zoom * (drawPoint.getX() + relX)), 
                    			(int) (m_zoom * (drawPoint.getY() + relY)));
                }
                
                g.setColor(drawPolygon.getColor());
                
                // Save the created polygon for later use (picking, etc.)
                drawPolygon.setPolygon(p);
                
                if (drawPolygon.getFilled())
                {
                    g.fillPolygon(p);
                }
                else
                {
                    g.drawPolygon(p);
                }
                                                                                
                break;
                
            case RenderInfo.INFOBOX:
                // special InfoBox rendering code
                drawInfoBox = (InfoBox) info;
                pos = drawInfoBox.getPos();
                relPos = drawInfoBox.getRelPos();
                
                // Calculate the correct position of the text                                 
                relPos = drawInfoBox.getParent().localToWorld(relPos);
                
                pos.setX(m_zoom * (relPos.getX()));
                pos.setY(m_zoom * (relPos.getY()));
                
                g.setColor(drawInfoBox.getColor());
                
                g.drawString(drawInfoBox.getText(), (int) pos.getX() - m_relX, (int) pos.getY() - m_relY);                
                break;
                
            default:
            }                                                 
        }
    }	        
    
    /**
     * Transforms a screen coordinate into world space
     * @return The screen x coordinate transformed into world space
     */
    public double screenToWorldX(int screenX)
    {
    	return screenX / m_zoom + m_relX;	
    }
    
    /**
     * Transforms a screen coordinate into world space
     * @return The screen y coordinate transformed into world space
     */
    public double screenToWorldY(int screenY)
    {
    	return screenY / m_zoom + m_relY;	
    }
    
    //////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////
    
    /** Allows the panning to use negative relative window positions */
    protected boolean m_allowFreePann = false;
    
    /** Sorted structure used to cache the requested shapes at a specific position */
    protected Vector  m_clickedShapes = null;    
    
    /** Time used for rendering the last frame in ms */
    protected long m_frameTime = 0;
    
    /** The color of the grid lines */
    protected Color m_gridColor = Color.gray;
    /** Spacing of the grid along the x axis */
    protected double m_gridWidth = 50.0;
    /** Spacing of the grid along the y axis */
    protected double m_gridHeight = 50.0;
    
    /** Set this flag to ignore all visibility information of the geometry objects when rendering */
    protected boolean m_ignoreVisiblity = false;
    
    /** X-Offset of the viewing rectangle */
    protected int m_relX = 0;
    /** Y-Offset of the viewing rectangle */
    protected int m_relY = 0;
    
    /** Used to be able to distinguish between foreground and background shapes when using <code>getVisibleObjects</code>*/
    protected boolean m_returnBackgroundShapes = false;
    
    /** Height of the complete scene */
    protected int m_sceneHeight = 480;
    /** Width of the complete scene */
    protected int m_sceneWidth = 640;        
    
    /** Height of the viewing rectangle */
    protected int m_screenHeight = 480;
    /** Width of the viewing rectangle */
    protected int m_screenWidth = 640;   
    
    /** Flag to show or hide the grid */
    protected boolean m_showGrid = false;
    
    /** Sorted structure used to cache the visible shapes for each frame */
    protected ArrayList m_visibleInfo = null;    
    /** Zoom factor, must be greater zero. No zoom == 1.0 */
    protected double m_zoom =  1.0;                        
}

