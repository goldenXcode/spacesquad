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

package de.steeringbehaviors.creator.editor;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/** The CenterPanel includes the EditorCanvas object and manages the scrollbars */
public class CenterPanel extends JPanel
implements  AdjustmentListener
{
	//////////////////////////////////////////////////////////////////////
	//
	//  CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Contructor
	* @param editorcanvas The EditorCanvas object
	*/
	public CenterPanel(EditorCanvas editorcanvas)
	{
		super(new BorderLayout());

		// set the editorCanvas
		m_editorCanvas = editorcanvas;
		
		// don't allow free pann
		m_editorCanvas.getRenderer().setAllowFreePann(false);
		
		// create the scrollbars
		m_sbEast = new JScrollBar(JScrollBar.VERTICAL, 1, m_editorCanvas.getHeight(), 1, m_editorCanvas.getRenderer().getSceneHeight());
		m_sbSouth = new JScrollBar(JScrollBar.HORIZONTAL,1, m_editorCanvas.getWidth(), 1, m_editorCanvas.getRenderer().getSceneWidth());
		
		// set the screenwidth/-height
		m_editorCanvas.getRenderer().setScreenWidth(this.getHeight());
		m_editorCanvas.getRenderer().setScreenHeight(this.getWidth());
		
		// add the adjustmentlistener
		m_sbEast.addAdjustmentListener(this);
		m_sbSouth.addAdjustmentListener(this);
		
		// create a info-panel
		m_infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		m_posLabel = new JLabel("X: Y:");
		m_infoPanel.add(m_posLabel);
		m_infoPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		//m_infoPanel.setSize(200,30);
		
		// add the components to this panel
		this.add(m_infoPanel, "North");
		this.add(m_editorCanvas, "Center");
		this.add(m_sbEast, "East");
		this.add(m_sbSouth, "South");
		
	} // CenterPanel
	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Refreshes the posLabel
	* @param x The x position of the mouse pointer
	* @param y the y position of the mouse pointer
	*/
	public void setLabelXY(double x, double y)
	{
		if (m_posLabel != null) m_posLabel.setText("X:"+java.lang.Math.round(x)+" Y:"+java.lang.Math.round(y));
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////
	
	
	/** 
	* Overrides the repaint method
	*/
	public void repaint()
	{
		super.repaint();
		
		if (m_editorCanvas != null)
		{
			// repaint the included canvas
			m_editorCanvas.repaint();
		
			// set the new screenwidth/-height
			m_editorCanvas.getRenderer().setScreenWidth(this.getWidth() -  m_sbEast.getWidth());
			m_editorCanvas.getRenderer().setScreenHeight(this.getHeight() - m_sbSouth.getHeight() - m_infoPanel.getHeight());
		
			// get the zoomfactor
			double zoom = m_editorCanvas.getRenderer().getZoom();
			
			double max;
			
			// calculate the maximal value of the scrollbar
			max = m_editorCanvas.getRenderer().getSceneWidth() - (this.getWidth() - m_sbEast.getWidth()) / zoom ;
		
			// set the scrollbar properties
			if (max > 0)
			{
				m_sbSouth.setEnabled(true);
				m_sbSouth.setMinimum(0);
				m_sbSouth.setMaximum((int) max);
			} 
			else m_sbSouth.setEnabled(false);
		
			// calculate the maximal value of the scrollbar
			max = m_editorCanvas.getRenderer().getSceneHeight()- (this.getHeight()- m_sbSouth.getHeight() - 30) / zoom;
			
			// set the scrollbar properties
			if (max > 0)
			{
				m_sbEast.setEnabled(true);
				m_sbEast.setMinimum(0);
				m_sbEast.setMaximum((int) max);
			} 
			else m_sbEast.setEnabled(false);
		}
	} // repaint
	
	//////////////////////////////////////////////////////////////////////
	//
	//  EVENT HANDLING FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Implementation of the Adjustmentlistener Interface
	*/
	public void adjustmentValueChanged(AdjustmentEvent event)
	{
		JScrollBar sb = (JScrollBar) event.getSource();
		
		// find out which Scrollbar has changed
		// and adjust the relX/relY properties of the renderer
		if (sb == m_sbEast)
		{
			m_editorCanvas.getRenderer().setRelY(event.getValue());
		}
		else if (sb == m_sbSouth)
		{
			m_editorCanvas.getRenderer().setRelX(event.getValue());
		}
		
		// repaint the canvas
		m_editorCanvas.repaint();
		
	} // adjustmentValueChanged
	
	
	
	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////
	
	
	/** The included EditorCanvas object */
	EditorCanvas	m_editorCanvas;
	/** The info panel */
	JPanel		m_infoPanel;
	/** This label shows the mouse position */
	JLabel		m_posLabel;
	/** The horizontal scrollbar */
	JScrollBar	m_sbSouth;
	/** The vertical scrollbar */
	JScrollBar	m_sbEast;
}