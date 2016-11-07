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

package fr.imag.ppplib.proj.plg;

import fr.imag.ppplib.calc.VectorCalculator;
import fr.imag.ppplib.calc.ImplementationFactory;

import java.util.List;
import java.util.LinkedList;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** A default implementation for polygons.
 **/

public class DefaultPolygon implements Polygon
{
    @Override
    public void addVertices(List <double[]> l)
    {
        for (double[] v : l)
        {
            if (v.length != 2)
                throw new PolygonException(nitpMessage);
            vertices.add(v);
        }
    }
    
    @Override
    public void scale(double a)
    {
        int n = vertices.size();
        for (int i = 0 ; i < n ; i++) vertices.set(i, vc.multiplyByScalar(a, vertices.get(i)));
    }
    
    @Override
    public void translate(double[] v)
    {
        if (v.length != 2)
            throw new PolygonException(nitpMessage);
        int n = vertices.size();
        for (int i = 0 ; i < n ; i++) vertices.set(i, vc.sum(v, vertices.get(i)));
    }
    
    @Override
    public List <double[]> getVertices()
    {
        return vertices;
    }
    
    @Override
    public Polygon clone()
    {
        DefaultPolygon res = new DefaultPolygon();
        res.vertices = new LinkedList <double[]> ();
        for (double[] v : vertices) res.vertices.add(new double[] {v[0], v[1]});
        return res;
    }
    
    @Override
    public Polygon toConvex()
    {
        System.out.println("toConvex");
        double[] p1 = farthestPoint(vertices, new double[] {0, -1});
        double[] p2 = farthestPoint(vertices, new double[] {0, 1});
        List <double[]> res = new LinkedList <double[]>();
        res.addAll(findHull(pointsAfter(vertices, rightOrtho(toVector(p1, p2)), p1), p1, p2));
        res.addAll(findHull(pointsAfter(vertices, rightOrtho(toVector(p2, p1)), p2), p2, p1));
        Polygon pres = new DefaultPolygon();
        pres.addVertices(res);
        return pres;
    }

    private List <double[]> findHull(List <double[]> l, double[] p1, double[] p2)
    {
        List <double[]> res = new LinkedList <double[]>();
        //System.out.println(p1[0] + " " + p1[1] + " ; " + p2[0] + " " + p2[1]);
        double[] d = vc.difference(p2, p1);
        //System.out.println(d[0] + " " + d[1]);
        if (l.isEmpty())
        {
            res.add(p1);
        }
        else
        {
            double[] p = farthestPoint(l, rightOrtho(toVector(p1, p2)));
            //System.out.println(p[0] + " " + p[1]);
            res.addAll(findHull(pointsAfter(l, p1, p), p1, p));
            res.addAll(findHull(pointsAfter(l, p, p2), p, p2));
        }
        return res;
    }

    /* Give a rounded value of the given double (10^-6 precision) */
    /*private double round(double x)
    {
        return BigDecimal.valueOf(x).setScale(6, RoundingMode.HALF_UP).doubleValue();
    }*/

    /* Give a list of points after p1 and p2 in the normal direction of p1p2 (to the right) */
    private List <double[]> pointsAfter(List <double[]> l, double[] p1, double[] p2)
    {
        double[] d = rightOrtho(toVector(p1, p2));
        double min = Math.max(vc.dotProduct(d, p1), vc.dotProduct(d, p2));
        List <double[]> res = new LinkedList <double[]>();
        for (double[] x : l)
        {
            double val = vc.dotProduct(d, x);
            if (val > min)
            // Dangerous line : vc.dotProduct(d, x) has more digits than min
            //if (vc.dotProduct(d, x) > min)
                res.add(x);
        }
        return res;
    }
    
    /* Give the farthest point is a given direction */
    private double[] farthestPoint(List <double[]> l, double[] d)
    {
        double[] res = null;
        double max = -Double.MAX_VALUE;
        for (double[] x : l)
        {
            double v = vc.dotProduct(d, x);
            if (v > max)
            {
                max = v;
                res = x;
            }
        }
        return res;
    }

    /* Give the vector p1p2 */
    private double[] toVector(double[] p1, double[] p2)
    {
        return vc.difference(p2, p1);
    }

    /* Give the left orthogonal vector to d */
    private double[] rightOrtho(double[] d)
    {
        return new double[] {d[1], -d[0]};
    }

    @Override
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
