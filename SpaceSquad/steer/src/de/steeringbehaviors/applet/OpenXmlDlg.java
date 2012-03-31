
package de.steeringbehaviors.applet;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;


public class OpenXmlDlg extends java.awt.Dialog
{
	public OpenXmlDlg(Frame parent)
	{
		super(parent);
		//{{INIT_CONTROLS
		setLayout(new BorderLayout(0,0));
		setSize(211,336);
		setVisible(false);
		CenterPanel.setLayout(new BorderLayout(0,0));
		add(BorderLayout.CENTER,CenterPanel);
		m_projectLabel.setText("Available Projects:");
		CenterPanel.add(BorderLayout.NORTH, m_projectLabel);
		m_projectLabel.setBackground(java.awt.Color.lightGray);
		m_listPanel.setLayout(new GridLayout(1,1,0,0));
		CenterPanel.add(BorderLayout.CENTER,m_listPanel);
		m_listPanel.add(m_projectList);
		SoutPanel.setLayout(new GridLayout(1,1,0,0));
		add(BorderLayout.SOUTH,SoutPanel);
		m_okButton.setLabel("Ok");
		SoutPanel.add(m_okButton);
		m_okButton.setBackground(java.awt.Color.lightGray);
		m_dummyButton.setLabel("dummyButton");
		SoutPanel.add(m_dummyButton);
		m_dummyButton.setBackground(java.awt.Color.lightGray);
		m_dummyButton.setVisible(false);
		m_cancelButton.setLabel("Cancel");
		SoutPanel.add(m_cancelButton);
		m_cancelButton.setBackground(java.awt.Color.lightGray);
		setTitle("Open Poject File");
		setResizable(false);
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		m_okButton.addActionListener(lSymAction);
		m_cancelButton.addActionListener(lSymAction);
		//}}
	}

	public OpenXmlDlg(Frame parent, boolean modal)
	{
		this(parent);
		setModal(modal);;
	}

	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension d = getSize();

		super.addNotify();

		if (fComponentsAdjusted)
			return;

		// Adjust components according to the insets
		Insets ins = getInsets();
		setSize(ins.left + ins.right + d.width, ins.top + ins.bottom + d.height);
		Component components[] = getComponents();
		for (int i = 0; i < components.length; i++)
		{
			Point p = components[i].getLocation();
			p.translate(ins.left, ins.top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}

	// Used for addNotify check.
	boolean fComponentsAdjusted = false;

	public OpenXmlDlg(Frame parent, String title, boolean modal)
	{
		this(parent, modal);
		setTitle(title);
	}

	public void setVisible(boolean b)
	{
		if (b)
		{
			m_projectList.removeAll();
			
			Rectangle bounds = getParent().getBounds();
			Rectangle abounds = getBounds();

			setLocation(bounds.x + (bounds.width - abounds.width)/ 2,
				bounds.y + (bounds.height - abounds.height)/2);
		}
		super.setVisible(b);
	}	

	public java.awt.List getList()
	{
		return m_projectList;	
	}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == OpenXmlDlg.this)
				OpenXmlDlg_WindowClosing(event);
		}
	}

	void OpenXmlDlg_WindowClosing(java.awt.event.WindowEvent event)
	{
		dispose();
	}
	//{{DECLARE_CONTROLS
	java.awt.Panel CenterPanel = new java.awt.Panel();
	java.awt.Label m_projectLabel = new java.awt.Label();
	java.awt.Panel m_listPanel = new java.awt.Panel();
	java.awt.List m_projectList = new java.awt.List(4);
	java.awt.Panel SoutPanel = new java.awt.Panel();
	java.awt.Button m_okButton = new java.awt.Button();
	java.awt.Button m_dummyButton = new java.awt.Button();
	java.awt.Button m_cancelButton = new java.awt.Button();
	//}}


	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == m_okButton)
				mOkButton_ActionPerformed(event);
			else if (object == m_cancelButton)
				mCancelButton_ActionPerformed(event);
		}
	}

	void mOkButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		if (m_projectList.getSelectedItem() != null)
		{
			m_projectName = m_projectList.getSelectedItem();
		}
		hide();
	}

	void mCancelButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		m_projectName = "";
		hide();
	}
	
	/** The name of the new project */
	public String m_projectName = "";
}