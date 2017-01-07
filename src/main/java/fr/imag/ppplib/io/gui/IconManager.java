/** Polygonal Planar Projection LIBrary (3plib) v0.2.0
 ** Copyright © 2016 Frédéric Viry
 ** author: Frédéric Viry (Laboratoire Verimag, Grenoble, France)
 ** mail: ask3plib@gmail.com
 **
 ** This file is part of 3plib.
 **
 ** 3plib is free software: you can redistribute it and/or modify
 ** it under the terms of the GNU Lesser General Public License as published by
 ** the Free Software Foundation, either version 3 of the License, or
 ** at your option) any later version.
 ** 
 ** 3plib is distributed in the hope that it will be useful,
 ** but WITHOUT ANY WARRANTY; without even the implied warranty of
 ** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 ** GNU Lesser General Public License for more details.
 ** 
 ** You should have received a copy of the GNU Lesser General Public License
 ** along with 3plib. If not, see <http://www.gnu.org/licenses/>.
 **/

package fr.imag.ppplib.io.gui;

import javax.swing.ImageIcon;
import java.util.Vector;

class IconManager
{
	private IconManager ()
	{
		icons = new Vector <IconStruct> ();
	}
	
	public static IconManager getInstance ()
	{
		return INSTANCE;
	}

	public ImageIcon getIcon (String name)
	{
		int index = getIndex (name);
		if (index == icons.size ())
			icons.add (new IconStruct (name));
		return icons.elementAt (index).icon;
	}
	
	private int getIndex (String name)
	{
		int i = 0;
		while (i < icons.size () && icons.elementAt (i).name != name) i++;
		return i;
	}
	
	class IconStruct
	{
		public IconStruct (String s)
		{
			icon = new ImageIcon (s);
			name = s;
		}

		public String name;
		public ImageIcon icon;
	}
	
	private static IconManager INSTANCE = new IconManager ();
	private static Vector <IconStruct> icons;
}
