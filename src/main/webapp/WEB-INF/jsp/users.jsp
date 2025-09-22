<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>
    table {
        border-collapse: collapse;
        width: 50%;
        margin: 20px auto;
    }

    th, td {
        border: 1px solid #444;
        padding: 8px;
        text-align: left;
    }

    th {
        background-color: #eee;
    }

    a {
        display: block;
        text-align: center;
        margin-top: 20px;
        text-decoration: none;
        color: #007bff;
    }
</style>


<h2 style="text-align:center;">Liste des Utilisateurs</h2>

<table>
    <tr>
        <th>ID</th>
        <th>Nom</th>
        <th>Email</th>
    </tr>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.email}</td>
        </tr>
    </c:forEach>
</table>

<a href="/users/create">Créer un nouvel utilisateur</a>

