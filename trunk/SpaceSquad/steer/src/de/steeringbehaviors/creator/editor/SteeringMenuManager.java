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

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.lang.*;
import java.awt.*;
import java.util.Hashtable;
import javax.swing.JToolBar.*;

/** 
* This class creates all menu entries and manages the status of
* the buttons. It creates context menues for each type of node which
* is selected
*/
public class SteeringMenuManager
{
	
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Constructor
	* @param listener The ActionListener to use
	*/
	public SteeringMenuManager(Object listener)
	{
		m_listener=listener;
		createTreeContextMenu();
		createMainMenu();
		
		createToolbar();
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////

		    
	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////

	/**
	* Returns the main menu
	* @return Main menu
	*/
	public JMenuBar getMainMenu()
	{
		return m_mainMenu;
	} 	
	
	/**
	* Returns the toolbar
	* @return Toolbar
	*/
	public JToolBar getToolbar()
	{
		return m_toolbar;
	}

	/**
	* Returns a context menu, depending on the node type
	* @param type Type of the context menu
	* @return Context menu
	*/
	public JPopupMenu getTreeContextMenu(String type)
	{
		JPopupMenu ret;
		if (type.equals("vehicle")) return m_vehicleContextMenu;
		if (type.equals("steering")) return m_steeringContextMenu;
		if (type.equals("mind")) return m_mindContextMenu;
		if (type.equals("obstacle")) return m_obstacleContextMenu;
		
		return m_defaultContextMenu;
	} 
	
	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////


	/** Creates the toolbar */
	private void createToolbar()
	{
		m_toolbar=new JToolBar();
		m_toolbarButtons = new Hashtable();
		
		
			
		JButton b = new JButton();
		
		b.setActionCommand("Add vehicle");
		
		b.setIcon(ImagePool.getPool().getIcon(ImagePool.ADDVEHICLE));
		b.setMargin(new Insets(0,0,0,0));
		b.addActionListener((ActionListener) m_listener);
		m_toolbarButtons.put(b.getActionCommand(), b);
		m_toolbar.add(b);
		
		b = new JButton();
		b.setActionCommand("Add obstacle");
		b.setIcon(ImagePool.getPool().getIcon(ImagePool.ADDOBSTACLE));
		b.setMargin(new Insets(0,0,0,0));
		b.addActionListener((ActionListener) m_listener);
		m_toolbarButtons.put(b.getActionCommand(), b);
		m_toolbar.add(b);
		
		Separator separator = new Separator();
		m_toolbar.add(separator);
		
		b = new JButton();
		b.setActionCommand("Add mind...");
		b.setIcon(ImagePool.getPool().getIcon(ImagePool.ADDMIND));
		b.setMargin(new Insets(0,0,0,0));
		b.addActionListener((ActionListener) m_listener);
		m_toolbarButtons.put(b.getActionCommand(), b);
		m_toolbar.add(b);
		
		b = new JButton();
		b.setActionCommand("Add behavior...");
		b.setIcon(ImagePool.getPool().getIcon(ImagePool.ADDBEHAVIOR));
		b.setMargin(new Insets(0,0,0,0));
		b.addActionListener((ActionListener) m_listener);
		m_toolbarButtons.put(b.getActionCommand(), b);
		m_toolbar.add(b);
		
		separator = new Separator();
		m_toolbar.add(separator);
		
		
		b = new JButton();
		b.setActionCommand("Delete");
		b.setIcon(ImagePool.getPool().getIcon(ImagePool.DELETE));
		b.setMargin(new Insets(0,0,0,0));
		b.addActionListener((ActionListener) m_listener);
		m_toolbarButtons.put(b.getActionCommand(), b);
		m_toolbar.add(b);
		
		b = new JButton();
		b.setActionCommand("Clone");
		b.setIcon(ImagePool.getPool().getIcon(ImagePool.CLONE));
		b.setMargin(new Insets(0,0,0,0));
		b.addActionListener((ActionListener) m_listener);
		m_toolbarButtons.put(b.getActionCommand(), b);
		m_toolbar.add(b);
		
		separator = new Separator();
		m_toolbar.add(separator);
		
		b = new JButton();
		b.setActionCommand("Run");
		b.setIcon(ImagePool.getPool().getIcon(ImagePool.RUN));
		b.setMargin(new Insets(0,0,0,0));
		b.addActionListener((ActionListener) m_listener);
		m_toolbarButtons.put(b.getActionCommand(), b);
		m_toolbar.add(b);
		
	}

	/** 
	* Enables and disables the buttons depending on the selected type
	* @param type The type of the menu
	 */
	public void updateToolbarButtons(String type)
	{
		if (type.equals("steering"))
		{
			((JButton) m_toolbarButtons.get("Add vehicle")).setEnabled(true);
			((JButton) m_toolbarButtons.get("Add obstacle")).setEnabled(true);
			((JButton) m_toolbarButtons.get("Add mind...")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Add behavior...")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Delete")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Clone")).setEnabled(false);
		}
		if (type.equals("vehicle"))
		{
			((JButton) m_toolbarButtons.get("Add vehicle")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Add obstacle")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Add mind...")).setEnabled(true);
			((JButton) m_toolbarButtons.get("Add behavior...")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Delete")).setEnabled(true);
			((JButton) m_toolbarButtons.get("Clone")).setEnabled(true);
		}
		if (type.equals("obstacle"))
		{
			((JButton) m_toolbarButtons.get("Add vehicle")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Add obstacle")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Add mind...")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Add behavior...")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Delete")).setEnabled(true);
			((JButton) m_toolbarButtons.get("Clone")).setEnabled(true);
		}
		if (type.equals("mind"))
		{
			((JButton) m_toolbarButtons.get("Add vehicle")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Add obstacle")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Add mind...")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Add behavior...")).setEnabled(true);
			((JButton) m_toolbarButtons.get("Delete")).setEnabled(true);
			((JButton) m_toolbarButtons.get("Clone")).setEnabled(false);
		}
		if (type.equals("behavior"))
		{
			((JButton) m_toolbarButtons.get("Add vehicle")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Add obstacle")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Add mind...")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Add behavior...")).setEnabled(false);
			((JButton) m_toolbarButtons.get("Delete")).setEnabled(true);
			((JButton) m_toolbarButtons.get("Clone")).setEnabled(false);
		}
		
	}

	/** Creates the main menu */
	private void createMainMenu()
	{
		m_mainMenu=new JMenuBar();
		JMenu m;
		
		m=new JMenu("File");
		m.setMnemonic('F');
		addNewMenuItem(m, "&New",(ActionListener) m_listener);
		addNewMenuItem(m, "&Open...",(ActionListener) m_listener);
		addNewMenuItem(m, "&Close",(ActionListener) m_listener);
		addNewMenuItem(m, "&Save",(ActionListener) m_listener);
		addNewMenuItem(m, "Save &As...",(ActionListener) m_listener);
		m.addSeparator();
		addNewMenuItem(m, "E&xit",(ActionListener) m_listener);
		
		m_mainMenu.add(m);
		
	} 
	
	/** Creates the context menues */
	private void createTreeContextMenu()
	{
		// create contextmenus for each type of treenode
		m_defaultContextMenu=new JPopupMenu();
		addNewContextMenuItem(m_defaultContextMenu, "Delete",(ActionListener) m_listener, null);
		
		m_vehicleContextMenu=new JPopupMenu();
		addNewContextMenuItem(m_vehicleContextMenu, "Rename...", (ActionListener)m_listener, null);
		addNewContextMenuItem(m_vehicleContextMenu, "Add mind...", (ActionListener)m_listener, new ImageIcon("mind.gif"));
		m_vehicleContextMenu.addSeparator();
		addNewContextMenuItem(m_vehicleContextMenu, "Clone", (ActionListener) m_listener, null);
		addNewContextMenuItem(m_vehicleContextMenu, "Delete",(ActionListener) m_listener, null);
		
		m_steeringContextMenu=new JPopupMenu();
		addNewContextMenuItem(m_steeringContextMenu, "Add vehicle",(ActionListener) m_listener, new ImageIcon("vehicle.gif"));
		addNewContextMenuItem(m_steeringContextMenu, "Add obstacle",(ActionListener) m_listener, new ImageIcon("rectobstacle.gif"));
		
		m_obstacleContextMenu=new JPopupMenu();
		addNewContextMenuItem(m_obstacleContextMenu, "Rename...",(ActionListener) m_listener, null);
		m_obstacleContextMenu.addSeparator();
		addNewContextMenuItem(m_obstacleContextMenu, "Clone",(ActionListener) m_listener, null);
		addNewContextMenuItem(m_obstacleContextMenu, "Delete",(ActionListener) m_listener, null);
		
		m_mindContextMenu=new JPopupMenu();
		addNewContextMenuItem(m_mindContextMenu, "Change mind type...",(ActionListener) m_listener, null);
		addNewContextMenuItem(m_mindContextMenu, "Add behavior...",(ActionListener) m_listener, new ImageIcon("behavior.gif"));
		m_mindContextMenu.addSeparator();
		addNewContextMenuItem(m_mindContextMenu, "Delete",(ActionListener) m_listener, null);
	}

	/**
	* Adds a new menu item to a existing JMenu 
	* @param menu JMenu to add the item
	* @param name Name of the menu item to add
	* @param listener ActionListener for event handling
	*/
	private void addNewMenuItem(JMenu menu, String name, ActionListener listener)
	{
		int pos=name.indexOf('&');
		//JMenuShortcut shortcut = null;
		JMenuItem mi;
		char c=0;
		if (pos!= -1)
		{
			if (pos<name.length() -1)
			{
				c = name.charAt(pos+1);
				//shortcut=new JMenuShortcut(Character.toLowerCase(c));
				name=name.substring(0,pos)+name.substring(pos+1);
			}
		}
		if (c!=0)
		{
			mi = new JMenuItem(name,c);
		}
		else
		{
			mi = new JMenuItem(name);
		}
		
		
		
		mi.setActionCommand(name);
		mi.addActionListener(listener);
		menu.add(mi);
	} 
	
	/**
	* Adds a new context menu item to a existing JMenu 
	* @param menu JPopupMenu to add the item
	* @param name Name of the menu item to add
	* @param listener ActionListener for event handling
	*/
	private void addNewContextMenuItem(JPopupMenu menu, String name, ActionListener listener, ImageIcon mIcon)
	{
		int pos=name.indexOf('&');
		//JMenuShortcut shortcut = null;
		JMenuItem mi;
		char c=0;
		if (pos!= -1)
		{
			if (pos<name.length() -1)
			{
				c = name.charAt(pos+1);
				//shortcut=new JMenuShortcut(Character.toLowerCase(c));
				name=name.substring(0,pos)+name.substring(pos+1);
			}
		}
		if (c!=0)
		{
			mi = new JMenuItem(name,c);
		}
		else
		{
			mi = new JMenuItem(name);
		}
		mi.setActionCommand(name);
		mi.addActionListener(listener);
		
		// set icon if available
		if (mIcon != null) mi.setIcon(mIcon);
		
		mi.setHorizontalTextPosition(JMenuItem.LEFT);
		
		
		menu.add(mi);
	}
	
	
	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////        

		
	/** Object, which implements the listener for event handling */
	private Object	 	m_listener;
	
	/** Main menu */
	private JMenuBar	m_mainMenu;
	
	/** The default context menu */
	private JPopupMenu	m_defaultContextMenu;
	
	/** The vehicle context menu */
	private JPopupMenu	m_vehicleContextMenu;	
	
	/** The mind context menu */
	private JPopupMenu	m_mindContextMenu;
	
	/** The obstacle context menu */
	private JPopupMenu	m_obstacleContextMenu;
	
	/** The steering context menu */
	private JPopupMenu	m_steeringContextMenu;
	
	/** The toolbar */
	private JToolBar	m_toolbar;
	
	/** Hashtable with toolbar-buttons */
	private Hashtable	m_toolbarButtons;
	
}