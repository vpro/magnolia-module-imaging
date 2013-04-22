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
package info.magnolia.module.imaging.tools;

import info.magnolia.module.admininterface.TemplatedMVCHandler;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * A page displaying the list of available input and output formats for javax.imageio.ImageIO.
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
            @Override
            public Object transform(Object input) {
                return ((String) input).toLowerCase();
            }
        });
        return set;
    }
}
