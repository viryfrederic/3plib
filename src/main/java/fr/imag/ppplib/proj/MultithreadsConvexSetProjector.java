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
import fr.imag.ppplib.calc.sf.ProjectionSupportFunction;
import fr.imag.ppplib.calc.ProjectionCalculator;
import fr.imag.ppplib.calc.VectorCalculator;
import fr.imag.ppplib.calc.ImplementationFactory;
import fr.imag.ppplib.proj.plg.Polygon;
import fr.imag.ppplib.proj.plg.DefaultPolygon;

import java.util.List;
import java.util.LinkedList;

/** A multithreads implementation for the projection of a convex set.
 **/

public class MultithreadsConvexSetProjector implements ConvexSetProjector
{
    /** Construct a new MultithreadsConvexSetProjector with default configuration : 1 thread.
     **/
    public MultithreadsConvexSetProjector() {}
    
    /** Construct a new MultithreadsConvexSetProjector with a custom configuration.
     ** @param tn the threads number.
     **/
    public MultithreadsConvexSetProjector(ThreadsNumber tn)
    {
        this.tn = tn;
    }
    
    @Override
    public void computeProjection(SupportFunction sf, ProjectionCalculator pc, ErrorType et, double err)
    {
        try
        {
            if (!initialized)
            {
                vc = ImplementationFactory.getNewVectorCalculator();
                initialized = true;
            }
            
            /* Exceptions */
            if (sf.getDimension() != pc.getDimension())
                throw new ConvexSetProjectorException(nsvsMessage);
            if (et == ErrorType.ABSOLUTE && err <= 0)
                throw new ConvexSetProjectorException(absMessage);
            if (et == ErrorType.RELATIVE && (err <= 0 || err > 1))
                throw new ConvexSetProjectorException(relMessage);
            
            /* Initialization : projection support function, result polygon */
            SupportFunction psf = new ProjectionSupportFunction(sf, pc);
            innerPolygon = new DefaultPolygon();
            outerPolygon = new DefaultPolygon();
            
            /* First approximations */
            List <LocalApproximation> fa = new LinkedList <LocalApproximation>();
            psf.evaluate(dNorth);
            double[] north = psf.getSupportVector();
            psf.evaluate(dWest);
            double[] west = psf.getSupportVector();
            psf.evaluate(dSouth);
            double[] south = psf.getSupportVector();
            psf.evaluate(dEast);
            double[] east = psf.getSupportVector();
            /* Reject equals points and compute local approximations */
            if (!vc.areEquals(north, west))
                fa.add(new LocalApproximation(dNorth, north, dWest, west));
            if (!vc.areEquals(west, south))
                fa.add(new LocalApproximation(dWest, west, dSouth, south));
            if (!vc.areEquals(south, east))
                fa.add(new LocalApproximation(dSouth, south, dEast, east));
            if (!vc.areEquals(east, north))
                fa.add(new LocalApproximation(dEast, east, dNorth, north));
            /* If fa is empty, then the projection is a point */
            if (fa.isEmpty())
            {
                Polygon res = new DefaultPolygon();
                List <double[]> l = new LinkedList <double[]>();
                l.add(north);
                res.addVertices(l);
                innerPolygon = res;
                outerPolygon = res;
            }
            /* If necessary, compute the absolute error in function of the relative : abserr = relerr * max(h,w), h height, w width */
            if (et == ErrorType.RELATIVE)
            {
                double w = east[0] - west[0];
                double h = north[1] - south[1];
                if (h > w)
                    err = h * err;
                else
                    err = w * err;
            }
            
            /* Successive multithreads approximations */
            /* Threads management */
            /* Only one */
            List <SuccessiveApproximationThread> tl = new LinkedList <SuccessiveApproximationThread>();
            int nbla = fa.size();
            if (tn == ThreadsNumber.TN_1)
            {
                tl.add(new SuccessiveApproximationThread(fa, psf, err));
            }
            /* Two */
            else if (tn == ThreadsNumber.TN_2)
            {
                if (nbla == 2)
                {
                    tl.add(new SuccessiveApproximationThread(fa.subList(0, 1), psf, err));
                    tl.add(new SuccessiveApproximationThread(fa.subList(1, 2), psf, err));
                }
                else if (nbla == 3)
                {
                    tl.add(new SuccessiveApproximationThread(fa.subList(0, 2), psf, err));
                    tl.add(new SuccessiveApproximationThread(fa.subList(2, 3), psf, err));
                }
                else if (nbla == 4)
                {
                    tl.add(new SuccessiveApproximationThread(fa.subList(0, 2), psf, err));
                    tl.add(new SuccessiveApproximationThread(fa.subList(2, 4), psf, err));
                }
            }
            /* At most four (as possible) */
            else
            {
                if (nbla == 2)
                {
                    tl.add(new SuccessiveApproximationThread(fa.subList(0, 1), psf, err));
                    tl.add(new SuccessiveApproximationThread(fa.subList(1, 2), psf, err));
                }
                else if (nbla == 3)
                {
                    tl.add(new SuccessiveApproximationThread(fa.subList(0, 1), psf, err));
                    tl.add(new SuccessiveApproximationThread(fa.subList(1, 2), psf, err));
                    tl.add(new SuccessiveApproximationThread(fa.subList(2, 3), psf, err));
                }
                else if (nbla == 4)
                {
                    tl.add(new SuccessiveApproximationThread(fa.subList(0, 1), psf, err));
                    tl.add(new SuccessiveApproximationThread(fa.subList(1, 2), psf, err));
                    tl.add(new SuccessiveApproximationThread(fa.subList(2, 3), psf, err));
                    tl.add(new SuccessiveApproximationThread(fa.subList(3, 4), psf, err));
                }
            }
            /* Calculus */
            int nbt = tl.size();
            for (int i = 1 ; i < nbt ; i++) tl.get(i).start();
            tl.get(0).run();
            try
            {
                for (int i = 1 ; i < nbt ; i++) tl.get(i).join();
            }
            catch (InterruptedException e)
            {
                throw new ConvexSetProjectorException(e.getMessage());
            }
            /* Construction of the solution */
            List <LocalApproximation> lal = new LinkedList <LocalApproximation>();
            for (SuccessiveApproximationThread t : tl) lal.addAll(t.getResult());
            List <double[]> il = new LinkedList <double[]>();
            List <double[]> ol = new LinkedList <double[]>();
            for (LocalApproximation la : lal)
            {
                il.add(la.getInnerVertice());
                ol.addAll(la.getOuterVertices());
            }
            innerPolygon.addVertices(il);
            outerPolygon.addVertices(ol);
        }
        catch(Exception e)
        {
            throw new ConvexSetProjectorException(cseMessage);
        }
        
        /* Flag update */
        evaluated = true;
    }
    
