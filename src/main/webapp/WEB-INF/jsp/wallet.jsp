<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script>
    function uuidv4() {
        return "10000000-1000-4000-8000-100000000000".replace(/[018]/g, c =>
            (+c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> +c / 4).toString(16)
        );
    }

    function depositWallet(event) {
        event.preventDefault(); // Empêche l'envoi classique du formulaire

        const userId = document.getElementById('userId').value;
        const amount = document.getElementById('amount').value;

        // Générer un Idempotency-Key unique
        const idempotencyKey = uuidv4();

        fetch('/wallet/deposit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'Idempotency-Key': idempotencyKey
            },
            body: new URLSearchParams({
                userId: userId,
                amount: amount
            })
        })
            .then(response => response.text())
            .then(data => {
                alert('Dépot effectué !');
                location.reload();
            })
            .catch(error => {
                console.error('Erreur:', error);
                alert('Erreur lors du dépôt.');
            });
    }
</script>


<h1>Portefeuille de <c:out value="${user.firstName} ${user.lastName}"/></h1>

<c:choose>
    <c:when test="${wallet != null}">
        <p>Solde actuel : $<c:out value="${wallet.balance}"/></p>
        <form onsubmit="depositWallet(event);">
            <input type="hidden" id="userId" value="${user.id}"/>
            Montant à déposer :
            <input type="number" step="0.01" id="amount"/>
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

