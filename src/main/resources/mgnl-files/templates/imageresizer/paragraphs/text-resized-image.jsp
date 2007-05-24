<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="cms-taglib" %>

<cms:ifNotEmpty nodeDataName="image">
    <cms:setNode var="para"/>
    <cms:out nodeDataName="image" var="imageurl"/>
    <img src="${pageContext.request.contextPath}${imageurl}" alt="${imagedata.imageAlt}"/>
</cms:ifNotEmpty>

<p>cropper info:
<cms:out nodeDataName="cropperInfo" />
</p>

<cms:ifNotEmpty nodeDataName="title">
    <h2><cms:out nodeDataName="title"/></h2>
</cms:ifNotEmpty>
<cms:out nodeDataName="text"/>
