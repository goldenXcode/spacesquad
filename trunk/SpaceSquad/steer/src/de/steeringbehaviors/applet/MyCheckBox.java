/*
 * MyCheckBox.java
 *
 * Created on 8. März 2001, 11:17
 */
package de.steeringbehaviors.applet;

import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.SystemColor;


/**
 *
 * @author  tom
 * @version 
 */
public class MyCheckBox extends java.awt.Checkbox {

    /** Creates new MyCheckBox */
    public MyCheckBox() {
    }

    /** Creates new MyCheckBox */
    public MyCheckBox(String label) {
        super(label);
        m_label = label;
        m_state = false;
    }
    
    /** Creates new MyCheckBox */
    public MyCheckBox(String label, boolean state) {
        super(label, state);
        m_label = label;
        m_state = state;
    }
    
    /** Creates new MyCheckBox */
    public MyCheckBox(String label, boolean state, CheckboxGroup group) {
        super(label, state, group);
        m_label = label;
        m_state = state;
    }
    
    /** Creates new MyCheckBox */
    public MyCheckBox(String label, CheckboxGroup group, boolean state) {
        super(label, group, state);
        m_label = label;
        m_state = state;
    }
    
    public void paint(Graphics g)
    {
        m_state = super.getState();
        m_label = super.getLabel();
        
        int width = (int) getSize().getWidth();
        int height = (int) getSize().getHeight();
        
        g.setColor(new Color(SystemColor.CONTROL));
        g.drawRect(0, 0, width, height);
        
        FontMetrics fm = g.getFontMetrics();
        int stringWidth = fm.stringWidth(m_label);
        int stringHeight = fm.getHeight();        
        
        // Draw the box pressed down
        if (m_state)
        {
            g.setColor(new Color(SystemColor.CONTROL_TEXT));
            g.drawString(m_label, (width / 2) - (stringWidth / 2), (height / 2) - (stringHeight / 2));
        }
        // Draw the box raised
        else
        {
            g.setColor(new Color(SystemColor.CONTROL_SHADOW));
            g.drawString(m_label, (width / 2) - (stringWidth / 2), (height / 2) - (stringHeight / 2));
        }
        
    }
    
    protected String m_label;
    protected boolean m_state;
}