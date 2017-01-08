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

import fr.imag.ppplib.proj.plg.Polygon;

import javax.swing.JFrame;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ComponentListener;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.util.List;

/** Class that represents the viewer of polyhedra.
 **/

public class View extends JFrame 
{
    private ViewerOperations vo = new ViewerOperations();
    private PolygonsPane pp = new PolygonsPane();
    private List<Polygon> innerPoly, outerPoly;
    private PolygonsViewer pv;
    
    /** Create a new View.
     **/
    public View(PolygonsViewer pv)
    {
        this.setSize(800, 600);
        this.setTitle("3PLIB's Polyhedra Viewer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new SettingsPane(), BorderLayout.NORTH);
        getContentPane().add(pp, BorderLayout.CENTER);
        this.setVisible(true);
        this.pv = pv;
    }
    
    /** Update the list of inner polygons and outer polygons.
     **/
    public void update(List<Polygon> innerPoly, List<Polygon> outerPoly)
    {
        this.innerPoly = innerPoly;
        this.outerPoly = outerPoly;
        pp.repaint();
    }
    
    private class SettingsPane extends JToolBar implements ActionListener
    {
        private Button openButton = new Button("/fr/imag/ppplib/icons/open");
        private Button saveButton = new Button("/fr/imag/ppplib/icons/save");
        private Button selectPlaneButton = new Button("/fr/imag/ppplib/icons/select_plane");
        private final JFileChooser fc = new JFileChooser();
        
        public SettingsPane()
        {
            super(JToolBar.HORIZONTAL);
            this.setLayout(new FlowLayout(FlowLayout.CENTER));
            this.setOpaque (false);
		    this.setFloatable (false);
            this.setBackground(new Color(238, 238, 238));
            this.add(openButton);
            this.add(saveButton);
            this.add(selectPlaneButton);
            openButton.addActionListener(this);
            saveButton.addActionListener(this);
            selectPlaneButton.addActionListener(this);            
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == openButton)
            {
                int val = fc.showOpenDialog(this);
                if (val == JFileChooser.APPROVE_OPTION)
                    pv.open(fc.getSelectedFile().getPath());
            }
            else if (e.getSource() == saveButton)
            {
                if (pv.existResults())
                {
                    int val = fc.showSaveDialog(this);
                    if (val == JFileChooser.APPROVE_OPTION)
                        pv.save(fc.getSelectedFile().getPath());
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "No results are available to export yet.", "Export Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
            else if (e.getSource() == selectPlaneButton)
            {
                // TODO toImplement
            }
        }
    }
    
    /* The class which manages the polygons drawing and the inputs. */
    private class PolygonsPane extends JPanel implements MouseListener, MouseWheelListener, MouseMotionListener, ComponentListener
    {
        /* Init the panel. */
        public PolygonsPane()
        {
            this.setBackground(Color.WHITE);
            this.addMouseListener(this);
            this.addMouseWheelListener(this);
            this.addMouseMotionListener(this);
            this.addComponentListener(this);
            h = (int)getSize().getHeight();
            w = (int)getSize().getWidth();
        }
        
        /* Paint the polygons. */
        protected void paintComponent(Graphics g)
        {
            /* Apply transformations and get how to transform points. */
            vo.update(new double[] {curX - lastX, curY - lastY}, lastWheelType, lastWheelRotation);
            double[] translation = vo.getTranslation();
            double scale = vo.getScale();
            
            /* Draw polygons */
            super.paintComponent(g);
            if (outerPoly != null)
            {
                g.setColor(Color.RED);
                for (Polygon p : outerPoly)
                {
                    List <double[]> l = p.getVertices();
                    int n = l.size();
                    int[] x = new int[n + 1];
                    int[] y = new int[n + 1];
                    int i = 0;
                    for (double[] v : l)
                    {
                        x[i] = (int)(scale*(v[0] + translation[0])) + w/2;
                        y[i] = h/2 - (int)(scale*(v[1] - translation[1]));
                        i++;
                    }
                    x[n] = x[0];
                    y[n] = y[0];
                    g.fillPolygon(x, y, n+1);
                }
            }
            if (innerPoly != null)
            {
                g.setColor(Color.BLUE);
                for (Polygon p : innerPoly)
                {
                    List <double[]> l = p.getVertices();
                    int n = l.size();
                    int[] x = new int[n + 1];
                    int[] y = new int[n + 1];
                    int i = 0;
                    for (double[] v : l)
                    {
                        x[i] = (int)(scale*(v[0] + translation[0])) + w/2;
                        y[i] = h/2 - (int)(scale*(v[1] - translation[1]));
                        i++;
                    }
                    x[n] = x[0];
                    y[n] = y[0];
                    g.fillPolygon(x, y, n+1);
                }
            }
            
            /* Reset mouse values. */
            lastWheelRotation = 0;
        }
        
        /* Mouse pressed. */
        public void mouseClicked(MouseEvent e){}
        public void mouseEntered(MouseEvent e){}
        public void mouseExited(MouseEvent e){}
        public void mousePressed(MouseEvent e)
        {
            lastX = e.getX();
            lastY = e.getY();
            curX = lastX;
            curY = lastY;
        }
        public void mouseReleased(MouseEvent e)
        {
            lastX = 0;
            lastY = 0;
            curX = 0;
            curY = 0;
            repaint();
        }
        
        /* Mouse wheel. */
        public void	mouseWheelMoved(MouseWheelEvent e)
        {
            lastWheelRotation = Math.abs(e.getWheelRotation());
            lastWheelType = e.getWheelRotation() < 0;
            repaint();
        }
        
        /* Mouse dragged. */
        public void mouseDragged(MouseEvent e)
        {
            lastX = curX;
            lastY = curY;
            curX = e.getX();
            curY = e.getY();
            repaint();
        }
        public void mouseMoved(MouseEvent e){}
        
        /* Resize. */
        public void componentHidden(ComponentEvent e){}
        public void componentMoved(ComponentEvent e){}
        public void componentResized(ComponentEvent e)
        {
            h = (int)getSize().getHeight();
            w = (int)getSize().getWidth();
            repaint();
        }
        public void componentShown(ComponentEvent e){}
        
        private int lastWheelRotation = 0;
        private boolean lastWheelType = false;
        private int lastX = 0;
        private int lastY = 0;
        private int curX = 0;
        private int curY = 0;
        private int h = 0;
        private int w = 0;
    }
}
