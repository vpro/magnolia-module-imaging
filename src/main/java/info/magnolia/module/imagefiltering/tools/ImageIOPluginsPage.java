/**
 * This file Copyright (c) 2003-2009 Magnolia International
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
