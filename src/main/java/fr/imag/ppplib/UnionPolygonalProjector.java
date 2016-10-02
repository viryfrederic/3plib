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
import java.util.ArrayList;

/** An implementation of PolygonalProjector, to compute the projection of a union of convex sets described by their support function.
 **/

public class UnionPolygonalProjector implements PolygonalProjector
{
    /** Create a new UnionPolygonalProjector with a default behavior: it crashes if one convex set is impossible to project.
     **/
    public UnionPolygonalProjector()
    {
        ignoreImpossible = false;
    }
    
    /** Create a new UnionPolygonalProjector with a custom behavior: it is possible to ignore sets that are impossible to project.
     ** @param ignoreImpossible choice for behavior.
     **/
    public UnionPolygonalProjector(boolean ignoreImpossible)
    {
        this.ignoreImpossible = ignoreImpossible;
    }
    
    /** Compute the projection on a plane.
     ** @param convSets a list of convex sets described by their support function.
     ** @param pc the projection calculator embedding data on the plane.
     ** @param et the type of error.
     ** @param err the maximum error.
     ** @exception PolygonalProjectorException thrown if the sets don't live in the same vectorspace.
     **/
    public void computeProjection(List <? extends SupportFunction> convSets, ProjectionCalculator pc, ErrorType et, double err)
    {
        /* Initialization */
        int n = convSets.size();
        innerPolygons = new ArrayList <Polygon>(n);
        outerPolygons = new ArrayList <Polygon>(n);
        int d = convSets.get(0).getDimension();
        ConvexSetProjector csp = ImplementationFactory.getNewConvexSetProjector();
        
        /* Computations in function of the choosen behavior */
        if (ignoreImpossible)
        {
            for (SupportFunction sf : convSets)
            {
                try
                {
                    if (sf.getDimension() != d)
                        throw new PolygonalProjectorException(nsvsMessage);
                    csp.computeProjection(sf, pc, et, err);
                    innerPolygons.add(csp.getInnerPolygon());
                    outerPolygons.add(csp.getOuterPolygon());
                }
                catch (ConvexSetProjectorException e){}
            }
        }
        else
        {
            for (SupportFunction sf : convSets)
            {
                if (sf.getDimension() != d)
                    throw new PolygonalProjectorException(nsvsMessage);
                csp.computeProjection(sf, pc, et, err);
                innerPolygons.add(csp.getInnerPolygon());
                outerPolygons.add(csp.getOuterPolygon());
            }
        }
        
        /* Update the flag */
        evaluated = true;
    }   
    
    /** Give the list of inner polygons (underapproximation).
     ** @return the list of inner polygons.
     ** @exception PolygonalProjectorException thrown if no computation has been made yet.
     **/
    public List <Polygon> getInnerPolygons()
    {
        if (!evaluated)
            throw new PolygonalProjectorException(nyeMessage);
        return innerPolygons;
    }
    
    /** Give the list of outer polygons (overapproximation).
     ** @return the list of outer polygons.
     ** @exception PolygonalProjectorException thrown if no computation has been made yet.
     **/
    public List <Polygon> getOuterPolygons()
    {
        if (!evaluated)
            throw new PolygonalProjectorException(nyeMessage);
        return outerPolygons;
    }
    
    private List <Polygon> innerPolygons;
    private List <Polygon> outerPolygons;
    private boolean evaluated = false;
    private final boolean ignoreImpossible;
    private static final String nsvsMessage = "A convex set of the given list doesn't live in the same vectorspace than the others.";
    private static final String nyeMessage = "This PolygonalProjector has not been yet evaluated.";
}
