/**
 * This file Copyright (c) 2007-2012 Magnolia International
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
classDef("mgnl.cropui.ImageCropper", {
    /**
     * @param resultControlId the (html) id of the control where the cropping data should be stored.
     * @param dialogUUID the uuid of the dialog that holds the configuration for the cropper.
     */
    openCropper: function(cropValueControlId, dialogUUID, imagePath) {
        // TODO : open to image's size (with a maximum) MGNLIMG-6
        //mgnlOpenWindow('/.magnolia/pages/imageCropper.html', 800, 800);
        //mgnlOpenWindow does not return the window handler
        //mgnlOpenDialog(???'image-cropper', 'website')

        // TODO : open win with image's size - is this crossbrowser ?
        // var imggg=document.getElementById('cropperImage');

        var currentCrop = document.getElementById(cropValueControlId).value;

        var imageCropperWindow = window.open(contextPath + '/.magnolia/pages/imageCropper.html?configDialogUUID=' + dialogUUID + '&imagePath=' + imagePath + '&currentCrop=' + currentCrop, 'imageCropper', 'width=800,height=800,scrollbars=auto,status=yes,resizable=yes');
        if (imageCropperWindow.focus) {
            imageCropperWindow.focus();
        }

        imageCropperWindow.cropperCallback = function(resultStr) {
            // put the result in the crop control
            document.getElementById(cropValueControlId).value = resultStr;

            // clear the preview image, replace it with a temp message
            var previewBlock = document.getElementById(cropValueControlId + '_previewZone');
            var img = previewBlock.getElementsByTagName("IMG");
            var p = previewBlock.getElementsByTagName("P");

            // remove the img
            if (img.length > 0) {
                previewBlock.removeChild(img[0]);
            }

            // remove the p with height/width
            if (p.length > 1) {
                previewBlock.removeChild(p[0]);
            }

            // display the warning text
            var textIdx = p.length - 1;
            p[textIdx].style.display = 'block';

            imageCropperWindow.close();
        }
    }
});

