/**
 * This file Copyright (c) 2009-2011 Magnolia International
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

import info.magnolia.cms.core.NodeData;
import info.magnolia.imaging.ImagingException;
import info.magnolia.imaging.ParameterProvider;

import javax.jcr.PropertyType;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public abstract class AbstractFromContent<PT> extends AbstractLoader<ParameterProvider<PT>> {

    @Override
    protected BufferedImage loadSource(ParameterProvider<PT> param) throws ImagingException {
        final NodeData data = getNodeData(param);
        if (!data.isExist() || data.getType() != PropertyType.BINARY) {
            throw new ImagingException("Nodedata " + data.getHandle() + " doesn't exist or is not of type binary.");
        }
        final InputStream in = data.getStream();
        if (in == null) {
            throw new ImagingException("Can't get InputStream from " + data.getHandle());
        }
        try {
            return doReadAndClose(in);
        } catch (IOException e) {
            throw new ImagingException("Can't load image from " + data.getHandle());
        }
    }

    /**
     * Gets the appropriate NodeData instance based on the given ParameterProvider.
     * If possible, the implementation should throw exceptions early, for instance if
     * the NodeData can't be loaded.
     */
    protected abstract NodeData getNodeData(ParameterProvider<PT> param) throws ImagingException;

}
