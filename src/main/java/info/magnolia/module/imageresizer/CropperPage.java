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

import info.magnolia.api.HierarchyManager;
import info.magnolia.cms.beans.config.ContentRepository;
import info.magnolia.cms.core.Content;
import info.magnolia.cms.gui.dialog.Dialog;
import info.magnolia.cms.gui.dialog.DialogControlImpl;
import info.magnolia.cms.gui.dialog.DialogTab;
import info.magnolia.context.MgnlContext;
import info.magnolia.freemarker.FreemarkerUtil;
import info.magnolia.module.admininterface.TemplatedMVCHandler;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

/**
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CropperPage extends TemplatedMVCHandler {
    private String configDialogUUID;
    private String imagePath;
    private long ratioX;
    private long ratioY;
    private long minHeight;
    private long minWidth;
    private long maxHeight;
    private long maxWidth;
    private Content imageControlConfigNode;

    public CropperPage(String name, HttpServletRequest request, HttpServletResponse response) {
        super(name, request, response);
    }

    public void init() {
        super.init();

        if (configDialogUUID == null) {
            throw new IllegalStateException("configDialogUUID should have been set");
        }
        try {
            final HierarchyManager hm = MgnlContext.getHierarchyManager(ContentRepository.CONFIG);
            imageControlConfigNode = hm.getContentByUUID(configDialogUUID);

            ratioX = getLong(imageControlConfigNode, "ratioX");
            ratioY = getLong(imageControlConfigNode, "ratioY");
            minHeight = getLong(imageControlConfigNode, "minHeight");
            minWidth = getLong(imageControlConfigNode, "minWidth");
            maxHeight = getLong(imageControlConfigNode, "maxHeight");
            maxWidth = getLong(imageControlConfigNode, "maxWidth");

            // TODO : get current crop zone !

        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    public void renderHtml(String view) throws IOException {
        try {
            // let's fake a dialog for the sake of its layout ... and save button.
            final Dialog dialog = new CropperDialog();
            dialog.init(request, response, null, imageControlConfigNode);
            dialog.setConfig("saveOnclick", "cropperSubmit();");
            dialog.setConfig("i18nBasename", "info.magnolia.module.imageresizer.messages");

            final String tabLabel = dialog.getMessage("cropper.tab.label");
            final DialogTab tab = dialog.addTab(tabLabel);
            tab.addSub(new CropperControl());

            dialog.drawHtml(getResponse().getWriter());

        } catch (RepositoryException e) {
            throw new RuntimeException(e); // TODO
        }
    }

    private long getLong(Content node, String name) {
        return node.getNodeData(name).getLong();
    }

    public void setConfigDialogUUID(String configDialogUUID) {
        this.configDialogUUID = configDialogUUID;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public long getRatioX() {
        return ratioX;
    }

    public long getRatioY() {
        return ratioY;
    }

    public long getMinHeight() {
        return minHeight;
    }

    public long getMinWidth() {
        return minWidth;
    }

    public long getMaxHeight() {
        return maxHeight;
    }

    public long getMaxWidth() {
        return maxWidth;
    }

    private final class CropperDialog extends Dialog {
        protected void drawHtmlPreSubsHead(Writer out) throws IOException {
            super.drawHtmlPreSubsHead(out);
            renderTemplate("/info/magnolia/module/imageresizer/CropperPage.head.html", out);
        }

//        public String getConfigValue(String key, String nullValue) {
//            if ("saveOnclick".equals(key)) {
//                return "cropperSubmit();";
//            }
//            return super.getConfigValue(key, nullValue);
//        }
    }

    private final class CropperControl extends DialogControlImpl {
        public void drawHtml(Writer out) throws IOException {
            renderTemplate("/info/magnolia/module/imageresizer/CropperPage.html", out);
        }
    }

    private void renderTemplate(String templateName, Writer out) {
        final HashMap map = new HashMap();
        map.put("this", this);
        FreemarkerUtil.process(templateName, map, out);
    }

}
