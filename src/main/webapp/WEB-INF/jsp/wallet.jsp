<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Portefeuille de l'utilisateur</title>
</head>
<body>
<h1>Portefeuille de <c:out value="${user.username}"/></h1>

<c:choose>
    <c:when test="${wallet != null}">
        <p>Solde actuel : $<c:out value="${wallet.balance}"/></p>
        <form action="/wallet/deposit" method="post">
            <input type="hidden" name="userId" value="${user.id}" />
            Montant à déposer : <input type="number" step="0.01" name="amount"/>
            <button type="submit">Déposer</button>
        </form>
    </c:when>
    <c:otherwise>
        <p>Aucun portefeuille trouvé pour cet utilisateur.</p>
    </c:otherwise>
</c:choose>

<c:if test="${not empty error}">
    <p style="color:red;">Erreur : <c:out value="${error}"/></p>
</c:if>
</body>
</html>
