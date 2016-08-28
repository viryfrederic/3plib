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

/** Interface for the projection of a convex set.
 **/

public interface ConvexSetProjector
{
    /** Compute the projection of the convex set described by the support function on a given plane, as an inner and an outer polygons, with a given error (Hausdorff distance between the two polygons).
     ** @param sf the support function of the convex set.
     ** @param pc the projection calculator embedding data on the plane.
     ** @param et the type of error.
     ** @param err the maximum error.
     **/
    void computeProjection(SupportFunction sf, ProjectionCalculator pc, ErrorType et, double err);
    
    /** Give the inner polygon.
     ** @return the polygon.
     **/
    Polygon getInnerPolygon();
    
    /** Give the outer polygon.
     ** @return the polygon.
     **/
    Polygon getOuterPolygon();
    
    /** Provide a new instance of the implementation of the ConvexSetProjector.
     ** @return the instance.
     **/
    ConvexSetProjector newInstance();
}
