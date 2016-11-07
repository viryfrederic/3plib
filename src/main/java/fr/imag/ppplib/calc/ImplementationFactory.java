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

import fr.imag.ppplib.calc.opti.LinearProgrammingSolver;
import fr.imag.ppplib.calc.opti.ACMLinearProgrammingSolver;
import fr.imag.ppplib.proj.MultithreadsConvexSetProjector;
import fr.imag.ppplib.proj.ConvexSetProjector;

/** Class which makes it possible to choose some implementations for linear programming and matrices/vectors computations (singleton). The default configuration uses "Apache Commons Math 3.6.1" based implementations and hardcoded implementations. It is also possible to create user implementations and to use it by reset in the factory.
 **/

public class ImplementationFactory
{
    /** Provide a new instance of the choosen LinearProgrammingSolver implementation.
     ** @return the instance.
     **/
    public static LinearProgrammingSolver getNewLinearProgrammingSolver()
    {
        if (instance.lpsInstance == null)
            instance.lpsInstance = new ACMLinearProgrammingSolver();
        return instance.lpsInstance.newInstance();
    }
    
    /** Reset the implementation of the LinearProgrammingSolver for future operations. It doesn't affect the previous LinearProgrammingSolver instances.
     ** @param impl the new implementation.
     **/
    public static void resetLinearProgrammingSolver(LinearProgrammingSolver impl)
    {
        instance.lpsInstance = impl;
    }
    
    /** Provide a new instance of the choosen VectorCalculator implementation.
     ** @return the instance.
     **/
    public static VectorCalculator getNewVectorCalculator()
    {
        if (instance.vcInstance == null)
            instance.vcInstance = new DefaultVectorCalculator();
        return instance.vcInstance.newInstance();
    }
    
    /** Reset the implementation of the VectorCalculator for future operations. It doesn't affect the previous VectorCalculator instances.
     ** @param impl the new implementation.
     **/
    public static void resetVectorCalculator(VectorCalculator impl)
    {
        instance.vcInstance = impl;
    }
    
    /** Provide a new instance of the choosen ProjectionCalculator implementation.
     ** @return the instance.
     **/
    public static ProjectionCalculator getNewProjectionCalculator(double[] p1, double[] p2)
    {
        if (instance.pcInstance == null)
            instance.pcInstance = new DefaultProjectionCalculator(null, null);
        return instance.pcInstance.newInstance(p1, p2);
    }
    
    /** Reset the implementation of the ProjectionCalculator for future operations. It doesn't affect the previous ProjectionCalculator instances.
     ** @param impl the new implementation.
     **/
    public static void resetProjectionCalculator(ProjectionCalculator impl)
    {
        instance.pcInstance = impl;
    }
    
    /** Provide a new instance of the choosen ConvexSetProjector implementation.
     ** @return the instance.
     **/
    public static ConvexSetProjector getNewConvexSetProjector()
    {
        if (instance.cspInstance == null)
            instance.cspInstance = new MultithreadsConvexSetProjector();
        return instance.cspInstance.newInstance();
    }
    
    /** Reset the implementation of the ConvexSetProjector for future operations. It doesn't affect the previous ConvexSetProjector instances.
     ** @param impl the new implementation.
     **/
    public static void resetConvexSetProjector(ConvexSetProjector impl)
    {
        instance.cspInstance = impl;
    }
    
    /** Provide a new instance of the choosen MatrixCalculator implementation.
     ** @return the instance.
     **/
    public static MatrixCalculator getNewMatrixCalculator()
    {
        if (instance.mcInstance == null)
            instance.mcInstance = new DefaultMatrixCalculator();
        return instance.mcInstance.newInstance();
    }
    
    /** Reset the implementation of the MatrixCalculator for future operations. It doesn't affect the previous MatrixCalculator instances.
     ** @param impl the new implementation.
     **/
    public static void resetMatrixCalculator(MatrixCalculator impl)
    {
        instance.mcInstance = impl;
    }
    
    /* Constructor with default implementations (Apache Commons Math 3.6.1) */
    private ImplementationFactory() {}
    
    private static ImplementationFactory instance = new ImplementationFactory();
    private VectorCalculator vcInstance;
    private LinearProgrammingSolver lpsInstance;
    private ProjectionCalculator pcInstance;
    private ConvexSetProjector cspInstance;
    private MatrixCalculator mcInstance;
}
