/*
Steering Behaviors Demo Applet

Copyright (C) 2001	Thomas Feilkas
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
package de.steeringbehaviors.simulation.xml;

import org.apache.xerces.parsers.*;
import org.w3c.dom.*;
import org.w3c.dom.traversal.*;
import org.xml.sax.InputSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;

/**
*   class XMLReader
*
*   Implements the main functions for parsing a XML-file
*/
public class XMLReader
{
	
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Constructor
	*/
	public XMLReader()
	{
	}
	
	
	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Returns the Document object
     	* @return The Document object
     	*/	
	public Document getDocument()	
	{
		
		return m_document;
	}
	                
	/**
	* Returns the TreeWalker object
	* @return The TreeWalker object
	*/
	public TreeWalker getTreeWalker()
	{
		// create a treeWalker object to walk trought the tree ------------------
		m_treeWalker = ((DocumentTraversal)m_document).createTreeWalker(
            		      	m_document,NodeFilter.SHOW_ELEMENT, null , true);
	 
	 	return m_treeWalker; 		
	}


	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////
	
	
 	/**
     	* Reads and parses a a XML-file
     	* @param filename Name of the XML-file to parse
     	*/
	public void readXMLFile(String path, String filename) {
		File file=new File(path,filename);
		readXMLFile(file);
	}
	
	public void readXMLFile(File file)
	{
		try {
			readXMLFile(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	/**
	* Reads and parses the specified inputstream
	* @param in InputStream to parse
	*/
	public void readXMLFile(InputStream in)
	{
		// parse the xml-file --------------------
			
		m_parser = new DOMParser();
		try
		{
			m_parser.parse(new InputSource(in));

			m_document = m_parser.getDocument();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		finally {
			try {
				in.close();
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		}
	}


    	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////        	

	/** The DOMParser object */
	DOMParser	m_parser;
	
	/** The Document object */
	Document	m_document;
	
	/** The TreeWalker object */
	TreeWalker	m_treeWalker;
	

}