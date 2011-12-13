/**
 * This file Copyright (c) 2007-2010 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This program and the accompanying materials are made
 * available under the terms of the Magnolia Network Agreement
 * which accompanies this distribution, and is available at
 * http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.module.cropui;

import info.magnolia.test.mock.MockContent;
import info.magnolia.test.mock.MockNodeData;
import info.magnolia.content2bean.Content2BeanException;
import info.magnolia.content2bean.Content2BeanTransformer;
import info.magnolia.content2bean.Content2BeanProcessor;
import info.magnolia.content2bean.TransformationState;
import info.magnolia.content2bean.TypeMapping;
import info.magnolia.content2bean.impl.Content2BeanTransformerImpl;
import info.magnolia.content2bean.impl.Content2BeanProcessorImpl;
import info.magnolia.content2bean.impl.TransformationStateImpl;
import info.magnolia.content2bean.impl.TypeMappingImpl;
import info.magnolia.cms.core.SystemProperty;
import junit.framework.TestCase;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CropperPageTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        SystemProperty.setProperty(Content2BeanTransformer.class.getName(), Content2BeanTransformerImpl.class.getName());
        SystemProperty.setProperty(Content2BeanProcessor.class.getName(), Content2BeanProcessorImpl.class.getName());
        SystemProperty.setProperty(TransformationState.class.getName(), TransformationStateImpl.class.getName());
        SystemProperty.setProperty(TypeMapping.class.getName(), TypeMappingImpl.class.getName());
    }

    /**
     * testFromNodeAlwaysSetsConfigNameToNodeNameAndUsesLabelOrNameOrNodeNameToSetConfigLabel().
     */
    public void testFromNodeHasNameAndLabelProperlySet() throws Content2BeanException {
        doTestFromNode("myName", "myLabel", "myLabel");
        doTestFromNode(null, "myLabel", "myLabel");
        doTestFromNode("myName", null, "myName");
        doTestFromNode(null, null, "theNodeName");
        doTestFromNode("myName", "myLabel", "myLabel");
        doTestFromNode("", "myLabel", "myLabel");
        doTestFromNode("myName", "", "myName");
        doTestFromNode("", "", "theNodeName");
    }

    private void doTestFromNode(String namePropOrNull, String labelPropOrNull, String expectedLabel) throws Content2BeanException {
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
