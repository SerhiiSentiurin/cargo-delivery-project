<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Welcome page</title>
        <meta charset="UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">

        <h1>
            Registration successful! Please enter.
        </h1>
    </head>

    <body>
        <form action = "/app/cargo/login" method = "POST">
            <label for="name">Login</label><br>
            <input type="text" name="login" pattern=".{3,}" title='Three or more characters required' required ><br><br>
            <label for="pass">Password</label><br>
            <input type="password" name="password" pattern="(?=.*\d)(?=.*[a-zа-яії])(?=.*[A-ZА-ЯІЇ]).{8,}" title='Must contain at least one  number and one uppercase and lowercase letter, and at least 8 or more characters' required><br><br>
            <input type = "submit" style="width: 8%" class="btn btn-primary" value='Log in'>
        </form>
    </body>
</html>