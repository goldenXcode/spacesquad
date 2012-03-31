/*
	Steering Behaviors Applet

    Copyright (C) 2001	Thomas Feilkas 			
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

package de.steeringbehaviors.applet;



import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.sql.Date;
import java.util.Iterator;
import java.util.Vector;

import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.renderer.RenderInfo;
import de.steeringbehaviors.simulation.renderer.SteeringRenderer;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Neighborhood;
import de.steeringbehaviors.simulation.simulationobjects.Obstacle;
import de.steeringbehaviors.simulation.simulationobjects.PathFindingSimulation;
import de.steeringbehaviors.simulation.simulationobjects.Simulation;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;
import de.steeringbehaviors.simulation.xml.SteeringFactory;



public class BasicApplet extends java.applet.Applet implements Runnable, ItemListener 
{
    public void init()
    {

        m_bmg = new BasicMenueGenerator(this);

        m_bmg.initControls();

        m_canvas.addMouseListener(new MyMouseClickListener());
        m_canvas.addMouseMotionListener(new MyMouseMotioListener());
        
        initObjects();
    }

    public void destroy()
    {
        running = false;
        
        // Kurz Warten, damit der Thread auch beendet wird, 
        // bevor der DoubleBuffer gelöscht wurde
        while (theThread != null)
        {
            try
            {
                Thread.sleep(40);
            }
            catch(InterruptedException e) {}        
        }
    }
    
    private void initObjects()
    {
    	//
    	// Create system objects
    	//
    	
        m_Renderer = new SteeringRenderer(640, 400);
		m_Simulation = new Simulation();

		//
        // Create the initial doublebuffer 
        //
        
        m_buffer = createImage( (int) getBounds().getWidth(), (int) getBounds().getHeight() );
        m_db = m_buffer.getGraphics();                		      

        //
        // Load the special cursor images
        //

        Image cursIm = getImage(getCodeBase(), "ScrollKreuz.gif");        
        scrollCurs = getToolkit().createCustomCursor(cursIm, new Point(25, 25), "Scroll");
        
        // Set the scene extends
        m_Renderer.setSceneWidth(m_anzGeoms * 100);
        m_Renderer.setSceneHeight(m_anzGeoms * 100);
        
        //
        // Create the scene based on the xml scene file
        //
        
        Image im = getImage(getCodeBase(),"BackTile.gif");
        
        SteeringFactory factory=new SteeringFactory(this);
        String file = getParameter("File");
        factory.createScene(getCodeBase()+file);
        
        m_Simulation.setSceneWidth(factory.getSceneWidth());
        m_Simulation.setSceneHeight(factory.getSceneHeight());
        m_Simulation.addBackground(factory.getBackground());
        m_Renderer.setSceneWidth(factory.getSceneWidth());
        m_Renderer.setSceneHeight(factory.getSceneHeight());
        
        Vector vlist=new Vector();
        vlist=factory.getVehicles();
        Iterator i=vlist.iterator();
        while (i.hasNext())
        {
        	m_Simulation.addVehicle((Vehicle) i.next());
        }
        
        Vector olist=new Vector();
        olist=factory.getObstacles();
        i=olist.iterator();
        while (i.hasNext())
        {
        	m_Simulation.addObstacle((Obstacle) i.next());
        }
        System.out.println("No Veh: "+vlist.size());  
        
        // Add some additional simulators                 
        m_Simulation.addPreSimulation(new Neighborhood());                
        m_Simulation.addPreSimulation(new PathFindingSimulation());
        
        //
		// Start the simulation thread
		// 
		
        if (theThread == null)
        {
            theThread = new Thread(this);
            running = true;
            theThread.start();
        }  
        
    }

    public void run()
    {
        int relY, relX;
        double axisX, axisY;
        float zoom;
        double scaleX;
        Geometrie g;

        while (running)
        {           	        	                 	        	
			// 
			// Simluation code goes here
			//
            
            m_Simulation.runSimulation();    
                        
            //
            // Repaint code
            //
            
            repaint(); 
            
            //
            // Use a fixed time step for the simulation
            //
                        
            m_oldTime = m_Simulation.getSimulationTime();

            if (m_oldTime > 0)
                m_frames = (int) (1000 / m_oldTime);
            else
                m_frames = -1;

			// Simulation auf max. 25 Frames/s reduzieren
            int sleep = (int) (40 - m_oldTime);
            if (sleep < 0) sleep = 0;
            
            try
			{
            	Thread.sleep(sleep);
            }
            catch(InterruptedException e) {}
            
        }
        theThread = null;
    }

    public void update(Graphics g)
    {

        int sizeX = m_canvas.getSize().width;
        int sizeY = m_canvas.getSize().height;

		//
        // Doublebuffer auf Fenstergrösse anpassen
        //
        if (m_buffer.getWidth(null) != sizeX || m_buffer.getHeight(null) != sizeY)
        {

            if (sizeX <= 0) sizeX = 1;
            if (sizeY <= 0) sizeY = 1;
            
            m_buffer = createImage(sizeX,sizeY);
            m_db = m_buffer.getGraphics();

            m_Renderer.setScreenWidth(sizeX);
            m_Renderer.setScreenHeight(sizeY);
        }
        else
        {
            m_db.setColor(Color.white);
            m_db.clearRect(0, 0, m_buffer.getWidth(null), m_buffer.getHeight(null));
        }

        paint(m_db);
        
        m_canvas.getGraphics().drawImage(m_buffer, 0, 0, this);

    }

    public void paint (Graphics g)
    {        
        Iterator it = m_Simulation.getScene().iterator();

        m_Renderer.renderScene(it, g);       

		// Frame Informationen darstellen
        g.setColor(Color.black);
        g.drawString("Visible Shapes: " + m_Renderer.getVisibleCount(), 20, 20);
        g.drawString("Frames/s: " + m_frames, 20, 35);        
    }    
    
    class MyMouseClickListener extends MouseAdapter
    {
        public void mousePressed(MouseEvent event)
        {
            // Reaktion auf linke Maustaste
            if (event.getModifiers() == event.BUTTON1_MASK)
            {                
                if (m_mouseState == STATE_SELECT)
                {
                	m_canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                	
                	Vector clicked = m_Renderer.getVisibleObjects(event.getX(), event.getY());
                	Iterator it = m_clicked.iterator();
                	while (it.hasNext())
                	{
                    	RenderInfo r = (RenderInfo) it.next();
                    	r.setColor(Color.blue);
                	}

                	m_clicked.clear();

                	it = clicked.iterator();
                	while (it.hasNext())
                	{
                    	RenderInfo r = (RenderInfo) it.next();
                    	r.setColor(Color.red);
                    	m_clicked.add(r);
                	}
                }
                else if (m_mouseState == STATE_PANN)
                {
                	m_canvas.setCursor(scrollCurs);
                	m_mouseX = event.getX();
                	m_mouseY = event.getY();
                }
                else if (m_mouseState == STATE_ZOOM)
                {
                	m_canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                	
                	m_mouseX = event.getX();
                	m_mouseY = event.getY();
                }
            }            
        }
        public void mouseReleased(MouseEvent event)
        {
            if (event.getModifiers() == event.BUTTON1_MASK)            
            {            	
                m_canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    class MyMouseMotioListener extends MouseMotionAdapter
    {
        public void mouseDragged(MouseEvent event)
        {
            if (event.getModifiers() == event.BUTTON1_MASK)            
            {
                if (m_mouseState == STATE_ZOOM)
                {
                    // Zoom in / out
                    double zoom = m_Renderer.getZoom();
                    double relZoom;
                    int relY = event.getY() - m_mouseY;
                                        
                    relZoom= 0.1 * (-relY / 10);
                    
                    zoom += relZoom;
                    
                    m_Renderer.setZoom(zoom);
                }
                else if (m_mouseState == STATE_PANN)
                {
                    // Pann the viewing rectangle
                    int relX = m_Renderer.getRelX();
                    int relY = m_Renderer.getRelY();
                
                    relX -= (int) ((event.getX() - m_mouseX) / m_Renderer.getZoom());
                    relY -= (int) ((event.getY() - m_mouseY) / m_Renderer.getZoom());
                
                    m_Renderer.setRelX(relX);
                    m_Renderer.setRelY(relY);                                    
                }
                
                // In all cases: Update the last position
                m_mouseX = event.getX();
                m_mouseY = event.getY();
            }
        }
    }
    
    public void itemStateChanged(ItemEvent e)
    {
    	Checkbox cb = (Checkbox) e.getItemSelectable();
    	CheckboxGroup chg = cb.getCheckboxGroup();
    	
    	cb = chg.getSelectedCheckbox();
    	
    	if (cb.getLabel().equals("Select"))
    		m_mouseState = STATE_SELECT;
    	if (cb.getLabel().equals("Pann"))
    		m_mouseState = STATE_PANN;
    	if (cb.getLabel().equals("Zoom"))
    		m_mouseState = STATE_ZOOM;
    		
    }
    
    public static final int STATE_SELECT = 0;
    public static final int STATE_PANN = 1;
    public static final int STATE_ZOOM = 2;
    
    // -------------------------------------------------------------------------
    private boolean running = false;
    private Thread theThread = null;    

    protected Canvas m_canvas;
    protected BasicMenueGenerator m_bmg;

    private SteeringRenderer 	m_Renderer = null;
	private Simulation			m_Simulation = null;

	// Für die drei möglichen Mauseingaben
    private Cursor scrollCurs = null;
    private int m_mouseX;
    private int m_mouseY;
    private int m_mouseState = STATE_ZOOM;
    
    boolean m_zoom = false;
    
    // Zum Frame - Messen
    private long m_oldTime = 0;
    private Date m_actTime = new Date(0);

    // Für den DoubleBuffer
    private Image m_buffer = null;
    private Graphics m_db = null;
    private int m_frames = 25;

    // These are just used for animation!!!!
    private int m_anzGeoms = 20;
    private int anzShapes = 4;
    private int directionX = 1;
    private int directionY = 1;
    private float directionZ = 0.01f;

    private double angle = 0.0;
    private Vector2d axis = new Vector2d();
    private boolean finished = true;

    private Vector m_clicked = new Vector(2, 2);
}
