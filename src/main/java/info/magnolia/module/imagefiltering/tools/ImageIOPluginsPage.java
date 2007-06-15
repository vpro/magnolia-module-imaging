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
package info.magnolia.module.imagefiltering.tools;

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
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImageIOPluginsPage extends TemplatedMVCHandler {

    public ImageIOPluginsPage(String name, HttpServletRequest request, HttpServletResponse response) {
        super(name, request, response);
    }

    public Collection getInputFormatNames() {
        return filter(ImageIO.getReaderFormatNames());
    }

    public Collection getInputFormatMimeTypes() {
        return filter(ImageIO.getReaderMIMETypes());
    }

    public Collection getOutputFormatNames() {
        return filter(ImageIO.getWriterFormatNames());
    }

    public Collection getOutputFormatMimeTypes() {
        return filter(ImageIO.getWriterMIMETypes());
    }

    // this is just static to make testing easier... hu.
    protected static Collection filter(String[] arr) {
        final TreeSet set = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        set.addAll(Arrays.asList(arr));
        CollectionUtils.transform(set, new Transformer() {
            public Object transform(Object input) {
                return ((String) input).toLowerCase();
            }
        });
        return set;
    }
}
