/**
 * This file Copyright (c) 2007-2011 Magnolia International
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
package info.magnolia.module.imaging.tools;

import info.magnolia.module.admininterface.TemplatedMVCHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

/**
 * A page displaying the list of available input and output formats for javax.imageio.ImageIO.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImageIOPluginsPage extends TemplatedMVCHandler {

    public ImageIOPluginsPage(String name, HttpServletRequest request, HttpServletResponse response) {
        super(name, request, response);
    }

    public Collection<String> getInputFormatNames() {
        return filter(ImageIO.getReaderFormatNames());
    }

    public Collection<String> getInputFormatMimeTypes() {
        return filter(ImageIO.getReaderMIMETypes());
    }

    public Collection<String> getOutputFormatNames() {
        return filter(ImageIO.getWriterFormatNames());
    }

    public Collection<String> getOutputFormatMimeTypes() {
        return filter(ImageIO.getWriterMIMETypes());
    }

    /**
     * Removes duplicates and returns a sorted set of all entries in lowercase.
     */
    protected static Collection<String> filter(String... formats) {
        final TreeSet<String> set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        set.addAll(Arrays.asList(formats));
        CollectionUtils.transform(set, new Transformer() {
            public Object transform(Object input) {
                return ((String) input).toLowerCase();
            }
        });
        return set;
    }
}
