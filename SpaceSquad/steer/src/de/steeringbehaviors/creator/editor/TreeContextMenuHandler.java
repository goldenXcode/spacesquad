package de.steeringbehaviors.creator.editor;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.lang.*;

class TreeContextMenuHandler
implements ActionListener
{
	Object	m_parent;

	TreeContextMenuHandler(Object parent)
	{
		m_parent=parent;
	}
	
	public void actionPerformed(ActionEvent event)
	{
		String cmd = event.getActionCommand();
		System.out.println("ActionCommand: "+event.getActionCommand());
		System.out.println("Source: "+event.getSource());
	}
	
}