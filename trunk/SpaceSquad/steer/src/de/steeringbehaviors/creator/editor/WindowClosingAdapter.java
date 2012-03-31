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

import java.awt.*;
import java.awt.event.*;

/** Class WindowClosingAdapter Implements a WindowAdapter for closing the window */
public class WindowClosingAdapter
extends WindowAdapter
{

	/** Constructor */
	public WindowClosingAdapter(boolean exitSystem)
	{
		this.exitSystem=exitSystem; 
	}
  
	/** Constructor */
	public WindowClosingAdapter() 
	{ 
		this(true); 
	}
  
	/** exitSystem */
	private boolean exitSystem;


	/** windowClosing */  
	public void windowClosing(WindowEvent event)
	{
		event.getWindow().setVisible(false);
		event.getWindow().dispose();
		if (exitSystem) { System.exit(0); }
	}
}