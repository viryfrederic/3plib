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

package fr.imag.ppplib.calc.sf;

import fr.imag.ppplib.calc.opti.LinearProgrammingSolver;
import fr.imag.ppplib.calc.ImplementationFactory;

import java.util.List;

/** An implementation of the SupportFunction for a convex polyhedron in H-representation.
 **/

public class ConvexPolyhedronSupportFunction implements SupportFunction
{        
    /** Add a linear constraint to the set, of the type a.x <= b.
     ** @param a a vector of the same dimension than the vector space.
     ** @param b a scalar
     **/
    public void addLinearConstraint(double[] a, double b)
    {
        lps.addLinearConstraint(a, b);
    }
    
    /** Compute the chebyshev center of this polyhedron.
     **/
    public void computeChebyshevCenter()
    {
        lps.computeChebyshevCenter();
    }
    
    /** Give the chebyshev center of this polyhedron.
     ** @return the chebyshev center.
     **/
    public double[] getChebyshevCenter()
    {
        return lps.getChebyshevCenter();
    }
    
    /** Give the chebyshev radius of this polyhedron.
     ** @return the chebyshev radius.
     **/
    public double getChebyshevRadius()
    {
        return lps.getChebyshevRadius();
    }
    
    /** Give the list of directions of each hyperplane.
     ** @return the list of directions.
     **/
    public List <double[]> getDirections()
    {
        return lps.getDirections();
    }
    
    /** Give the list of constraint values of each hyperplane.
     ** @return the list of constraint values.
     **/
    public List <Double> getValues()
    {
        return lps.getValues();
    }
    
    @Override
    public int getDimension()
    {
        return lps.getDimension();
    }
    
    @Override
    public void evaluate(double[] dir)
    {
        lps.solve(dir);
    }
    
    @Override
    public double[] getSupportVector()
    {
        return lps.getPoint();
    }
    
    @Override
    public double getSupportValue()
    {
        return lps.getValue();
    }
    
    @Override
    public SupportFunction clone()
    {
        ConvexPolyhedronSupportFunction res = new ConvexPolyhedronSupportFunction();
        res.lps = this.lps.clone();
        return res;
    }
    
    private LinearProgrammingSolver lps = ImplementationFactory.getNewLinearProgrammingSolver();
}
