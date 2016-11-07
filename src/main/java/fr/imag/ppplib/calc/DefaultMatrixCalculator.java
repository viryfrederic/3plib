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

package fr.imag.ppplib.calc;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.exception.DimensionMismatchException;

/** A simple implementation using Apache Commons Math for basic operations on matrices.
 **/

public class DefaultMatrixCalculator implements MatrixCalculator
{
    @Override
    public double[] multiply(double[][] a, double[] x)
    {
        try
        {
            return new Array2DRowRealMatrix(a).operate(x);
        }
        catch (DimensionMismatchException e)
        {
            throw new MatrixCalculatorException("The dimensions of a and x are incompatible.");
        }
    }
    
    @Override
    public double[][] transpose(double[][] a)
    {
        return new Array2DRowRealMatrix(a).transpose().getData();
    }
    
    @Override
    public MatrixCalculator newInstance()
    {
        return new DefaultMatrixCalculator();
    }
}
