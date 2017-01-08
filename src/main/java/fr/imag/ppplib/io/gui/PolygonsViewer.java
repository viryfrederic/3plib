/** Polygonal Planar Projection LIBrary (3plib) v0.2.0
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

package fr.imag.ppplib.io.gui;

import fr.imag.ppplib.proj.*;
import fr.imag.ppplib.proj.plg.Polygon;
import fr.imag.ppplib.calc.ImplementationFactory;
import fr.imag.ppplib.calc.sf.SupportFunction;
import fr.imag.ppplib.io.files.PolyhedraReader;
import fr.imag.ppplib.calc.ProjectionCalculator;

import java.util.List;
import java.util.ArrayList;

/** Main class of the viewer.
 **/

public class PolygonsViewer
{
    /* View */
    private View view;

    /* Data */
    private double[] v1;
    private double[] v2;
    private double err;
    private List<SupportFunction> data;

    /* Output */
    private List<Polygon> outerPoly;
    private List<Polygon> innerPoly;

    /** Construct a new view 
     **/
    public PolygonsViewer()
    {
        view = new View(this);
    }

    /** Open a new polyhedra file, and compute the projection on the plane given by (1, 0, ..., 0) (0, 1, 0, ..., 0), with an error of 20%.
     ** @param fileName the file name.
     ** @exception NoDataException
     **/
    public void open(String fileName)
    {
        data = (new PolyhedraReader()).read(fileName);
        if (data.size() == 0)
            throw new ViewerException("ERROR : " + fileName + " is empty !");
        int dim = data.get(0).getDimension();
        v1 = new double[dim];
        v2 = new double[dim];
        for (int i = 0 ; i < dim ; i++)
        {
            v1[i] = 0.0;
            v2[i] = 0.0;
        }
        v1[0] = 1.0;
        v2[1] = 1.0;
        err = 0.2;
        computeNewProjection();
    }

    /** Change the settings of the projection.
     ** @param v1 the first vector of the plane.
     ** @param v2 the second vector of the plane.
     ** @param err the maximal error in ]0, 1].
     **/
    public void changeSettings(double[] v1, double[] v2, double err)
    {
        this.v1 = v1;
        this.v2 = v2;
        if (err <= 0.0 || err > 1.0)
            throw new ViewerException("ERROR : The given error " + err + " is not in ]0, 1]");
        this.err = err;
        computeNewProjection();
    }

    /* Compute a new projection. Called when it is necessary. */
    private void computeNewProjection()
    {
        ProjectionCalculator pc = ImplementationFactory.getNewProjectionCalculator(v1, v2);
        PolygonalProjector pp = new UnionPolygonalProjector(true);
        pp.computeProjection(data, pc, ErrorType.RELATIVE, err);
        outerPoly = pp.getOuterPolygons();
        innerPoly = pp.getInnerPolygons();
        view.update(innerPoly, outerPoly);
    }
}
