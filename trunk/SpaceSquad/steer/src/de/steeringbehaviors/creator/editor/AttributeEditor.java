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

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.*;
import javax.swing.event.*;
import java.lang.*;
import javax.swing.border.*;
import java.lang.Math;

import org.apache.xerces.parsers.*;
import org.w3c.dom.*;
import org.w3c.dom.traversal.*;

/** Class AttributeEditor Implements the gui for editing the attributes of a tree node */
public class AttributeEditor
extends JPanel
implements ActionListener
{
	
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////

	/** Constructor */	
	public AttributeEditor(JFrame appl)
	{
		super();
	}
	
	/** 
	* Constructor 
	* @param stn SteeringTreeNode from which the attributes should be edited
	* @param appl Parent application which uses the AttributeEditor
	*/
	public AttributeEditor(SteeringTreeNode stn, JFrame appl)
	{
		super();
		
		m_stn=stn;
		setAttributes(stn.getAttributes());
		
		// update the value view (e.g. for rounding the values)
		updateValueView();
	}
	
	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Sets the attributes stored in the TreeMap
	* @param attr Attributes to set
	*/
	public void setAttributes(TreeMap attr)
	{
	
		m_components=new Hashtable();
		m_attr=attr;
		
		// remove all elements
		removeAll();
		
		// set the layoutmanager
		m_gbl = new GridBagLayout();
		setLayout(m_gbl);

		// set a titled-border			
		setBorder(new TitledBorder("Attributes of "+m_stn.getCaption()));
		
		int row=0;
		
		// if no attributes available, don't continue adding components
		if (attr.size()>0)
		{	
			// add a label and a textfield for each attribute
			
			//TreeMap attr=m_stn.getAttributes();
 			Iterator it=attr.keySet().iterator();
 		
 			while(it.hasNext())
 			{
 				String name=(String) it.next();
 				String value=(String) attr.get(name);
 				if (!name.equals("name")) 
 				{		
 					insertAttributeComponent(name,value,row);
 					row++;
 				}
 			}
		}
	
		// insert buttons
	
		JButton button=new JButton("Add");
		button.addActionListener(this);
		m_gbc=makegbc(0,row+1,1,1); 
		m_gbl.setConstraints(button,m_gbc);
		add(button);
		
		button= new JButton("Delete");
		button.addActionListener(this);
		m_gbc=makegbc(1,row+1,1,1); 
		m_gbl.setConstraints(button,m_gbc);
		add(button);
		
	} // setAttributes

	/**
	* Sets the EditorCanvas object
	* @param canv EditorCanvas to set
	*/
	public void setEditorCanvas(EditorCanvas canv)
	{
		m_editorCanvas=canv;
	}


	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** 
	* Updates the values of the attributes in the gui 
	*/
	public void updateValueView()
	{
		if (m_attr.size()>0)
		{	
			//TreeMap attr=m_stn.getAttributes();
			Iterator it=m_components.keySet().iterator();
		
			while(it.hasNext())
			{
				Component comp = (Component) it.next();
				
				// update, if class is a JTextField-instance
				if (comp.getClass().isInstance(new JTextField()))
				{
					JTextField component=(JTextField) comp;
					String name=(String)m_components.get(component);
					String value=(String) m_attr.get(name);
				
					// round the value
					try
					{
						Double val = new Double(value);
						value =new Double(java.lang.Math.round(val.doubleValue() * 1000)/1000.0).toString();
					}
					catch (Exception e) {}
				
					if (!name.equals("name")) 
					{		
						component.setText(value);
					}
				}
				// its not necessary to update a JCheckBox,
				// because no boolean attribute is changing
				// while editing with the canvas.
			}
		}
	} // updateValueView
	
	/**
	* Inserts a component to edit an attribute
	* @param name Name of the attribute
	* @param value Default value of the attribute
	* @param pos Position of the component. Used by the LayoutManager
	*/
	private void insertAttributeComponent(String name, String value, int pos)
	{	
		// It's possible to differ between the types of attributes
		// and a specific component can be added for each attribute.
		// But now let's try the simple method:
		try
		{
			// the label shows the name of the attribute
			JLabel attr_label=new JLabel(name);
			m_gbc=makegbc(0,pos,1,1);
			m_gbl.setConstraints(attr_label,m_gbc);
			add(attr_label);
			
			Component value_component;
				
			if ( value.toUpperCase().equals("TRUE") )
			{
				// create a checked checkbox
				value_component = new JCheckBox();
				((JCheckBox) value_component).setSelected(true);
				((JCheckBox) value_component).addActionListener(this);
			} 
			else if ( value.toUpperCase().equals("FALSE") )
			{
				// create a non-checked checkbox
				value_component = new JCheckBox();
				((JCheckBox) value_component).setSelected(false);
				((JCheckBox) value_component).addActionListener(this);
			}
			else
			{
				// create a textfield
				value_component = new JTextField(value,10);
				((JTextField) value_component).addActionListener(this);
			}
		

			// add this component to the hashtable
			m_components.put(value_component,name);
			
			m_gbc=makegbc(1,pos,1,1); 
			m_gbl.setConstraints(value_component,m_gbc);
			//value_component.addActionListener(this);
			
			add(value_component);
		}
		catch (Exception e)
		{}
	}

	/**
	* Sets the properties of GridBagConstraints
	* @param x gridx of GridBagConstraints
	* @param y gridy of GridBagConstraints
	* @param width gridwidth of GridBagConstraints
	* @param height gridheight of GridBagConstrains
	*/
	private GridBagConstraints makegbc(int x, int y, int width, int height)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx=x;
		gbc.gridy=y;
		gbc.gridwidth=width;
		gbc.gridheight=height;
		gbc.insets=new Insets(1,1,1,1);
		gbc.anchor=GridBagConstraints.NORTHWEST;
		return gbc;
	}
	
	
	/** 
	* Implements the ActionListener function actionPerformed.
	* Acts on changing values of a component
	*/
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() instanceof JButton)
		{
			if (event.getActionCommand().equals("Add")) 
			{ 
				addAttribute();	
			}
		
			if (event.getActionCommand().equals("Delete"))	
			{
				deleteAttribute();
			}
		
		}
		else
		{
			String name=(String) m_components.get(event.getSource());
			
			// differ between the type of the component
			String value="";
			
			// if component is a textfield:
			if (event.getSource().getClass().isInstance(new JTextField()))
			{
				value=((JTextField) event.getSource()).getText();
			}
			// if component is a checkbox:
			else if (event.getSource().getClass().isInstance(new JCheckBox()))
			{
				if ( ((JCheckBox) event.getSource()).isSelected() )
					value = "true";
				else 	value = "false";
			}
			// put the value of the attribute to which the componenent belongs
			m_stn.setAttribute(name,value);
			
			// special case: if type is the steering node, change
			// the canvas size on changing the attributes 
			// "width" and "height"
			if (m_stn.getType().equals("steering"))
			{	
				int val;
				try
				{ 
					val=(int) (new Double(value)).doubleValue();
				}
				catch (Exception e) { val = 20; }
				
				if (name.equals("width")) m_editorCanvas.setWidth(val);
				if (name.equals("height")) m_editorCanvas.setHeight(val);
				if (name.equals("showgrid")) 
				{
					if (value.equals("true")) m_editorCanvas.getRenderer().setShowGrid(true);
					if (value.equals("false")) m_editorCanvas.getRenderer().setShowGrid(false);
				}
				if (name.equals("image"))
				{
					m_editorCanvas.loadBackgroundImage(value);
					m_editorCanvas.repaint();
					
					// set the attributes for the image size
					
					String temp;

					if (m_editorCanvas.getBackgroundImageTile() != null)
					{	
						temp = new Integer(m_editorCanvas.getBackgroundImageTile().getWidth()).toString();
					}
					else temp="0";
					m_attr.put("imgsizex",temp);
					
					if (m_editorCanvas.getBackgroundImageTile() != null)
					{
						temp = new Integer(m_editorCanvas.getBackgroundImageTile().getHeight()).toString();
					}
					else temp="0";
					m_attr.put("imgsizey",temp);
					
					// update the view
					updateValueView();
				}
				
				if (name.equals("imgsizex"))
				{
					// set the value back to the original value
					String temp;

					if (m_editorCanvas.getBackgroundImageTile() != null)
					{	
						temp = new Integer(m_editorCanvas.getBackgroundImageTile().getWidth()).toString();
					}
					else temp="0";
					m_attr.put("imgsizex",temp);
					
					updateValueView();
					
					
				}
			
				if (name.equals("imgsizey"))
				{
					// set the value back to the original value
					String temp;

					if (m_editorCanvas.getBackgroundImageTile() != null)
					{	
						temp = new Integer(m_editorCanvas.getBackgroundImageTile().getHeight()).toString();
					}
					else temp="0";
					m_attr.put("imgsizey",temp);
					
					updateValueView();
					
				}
				
				m_editorCanvas.resize();
				m_editorCanvas.repaint();
			}
			else
			{
				if (m_stn.getGeometrieObject() != null) m_stn.getGeometrieObject().update();
			}
			
			// update the canvas
			m_editorCanvas.repaint();		
		}
	}
	
	/**
	* Adding a new attribute by opening a dialog and asking the name of
	* the new attribute
	*/
	private void addAttribute()
	{
		// ask the new name of the object
		String ret = (String) JOptionPane.showInputDialog(this, "Enter name of the new attribute","Add attribute",JOptionPane.QUESTION_MESSAGE);
		if (ret!=null)
		{
			// set the attribute
			m_stn.setAttribute(ret,"");
			
			// refresh the attributeeditor
			setAttributes(m_stn.getAttributes());
			validate();
		}	
	}
	
	/**
	* Deletes a attribute by opening a dialog and asking the name of
	* the attribute to delete
	*/
	private void deleteAttribute()
	{
		try
		{
			Object[] attr=m_attr.keySet().toArray();
		
			String ret = (String) JOptionPane.showInputDialog(
				this, "Choose attribute to delete", "Delete attribute", JOptionPane.QUESTION_MESSAGE,
				null, attr, attr[0]);
		
			if ((ret!=null) && (!ret.equals("name")))
			{
				// delete attribute
				m_stn.removeAttribute(ret);
				System.out.println("DelAttr: "+ret);
				// refresh the attributeeditor
				setAttributes(m_stn.getAttributes());
				validate();
			}
		} 
		catch(Exception e) {}
	} // deleteAttribute
	
	
	
	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////        
	
	/** GridBagLayout */
	protected GridBagLayout 	m_gbl;
	
	/** GridBagConstraints */
	protected GridBagConstraints 	m_gbc;
	
	/** TreeMap of attributes */
	protected TreeMap		m_attr;
	
	/** Hashtable of components, stored with the attribute name as key */
	protected Hashtable		m_components;
	
	/** SteeringTreeNode which attributes should be edited */
	protected SteeringTreeNode	m_stn;
	
	/** The canvas of the editor */
	protected EditorCanvas		m_editorCanvas;
	
} // class AttributeEditor