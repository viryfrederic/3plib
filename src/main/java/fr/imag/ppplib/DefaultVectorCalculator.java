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

/** A default hardcoded implementation for the VectorCalculator.
 **/

public class DefaultVectorCalculator implements VectorCalculator
{
    /** Give the sum between a and b.
     ** @param a a vector.
     ** @param b a vector.
     ** @return the sum.
     ** @exception VectorCalculatorException thrown if a and b doesn't live in the same vectorspace.
     **/
    public double[] sum(double[] a, double[] b)
    {
        int n = a.length;
        if (n != b.length)
            throw new VectorCalculatorException(nsvsMessage);
        double[] res = new double[n];
        for (int i = 0 ; i < n ; i++) res[i] = a[i] + b[i];
        return res;
    }
    
    /** Give the difference between a and b.
     ** @param a a vector.
     ** @param b a vector.
     ** @return the difference.
     **/
    public double[] difference(double[] a, double[] b)
    {
        int n = a.length;
        if (n != b.length)
            throw new VectorCalculatorException(nsvsMessage);
        double[] res = new double[n];
        for (int i = 0 ; i < n ; i++) res[i] = a[i] - b[i];
        return res;
    }
    
    /** Give the vector a multiplied by c.
     ** @param c a scalar.
     ** @param a a vector.
     ** @return the multiplication.
     **/
    public double[] multiplyByScalar(double c, double[] a)
    {
        int n = a.length;
        double[] res = new double[n];
        for (int i = 0 ; i < n ; i++) res[i] = c * a[i];
        return res;
    }
    
    /** Give the dotproduct between a and b.
     ** @param a a vector.
     ** @param b a vector.
     ** @return the dotproduct.
     ** @exception VectorCalculatorException thrown if a and b doesn't live in the same vectorspace.
     **/
    public double dotProduct(double[] a, double[] b)
    {
        int n = a.length;
        if (n != b.length)
            throw new VectorCalculatorException(nsvsMessage);
        double res = 0;
        for (int i = 0 ; i < n ; i++) res += a[i] * b[i];
        return res;
    }
    
    /** Give the norm of a.
     ** @param a a vector.
     ** @return the norm.
     **/
    public double norm(double[] a)
    {
        return Math.sqrt(dotProduct(a, a));
    }
    
    /** Test if two vectors are colinears.
     ** @param a a vector.
     ** @param b a vector.
     ** @return true if colinears, otherwise false.
     ** @exception VectorCalculatorException thrown if a and b doesn't live in the same vectorspace.
     **/
    public boolean areColinears(double[] a, double[] b)
    {
        int n = a.length;
        /* in the same vectorspace ? */
        if (n != b.length)
            throw new VectorCalculatorException(nsvsMessage);
        /* colinearity test */
        /* search k such as a_i = k*b_i */
        int i = 0;
        while (i < n && a[i] == 0 && b[i] == 0) i++;
        if (a[i] == 0 || b[i] == 0)
            return false;
        double k = a[i]/b[i];
        while (i < n && a[i] == k*b[i]) i++;
        return i == n;
    }
    
    /** Test if two vectors are equals.
     ** @param a a vector.
     ** @param b a vector.
     ** @return true if strictly equals, otherwise false.
     ** @exception VectorCalculatorException thrown if a and b doesn't live in the same vectorspace.
     **/
    public boolean areEquals(double[] a, double[] b)
    {
        int n = a.length;
        /* in the same vectorspace ? */
        if (n != b.length)
            throw new VectorCalculatorException(nsvsMessage);
        /* equality test */
        int i = 0;
        while (i < n && a[i] == b[i]) i++;
        return i == n;
    }
    
    /** Test if the given vector is a zero vector.
     ** @param a a vector.
     ** @return true if a is a zero vector, otherwise false.
     **/
    public boolean isZeroVector(double[] a)
    {
        int n = a.length;
        int i = 0;
        while (i < n && a[i] == 0) i++;
        return i == n;
    }
    
    /** Give a new instance whose the type is the same than the current VectorCalculator (can be usefull for multithreading).
     ** @return the new instance.
     **/
    public VectorCalculator newInstance()
    {
        return new DefaultVectorCalculator();
    }
    
    private static final String nsvsMessage = "The two vectors don't live in the same vectorspace.";
}