    @Override
    public Polygon getInnerPolygon()
    {
        if (!evaluated)
            throw new ConvexSetProjectorException(nyeMessage);
        return innerPolygon;
    }
    
    @Override
    public Polygon getOuterPolygon()
    {
        if (!evaluated)
            throw new ConvexSetProjectorException(nyeMessage);
        return outerPolygon;
    }
    
    @Override
    public ConvexSetProjector newInstance()
    {
        return new MultithreadsConvexSetProjector(tn);
    }
    
    /* A class with references to two inner polygon vertices and one outer polygon vectex, to make easily successive approximations. */
    private class LocalApproximation
    {
        /* Construct a new local approximation with inner vertices and directions to describe support lines. */
        public LocalApproximation(double[] d1, double[] innerV1, double[] d2, double[] innerV2)
        {
            this.innerV1 = innerV1;
            this.innerV2 = innerV2;
            this.d1 = d1;
            this.d2 = d2;
            outerV = intersectionPoint(d1, innerV1, d2, innerV2);
            n = turningRightOrthogonal(vc.difference(innerV2, innerV1));
            error = computeError();
        }
        
        /* Return the "begining" inner vertice (vertice 1) */
        public double[] getInnerVertice()
        {
            return innerV1;
        }
        
        /* Return the outer vertice */
        public List<double[]> getOuterVertices()
        {
            List<double[]> l = new LinkedList <double[]> ();
            if (innerV1[0] != outerV[0] || innerV1[1] != outerV[1])
                l.add(innerV1);
            l.add(outerV);
            return l;
        }
        
        /* Return the error of the approximation */
        public double getError()
        {
            return error;
        }
        
