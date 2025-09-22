<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Vérification OTP</title>
</head>
<body>
<h2>Entrez le code OTP reçu par email</h2>

<c:if test="${not empty error}">
    <p style="color:red;">${error}</p>
</c:if>

<form action="/verify-otp" method="post">
    <input type="hidden" name="email" value="${email}"/>
    Code OTP : <input type="text" name="otpCode"/>
    <button type="submit">Valider</button>
</form>
</body>
</html>
