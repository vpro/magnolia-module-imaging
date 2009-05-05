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
package info.magnolia.imaging.filters.load;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.NodeData;
import info.magnolia.imaging.filters.ImageFilter;
import info.magnolia.imaging.filters.NodeFilterParameterStrategy;

import javax.imageio.ImageIO;
import javax.jcr.RepositoryException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;


/**
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class FromNode implements ImageFilter<NodeFilterParameterStrategy> {

    public BufferedImage apply(BufferedImage source, NodeFilterParameterStrategy filterParams) {
        try {
            // TODO - ensure this is an appropriate node/property
            final Content node = filterParams.getParameter();
            final NodeData data = node.getNodeData("binary");
            final InputStream in = data.getValue().getStream();
            return ImageIO.read(in);
        } catch (IOException e) {
            throw new RuntimeException(e); // TODO
        } catch (RepositoryException e) {
            throw new RuntimeException(e); // TODO
        }


    }
}