        /* Compute the refinement of the approximation, given the projection support function */
        public List <LocalApproximation> refine(SupportFunction psf)
        {
            psf.evaluate(n);
            double[] innerV = psf.getSupportVector();
            List <LocalApproximation> res = new LinkedList <LocalApproximation>();
            res.add(new LocalApproximation(d1, innerV1, n, innerV));
            res.add(new LocalApproximation(n, innerV, d2, innerV2));
            return res;
        }
        
        /* Give the intersection point of the support lines d1*x = d1*p1 and d2*x = d2*p2.
         * NB : a little error implies closed points and closed slopes. If the calculus is impossible, then the error is too small for the precision of double.
         */
        private double[] intersectionPoint(double[] d1, double[] p1, double[] d2, double[] p2)
        {
            /* Transform to lines t*u1+p1 and tp*u2+p2 */
            double[] u1 = turningRightOrthogonal(d1);
            double[] u2 = turningRightOrthogonal(d2);
            double det = u2[0]*u1[1] - u2[1]*u1[0];
            /* Exception if too small error */
            if (det == 0)
                new ConvexSetProjectorException(tseMessage);
            double tp = ((p2[1]-p1[1])*u1[0] - (p2[0]-p1[0])*u1[1])/det;
            return new double[] {u2[0]*tp + p2[0], u2[1]*tp + p2[1]};
        }
        
        /* Give a "turning right" orthogonal vector (2d vector) */
        private double[] turningRightOrthogonal(double[] v)
        {
            return new double[] {v[1], -v[0]};
        }
        
        /* Compute the error of the approximation */
        private double computeError()
        {
            double normN = vc.norm(n);
            n[0] /= normN;
            n[1] /= normN;
            double[] v = vc.difference(outerV, innerV1);            
            return vc.dotProduct(n, v);
        }
        
        private final double[] innerV1;
        private final double[] innerV2;
        private final double[] d1;
        private final double[] d2;
        private final double[] outerV;
        private final double[] n;
        private final double error;
    }
    
    /* A class to compute the succesive approximations into a thread */
    private class SuccessiveApproximationThread extends Thread
    {
        /* Create a new thread, with a list of LocalApproximation to refine, the projection support function (will be cloned), and an absolute error. */
        public SuccessiveApproximationThread(List <LocalApproximation> l, SupportFunction psf, double absErr)
        {
            this.l = l;
            this.psf = psf.clone();
            this.absErr = absErr;
        }
        
        /* Compute the successive approximations. */
        public void run()
        {
            for(LocalApproximation la : l) resultList.addAll(stepSuccessiveApproximations(la));
        }
        
        /* Give the result list */
        public List <LocalApproximation> getResult()
        {
            return resultList;
        }
        
        /* A step of successive approximations */
        private List <LocalApproximation> stepSuccessiveApproximations(LocalApproximation la)
        {
            List <LocalApproximation> res = new LinkedList <LocalApproximation> ();
            if (la.getError() > absErr)
            {
                List <LocalApproximation> laRefined = la.refine(psf);
                for (LocalApproximation lap : laRefined) res.addAll(stepSuccessiveApproximations(lap));
            }
            else
                res.add(la);
            return res;
        }
        
        private List <LocalApproximation> resultList = new LinkedList <LocalApproximation> ();
        private final List <LocalApproximation> l;
        private final SupportFunction psf;
        private final double absErr;
    }
    
    private ThreadsNumber tn = ThreadsNumber.TN_1;
    private VectorCalculator vc;
    private Polygon innerPolygon;
    private Polygon outerPolygon;
    private boolean evaluated = false;
    private boolean initialized = false;
    private static final double[] dNorth = new double[] {0, 1};
    private static final double[] dWest = new double[] {-1, 0};
    private static final double[] dSouth = new double[] {0, -1};
    private static final double[] dEast = new double[] {1, 0};
    private static final String nsvsMessage = "The convex set and the plane don't live in the same vectorspace.";
    private static final String absMessage = "The absolute error cannot be negative or zero.";
    private static final String relMessage = "The relative error cannot be negative or zero or greater than one.";
    private static final String tseMessage = "Calculus impossible, because of floating precision : the given error is too small.";
    private static final String cseMessage = "Calculus impossible, maybe because the convex set is empty.";
    private static final String nyeMessage = "No computation has been made yet.";
}
