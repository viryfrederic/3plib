/** Polygonal Planar Projection LIBrary (3plib) v0.1.0
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

package fr.imag.ppplib;

import java.io.File;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.util.List;

/** A class for XML outputs of the list of polygons.
 **/

public class XMLOutput
{
    /** Create a XML file containing each polygon of the list.
     ** @param fn the output file name.
     ** @param pl the list of polygons.
     **/
    public void createXML(String fn, List <Polygon> pl)
    {
        /* source code inspired by www.mkyong.com/java/how-to-create-an-xml-file-in-java-dom/ */
        
        try
        {
            /* XML builder generation */
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            
            /* Add each polygon */
            Element rootElement = doc.createElement("polygonset");
            doc.appendChild(rootElement);
            for (Polygon p : pl)
            {
                Element polygon = doc.createElement("polygon");
                rootElement.appendChild(polygon);
                for (double[] v : p.getVertices())
                {
                    Element vertex = doc.createElement("vertex");
                    polygon.appendChild(vertex);
                    vertex.appendChild(doc.createTextNode(v[0] + " " + v[1]));
                }
            }
            
            /* Write file */
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fn));
            transformer.transform(source, result);
        }
        catch(ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch(TransformerException e)
        {
            e.printStackTrace();
        }
    }
}
