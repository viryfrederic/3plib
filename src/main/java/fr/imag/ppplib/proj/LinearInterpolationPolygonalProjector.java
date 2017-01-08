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

package fr.imag.ppplib.proj;

import fr.imag.ppplib.calc.sf.SupportFunction;
import fr.imag.ppplib.calc.sf.ConvexHullSupportFunction;
import fr.imag.ppplib.calc.ProjectionCalculator;
import fr.imag.ppplib.proj.plg.Polygon;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/** An implementation of PolygonalProjector, to compute the projection of the linear interpolation of convex sets described by their support function, i.e. the union of the convex hulls of each successive couple of convex sets.
 **/

public class LinearInterpolationPolygonalProjector implements PolygonalProjector
{
    /** Create a new LinearInterpolationPolygonalProjector with a default behavior: it crashes if one convex set is impossible to project.
     **/
    public LinearInterpolationPolygonalProjector()
    {
        upp = new UnionPolygonalProjector(false);
    }
    
    /** Create a new LinearInterpolationPolygonalProjector with a custom behavior: it is possible to ignore sets that are impossible to project.
     ** @param ignoreImpossible choice for behavior.
     **/
    public LinearInterpolationPolygonalProjector(boolean ignoreImpossible)
    {
        upp = new UnionPolygonalProjector(ignoreImpossible);
    }
    
    @Override
    public void computeProjection(List <? extends SupportFunction> convSets, ProjectionCalculator pc, ErrorType et, double err)
    {
        /* Generate the list of convex hulls */
        List <SupportFunction> chspl = new ArrayList <SupportFunction>(convSets.size()-1);
        Iterator <? extends SupportFunction> it = convSets.iterator();
        SupportFunction curr = null;
        if (it.hasNext())
            curr = it.next();
        while (it.hasNext())
        {
            List <SupportFunction> chl = new ArrayList <SupportFunction>(2);
            chl.add(curr);
            curr = it.next();
            chl.add(curr);
            chspl.add(new ConvexHullSupportFunction(chl));
        }
        
        /* Compute the projection of the union of the convex hulls */
        upp.computeProjection(chspl, pc, et, err);
    }   
    
    @Override
    public List <Polygon> getInnerPolygons()
    {
        return upp.getInnerPolygons();
    }
    
    @Override
    public List <Polygon> getOuterPolygons()
    {
        return upp.getOuterPolygons();
    }

    @Override
    public boolean existResults()
    {
        return upp.existResults();
    }

    private UnionPolygonalProjector upp;
}

