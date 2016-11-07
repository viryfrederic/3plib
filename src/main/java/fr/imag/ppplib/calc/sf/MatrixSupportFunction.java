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

import fr.imag.ppplib.calc.ImplementationFactory;
import fr.imag.ppplib.calc.MatrixCalculator;

/** An implementation of the SupportFunction for the linear transformation of a given set described by its SupportFunction. Given a convex set (by its SupportFunction) and a matrice, it makes it possible to evaluate the support function of the convex set transformed by the matrix.
 **/

public class MatrixSupportFunction implements SupportFunction
{
    /** Create a new MatrixSupportFunction, associated to a convex set and a linear transformation. The convex set is cloned.
     ** @param convex a convex set given by its SupportFunction.
     ** @param matrix a matrix.
     ** @exception SupportFunctionException thrown if the convex set and the matrix are incompatible.
     **/
    public MatrixSupportFunction(SupportFunction convex, double[][] matrix)
    {
        if (convex.getDimension() != matrix[0].length)
            throw new SupportFunctionException(nsvsMessage);
        this.convex = convex.clone();
        this.matrix = matrix;
        mc = ImplementationFactory.getNewMatrixCalculator();
        matrixT = mc.transpose(matrix);
    }
    
    @Override
    public int getDimension()
    {
        return matrix.length; // number of rows of the matrix
    }
    
    @Override
    public void evaluate(double[] dir)
    {
        double[] dTransformed = mc.multiply(matrixT, dir);
        convex.evaluate(dTransformed);
        supportVector = mc.multiply(matrix, convex.getSupportVector());
        supportValue = convex.getSupportValue();
        evaluated = true;
    }
    
    @Override
    public double[] getSupportVector()
    {
        if (!evaluated)
            throw new SupportFunctionException(nyeMessage);
        return supportVector;
    }
    
    @Override
    public double getSupportValue()
    {
        if (!evaluated)
            throw new SupportFunctionException(nyeMessage);
        return supportValue;
    }
    
    @Override
    public SupportFunction clone()
    {
        MatrixSupportFunction res = new MatrixSupportFunction();
        res.convex = convex.clone();
        res.mc = ImplementationFactory.getNewMatrixCalculator();
        res.matrix = matrix; // TODO
        res.matrixT = matrixT; // TODO
        res.supportVector = supportVector;
        res.supportValue = supportValue;
        res.evaluated = evaluated;
        return res;
    }
    
    private MatrixSupportFunction() {}
    
    private MatrixCalculator mc;
    private SupportFunction convex;
    private double[][] matrix, matrixT;
    private double[] supportVector;
    private double supportValue;
    private boolean evaluated = false;
    private static final String nsvsMessage = "The convex set and the matrix are incompatible.";
    private static final String nyeMessage = "The support function has never been solved evaluated.";
}
