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

/** Interface that represents basic operations on matrices.
 **/

public interface MatrixCalculator
{
    /** Compute and give a*x.
     ** @param a the matrix.
     ** @param x the vector.
     ** @return the result vector.
     **/
    double[] multiply(double[][] a, double[] x);
    
    /** Transpose a.
     ** @param a the matrix.
     ** @return a transposed.
     **/
    double[][] transpose(double[][] a);
    
    /** Give a new instance whose the type is the same than the current MatrixCalculator (can be usefull for multithreading).
     ** @return the new instance.
     **/
    MatrixCalculator newInstance();
}
