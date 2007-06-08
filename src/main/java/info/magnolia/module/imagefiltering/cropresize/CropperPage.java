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

import info.magnolia.api.HierarchyManager;
import info.magnolia.cms.beans.config.ContentRepository;
import info.magnolia.cms.core.Content;
import info.magnolia.cms.gui.dialog.Dialog;
import info.magnolia.cms.gui.dialog.DialogControlImpl;
import info.magnolia.cms.gui.dialog.DialogTab;
import info.magnolia.cms.util.ContentUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.freemarker.FreemarkerUtil;
import info.magnolia.module.admininterface.TemplatedMVCHandler;
import org.apache.commons.lang.StringUtils;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * The page that provides the image cropping dialog/widget.
 *
 * @author gjoseph
 * @version $Revision: $ ($Author: $)
 */
public class CropperPage extends TemplatedMVCHandler {
    private String configDialogUUID;
    private String imagePath;
    /**
     * The current params (crop values, chosen config) as a JSON string.
     */
    private String currentCrop;
    private List cropAndResizeConfigs; // List<String nodeName, CropAndResizeConfig>
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

            cropAndResizeConfigs = new ArrayList();
            final Collection configNodes = imageControlConfigNode.getChildren();
            final Iterator it = configNodes.iterator();
            while (it.hasNext()) {
                final Content configNode = (Content) it.next();
                cropAndResizeConfigs.add(fromNode(configNode));
            }

        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    public static CropAndResizeConfig fromNode(Content node) {
        // TODO : use content2bean + cleanup
        final CropAndResizeConfig cfg = (CropAndResizeConfig) ContentUtil.setProperties(new CropAndResizeConfig(), node);

        // if label property wasn't set, we try using the name property, or the node name if neither was set.        
        if (StringUtils.isEmpty(cfg.getName())) {
            cfg.setName(node.getName());
        }
        if (StringUtils.isEmpty(cfg.getLabel())) {
            // todo: i18n ?
            // dialog.getMessage(cfg.getName());
            cfg.setLabel(cfg.getName());
        }

        // name is always node name, to avoid illegal characters
        cfg.setName(node.getName());
        return cfg;

    }

    public void renderHtml(String view) throws IOException {
        try {
            // let's fake a dialog for the sake of its layout ... and save button.
            final Dialog dialog = new CropperDialog();
            dialog.init(request, response, null, imageControlConfigNode);
            dialog.setConfig("saveOnclick", "cropperSubmit();");
            dialog.setConfig("i18nBasename", "info.magnolia.module.imagefiltering.messages");

            final String tabLabel = dialog.getMessage("cropper.tab.label");
            final DialogTab tab = dialog.addTab(tabLabel);
            tab.addSub(new CropperControl());

            dialog.drawHtml(getResponse().getWriter());

        } catch (RepositoryException e) {
            throw new RuntimeException(e); // TODO ?
        }
    }

    public void setConfigDialogUUID(String configDialogUUID) {
        this.configDialogUUID = configDialogUUID;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setCurrentCrop(String currentCrop) {
        this.currentCrop = currentCrop;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getCurrentCrop() {
        return currentCrop;
    }

    public List getConfigs() { // <CropAndResizeConfig>
        return cropAndResizeConfigs;
    }

    private final class CropperDialog extends Dialog {
        protected void drawHtmlPreSubsHead(Writer out) throws IOException {
            super.drawHtmlPreSubsHead(out);
            renderTemplate("/info/magnolia/module/imagefiltering/cropresize/CropperPage.head.html", out);
        }
    }

    private final class CropperControl extends DialogControlImpl {
        public void drawHtml(Writer out) throws IOException {
            renderTemplate("/info/magnolia/module/imagefiltering/cropresize/CropperPage.html", out);
        }
    }

    private void renderTemplate(String templateName, Writer out) {
        final HashMap map = new HashMap();
        map.put("this", this);
        FreemarkerUtil.process(templateName, map, out);
    }

}
