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

/** Interface that represents the support function of a convex set.
 **/

public interface SupportFunction
{
    /** Give the dimension of the vectorspace where lives the convex set described.
     ** @return the dimension.
     **/
    int getDimension();
    
    /** Evaluate the support function.
     ** @param dir a direction.
     **/
    void evaluate(double[] dir);
    
    /** Give the support vector.
     ** @return the support vector.
     **/
    double[] getSupportVector();
    
    /** Give the support function value.
     ** @return the support value.
     **/
    double getSupportValue();
    
    /** Give a clone of the current SupportFunction (usefull for multithreading).
     ** @return the clone.
     **/
    SupportFunction clone();
}
