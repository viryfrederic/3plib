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

import java.util.List;

/** Generic interface for polygons.
 **/

public interface Polygon
{
    /** Add the given list to the current.
     ** @param l the list of vertices.
     **/
    void addVertices(List <double[]> l);
    
    /** Apply a homothety.
     ** @param a the homothety coefficient.
     **/
    void scale(double a);
    
    /** Apply a translation.
     ** @param v the translation vector.
     **/
    void translate(double[] v);
    
    /** Give a list of the vertices of the current polygon.
     ** @return the list.
     **/
    List <double[]> getVertices();
    
    /** Give a clone of the current polygon.
     **/
    Polygon clone();
}
