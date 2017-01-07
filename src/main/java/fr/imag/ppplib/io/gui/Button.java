/** Polygonal Planar Projection LIBrary (3plib) v0.1.0
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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Button extends JButton implements MouseListener
{
	private ImageIcon normalIcon;
	private ImageIcon mouseIcon;
	private ImageIcon onClickIcon;
	private int key;
	
	public Button(String name)
	{
		super();
		this.normalIcon = IconManager.getInstance().getIcon(name + "_normal.png");
		this.mouseIcon = IconManager.getInstance().getIcon(name + "_mouse.png");
		this.onClickIcon = IconManager.getInstance().getIcon(name + "_onclick.png");
		this.setIcon(normalIcon);
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setMargin(new Insets (0, 0, 0, 0));
		this.setContentAreaFilled(false);
		this.addMouseListener(this);
		this.key = 0;
	}
	
	public void setNormal()
	{
		this.setIcon (normalIcon);
	}
	
	public void setOnClick()
	{
		this.setIcon (onClickIcon);
	}
	
	public void mouseClicked(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e)
	{
		this.setIcon(mouseIcon);
	}
	
	public void mouseExited (MouseEvent e)
	{
		this.setIcon(normalIcon);
	}
	
	public void mousePressed (MouseEvent e)
	{
		this.setIcon(onClickIcon);
	}
	
	public void mouseReleased(MouseEvent e)
	{
		if (e.getX() > 0 && e.getY() > 0 && e.getX() < this.getWidth() && e.getY() < this.getHeight())
			this.setIcon (mouseIcon);
		else
			this.setIcon (normalIcon);
	}
}
