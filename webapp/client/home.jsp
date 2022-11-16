<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <head>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
        <%@ include file="/view/header.jsp" %>
    </head>
    <body>
        <h2>
            <lang:print message = "clientHome.jsp.hello"/> ${sessionScope.user.login}
        </h2>

        <div>
            <img src="https://dnepr-more.com/wp-content/uploads/2018/02/gruzoperevozki.jpg" alt="cargo" style="float:left; margin-right:10px;" />
            <h1><lang:print message = "clientHome.jsp.about.cargo.application"/></h1>
            <p class="lh-lg" style="margin-left: 20px;">
                <h5><lang:print message = "clientHome.jsp.this.is.your.home.page"/></h5><br>
                    <lang:print message = "clientHome.jsp.at.the.top.of.the.page"/><br>

                    &#34;<u><lang:print message = "clientHome.jsp.place.an.order.for.delivery"/></u>&#34;
                    - <lang:print message = "clientHome.jsp.about.button.place.an.order.for.delivery"/><br>

                    &#34;<u><lang:print message = "clientHome.jsp.my.orders"/></u>&#34;
                    - <lang:print message = "clientHome.jsp.clientHome.jsp.about.button.my.orders"/><br>

                    &#34;<u><lang:print message = "clientHome.jsp.my.wallet"/></u>&#34;
                    - <lang:print message = "clientHome.jsp.about.button.my.wallet"/>
            </p>
        </div>

    <%@ include file="/view/footer.jsp" %>

    </body>
</html>
