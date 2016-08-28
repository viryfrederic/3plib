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

/** A default implementation for the ProjectionCalculator, using the DefaultVectorCalculator.
 **/

public class DefaultProjectionCalculator implements ProjectionCalculator
{
    /** Create a new DefaultProjectionCalculator, which makes it possible to compute easily projections on the plane defined by p1 and p2.
     ** @param p1 a vector.
     ** @param p2 a vector.
     ** @exception ProjectionCalculatorException thrown if p1 and p2 don't live in the same vectorspace, p1 p2 are colinears, or one is zero.
     **/
    public DefaultProjectionCalculator(double[] p1, double[] p2)
    {
        if (p1 == null || p2 == null)
            emptyPC = true;
        else
        {
            if (p1.length != p2.length)
                throw new ProjectionCalculatorException(nsvsp1p2Message);
            if (vc.areColinears(p1, p2))
                throw new ProjectionCalculatorException(cp1p2Message);
            if (vc.isZeroVector(p1) || vc.isZeroVector(p2))
                throw new ProjectionCalculatorException(zvMessage);
            this.p1 = p1;
            this.p2 = p2;
        }
    }
    
    /** Give the dimension of the plane.
     ** @return the dimension.
     **/
    public int getDimension()
    {
        return p1.length;
    }
    
    /** Give the orthogonal projection of a on the plane defined by p1 and p2, as coordinates of the projection in function of p1 and p2.
     ** @param a a vector.
     ** @return the projection of a on the plane defined by p1 and p2 in 2d.
     ** @exception ProjectionCalculatorException thrown if a doesn't live in the same vectorspace than p1 and p2.
     **/
    // TODO A CORRIGER
    public double[] planarProjection(double[] a)
    {
        /* exception test */
        if (a.length != p1.length)
            throw new ProjectionCalculatorException(nsvsMessage);
        
        /* initialization */
        if (!initialized)
        {
            double p1p2 = vc.dotProduct(p1, p2);
            double np1 = vc.norm(p1);
            double np1sq = np1*np1;
            invnp1 = 1/np1;
            lambda = 0.0;
            mu = 1/vc.norm(p2);
            if (p1p2 != 0)
            {
                lambda = 1/(vc.norm(vc.difference(p1, vc.multiplyByScalar(np1sq/p1p2, p2))));
                mu = -lambda*np1sq/p1p2;
            }
            q1 = vc.multiplyByScalar(invnp1, p1);
            q2 = vc.sum(vc.multiplyByScalar(lambda, p1), vc.multiplyByScalar(mu, p2));
            
            initialized = true;
        }
        
        /* calculus and return */
        double t1 = vc.dotProduct(a, q1);
        double t2 = vc.dotProduct(a, q2);
        return new double[] {t1*invnp1 + t2*lambda, t2*mu};
    }
    
    /** Give the nd representation of a which is in the plane defined by p1 and p2.
     ** @param a2d a 2d-vector.
     ** @return the coordinates of a in the original vectorspace.
     ** @exception ProjectionCalculatorException thrown if a is not a 2d-vector.
     **/
    public double[] toOriginalSpace(double[] a2d)
    {
        if (a2d.length != 2)
            throw new ProjectionCalculatorException(n2dMessage);
        return vc.sum(vc.multiplyByScalar(a2d[0], p1), vc.multiplyByScalar(a2d[1], p2));
    }
    
    /** Give a new instance whose the type is the same than the current ProjectionCalculator (can be usefull for multithreading). The plane of projection is defined by p1 and p2.
     ** @param p1 a vector.
     ** @param p2 a vector.
     ** @return the new instance.
     **/
    public ProjectionCalculator newInstance(double[] p1, double[] p2)
    {
        return new DefaultProjectionCalculator(p1, p2);
    }
    
    /** Give a clone of the current ProjectionCalculator: same plane (usefull for multithreading).
     ** @return the clone.
     ** @exception ProjectionCalculatorException thrown if the projection calculator has no plane, as the default of the ImplementationFactory.
     **/
    public ProjectionCalculator clone()
    {
        if (emptyPC)
            throw new ProjectionCalculatorException(npaMessage);
        DefaultProjectionCalculator res = new DefaultProjectionCalculator(p1, p2);
        res.invnp1 = invnp1;
        res.lambda = lambda;
        res.mu = mu;
        res.q1 = q1;
        res.q2 = q2;
        res.initialized = true;
        return res;
    }
    
    private VectorCalculator vc = new DefaultVectorCalculator();
    private double[] p1;
    private double[] p2;
    private double invnp1, lambda, mu;
    private double[] q1, q2;
    private boolean initialized = false;
    private boolean emptyPC = false;
    private static final String nsvsMessage = "The given vector doesn't live in the same vectorspace than the plane.";
    private static final String nsvsp1p2Message = "Two different dimensions vectors cannot define a plane.";
    private static final String cp1p2Message = "Two colinears vectors cannot define a plane.";
    private static final String npaMessage = "No plane has been attributed to this projection calculator, create a new instance.";
    private static final String n2dMessage = "The given vector is not a 2d vector.";
    private static final String zvMessage = "A zero vector cannot define a plane.";
}
