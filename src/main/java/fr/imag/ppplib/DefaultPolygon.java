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
import java.util.LinkedList;

/** A default implementation for polygons.
 **/

public class DefaultPolygon implements Polygon
{
    /** Add the given list to the current.
     ** @param l the list of vertices.
     ** @exception PolygonException thrown if there exists one point of the list which doesn't live in the plane.
     **/
    public void addVertices(List <double[]> l)
    {
        for (double[] v : l)
        {
            if (v.length != 2)
                throw new PolygonException(nitpMessage);
            vertices.add(v);
        }
    }
    
    /** Apply a homothety.
     ** @param a the homothety coefficient.
     **/
    public void scale(double a)
    {
        int n = vertices.size();
        for (int i = 0 ; i < n ; i++) vertices.set(i, vc.multiplyByScalar(a, vertices.get(i)));
    }
    
    /** Apply a translation.
     ** @param v the translation vector.
     ** @exception PolygonException thrown if v doesn't live in the plane.
     **/
    public void translate(double[] v)
    {
        if (v.length != 2)
            throw new PolygonException(nitpMessage);
        int n = vertices.size();
        for (int i = 0 ; i < n ; i++) vertices.set(i, vc.sum(v, vertices.get(i)));
    }
    
    /** Give a list of the vertices of the current polygon.
     ** @return the list.
     **/
    public List <double[]> getVertices()
    {
        return vertices;
    }
    
    /** Give a clone of the current polygon.
     **/
    public Polygon clone()
    {
        DefaultPolygon res = new DefaultPolygon();
        res.vertices = new LinkedList <double[]> ();
        for (double[] v : vertices) res.vertices.add(new double[] {v[0], v[1]});
        return res;
    }
    
    /** Give a printable version of the polygon.
     ** @return the string to print.
     **/
    public String toString()
    {
        String res = "";
        for (double[] v : vertices)
        {
            res += "(" + v[0] + ", " + v[1] + ") ";
        }
        return res;
    }
    
    private List <double[]> vertices = new LinkedList <double[]>();
    private VectorCalculator vc = ImplementationFactory.getNewVectorCalculator();
    private static final String nitpMessage = "A given vector doesn't live in the plane.";
}
