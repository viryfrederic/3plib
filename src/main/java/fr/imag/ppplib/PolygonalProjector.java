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

/** Interface that represents the commons operations on projections of sets.
 **/

public interface PolygonalProjector
{
    /** Compute the projection on a plane.
     ** @param convSets a list of convex sets described by their support function.
     ** @param pc the projection calculator embedding data on the plane.
     ** @param et the type of error.
     ** @param err the maximum error.
     **/
    void computeProjection(List <? extends SupportFunction> convSets, ProjectionCalculator pc, ErrorType et, double err);    
    
    /** Give the list of inner polygons (underapproximation).
     ** @return the list of inner polygons.
     **/
    List <Polygon> getInnerPolygons();
    
    /** Give the list of outer polygons (overapproximation).
     ** @return the list of outer polygons.
     **/
    List <Polygon> getOuterPolygons();
}
