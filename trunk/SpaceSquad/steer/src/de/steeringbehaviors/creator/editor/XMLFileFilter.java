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

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/** Class XMLFileFilter Implements a custom file filter for xml files */
public class XMLFileFilter extends FileFilter 
{
	//////////////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////////////
	
	/** Constructor */
	public XMLFileFilter() {}

	
	/** 
	* Returns if the file file is accepted
	* @param f File to check
	* @return Accept the file
	*/
	public boolean accept(File f) 
	{
	if(f != null) 
	{
		if(f.isDirectory()) 
		{
			return true;
		}
		String extension = getExtension(f);
		if ((extension != null) && getExtension(f).equals("xml"))
		{
			return true;
		};
	}
	return false;
}

	//////////////////////////////////////////////////////////////////////
	//
	//  UTILITY FUNCTIONS
	//
	//////////////////////////////////////////////////////////////////////


	/**
	* Returns the extension of a file
	* @param f File object
	* @return The extension of the specified file
	*/
	public String getExtension(File f) 
	{
		if(f != null) 
		{
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if(i>0 && i<filename.length()-1) 
			{
				return filename.substring(i+1).toLowerCase();
			};
		}
		return null;
	}


	/**
	* Returns the description of the FileFilter
	* @return File type description
	*/
	public String getDescription() 
	{
		return new String("XML-Files");
	}

}
