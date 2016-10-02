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

/** Interface that represents basic operations on vectors.
 **/

public interface VectorCalculator
{
    /** Give the sum between a and b.
     ** @param a a vector.
     ** @param b a vector.
     ** @return the sum.
     **/
    double[] sum(double[] a, double[] b);
    
    /** Give the difference between a and b.
     ** @param a a vector.
     ** @param b a vector.
     ** @return the difference.
     **/
    double[] difference(double[] a, double[] b);
    
    /** Give the vector a multiplied by c.
     ** @param c a scalar.
     ** @param a a vector.
     ** @return the multiplication.
     **/
    double[] multiplyByScalar(double c, double[] a);
    
    /** Give the dotproduct between a and b.
     ** @param a a vector.
     ** @param b a vector.
     ** @return the dotproduct.
     **/
    double dotProduct(double[] a, double[] b);
    
    /** Give the norm of a.
     ** @param a a vector.
     ** @return the norm.
     **/
    double norm(double[] a);
    
    /** Test if two vectors are colinears.
     ** @param a a vector.
     ** @param b a vector.
     ** @return true if colinears, otherwise false.
     **/
    boolean areColinears(double[] a, double[] b);
    
    /** Test if two vectors are equals.
     ** @param a a vector.
     ** @param b a vector.
     ** @return true if strictly equals, otherwise false.
     **/
    boolean areEquals(double[] a, double[] b);
    
    /** Test if the given vector is a zero vector.
     ** @param a a vector.
     ** @return true if a is a zero vector, otherwise false.
     **/
    boolean isZeroVector(double[] a);
    
    /** Give a new instance whose the type is the same than the current VectorCalculator (can be usefull for multithreading).
     ** @return the new instance.
     **/
    VectorCalculator newInstance();
}
