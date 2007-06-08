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
package info.magnolia.module.imagefiltering.cropresize;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.gui.control.Button;
import info.magnolia.cms.gui.control.File;
import info.magnolia.cms.gui.control.Hidden;
import info.magnolia.cms.gui.dialog.DialogBox;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CropAndResizeControl extends DialogBox {
    private Content configNode;

    public void init(HttpServletRequest request, HttpServletResponse response, Content websiteNode, Content configNode) throws RepositoryException {
        super.init(request, response, websiteNode, configNode);
        this.configNode = configNode;
    }

    public Content getConfigNode() {
        return configNode;
    }

    public void drawHtml(Writer out) throws IOException {
        drawHtmlPre(out);

        // TODO : for now we can't handle a paragraph where the image hasn't been uploaded yet. (MGNLIMG-7)
        if (getWebsiteNode() != null) {
            final String fileControlName = getConfigValue("fileControlName", null);
            if (fileControlName == null) {
                throw new IllegalStateException("Need a fileControlName config parameter to know which file control to use.");
            }
            final File fileControl = new File(fileControlName, getWebsiteNode());
            final String imagePath = fileControl.getHandle();

            final String cropperInfoControlName = getCropperInfoPropertyName(fileControlName);
            final Hidden cropperInfo = new Hidden(cropperInfoControlName, getWebsiteNode());
            cropperInfo.setId(cropperInfoControlName);
            cropperInfo.setSaveInfo(true);

            final String controlUUID = configNode.getUUID();
            final Button button = new Button();
            button.setLabel(getMessage("cropper.edit.button"));
            button.setOnclick("new mgnl.imagefiltering.ImageResizer.openCropper('" + cropperInfo.getId() + "', '" + controlUUID + "', '" + imagePath + "');");

            out.write(button.getHtml());
            out.write(cropperInfo.getHtml());
        }

        drawHtmlPost(out);
    }

    public static String getCropperInfoPropertyName(String fileControlName) {
        return fileControlName + "_cropperInfo";
    }

    public static String getTargetBinaryPropertyName(String fileControlName) {
        return fileControlName + "_resized";
    }
}
