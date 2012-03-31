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

import  javax.swing.tree.DefaultTreeModel;
import  javax.swing.tree.DefaultMutableTreeNode;
import  org.w3c.dom.*;
import  org.apache.xerces.dom.DocumentImpl;
import  org.apache.xerces.dom.DOMImplementationImpl;
import  org.w3c.dom.Document;
import  org.apache.xml.serialize.OutputFormat;
import  org.apache.xml.serialize.Serializer;
import  org.apache.xml.serialize.SerializerFactory;
import  org.apache.xml.serialize.XMLSerializer;
import  java.io.*;

/** Class XMLWriter Implements functions to write a Document in a xml file */
public class XMLWriter
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Constructor
	* @param document Document to write in file
	*/
	public XMLWriter(Document document)
	{
		m_document=document;
	}
	
	
	//////////////////////////////////////////////////////////////////////
	//
	// MANIPULATORS
	//
	//////////////////////////////////////////////////////////////////////

	/**
	* Sets the Document to write in the file
	* @param document Document object to write in the file
	*/
	public void setDocument(Document document) 	
	{	
		m_document=document;	
	}
		    
	
	//////////////////////////////////////////////////////////////////////
	//
	//  ACCESSORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Returns the Document
	* @retunr The Document
	*/
	Document getDocument()	
	{
		return m_document; 	
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////
	
	/**
	* Writes the Document in a xml file using the XMLSerializer
	* @param path Path of the xml file
	* @param filename Filename of the xml file
	*/
	public void writeXML(String path, String filename)
	{
		try
		{
			OutputFormat    format  = new OutputFormat( m_document );  
			//StringWriter  stringOut = new StringWriter();        
			
			File file=new File(path,filename);
			file.createNewFile();
			
			FileOutputStream fileOut = new FileOutputStream(file);
			XMLSerializer    serial = new XMLSerializer( fileOut, format );

			serial.asDOMSerializer();                            
			serial.serialize( m_document.getDocumentElement() );
			
			//System.out.println(stringOut.toString());
			fileOut.close();
		}
		catch (Exception e)
		{ 
			System.out.println("Error: writing XML:"); 
			System.out.println(e.getMessage());
		}
		
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	//  VARIABLES
	//
	//////////////////////////////////////////////////////////////////////        
	
	/** The Document object */
	Document	m_document;	
}