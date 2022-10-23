<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<!DOCTYPE html>
<html>
    <head>
        <title>error:400</title>
        <meta charset="UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
        <%@ include file="/view/header.jsp" %>
    </head>
    <body>
        <p>
            <jsp:text>
                <lang:print message = "error.jsp.error"/> ${message}
            </jsp:text>
        </p>
        <input type="button" class="btn btn-secondary" onclick="history.back();" value='<lang:print message = "internalError.jsp.back"/>'>
    </body>
</html>