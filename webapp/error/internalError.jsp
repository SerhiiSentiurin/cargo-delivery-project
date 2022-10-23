<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<!DOCTYPE html>
<html>
    <head>
        <title>error:500</title>
        <meta charset="UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
    </head>
    <body>
        <p>
            <lang:print message = "internalError.jsp.something.went.wrong"/>
        </p>
        <button class="btn btn-secondary" onclick="location.href='/app'" ><lang:print message = "internalError.jsp.back"/></button>
    </body>
</html>