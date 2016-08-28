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

/** Interface that represents a linear programming solver for maximization in a convex polyhedron. This polyhedron is given in H-representation: a_i.x <= b_i, with {a_i} vectors and {b_i} scalars.
 **/

public interface LinearProgrammingSolver
{
    /** Give the dimension of the vectorspace where lives the convex set described.
     ** @return the dimension.
     **/
    int getDimension();
    
    /** Add a linear constraint to the set, of the type a.x <= b.
     ** @param a a vector of the same dimension than the vector space.
     ** @param b a scalar
     **/
    void addLinearConstraint(double[] a, double b);
    
    /** Solve the linear program.
     ** @param dir a direction.
     **/
    void solve(double[] dir);
    
    /** Give the point which satisfies the maximization.
     ** @return the point.
     **/
    double[] getPoint();
    
    /** Give the maximum.
     ** @return the maximum value.
     **/
    double getValue();
    
    /** Give a new instance whose the type is the same than the current LinearProgrammingSolver (can be usefull for multithreading).
     ** @return the new instance.
     **/
    LinearProgrammingSolver newInstance();
    
    /** Give a clone of the current LinearProgrammingSolver (usefull for multithreading). It copies only the constraints set (for eventual future modifications).
     ** @return the clone.
     **/
    LinearProgrammingSolver clone();
}
