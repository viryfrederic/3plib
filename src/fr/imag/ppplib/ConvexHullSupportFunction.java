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

/** An implementation of the SupportFunction for the convex hull of a list of convex sets.
 **/

public class ConvexHullSupportFunction implements SupportFunction
{
    /** Create a new ConvexHullSupportFunction for the convex hull of the given convex sets.
     ** @param convSets a list of convex sets.
     ** @exception SupportFunctionException thrown if the sets don't live in the same vectorspace.
     **/
    public ConvexHullSupportFunction(List <SupportFunction> convSets)
    {
        /* Same dimension ? */
        d = convSets.get(0).getDimension();
        for(SupportFunction sf : convSets)
        {
            if (sf.getDimension() != d)
                throw new SupportFunctionException(nsvsMessage);
        }
        
        /* OK */
        this.convSets = convSets;
    }
    
    /** Give the dimension of the vectorspace where lives the convex set described.
     ** @return the dimension.
     **/
    public int getDimension()
    {
        return d;
    }
    
    /** Evaluate the support function.
     ** @param dir a direction.
     **/
    public void evaluate(double[] dir)
    {
        /* Evaluation of each SupportFunction, maximum searching and result */
        supportValue = Double.NEGATIVE_INFINITY;
        supportVector = null;
        for (SupportFunction sf : convSets)
        {
            sf.evaluate(dir);
            if (sf.getSupportValue() > supportValue)
            {
                supportValue = sf.getSupportValue();
                supportVector = sf.getSupportVector();
            }
        }
        
        /* Flag update */
        evaluated = true;
    }
    
    /** Give the support vector.
     ** @return the support vector.
     ** @exception SupportFunctionException thrown if no computation has been made yet.
     **/
    public double[] getSupportVector()
    {
        if (!evaluated)
            throw new SupportFunctionException(nyeMessage);
        return supportVector;
    }
    
    /** Give the support function value.
     ** @return the support value.
     ** @exception SupportFunctionException thrown if no computation has been made yet.
     **/
    public double getSupportValue()
    {
        if (!evaluated)
            throw new SupportFunctionException(nyeMessage);
        return supportValue;
    }
    
    /** Give a clone of the current SupportFunction (usefull for multithreading). Copy each contained SupportFunction.
     ** @return the clone.
     **/
    public SupportFunction clone()
    {
        List <SupportFunction> l = new ArrayList <SupportFunction>(convSets.size());
        for (SupportFunction sf : convSets) l.add(sf.clone());
        return new ConvexHullSupportFunction(l);
    }
    
    private int d;
    private List <SupportFunction> convSets;
    private double supportValue;
    private double[] supportVector;
    private boolean evaluated = false;
    private static final String nsvsMessage = "A convex set of the given list doesn't live in the same vectorspace than the others.";
    private static final String nyeMessage = "This SupportFunction has not been yet evaluated.";
}
