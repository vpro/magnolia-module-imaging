/**
 * This file Copyright (c) 2003-2007 Magnolia International
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
package info.magnolia.module.imagefiltering;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.gui.dialog.DialogControlImpl;
import info.magnolia.module.admininterface.SaveHandler;
import info.magnolia.module.admininterface.dialogs.ParagraphEditDialog;
import info.magnolia.module.imagefiltering.cropresize.CropAndResizeControl;
import info.magnolia.module.imagefiltering.cropresize.CropAndResizeFilter;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 * @deprecated for 3.5, we should use a SaveHandler, this is only to be compatible with 3.0
 */
public class ImageFilteringDialogHandler extends ParagraphEditDialog {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ImageFilteringDialogHandler.class);

    public ImageFilteringDialogHandler(String name, HttpServletRequest request, HttpServletResponse response, Content configNode) {
        super(name, request, response, configNode);
    }

    protected boolean onPostSave(SaveHandler control) {
        final boolean result = super.onPostSave(control);

        if (!result) {
            return false;
        }

        try {
            final ImageFilter imageFilter = new CropAndResizeFilter();
            final ImagesProcessor processor = new ImagesProcessor(imageFilter);

            final Content storageNode = getStorageNode();

            final List croppers = new LinkedList();
            final List dialogSubs = getDialog().getSubs();
            findCroppersConfigNodes(croppers, dialogSubs);

            // TODO : test this ? (multiple croppers)
            final Iterator it = croppers.iterator();
            while (it.hasNext()) {
                final Content configNode = (Content) it.next();
                processor.processImages(storageNode, configNode);
            }

            storageNode.save();
            return true;
        } catch (IOException e) {
            log.error("Couldn't resize image: " + e.getMessage(), e);
            return false;
        } catch (RepositoryException e) {
            log.error("Couldn't resize image: " + e.getMessage(), e);
            return false;
        }
    }

    // TODO : write tests for this ?
    private void findCroppersConfigNodes(List foundCroppers, List dialogSubs) {
        final Iterator it = dialogSubs.iterator();
        while (it.hasNext()) {
            final DialogControlImpl c = (DialogControlImpl) it.next();
            if (c instanceof CropAndResizeControl) {
                final CropAndResizeControl irc = (CropAndResizeControl) c;
                foundCroppers.add(irc.getConfigNode());
            } else {
                findCroppersConfigNodes(foundCroppers, c.getSubs());
            }
        }
    }

}
