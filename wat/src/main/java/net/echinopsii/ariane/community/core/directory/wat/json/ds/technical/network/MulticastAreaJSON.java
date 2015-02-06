/**
 * Directory WAT
 * RoutingArea to JSON tools
 * Copyright (C) 06/05/14 echinopsii
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.echinopsii.ariane.community.core.directory.wat.json.ds.technical.network;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import net.echinopsii.ariane.community.core.directory.base.model.technical.network.Datacenter;
import net.echinopsii.ariane.community.core.directory.base.model.technical.network.RoutingArea;
import net.echinopsii.ariane.community.core.directory.base.model.technical.network.Subnet;
import net.echinopsii.ariane.community.core.directory.wat.DirectoryBootstrap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Our own object to JSON tools as :
 *  - we can have cycle in object graphs
 *  - we still want to have references to linked objects through IDs (and so we don't want @XmlTransient or @JsonIgnore)
 */
public class MulticastAreaJSON {

    public final static String MAREA_ID          = "multicastAreaID";
    public final static String MAREA_VERSION     = "multicastAreaVersion";
    public final static String MAREA_NAME        = "multicastAreaName";
    public final static String MAREA_DESCRIPTION = "multicastAreaDescription";
    public final static String MAREA_SUBNETS_ID  = "multicastAreaSubnetsID";
    public final static String MAREA_DC_ID       = "multicastAreaDatacentersID";

    public final static void multicastArea2JSON(RoutingArea routingArea, JsonGenerator jgenerator) throws IOException {
        jgenerator.writeStartObject();
        jgenerator.writeNumberField(MAREA_ID, routingArea.getId());
        jgenerator.writeNumberField(MAREA_VERSION, routingArea.getVersion());
        jgenerator.writeStringField(MAREA_NAME, routingArea.getName());
        jgenerator.writeStringField(MAREA_DESCRIPTION, routingArea.getDescription());
        jgenerator.writeArrayFieldStart(MAREA_SUBNETS_ID);
        for (Subnet subnet : routingArea.getSubnets())
            jgenerator.writeNumber(subnet.getId());
        jgenerator.writeEndArray();
        jgenerator.writeArrayFieldStart(MAREA_DC_ID);
        for (Datacenter dc : routingArea.getDatacenters())
            jgenerator.writeNumber(dc.getId());
        jgenerator.writeEndArray();
        jgenerator.writeEndObject();
    }

    public final static void oneMulticastArea2JSON(RoutingArea routingArea, ByteArrayOutputStream outStream) throws IOException {
        JsonGenerator jgenerator = DirectoryBootstrap.getjFactory().createGenerator(outStream, JsonEncoding.UTF8);
        MulticastAreaJSON.multicastArea2JSON(routingArea, jgenerator);
        jgenerator.close();
    }

    public final static void manyMulticastAreas2JSON(HashSet<RoutingArea> routingAreas, ByteArrayOutputStream outStream) throws IOException {
        JsonGenerator jgenerator = DirectoryBootstrap.getjFactory().createGenerator(outStream, JsonEncoding.UTF8);
        jgenerator.writeStartObject();
        jgenerator.writeArrayFieldStart("routingAreas");
        Iterator<RoutingArea> iter = routingAreas.iterator();
        while (iter.hasNext()) {
            RoutingArea current = iter.next();
            MulticastAreaJSON.multicastArea2JSON(current, jgenerator);
        }
        jgenerator.writeEndArray();
        jgenerator.writeEndObject();
        jgenerator.close();
    }
}