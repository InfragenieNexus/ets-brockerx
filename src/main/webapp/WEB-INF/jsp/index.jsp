<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bienvenue sur BrokerX</title>
</head>
<body>
<h1>Bienvenue sur BrokerX !</h1>

<c:choose>
    <c:when test="${not empty sessionScope.user}">
        <p>Bonjour, <c:out value="${sessionScope.user.firstName}"/> !</p>
        <a href="${pageContext.request.contextPath}/wallet/view?userId
        =${sessionScope.user.id}">Accéder au Dashboard</a>
    </c:when>
    <c:otherwise>
        <p>
            <a href="${pageContext.request.contextPath}/signup">Créer un
                compte</a> |
            <a href="${pageContext.request.contextPath}/login">Se connecter</a>
        </p>
    </c:otherwise>
</c:choose>

</body>
</html>
