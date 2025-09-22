<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h1>Vérifier votre code TOTP</h1>

<form action="/verify-totp" method="post">
    Code TOTP: <input type="number" name="code" required/><br/>
    <button type="submit">Vérifier</button>
</form>

<c:if test="${not empty error}">
    <p style="color:red;"><c:out value="${error}"/></p>
</c:if>


