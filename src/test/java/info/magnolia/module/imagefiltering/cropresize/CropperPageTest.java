/**
 *
 * Magnolia and its source-code is licensed under the LGPL.
 * You may copy, adapt, and redistribute this file for commercial or non-commercial use.
 * When copying, adapting, or redistributing this document in keeping with the guidelines above,
 * you are required to provide proper attribution to obinary.
 * If you reproduce or distribute the document without making any substantive modifications to its content,
 * please use the following attribution line:
 *
 * Copyright 1993-2006 obinary Ltd. (http://www.obinary.com) All rights reserved.
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
