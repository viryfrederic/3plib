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

import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.List;
import java.util.LinkedList;

/** An implementation of the linear programming solver using the linear programming module of Apache Commons Math.
 **/

public class ACMLinearProgrammingSolver implements LinearProgrammingSolver
{
    /** Give the dimension of the vectorspace where lives the convex set described.
     ** @return the dimension.
     **/
    public int getDimension()
    {
        return d;
    }
    
    /** Add a linear constraint to the set, of the type a.x <= b.
     ** @param a a vector of the same dimension than the vector space.
     ** @param b a scalar
     ** @exception LinearProgrammingSolverException thrown if a doesn't live in the same space than the convex polyhedron.
     **/
    public void addLinearConstraint(double[] a, double b)
    {
        /* The first linear constraint determines the dimension */
        if (lcList.isEmpty ())
            d = a.length;
        /* The nexts have to be controlled */
        else if (a.length != d)
            throw new LinearProgrammingSolverException(ddconstMessage);
        /* Update the list and the flag */
        lcList.add(new LinearConstraint(a, Relationship.LEQ, b));
        lcListModified = true;
    }
    
    /** Solve the linear program.
     ** @param dir a direction.
     ** @exception LinearProgrammingSolverException thrown if dir doesn't live in the same space than the convex polyhedron.
     **/
    public void solve(double[] dir)
    {
        /* Check the dimension of the vectorspace where lives dir */
        if (dir.length != d)
            throw new LinearProgrammingSolverException(dddirMessage);
        /* Update the constraints set of the simplex solver if modification */
        if (lcListModified)
            lcSet = new LinearConstraintSet(lcList);
        /* Evaluation */
        PointValuePair res = solver.optimize(new LinearObjectiveFunction(dir, 0), lcSet, GoalType.MAXIMIZE);
        /* Update the results and the flags */
        point = res.getPoint ();
        value = res.getSecond ();
        evaluated = true;
        lcListModified = false;
    }
    
    /** Give the point which satisfies the maximization.
     ** @return the point.
     ** @exception LinearProgrammingSolverException thrown if the problem has never been solved.
     **/
    public double[] getPoint()
    {
        if (!evaluated)
            throw new LinearProgrammingSolverException(nyeMessage);
        return point;
    }
    
    /** Give the maximum.
     ** @return the maximum value.
     ** @exception LinearProgrammingSolverException thrown if the problem has never been solved.
     **/
    public double getValue()
    {
        if (!evaluated)
            throw new LinearProgrammingSolverException(nyeMessage);
        return value;
    }
    
    /** Compute the chebyshev center of this polyhedron.
     **/
    public void computeChebyshevCenter()
    {
        LinearProgrammingSolver cp = new ACMLinearProgrammingSolver();
        for (LinearConstraint lc : lcList)
        {
            double[] dir = lc.getCoefficients().toArray();
            double[] dirp = new double[d+1];
            for (int i = 0 ; i < d ; i++) dirp[i] = dir[i];
            dir[d] = vc.norm(dir);
            cp.addLinearConstraint(dirp, lc.getValue());
        }
        double[] dirRadius = new double[d+1];
        dirRadius[d] = 1.0;
        cp.solve(dirRadius);
        double[] res = new double[d];
        double[] resp = cp.getPoint();
        for (int i = 0 ; i < d ; i++) res[i] = resp[i];
        cc = res;
        cr = cp.getValue();
    }
    
    /** Give the chebyshev center of this polyhedron.
     ** @return the chebyshev center.
     **/
    public double[] getChebyshevCenter()
    {
        return cc;
    }
    
    /** Give the chebyshev radius of this polyhedron.
     ** @return the chebyshev radius.
     **/
    public double getChebyshevRadius()
    {
        return cr;
    }
    
    /** Give a new instance whose the type is the same than the current LinearProgrammingSolver (can be usefull for multithreading).
     ** @return the new instance.
     **/
    public LinearProgrammingSolver newInstance()
    {
        return new ACMLinearProgrammingSolver();
    }
    
    /** Give a clone of the current LinearProgrammingSolver (usefull for multithreading). It copies only the constraints set (for eventual future modifications).
     ** @return the clone.
     **/
    public LinearProgrammingSolver clone()
    {
        ACMLinearProgrammingSolver res = new ACMLinearProgrammingSolver();
        /* Copy lcList */
        List <LinearConstraint> newLcList = new LinkedList <LinearConstraint>();
        newLcList.addAll(lcList);
        /* Update and return */
        res.lcList = newLcList;
        res.d = d;
        return res;
    }
    
    private int d;
    private List <LinearConstraint> lcList = new LinkedList <LinearConstraint>();
    private LinearConstraintSet lcSet;
    private SimplexSolver solver = new SimplexSolver();
    private VectorCalculator vc = ImplementationFactory.getNewVectorCalculator();
    private double[] point, cc;
    private double value, cr;
    private boolean lcListModified = true;
    private boolean evaluated = false;
    private static final String nyeMessage = "The linear program has never been solved.";
    private static final String dddirMessage = "The direction and the convex polyhedron don't live in the same dimension vectorspaces.";
    private static final String ddconstMessage = "The current constraint and the convex polyhedron don't live in the same dimension vectorspaces.";
}
