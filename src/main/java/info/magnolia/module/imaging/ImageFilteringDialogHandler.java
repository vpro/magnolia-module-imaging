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
package info.magnolia.module.imaging;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.gui.dialog.DialogControlImpl;
import info.magnolia.module.admininterface.SaveHandler;
import info.magnolia.module.admininterface.dialogs.ParagraphEditDialog;
import info.magnolia.module.cropui.CropAndResizeControl;
import info.magnolia.imaging.cropresize.CropAndResizeFilter;
import info.magnolia.content2bean.Content2BeanException;
import info.magnolia.imaging.operations.ImageFilter;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * -- this class is a remnant of the previous incarnation of m-m-imagefiltering - only kept around for reference until m-m-cropui is back into a useable state.
 *
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
//            final ImageFilter imageFilter = new TextOverlayImageFilter();
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
        } catch (Content2BeanException e) {
             // TODO
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
