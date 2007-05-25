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
package info.magnolia.module.imageresizer.control;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.gui.control.Button;
import info.magnolia.cms.gui.control.Hidden;
import info.magnolia.cms.gui.dialog.DialogFile;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class ImageResizeControl extends DialogFile {
    private String controlUUID;

    public void init(HttpServletRequest request, HttpServletResponse response, Content websiteNode, Content configNode) throws RepositoryException {
        super.init(request, response, websiteNode, configNode);
        controlUUID = configNode.getUUID();
    }

    public void drawHtmlPost(Writer out) throws IOException {
        // TODO ? use 2 separate controls, one for the image and one JUST for the cropping ?
        // could allow for instance multiple sizes of one image in the same paragraph,
        // and decouples the cropper from the file upload mechanism

        // TODO : for now we can't handle a paragraph where the image hasn't been uploaded yet.
        if (getWebsiteNode() != null) {
            final String imagePath = getFileURI(getFileControl());

            final Hidden cropperInfo = new Hidden("cropperInfo", getWebsiteNode());
            cropperInfo.setId("cropperInfo");
            cropperInfo.setSaveInfo(true);

            final Button button = new Button();
            button.setLabel(getMessage("cropper.edit.button"));
            button.setOnclick("new mgnl.imageresizer.ImageResizer.openCropper('" + cropperInfo.getId() + "', '" + controlUUID + "', '" + imagePath + "');");

            out.write(button.getHtml());
            out.write(cropperInfo.getHtml());
        }

        super.drawHtmlPost(out);
    }
}
