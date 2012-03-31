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

package de.steeringbehaviors.creator.editor;

import java.lang.Runnable;
import java.lang.Thread;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.Toolkit.*;
import javax.swing.*;

import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.renderer.SteeringRenderer;
import de.steeringbehaviors.simulation.simulationobjects.Neighborhood;
import de.steeringbehaviors.simulation.simulationobjects.Obstacle;
import de.steeringbehaviors.simulation.simulationobjects.PathFindingSimulation;
import de.steeringbehaviors.simulation.simulationobjects.Simulation;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;
import de.steeringbehaviors.simulation.xml.SteeringFactoryLocal;



/**
* Implements the canvas and the main functions for the simulation preview
*/
public class SimulationCanvas extends JPanel implements Runnable //, ItemListener
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Constructor
	*/
	public SimulationCanvas()
	{
		super();
		initObjects();
	}

	//////////////////////////////////////////////////////////////////////
	//
	// ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////

	/**
	* Returns the Renderer object
	* @return The Renderer object
	*/
	public SteeringRenderer getRenderer() { return m_Renderer; }

	public void run()
	{
		int relY, relX;
		double axisX, axisY;
		float zoom;
		double scaleX;
		Geometrie g;

		while (running)
		{
		
			// run one step of the simulation
			m_Simulation.runSimulation();    

			// repaint the scene
			repaint(); 


			// calculate no. of frames
			m_oldTime = m_Simulation.getSimulationTime();

			if (m_oldTime > 0)
				m_frames = (int) (1000 / m_oldTime);
			else
				m_frames = -1;

			// reduce no. of frames to 25 fps
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

	//////////////////////////////////////////////////////////////////////
	//
	// UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////

	/**
	*
	*/
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
	} // destroy

	
	/**
	* Initialisize the used objects
	*/
	private void initObjects()
	{
		//
		// Create system objects
		//
	
		m_Renderer = new SteeringRenderer(640, 400);
		m_Simulation = new Simulation();

		// Set the scene extends
		m_Renderer.setSceneWidth(m_anzGeoms * 100);
		m_Renderer.setSceneHeight(m_anzGeoms * 100);

		//
		// Create the scene based on the xml scene file
		//

		Image im = ImagePool.getPool().getIcon(ImagePool.BACK_TILE).getImage();

		SteeringFactoryLocal factory=new SteeringFactoryLocal(this);
		// load the temporary saved file "preview.tmp"
		String file = "preview.tmp";
		factory.createScene(file);
		
		// set the scene size
		m_Simulation.setSceneWidth(factory.getSceneWidth());
		m_Simulation.setSceneHeight(factory.getSceneHeight());
		m_Simulation.addBackground(factory.getBackground());
		m_Renderer.setSceneWidth(factory.getSceneWidth());
		m_Renderer.setSceneHeight(factory.getSceneHeight());

		// add vehicles to simulation
		Vector vlist=new Vector();
		vlist=factory.getVehicles();
		Iterator i=vlist.iterator();
		while (i.hasNext())
		{
			m_Simulation.addVehicle((Vehicle) i.next());
		}

		// add the obstacles to simulation
		Vector olist=new Vector();
		olist=factory.getObstacles();
		i=olist.iterator();
		while (i.hasNext())
		{
			m_Simulation.addObstacle((Obstacle) i.next());
		}
		
		// Add some additional simulators                 
		m_Simulation.addPreSimulation(new Neighborhood());                
		m_Simulation.addPreSimulation(new PathFindingSimulation());


		// Start the simulation thread
		
		if (theThread == null)
		{
			theThread = new Thread(this);
			running = true;
			theThread.start();
		}  

	} // initObjects

	
	/**
	* Paints the scene
	*/
	public void paint (Graphics g)
	{
		
		// Render the scene
		Iterator it = m_Simulation.getScene().iterator();
		g.clearRect(0, 0, getWidth(), getHeight());
		m_Renderer.renderScene(it, g);       

		// Frame Informationen darstellen
		g.setColor(Color.black);
		g.drawString("Visible Shapes: " + m_Renderer.getVisibleCount(), 20, 20);
		g.drawString("Frames/s: " + m_frames, 20, 35);        
	} // paint
    
  
	//////////////////////////////////////////////////////////////////////
	//
	// VARIABLES
	//
	//////////////////////////////////////////////////////////////////////

	
	/** */
	private boolean running = false;
	/** */
	private Thread theThread = null;    
	/** The Renderer object */
	private SteeringRenderer 	m_Renderer = null;
	/** The Simulation object */
	private Simulation		m_Simulation = null;
    	/** */
	private long m_oldTime = 0;
	/** */
	private Date m_actTime = new Date(0);
	/** */
	private int m_frames = 25;
	/** */
	private int m_anzGeoms = 20;
	/** */
	private int anzShapes = 4;
}
