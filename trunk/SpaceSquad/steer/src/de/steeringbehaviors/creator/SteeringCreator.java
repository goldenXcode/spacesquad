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

package de.steeringbehaviors.creator;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.w3c.dom.Document;
import org.w3c.dom.traversal.TreeWalker;

import de.steeringbehaviors.creator.editor.AttributeEditor;
import de.steeringbehaviors.creator.editor.CenterPanel;
import de.steeringbehaviors.creator.editor.DOMGenerate;
import de.steeringbehaviors.creator.editor.EditorCanvas;
import de.steeringbehaviors.creator.editor.ObjectGenerator;
import de.steeringbehaviors.creator.editor.SimulationCanvas;
import de.steeringbehaviors.creator.editor.SteeringMenuManager;
import de.steeringbehaviors.creator.editor.SteeringTree;
import de.steeringbehaviors.creator.editor.SteeringTreeNode;
import de.steeringbehaviors.creator.editor.WindowClosingAdapter;
import de.steeringbehaviors.creator.editor.XMLFileFilter;
import de.steeringbehaviors.creator.editor.XMLWriter;
import de.steeringbehaviors.simulation.xml.XMLReader;






/** Implements the main class of the SteeringCreator */
public class SteeringCreator
extends JFrame
implements ActionListener, ChangeListener
{
	private static final String DEFAULT_XML = "de.steeringbehaviors.creator.config.default";
	
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	public SteeringCreator()
	{
		super("Steering Creator v1.0 BETA");
		
		addWindowListener(new WindowClosingAdapter(true));
		m_filename = new String();
		m_filename = null;
	
		m_geometrieObjects = new Hashtable();
		
		// get the contentpane
		m_cp = getContentPane();		
		
		m_cp.add(new JPanel());
		
		// create menus
		m_menuManager = new SteeringMenuManager(this);		
		setJMenuBar(m_menuManager.getMainMenu());
	
		m_objectGenerator=new ObjectGenerator(this);	
		
	
	}  // SteeringCreator


	//////////////////////////////////////////////////////////////////////
	//
	// MAIN FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////

	/** The main function of the SteeringCreator */
	public static void main(String[] args)
	{
		SteeringCreator m_steeringCreator = new SteeringCreator();
		
		m_steeringCreator.setLocation(50,50);
		m_steeringCreator.setSize(900,600);
		m_steeringCreator.setVisible(true);
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
	
	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////

	
	
	public void loadFile(InputStream iStream) {
		try {
			XMLReader xmlReader = new XMLReader();
			xmlReader.readXMLFile(iStream);
			m_treeWalker = xmlReader.getTreeWalker();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	* Loads a scene specified by the filename
	* @param path Path of the xml file
	* @param file Filename of the xml file
	*/
	public void loadFile(String path, String file)
	{
		try
		{
			// set the current opened filename
			m_filename = file;
			m_path = path;
			// parse XML-file and create a TreeWalker
			XMLReader xmlReader = new XMLReader();
			xmlReader.readXMLFile(path, file);
			m_treeWalker = xmlReader.getTreeWalker();
		}
		catch (Exception e)
		{
			System.out.println("Error loading file ("+file+")");
		}
	} // loadFile

	/** Saves a scene into a xml file
	* @param path Path of the xml file
	* @param file Filename of the xml file
	*/    	
	public void saveFile(String path, String file)
	{
		try
		{
			// write DOM into a xml-file
			DOMGenerate generator=new DOMGenerate();
			Document doc=generator.createDOM((DefaultTreeModel) m_steeringTree.getModel());
		
			XMLWriter xmlwriter = new XMLWriter(doc);
			xmlwriter.writeXML(path, file);
		
			// set the current filename
			m_filename=file;
			m_path=path;
		}
		catch(Exception e)
		{
			System.out.println("Error saving file ("+file+" in "+path+")");
			System.out.println(e.getMessage());
		}
	} // saveFile

	/** Loads default.xml */
	public void newFile()
	{
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		
		String defaultFile = DEFAULT_XML.replace(".",File.separator)+".xml";
		URL objectsURL = cl.getResource(defaultFile);

		
		InputStream iStream;
		try {
			iStream = objectsURL.openStream();
			loadFile(iStream);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		m_filename=null;
		m_path=".";
	} 
	
	

	/** Creates the UI of the editor */
	public void createUI()
	{
		// get the size of the frame
		int width = (int) this.getSize().getWidth();
		int height= (int) this.getSize().getHeight();
		
		// set the layoutmanager
		m_cp = getContentPane();
		m_cp.removeAll();
		m_cp.setLayout(new BorderLayout(10,10));

		// create a JTree
		String cap=m_filename;
		if (cap==null) cap=new String("new scene");
		m_steeringTree=new SteeringTree(new DefaultMutableTreeNode(cap),m_treeWalker);
		// steering is selected
		m_menuManager.updateToolbarButtons("steering");
		
		// create and add the JTree
		m_treeScrollPane = new JScrollPane(m_steeringTree);
		m_treeScrollPane.setPreferredSize(new Dimension((int) (width*0.8),(int) (height*0.7) ));
		
		// create and add the hintWindow
		m_hintWindow = new JTextArea();
		m_hintWindow.setEditable(false);
		m_hintScrollPane = new JScrollPane(m_hintWindow);
		//m_hintScrollPane.setPreferredSize(new Dimension(300,(int) (height*0.3) ));
		//m_cp.add(m_hintScrollPane, "South");
		      		
		// create and add the attribute-editor
		m_attributeEditor = new AttributeEditor(m_steeringTree.getSelectedSteeringTreeNode(),this);
		m_attributeScrollPane = new JScrollPane(m_attributeEditor);
		//m_attributeScrollPane.setPreferredSize(new Dimension((int) (width*0.2) ,height));
		m_cp.add(m_attributeScrollPane, "East");      	
		
		// create a canvas
		m_editorCanvas=new EditorCanvas(m_geometrieObjects, m_steeringTree);
		m_editorCanvas.setGeometrieObjects(m_geometrieObjects);
		m_editorCanvas.setAttributeEditor(m_attributeEditor);
		m_editorCanvas.setMenuManager(m_menuManager);
		
		m_attributeEditor.setEditorCanvas(m_editorCanvas);
		
		m_centerPanel = new CenterPanel(m_editorCanvas);
		
		// create a toolbox ************************************************
		
		// zoom
		m_zoomSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, 100 );
		m_zoomSlider.setPaintTicks(false);
		m_zoomSlider.setPaintLabels(false);
		m_zoomSlider.setMajorTickSpacing(100);
		m_zoomSlider.setMinorTickSpacing(10);
		m_zoomSlider.setSnapToTicks(true);
		m_zoomSlider.addChangeListener(this);
		
		m_zoomText = new JLabel(m_zoomSlider.getValue() + "%");
		
		// grid on/off
		//JCheckBox m_gridCheck = new JCheckBox("Show Grid",true);
		
		

		JPanel m_toolBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
		//m_toolBox.add(m_gridCheck);
		
		m_toolBox.add(new JLabel("Zoom"));
		m_toolBox.add(m_zoomSlider);
		m_toolBox.add(m_zoomText);
		m_toolBox.add(m_menuManager.getToolbar());
		
		m_cp.add(m_toolBox,"North");
		// *****************************************************************			
		// update scene description
		m_editorCanvas.updateDescriptions(m_steeringTree);
				
		// create a moveable horizontal splitpane		
		JSplitPane hsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		hsp.setLeftComponent(m_treeScrollPane);
		    		    		
		hsp.setRightComponent(m_centerPanel);
		hsp.setOneTouchExpandable(true);
		hsp.setContinuousLayout(true);
		
		hsp.setPreferredSize(new Dimension(0 ,(int) (height*0.7) ));
		
		// create a moveable vertical splitpane
		JSplitPane m_verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		m_verticalSplitPane.setTopComponent(hsp);
		m_verticalSplitPane.setBottomComponent(m_hintScrollPane);
		m_verticalSplitPane.setOneTouchExpandable(true);
		m_verticalSplitPane.setContinuousLayout(true);
		
		m_cp.add(m_verticalSplitPane,"Center");

		m_editorCanvas.resize();
		
		// add a TreeSelectionListener
		m_steeringTree.addTreeSelectionListener( new TreeSelectionListener()
						{
							public void valueChanged(TreeSelectionEvent ev)
							{
								TreePath tp=ev.getNewLeadSelectionPath();
								if (tp!=null)
								{	
									// value has changed
									onTreeValueChanged(tp);
								}
							}
						}
						);
		
		//enableEvents(AWTEvent.MOUSE_EVENT_MASK);				
		MouseListener ml = new MouseAdapter() 
		{
			public void mouseReleased(MouseEvent event) 
			{
				if (event.isPopupTrigger())
				{
					int selRow = m_steeringTree.getRowForLocation(event.getX(), event.getY());
					TreePath treepath = m_steeringTree.getPathForLocation(event.getX(), event.getY());

					if((selRow != -1)&&(selRow!=0)) 
					{
						if(event.getClickCount() == 1) 
						{
							// select the node
							m_steeringTree.setSelectionPath(treepath);
							
							// get the selected node
							SteeringTreeNode stn=(SteeringTreeNode) ((DefaultMutableTreeNode) treepath.getLastPathComponent()).getUserObject();
							JPopupMenu popup=m_menuManager.getTreeContextMenu(stn.getType());
							
							// show a context menu
							popup.show(event.getComponent(),event.getX(),event.getY() );
						}
					}	
				}
				
			}
		};
		
		m_steeringTree.addMouseListener(ml);
		
	} // createUI
	
	
	//////////////////////////////////////////////////////////////////////
	//
	//  EVENT HANDLING FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////
	
	
	
	/**
	* Implement the ChangeListener method stateChanged
	*/
	public void stateChanged(ChangeEvent event)
	{
		// zooming
		JSlider zoomSlider = (JSlider) event.getSource();
		if (zoomSlider.getValue()>5)
			m_editorCanvas.getRenderer().setZoom(zoomSlider.getValue()/100.0);

		m_zoomText.setText(zoomSlider.getValue()+"%");
		// repaint the center panel and the included editorCanvas
		if (m_centerPanel!=null) m_centerPanel.repaint();
				
	}
	
	/** 
	* The selection of the tree has changed
	* @param tp Path to new selected node
	*/
	private void onTreeValueChanged(TreePath tp)
	{
		try
		{			
			SteeringTreeNode stn=(SteeringTreeNode)((DefaultMutableTreeNode) tp.getLastPathComponent()).getUserObject();			
			
			// Get the hint of the selected object
			String hint = m_objectGenerator.getHint(stn.getName());
			if (hint==null) hint=new String("No informations available of this object");
			
			// enable/disable menu buttons
			m_menuManager.updateToolbarButtons(stn.getType());
			
			// set the hint text
			m_hintWindow.setText(hint);
			m_hintScrollPane.getVerticalScrollBar().setValue(103);
			
			// the attribute-editor					
			m_attributeEditor=new AttributeEditor(stn, this);
			m_editorCanvas.setAttributeEditor(m_attributeEditor);
			m_attributeEditor.setEditorCanvas(m_editorCanvas);
			
			m_cp.remove(m_attributeScrollPane);
			
			m_attributeScrollPane = new JScrollPane(m_attributeEditor);
			m_attributeScrollPane.setPreferredSize(new Dimension(200,100));
			
			m_cp.add(m_attributeScrollPane,BorderLayout.EAST);
			
			// validate
			m_cp.validate();
			
			// select the object
			m_editorCanvas.setActiveObject(stn.getObjectDescription());
			m_editorCanvas.repaint();
		} 
		catch (Exception e) 
		{ 	
			// deselect objects in the canvas
			m_editorCanvas.deselectObjects();
			m_editorCanvas.repaint();
		}
	} // onTreeValueChanged

	
	/**
	* Menu item clicked
	*/
	private void onAddObstacleClick()
	{
		DefaultMutableTreeNode newnode=m_objectGenerator.addObstacle(m_steeringTree);
		if (newnode != null)
		{
			m_steeringTree.setNewName(newnode, ((SteeringTreeNode)newnode.getUserObject()).getCaption());
		
			m_editorCanvas.updateDescriptions(m_steeringTree);
			m_editorCanvas.repaint();
		}
	} 
	
	/**
	* Menu item clicked
	*/
	private void onAddBehaviorClick()
	{
		m_objectGenerator.addBehavior(m_steeringTree);
	} 
	
	/**
	* Menu item clicked
	*/
	private void onAddMindClick()
	{
		m_objectGenerator.addMind(m_steeringTree);
	}
	
	/**
	* Menu item clicked
	*/
	private void onAddVehicleClick()
	{      
		DefaultMutableTreeNode newnode=m_objectGenerator.addVehicle(m_steeringTree);
		m_steeringTree.setNewName(newnode, ((SteeringTreeNode)newnode.getUserObject()).getCaption());
		
		m_editorCanvas.updateDescriptions(m_steeringTree);
		m_editorCanvas.repaint();
	}
	
	/**
	* Menu item clicked
	*/
	private void onRenameClick()
	{
		// look which object is selected
		DefaultMutableTreeNode treeNode=m_steeringTree.getSelectedNode();
		
		// ask the new name of the object
		String ret = (String) JOptionPane.showInputDialog(this, "Input new name","Rename",JOptionPane.QUESTION_MESSAGE);
		if (ret!=null)
		{
			m_steeringTree.setNewName(treeNode, ret);
		}
	} 
	
	/**
	* Menu item clicked
	*/
	private void onDeleteClick()
	{
		// look which object is selected
		DefaultMutableTreeNode treeNode=m_steeringTree.getSelectedNode();
		
		// delete this node
		DefaultTreeModel treeModel=(DefaultTreeModel) m_steeringTree.getModel();
		
		// select the parent node 
		m_steeringTree.selectNode((DefaultMutableTreeNode)treeNode.getParent());
		
		// remove the node
		treeModel.removeNodeFromParent(treeNode);		
		
		m_editorCanvas.updateDescriptions(m_steeringTree);	
	} 
	
		
	/**
	* Menu item "Clone" clicked
	*/
	private void onCloneObjectClick()
	{
		// look which object is selected
		DefaultMutableTreeNode source = m_steeringTree.getSelectedNode();
		DefaultMutableTreeNode destination = (DefaultMutableTreeNode) source.getParent();
		
		// clone the subtree
		DefaultMutableTreeNode newNode = m_steeringTree.cloneSubTree(source, destination);
		
		// select the root of the cloned subtree
		m_steeringTree.selectNode(newNode);
		
		// change position of cloned object
		try
		{
			// get the position of the object
			double x = new Double( ((SteeringTreeNode)source.getUserObject()).getAttribute("x") ).doubleValue();
			double y = new Double( ((SteeringTreeNode)source.getUserObject()).getAttribute("y") ).doubleValue();
			
			// add an offset value
			x = x + 20;
			y = y + 20;
						
			// set the new position
			((SteeringTreeNode) newNode.getUserObject()).setAttribute("x", (new Double(x)).toString());
			((SteeringTreeNode) newNode.getUserObject()).setAttribute("y", (new Double(y)).toString());
			
			// update the canvas view
			m_editorCanvas.updateDescriptions(m_steeringTree);
			m_editorCanvas.repaint();

		} catch(Exception e) {}
	}


	/**
	* Menu item "Open File" clicked
	*/
	private void onFileOpenClick()
	{
		// create filechooser
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileFilter(new XMLFileFilter());
		chooser.setCurrentDirectory(new File(m_path,""));
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) 
		{
			// open selected file
			m_cp.removeAll();
		
			loadFile(chooser.getCurrentDirectory().toString(),chooser.getSelectedFile().getName());
		
			createUI();
			m_cp.validate();
		}
	} 
	
	/**
	* Menu item "Close" clicked
	*/
	private void onFileCloseClick()
	{
		m_cp.removeAll();
		m_cp.add(new JPanel());
		m_cp.validate();
	} 

	/**
	* Menu item "New" clicked
	*/
	private void onFileNewClick()
	{
		m_cp.removeAll();
		newFile();
		createUI();
		m_cp.validate();
	}
	
	/**
	* Menu item "Save" clicked
	*/
	private void onFileSaveClick()
	{
		// check if filename is set
		if (m_filename==null) onFileSaveAsClick();
		else saveFile(m_path,m_filename);
	}
	
	/**
	* Menu item "Save As" clicked
	*/
	private void onFileSaveAsClick()
	{
		// create filechooser
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new XMLFileFilter());
		chooser.setMultiSelectionEnabled(false);
		
		chooser.setCurrentDirectory(new File(m_path,""));
		//chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		
		int returnVal = chooser.showSaveDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) 
		{	
			saveFile(chooser.getCurrentDirectory().toString(),chooser.getSelectedFile().getName());
		}
	} 

	/**
	* Menu item "Run" clicked
	*/
	private void onRunClick()
	{
		saveFile(".","preview.tmp");
		
		JFrame sim = new JFrame("Simulation Preview");
		
		sim.setLocation(80,80);
		
		
		// set the layoutmanager
		Container cp = sim.getContentPane();
		cp.removeAll();
		SimulationCanvas m_simulationCanvas = new SimulationCanvas();
		
		int width = m_simulationCanvas.getRenderer().getSceneWidth();
		int height = m_simulationCanvas.getRenderer().getSceneHeight();
		sim.setSize(width,height);
		
		cp.add(m_simulationCanvas);
		
		sim.setVisible(true);
		
	}	
	
	/** Implement the ActionListener function actionPerfomed */    	
	public void actionPerformed(ActionEvent event)
	{
		String cmd = event.getActionCommand();
		    		
		if ( cmd.equals("Exit") ) 
			System.exit(0); 
			
		if (cmd.equals("Open..."))		{ onFileOpenClick(); 	}
		if (cmd.equals("Close"))		{ onFileCloseClick();	}
		if (cmd.equals("New"))			{ onFileNewClick();	}
		if (cmd.equals("Save"))			{ onFileSaveClick();	}
		if (cmd.equals("Save As..."))		{ onFileSaveAsClick();	}
		
		if (cmd.equals("Delete"))		{ onDeleteClick();	}
		if (cmd.equals("Rename..."))		{ onRenameClick();	}
		if (cmd.equals("Add vehicle"))		{ onAddVehicleClick();	}
		if (cmd.equals("Add obstacle")) 	{ onAddObstacleClick(); }
		if (cmd.equals("Add mind..."))  	{ onAddMindClick();	}
		if (cmd.equals("Add behavior...")) 	{ onAddBehaviorClick(); }
		
		if (cmd.equals("Run"))			{ onRunClick();		}
		
		if (cmd.equals("Clone"))		{ onCloneObjectClick();	}	
	}
	
	
	
	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////       
	
	/** The AttributeEditor */
	AttributeEditor 	m_attributeEditor;
	/** ContentPane */
	Container		m_cp;
	/** The ScrollPane for the AttributeEditor */
	JScrollPane		m_attributeScrollPane;
	/** The ScrollPane for the JTree */
	JScrollPane		m_treeScrollPane;
	/** The ScrollPane for the hint window */
	JScrollPane		m_hintScrollPane;
	/** SteeringMenuManager which creates the menues */
	SteeringMenuManager	m_menuManager;
	/** The TreeWalker object */
	TreeWalker		m_treeWalker;
	/** The JTree which shows the scene description */
	SteeringTree		m_steeringTree;
	/** Filename of the current opened file */
	String			m_filename;
	/** Path of the current opened file */
	String			m_path=".";
	/** Hashtable of all scene objects, stored with their name as key */
	Hashtable		m_geometrieObjects;
	/** The canvas of the editor */
	EditorCanvas		m_editorCanvas;
	/** The center panel */
	CenterPanel		m_centerPanel;
	/** The ScrollPane of the editorcanvas */
	JScrollPane		m_canvasScrollPane;
	/** The text which shows the zoom value in percent */
	JLabel			m_zoomText;
	/** A JSlider for zooming the scene */
	JSlider			m_zoomSlider;
	/** A textfield which contains informations of an selected object */
	JTextArea		m_hintWindow;
	/** The vertical Split pane */
	JSplitPane		m_verticalSplitPane;	
	/** The ObjectGenerator object */
	ObjectGenerator 	m_objectGenerator;

 } 