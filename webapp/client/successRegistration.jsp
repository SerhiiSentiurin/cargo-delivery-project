<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <head>
        <title>Welcome page</title>
        <meta charset="UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">

        <h1>
            <lang:print message = "successRegistration.jsp.registration.successful.please.enter"/>
        </h1>
    </head>

    <body>
        <form action = "/app/cargo/login" method = "POST">
            <label for="name"><lang:print message = "index.jsp.login"/></label><br>
            <input type="text" name="login" pattern=".{3,}" title='Three or more characters required' required ><br><br>
            <label for="pass"><lang:print message = "index.jsp.password"/></label><br>
            <input type="password" name="password" pattern="(?=.*\d)(?=.*[a-zа-яії])(?=.*[A-ZА-ЯІЇ]).{8,}" title='Must contain at least one  number and one uppercase and lowercase letter, and at least 8 or more characters' required><br><br>
            <input type = "submit" style="width: 8%" class="btn btn-primary" value='<lang:print message = "index.jsp.log.in"/>'>
        </form>
    </body>
</html>