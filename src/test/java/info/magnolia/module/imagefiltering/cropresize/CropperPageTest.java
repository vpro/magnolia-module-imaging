/**
 * This file Copyright (c) 2003-2007 Magnolia International
 * Ltd.  (http://www.magnolia.info). All rights reserved.
 *
 *
 * This file is dual-licensed under both the Magnolia
 * Network Agreement and the GNU General Public License.
 * You may elect to use one or the other of these licenses.
 *
 * This file is distributed in the hope that it will be
 * useful, but AS-IS and WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, or NONINFRINGEMENT.
 * Redistribution, except as permitted by whichever of the GPL
 * or MNA you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or
 * modify this file under the terms of the GNU General
 * Public License, Version 3, as published by the Free Software
 * Foundation.  You should have received a copy of the GNU
 * General Public License, Version 3 along with this program;
 * if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * 2. For the Magnolia Network Agreement (MNA), this file
 * and the accompanying materials are made available under the
 * terms of the MNA which accompanies this distribution, and
 * is available at http://www.magnolia.info/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.module.imagefiltering.cropresize;

import info.magnolia.test.MgnlTestCase;
import info.magnolia.test.mock.MockContent;
import info.magnolia.test.mock.MockNodeData;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CropperPageTest extends MgnlTestCase {
    /**
     * testFromNodeAlwaysSetsConfigNameToNodeNameAndUsesLabelOrNameOrNodeNameToSetConfigLabel()
     */
    public void testFromNodeHasNameAndLabelProperlySet() {
        doTestFromNode("myName", "myLabel", "myLabel");
        doTestFromNode(null, "myLabel", "myLabel");
        doTestFromNode("myName", null, "myName");
        doTestFromNode(null, null, "theNodeName");
        doTestFromNode("myName", "myLabel", "myLabel");
        doTestFromNode("", "myLabel", "myLabel");
        doTestFromNode("myName", "", "myName");
        doTestFromNode("", "", "theNodeName");
    }

    private void doTestFromNode(String namePropOrNull, String labelPropOrNull, String expectedLabel) {
        final MockContent node = new MockContent("theNodeName");
        if (namePropOrNull != null) {
            node.addNodeData(new MockNodeData("name", namePropOrNull));
        }
        if (labelPropOrNull != null) {
            node.addNodeData(new MockNodeData("label", labelPropOrNull));
        }
        final CropAndResizeConfig result = CropperPage.fromNode(node);
        assertEquals(expectedLabel, result.getLabel());
        assertEquals("theNodeName", result.getName());
    }

}
