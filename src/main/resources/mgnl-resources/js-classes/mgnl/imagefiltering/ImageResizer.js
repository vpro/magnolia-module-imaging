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
classDef("mgnl.imagefiltering.ImageResizer", {
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

