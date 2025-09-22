<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title> BrockerX </title>
</head>
<body>

<!-- Header -->
<jsp:include page="header.jsp"/>

<!-- Contenu spécifique -->
<div class="content">
    <jsp:include page="${contentPage}"/>
</div>

<!-- Footer -->

</body>
</html>
