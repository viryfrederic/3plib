/** Polygonal Planar Projection LIBrary (3plib) v0.2.0
 ** Copyright © 2016 Frédéric Viry
 ** author: Frédéric Viry (Laboratoire Verimag, Grenoble, France)
 ** mail: ask3plib@gmail.com
 ** some source code is inspired by www.mkyong.com/java/how-to-create-an-xml-file-in-java-dom/
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

package fr.imag.ppplib.io.files;

import fr.imag.ppplib.calc.sf.*;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

public class PolyhedraReader
{
    /** Read from file.
     ** @param fileName the file name.
     ** @return the list of polyhedra.
     **/
    public List<SupportFunction> read(String fn)
    {
        List <SupportFunction> res = new ArrayList<SupportFunction>();
        try
        {
            File f = new File(fn);
            Scanner sc = new Scanner(f);
            sc.useLocale(Locale.ENGLISH);
            int dim = sc.nextInt ();
            int n = sc.nextInt ();
            /* For each polyhedron */
            for (int i = 0 ; i < n ; i++)
            {
                int m = sc.nextInt();
                ConvexPolyhedronSupportFunction p = new ConvexPolyhedronSupportFunction();
                /* For each halfspace */
                for (int j = 0 ; j < m ; j++)
                {
                    double[] a = new double[dim];
                    double b;
                    for (int k = 0 ; k < dim ; k++) a[k] = sc.nextDouble();
                    b = sc.nextDouble();
                    p.addLinearConstraint(a, b);
                }
                res.add(p);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return res;
    }
}
