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

/** An implementation of the SupportFunction for the projection of a given set described by its SupportFunction. Given a convex set (by its SupportFunction) and a plane (2 vectors), it makes it possible to evaluate the support function of the projection of the convex set on the plane.
 **/

public class ProjectionSupportFunction implements SupportFunction
{
    /** Give the dimension of the vectorspace where lives the convex set described.
     ** @return the dimension.
     **/
    public int getDimension()
    {
        return convex.getDimension();
    }
    
    /** Create a new ProjectionSupportFunction, associated to a convex set and a plane. The convex set is cloned.
     ** @param convex a convex set given by its SupportFunction.
     ** @param pc a projection calculator, associated to a plane.
     ** @exception SupportFunctionException thrown if the convex set and the plane are incompatible.
     **/
    public ProjectionSupportFunction(SupportFunction convex, ProjectionCalculator pc)
    {
        if (convex.getDimension() != pc.getDimension())
            throw new SupportFunctionException(nsvsMessage);
        this.convex = convex.clone();
        this.pc = pc;
    }
    
    /** Evaluate the support function.
     ** @param dir a direction.
     **/
    public void evaluate(double[] dir)
    {
        double[] dirnd = pc.toOriginalSpace(dir);
        convex.evaluate(dirnd);
        supportVector = pc.planarProjection(convex.getSupportVector());
        supportValue = convex.getSupportValue();
        evaluated = true;
    }
    
    /** Give the support vector.
     ** @return the support vector.
     ** @exception SupportFunctionException thrown if the support function has never been evaluated
     **/
    public double[] getSupportVector()
    {
        if (!evaluated)
            throw new SupportFunctionException(nyeMessage);
        return supportVector;
    }
    
    /** Give the support function value.
     ** @return the support value.
     ** @exception SupportFunctionException thrown if the support function has never been evaluated
     **/
    public double getSupportValue()
    {
        if (!evaluated)
            throw new SupportFunctionException(nyeMessage);
        return supportValue;
    }
    
    /** Give a clone of the current SupportFunction (usefull for multithreading). Copy the given SupportFunction.
     ** @return the clone.
     **/
    public SupportFunction clone()
    {
        ProjectionSupportFunction res = new ProjectionSupportFunction();
        res.convex = convex.clone();
        res.pc = pc.clone();
        res.supportVector = supportVector;
        res.supportValue = supportValue;
        res.evaluated = evaluated;
        return res;
    }
    
    private ProjectionSupportFunction() {}
    
    private SupportFunction convex;
    private ProjectionCalculator pc;
    private double[] supportVector;
    private double supportValue;
    private boolean evaluated = false;
    private static final String nsvsMessage = "The convex set and the plane don't live in the same vector space.";
    private static final String nyeMessage = "The support function has never been solved evaluated.";
}
