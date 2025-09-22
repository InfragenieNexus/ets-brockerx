<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Inscription BrokerX</title>
</head>
<body>
<h1>Inscription</h1>

<c:if test="${not empty error}">
    <p style="color:red;"><c:out value="${error}"/></p>
</c:if>

<form action="/signup" method="post">
    Email: <input type="email" name="email" required/><br/>
    Mot de passe: <input type="password" name="password" required/><br/>
    Téléphone: <input type="text" name="phone"/><br/>
    Nom: <input type="text" name="firstName"/><br/>
    Prénom: <input type="text" name="lastName"/><br/>
    Adresse: <input type="text" name="address"/><br/>
    Date de naissance: <input type="date" name="dateOfBirth"/><br/>
    <button type="submit">S’inscrire</button>
</form>

<p>Déjà inscrit ? <a href="/login">Se connecter</a></p>
</body>
</html>
