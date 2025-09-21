<c:choose>
    <c:when test="${valid}">
        <p>✅ Code correct ! Vous êtes authentifié.</p>
    </c:when>
    <c:otherwise>
        <p>❌ Code incorrect, essayez encore.</p>
    </c:otherwise>
</c:choose>
