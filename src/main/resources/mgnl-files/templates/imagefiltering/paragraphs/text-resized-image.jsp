<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="cms-taglib" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<cms:ifNotEmpty nodeDataName="title">
    <h2><cms:out nodeDataName="title"/></h2>
</cms:ifNotEmpty>
<p>
<cms:ifNotEmpty nodeDataName="image">
    <cms:setNode var="para"/>
    <cms:out nodeDataName="image" var="originalImageUrl"/>
    <cms:out nodeDataName="image_resized" var="resizedImageUrl"/>
    <%-- inline styles shouldn't be used, but hey, this is only a sample --%>
    <a href="${ctx}${originalImageUrl}"><img src="${ctx}${resizedImageUrl}" style="border: none; float: left; margin: 0; padding: 0 1em 0 0;"/></a>
</cms:ifNotEmpty>
<cms:out nodeDataName="text"/>
</p>
<!-- cropper info: <cms:out nodeDataName="image_cropperInfo" /> -->
