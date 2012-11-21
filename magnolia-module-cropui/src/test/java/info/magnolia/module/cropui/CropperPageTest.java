/**
 * This file Copyright (c) 2007-2012 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
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
 * is available at http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.module.cropui;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import info.magnolia.content2bean.Content2BeanException;
import info.magnolia.content2bean.Content2BeanProcessor;
import info.magnolia.content2bean.Content2BeanTransformer;
import info.magnolia.content2bean.TransformationState;
import info.magnolia.content2bean.TypeMapping;
import info.magnolia.content2bean.impl.Content2BeanProcessorImpl;
import info.magnolia.content2bean.impl.Content2BeanTransformerImpl;
import info.magnolia.content2bean.impl.TransformationStateImpl;
import info.magnolia.content2bean.impl.TypeMappingImpl;
import info.magnolia.objectfactory.Components;
import info.magnolia.test.ComponentsTestUtil;
import info.magnolia.test.mock.MockContent;

/**
 * @version $Id$
 */
public class CropperPageTest {

    @Before
    public void setUp() throws Exception {
        ComponentsTestUtil.setImplementation(Content2BeanTransformer.class, Content2BeanTransformerImpl.class);
        ComponentsTestUtil.setImplementation(Content2BeanProcessor.class, Content2BeanProcessorImpl.class);
        ComponentsTestUtil.setImplementation(TransformationState.class, TransformationStateImpl.class);
        ComponentsTestUtil.setImplementation(TypeMapping.class, TypeMappingImpl.class);
    }

    @After
    public void tearDown() throws Exception {
        Components.setComponentProvider(null);
    }

    @Test
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
            node.addNodeData("name", namePropOrNull);
        }
        if (labelPropOrNull != null) {
            node.addNodeData("label", labelPropOrNull);
        }
        final CropAndResizeConfig result = CropperPage.fromNode(node);
        assertEquals(expectedLabel, result.getLabel());
        assertEquals("theNodeName", result.getName());
    }
}
