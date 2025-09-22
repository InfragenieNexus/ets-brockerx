<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Connexion BrokerX</title>
</head>
<body>
<h1>Connexion</h1>

<c:if test="${not empty error}">
    <p style="color:red;"><c:out value="${error}"/></p>
</c:if>

<form action="/login" method="post">
    Email: <input type="email" name="username" required/><br/>
    Mot de passe: <input type="password" name="password" required/><br/>
    <button type="submit">Se connecter</button>
</form>

<p>Pas encore inscrit ? <a href="/signup">Créer un compte</a></p>
</body>
</html>
