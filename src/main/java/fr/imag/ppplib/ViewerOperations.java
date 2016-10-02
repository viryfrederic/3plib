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

package fr.imag.ppplib;

/** Class that represents basic operations for a viewer.
 **/

public class ViewerOperations
{
    /** Create a new PolygonsViewer with a default configuration (in terms of sensitivity).
     **/
    public ViewerOperations()
    {
        scaleFactor = 1.1;
        pixelsSensitivity = 1.0;
    }
    
    /** Update the translation/scaling factors.
     ** @param deltaPixels the delta of pixels.
     ** @param zoomIn true if the zoom type is a zoom in.
     ** @param n the number of zooms.
     **/
    public void update(double[] deltaPixels, boolean zoomIn, int n)
    {
        if (zoomIn)
            for (int i = 0 ; i < n ; i++)
                zoomIn();
        else
            for (int i = 0 ; i < n ; i++)
                zoomOut();
        translate(deltaPixels);
    }
    
    /** Give the translation vector.
     ** @return the translation vector.
     **/
    public double getScale()
    {
        return scale;
    }
    
    /** Give the scaling factor.
     ** @return the scaling factor.
     **/
    public double[] getTranslation()
    {
        return translation;
    }
    
    /* Translate the image by the given delta of pixels. */
    private void translate(double[] deltaPixels)
    {
        translation[0] += deltaPixels[0]*pixelsSensitivity/scale;
        translation[1] += deltaPixels[1]*pixelsSensitivity/scale;
    }
    
    /* Zoom in in the image. */
    private void zoomIn()
    {
        scale *= 1.1;
    }
    
    /* Zoom out in the image. */
    private void zoomOut()
    {
        scale /= 1.1;
    }
    
    private double scale = 1.0;
    private double[] translation = new double[] {0.0, 0.0};
    private double scaleFactor;
    private double pixelsSensitivity;
}
