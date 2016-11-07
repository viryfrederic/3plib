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

package fr.imag.ppplib.calc;

/** Interface that represents operations on projections on a plane of vectors.
 **/

public interface ProjectionCalculator
{
    /** Give the dimension of the plane.
     ** @return the dimension.
     **/
    int getDimension();
    
    /** Give the orthogonal projection of a on the plane defined by p1 and p2, as coordinates of the projection in function of p1 and p2.
     ** @param a a vector.
     ** @return the projection of a on the plane defined by p1 and p2 in 2d.
     **/
    double[] planarProjection(double[] a);
    
    /** Give the nd representation of a which is in the plane defined by p1 and p2.
     ** @param a2d a 2d-vector.
     ** @return the coordinates of a in the original vectorspace.
     **/
    double[] toOriginalSpace(double[] a2d);
    
    /** Give a new instance whose the type is the same than the current ProjectionCalculator (can be usefull for multithreading). The plane of
     ** projection is defined by p1 and p2.
     ** @param p1 a vector.
     ** @param p2 a vector.
     ** @return the new instance.
     **/
    ProjectionCalculator newInstance(double[] p1, double[] p2);
    
    /** Give a clone of the current ProjectionCalculator: same plane (usefull for multithreading).
     ** @return the clone.
     **/
    ProjectionCalculator clone();
}
