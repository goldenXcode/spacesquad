/*
 * BasicMenueGenerator.java
 *
 * Created on 5. März 2001, 15:09
 */

package de.steeringbehaviors.applet;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Scrollbar;



/**
 *
 * @author  tom
 * @version 
 */
public class BasicMenueGenerator extends Object {

    /** Reference to thhe applet */
    private BasicApplet  m_applet;
    
    public Scrollbar   scrollUp, scrollLeft;
    
    /** Creates new BasicMenueGenerator */
    public BasicMenueGenerator(BasicApplet applet) 
    {
        m_applet = applet;
    }

    /**
     * Methode, to initialize the graphical user interface.<br>
     * Creates all panels, scrollbars, buttons and LayoutManagers.
     * For this applet, a <code>GridBagLayout</code> has been chosen.
     */
    public void initControls()
    {		
        GridBagLayout gbl = new GridBagLayout();
        m_applet.setLayout(gbl);

        GridBagConstraints gbConstr = new GridBagConstraints();
        gbConstr.fill = GridBagConstraints.BOTH;
        gbConstr.insets = new Insets(0, 0, 0, 0);

        gbConstr.gridx = 0;
        gbConstr.gridy = 0;
        gbConstr.gridwidth = 4;
        gbConstr.gridheight = 6;
        gbConstr.weightx = gbConstr.weighty = 1.0;

        m_applet.m_canvas = new Canvas();        
        m_applet.add(m_applet.m_canvas, gbConstr);

        gbConstr = new GridBagConstraints();
        gbConstr.gridx = 0;
        gbConstr.gridy = 7;
        gbConstr.gridwidth = 4;
        gbConstr.gridheight = 1;
        gbConstr.weightx= 1.0; gbConstr.weighty = 0.05;
        gbConstr.fill = GridBagConstraints.BOTH;

        Panel panel1 = new Panel(new GridBagLayout());        
        m_applet.add(panel1, gbConstr);
        
        // Unteres Panel in zwei weitere Panels aufteilen
        gbConstr.gridx = 0;
        gbConstr.gridy = 0;
        gbConstr.gridwidth = 1;
        gbConstr.gridheight = 1;
        Panel panel2 = new Panel(new GridBagLayout());
        panel1.add(panel2, gbConstr);
        
        gbConstr.gridx = 1;
        gbConstr.gridy = 0;
        gbConstr.gridwidth = 1;
        gbConstr.gridheight = 1;
        Panel panel3 = new Panel(new GridBagLayout());
        panel1.add(panel3, gbConstr);
        
        // Start / Reset Buttons in das linke untere Panel einfügen
        
        Button b1 = new Button("Pause");
        gbConstr = new GridBagConstraints();
        gbConstr.insets = new Insets(3, 3, 3, 3);
        gbConstr.gridx = 0;
        gbConstr.gridy = 0;
        gbConstr.gridwidth = 1;
        gbConstr.gridheight = 1;
        gbConstr.weightx= 1.0; gbConstr.weighty = 1.0;
        gbConstr.fill = GridBagConstraints.BOTH;
        panel2.add(b1, gbConstr);
        
        Button b2 = new Button("Restart");
        gbConstr = new GridBagConstraints();
        gbConstr.insets = new Insets(3, 3, 3, 3);
        gbConstr.gridx = 1;
        gbConstr.gridy = 0;
        gbConstr.gridwidth = 1;
        gbConstr.gridheight = 1;
        gbConstr.weightx= 1.0; gbConstr.weighty = 1.0;
        gbConstr.fill = GridBagConstraints.BOTH;
        panel2.add(b2, gbConstr);
        
        // Das untere, rechte Panel erhält die auswahlmöglichkeit zwischen
        // Panning, Zooming und Selecting Objekte per linker Maustaste
        
        CheckboxGroup grp = new CheckboxGroup();
        
        Checkbox chk1 = new Checkbox("Select", true, grp);
        gbConstr = new GridBagConstraints();
        gbConstr.insets = new Insets(3, 3, 3, 3);
        gbConstr.gridx = 1;
        gbConstr.gridy = 0;
        gbConstr.gridwidth = 1;
        gbConstr.gridheight = 1;
        gbConstr.weightx= 1.0; gbConstr.weighty = 1.0;
        gbConstr.fill = GridBagConstraints.BOTH;
        panel3.add(chk1, gbConstr);
        
        chk1.addItemListener(m_applet);
        
        Checkbox chk2 = new Checkbox("Pann", true, grp);
        gbConstr = new GridBagConstraints();
        gbConstr.insets = new Insets(3, 3, 3, 3);
        gbConstr.gridx = 2;
        gbConstr.gridy = 0;
        gbConstr.gridwidth = 1;
        gbConstr.gridheight = 1;
        gbConstr.weightx= 1.0; gbConstr.weighty = 1.0;
        gbConstr.fill = GridBagConstraints.BOTH;
        panel3.add(chk2, gbConstr);
        
        chk2.addItemListener(m_applet);
        
        Checkbox chk3 = new Checkbox("Zoom", true, grp);
        gbConstr = new GridBagConstraints();
        gbConstr.insets = new Insets(3, 3, 3, 3);
        gbConstr.gridx = 3;
        gbConstr.gridy = 0;
        gbConstr.gridwidth = 1;
        gbConstr.gridheight = 1;
        gbConstr.weightx= 1.0; gbConstr.weighty = 1.0;
        gbConstr.fill = GridBagConstraints.BOTH;
        panel3.add(chk3, gbConstr);
        
        chk3.addItemListener(m_applet);
        
        //CubePanel panel = new CubePanel();        
    }
}