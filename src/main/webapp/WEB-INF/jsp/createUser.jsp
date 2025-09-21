<!-- src/main/webapp/WEB-INF/jsp/createUser.jsp -->
<form action="/users/save" method="post">
    <input type="text" name="username" placeholder="Username"/>
    <input type="text" name="email" placeholder="Email"/>
    <input type="password" name="password" placeholder="Password"/>
    <button type="submit">Créer User</button>
</form>
