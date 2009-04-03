/**
 * This file Copyright (c) 2003-2009 Magnolia International
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
package info.magnolia.module.imagefiltering.cropresize;

import info.magnolia.cms.beans.config.ContentRepository;
import info.magnolia.cms.core.Content;
import info.magnolia.cms.core.HierarchyManager;
import info.magnolia.cms.gui.dialog.Dialog;
import info.magnolia.cms.gui.dialog.DialogControlImpl;
import info.magnolia.cms.gui.dialog.DialogTab;
import info.magnolia.context.MgnlContext;
import info.magnolia.freemarker.FreemarkerUtil;
import info.magnolia.module.admininterface.TemplatedMVCHandler;
import info.magnolia.content2bean.Content2BeanUtil;
import info.magnolia.content2bean.Content2BeanException;
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
        final CropAndResizeConfig cfg;
        try {
            cfg = (CropAndResizeConfig) Content2BeanUtil.setProperties(new CropAndResizeConfig(), node);
        } catch (Content2BeanException e) {
            throw new IllegalStateException(e); // TODO
        }

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
            // TODO : this is not needed with 3.5
            dialog.setConfig("i18nBasename", "info.magnolia.module.imagefiltering.messages");
            dialog.setConfig("saveLabel", dialog.getMessage("cropper.apply.button"));
            dialog.setConfig("saveOnclick", "cropperSubmit();");

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
            out.flush();
        }
    }

    private final class CropperControl extends DialogControlImpl {
        public void drawHtml(Writer out) throws IOException {
            out.write("<tbody><tr><td>");
            out.flush();
            renderTemplate("/info/magnolia/module/imagefiltering/cropresize/CropperPage.html", out);
            out.flush();
            out.write("</td></tr></tbody>");
        }
    }

    private void renderTemplate(String templateName, Writer out) {
        final HashMap map = new HashMap();
        map.put("this", this);
        FreemarkerUtil.process(templateName, map, out);
    }

}
