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
package info.magnolia.module.imagefiltering.cropresize;

import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.NodeData;
import info.magnolia.cms.gui.control.Button;
import info.magnolia.cms.gui.control.File;
import info.magnolia.cms.gui.control.Hidden;
import info.magnolia.cms.gui.dialog.DialogBox;
import info.magnolia.cms.gui.misc.CssConstants;
import org.apache.commons.lang.StringUtils;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * A control that's related to another control (named with fileControlName) and opens an edit window.
 *
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
            final String imagePath = fileControl.getHandle() + "." + fileControl.getExtension();
            final String cropperInfoControlName = getCropperInfoPropertyName(fileControlName);
            final String previewId = cropperInfoControlName + "_previewZone";

            out.write("\n<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
            //width=\"" + width + "\">");
            out.write("<tr><td class=\"" + CssConstants.CSSCLASS_FILEIMAGE + "\">");

            // preview
            final NodeData bin = getWebsiteNode().getNodeData(getTargetBinaryPropertyName(fileControlName));
            out.write("<div id=\"");
            out.write(previewId);
            out.write("\">");
            if (bin.isExist()) {
                final File preview = new File(getTargetBinaryPropertyName(fileControlName), getWebsiteNode());
                int previewHeight = -1;
                try {
                    previewHeight = Integer.parseInt(preview.getImageHeight());
                    if (previewHeight > 100) {
                        previewHeight = 100;
                    }
                } catch (NumberFormatException e) {
                    // if the height wasn't stored .. should really not happen except if you used an old snapshot of this module ...
                }

                out.write("<img src=\"");
                out.write(getRequest().getContextPath());
                out.write(preview.getPath());
                if (previewHeight > 0) {
                    out.write("\" height=\"");
                    out.write(String.valueOf(previewHeight));
                }
                out.write("\" />");
                if (StringUtils.isNotEmpty(preview.getImageWidth())) {
                    out.write("<p><em style='white-space:nowrap'>");
                    out.write(getMessage("cropper.preview.widthheight", new String[]{preview.getImageWidth(), preview.getImageHeight()}));
                    out.write("</em></p>\n");
                }
            }
            out.write("<p style=\"display: none;\" class=\"warning\">");
            out.write(getMessage("cropper.preview.tobeprocessed"));
            out.write("</p>");
            out.write("</div>");

            out.write("</td><td>");

            final Hidden cropperInfo = new Hidden(cropperInfoControlName, getWebsiteNode());
            cropperInfo.setId(cropperInfoControlName);
            cropperInfo.setSaveInfo(true);

            final String controlUUID = configNode.getUUID();
            final Button button = new Button();
            button.setLabel(getMessage("cropper.edit.button"));
            button.setOnclick("new mgnl.imagefiltering.ImageResizer.openCropper('" + cropperInfo.getId() + "', '" + controlUUID + "', '" + imagePath + "');");

            out.write("<div class=\""); // just to give a little padding
            out.write(CssConstants.CSSCLASS_DESCRIPTION);
            out.write("\">");
            out.write(button.getHtml());
            out.write(cropperInfo.getHtml());
            out.write("</div>");

            out.write("</td></tr></table>");
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
