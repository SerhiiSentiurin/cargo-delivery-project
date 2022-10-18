<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
        <%@ include file="/view/header.jsp" %>
    </head>
    <body>
        <h2>
            Hello, ${sessionScope.user.login}
        </h2>

        <div>
            <img src="https://dnepr-more.com/wp-content/uploads/2018/02/gruzoperevozki.jpg" alt="Зимняя равнина" style="float:left; margin-right:10px;" />
                <h1>About Cargo Application</h1>
                <p>
                    Some rules how to use it.....
                </p>
        </div>

    <%@ include file="/view/footer.jsp" %>

    </body>
</html>
