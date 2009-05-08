/**
 * This file Copyright (c) 2009 Magnolia International
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
package info.magnolia.imaging.operations.load;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.NodeData;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.operations.ImageOperation;
import info.magnolia.imaging.parameters.NodeParameterProvider;

import javax.imageio.ImageIO;
import javax.jcr.PropertyType;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class FromNode implements ImageOperation<NodeParameterProvider> {
    private String propertyName = "binary";

    public BufferedImage apply(BufferedImage source, NodeParameterProvider filterParams) throws ImagingException {
        final Content node = filterParams.getParameter();
        final NodeData data = node.getNodeData(propertyName);
        if (!data.isExist() || data.getType() != PropertyType.BINARY) {
            throw new ImagingException("Property " + propertyName + " for " + node + " doesn't exist or is not of type binary.");
        }
        final InputStream in = data.getStream();
        if (in == null) {
            throw new ImagingException("Can't get InputStream from " + data.getHandle());
        }
        try {
            // TODO - ensure this is an appropriate node/property
            return ImageIO.read(in);
        } catch (IOException e) {
            throw new ImagingException("Can't load image from " + data.getHandle());
        }
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

}
