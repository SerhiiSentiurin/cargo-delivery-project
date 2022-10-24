<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <head>
        <title>Welcome page</title>
        <meta charset="UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
        <h1 align = "center">
           <lang:print message = "index.jsp.welcome.to.app"/>
        </h1>
    </head>
    <body>
        <div align = "center">
            <form action = "/app/cargo/login" method = "POST" >
                <label for="name" ><lang:print message = "index.jsp.login"/></label><br>
                <input type="text" name="login" pattern=".{3,}" title='<lang:print message = "index.jsp.title.login"/>' required ><br><br>
                <label for="pass"><lang:print message = "index.jsp.password"/></label><br>
                <input type="password" name="password" pattern="(?=.*\d)(?=.*[a-zа-яії])(?=.*[A-ZА-ЯІЇ]).{8,}" title='<lang:print message = "index.jsp.title.password"/>' required><br><br>
                <input type = "submit" style="width: 8%" class="btn btn-primary" value='<lang:print message = "index.jsp.log.in"/>'>
                <br><br>
                <input type = "submit" style="width: 8%" class="btn btn-primary" formaction="/app/cargo/client/create" value='<lang:print message = "index.jsp.sign.up"/>'>
            </form>
        </div>
        <br><br>
        <div align = "center">
            <h3>
                <lang:print message = "index.jsp.get.delivery.cost.without.registration"/>
            </h3>
            <form action = "/app/cargo/routes" method = "GET">
            <input type = "submit" class = "btn btn-secondary" value = '<lang:print message = "index.jsp.get.cost"/>'/>
            </form>
            <br><br><br>
            <form action = "/app/cargo/changeLocale" method = "POST">
                <input type = "submit" class="btn btn-secondary" value = '<lang:print message = "common.message.update"/>'><br>
                <select name = "selectedLocale">
                    <c:forEach var = "locale" items = "${sessionScope.locales}">
                        <option value = "${locale}">
                            ${locale}
                        </option>
                    </c:forEach>
                </select>
            </form>

        </div>
    </body>
</html>