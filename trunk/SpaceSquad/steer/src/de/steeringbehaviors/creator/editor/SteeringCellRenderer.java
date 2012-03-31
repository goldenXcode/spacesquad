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
import javax.swing.*;
import javax.swing.tree.*;
import java.lang.*;

import org.apache.xerces.parsers.*;
import org.w3c.dom.*;
import org.w3c.dom.traversal.*;

/** Class SteeringCellRenderer Implements a custom TreeCellRenderer of the SteeringTree */
public class SteeringCellRenderer extends  DefaultTreeCellRenderer
{
	
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Constructor */
	public SteeringCellRenderer() 
	{	
		super();
	}


	//////////////////////////////////////////////////////////////////////
	//
	// ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////

	/**   
	* Configures the renderer based on the passed in components
	* @returns Component that the renderer uses to draw the value
	*/
	public Component getTreeCellRendererComponent(JTree tree,
							Object value, boolean sel, 
							boolean expanded, boolean leaf,
							int row, boolean hasFocus) 
	{
		DefaultMutableTreeNode treenode = (DefaultMutableTreeNode)value;
		super.getTreeCellRendererComponent(tree,value,sel,expanded,leaf,row,hasFocus);
		
		try
		{
			SteeringTreeNode stn=(SteeringTreeNode) treenode.getUserObject();

			if (stn.getType().equals("vehicle"))
			setIcon(ImagePool.getPool().getIcon(ImagePool.VEHICLE));

			if (stn.getType().equals("circleobstacle"))
			setIcon(ImagePool.getPool().getIcon(ImagePool.CIRCLEOBSTACLE));
			
			if (stn.getType().equals("rectobstacle"))
			setIcon(ImagePool.getPool().getIcon(ImagePool.RECTOBSTACLE));
			
			if (stn.getType().equals("obstacle"))
			setIcon(ImagePool.getPool().getIcon(ImagePool.RECTOBSTACLE));
			
			if (stn.getType().equals("behavior"))
			setIcon(ImagePool.getPool().getIcon(ImagePool.BEHAVIOR));
			
			if (stn.getType().equals("mind"))
			setIcon(ImagePool.getPool().getIcon(ImagePool.MIND));
			
			if (stn.getType().equals("steering"))
			setIcon(ImagePool.getPool().getIcon(ImagePool.STEERING));
		
		} // try
		catch (Exception e)
		{
		}
	
		return this;
	} 

} // class SteeringCellRenderer