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
package info.magnolia.module.imageresizer;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.gui.dialog.DialogControlImpl;
import info.magnolia.module.admininterface.SaveHandler;
import info.magnolia.module.admininterface.dialogs.ParagraphEditDialog;
import info.magnolia.module.imageresizer.control.ImageResizeControl;

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
 * @deprecated for 3.1, we should use a SaveHandler, this is only to be compatible with 3.0
 */
public class CroppedImageDialogHandler extends ParagraphEditDialog {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CroppedImageDialogHandler.class);

    public CroppedImageDialogHandler(String name, HttpServletRequest request, HttpServletResponse response, Content configNode) {
        super(name, request, response, configNode);
    }

    protected boolean onPostSave(SaveHandler control) {
        final boolean result = super.onPostSave(control);

        if (!result) {
            return false;
        }

        try {
            final ImageFilter imageFilter = new ImageResizerImpl();
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
            DialogControlImpl c = (DialogControlImpl) it.next();
            if (c instanceof ImageResizeControl) {
                final ImageResizeControl irc = (ImageResizeControl) c;
                foundCroppers.add(irc.getConfigNode());
            } else {
                findCroppersConfigNodes(foundCroppers, c.getSubs());
            }
        }
    }

}
