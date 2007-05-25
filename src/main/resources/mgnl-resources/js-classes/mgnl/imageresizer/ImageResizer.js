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
classDef("mgnl.imageresizer.ImageResizer", {
    /**
     * @param resultControlId the (html) id of the control where the cropping data should be stored.
     * @param dialogUUID the uuid of the dialog that holds the configuration for the cropper.
     */
    openCropper: function(cropValueControlId, dialogUUID, imagePath) {
        // TODO : open to image's size (with a maximum)
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
            document.getElementById(cropValueControlId).value = resultStr;
            imageCropperWindow.close();
        }
    }
});

