<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header>
    <nav>
        <ul>
            <li><a href="/">Accueil</a></li>

            <!-- Vérifie si l'utilisateur est connecté -->
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <li>Bonjour, ${sessionScope.user.firstName}</li>
                    <li><a href="/logout">Déconnexion</a></li>

                    <!-- MFA -->
                    <c:if test="${not sessionScope.user.mfaEnabled}">
                        <li><a href="/settings/mfa">Activer MFA</a></li>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <li><a href="/login">Connexion</a></li>
                    <li><a href="/signup">S'inscrire</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
</header>
<hr/>
