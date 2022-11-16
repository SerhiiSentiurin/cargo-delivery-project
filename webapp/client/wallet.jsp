<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <head>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
        <%@ include file="/view/header.jsp" %>
    </head>
    <body>

        <table id="manageWallet" class="table table-bordered" vertical-align = "center" style = "width: 600px ">
            <caption align = "top">
                <h3><lang:print message = "clientWallet.jsp.wallet.balance"/> ${client.amount} <lang:print message = "common.message.(uah)"/></h3>
            </caption>
            <tr align="center">
                <th><lang:print message = "clientWallet.jsp.top.up.your.wallet"/></th>
                <th><lang:print message = "clientWallet.jsp.choose.delivery"/></th>
            </tr>
            <tr>
                <td align="center">
                    <form action ="/app/cargo/client/wallet" method = "POST">
                        <input type = "hidden" name="clientId" value = "${sessionScope.user.id}"/>
                        <input type="number" min = "0" step = 0.01 name="amount" required><br><br>
                        <input type = "submit" class = "btn btn-primary" value ='<lang:print message = "clientWallet.jsp.top.up"/>'>
                    </form>
                </td>
                <td align="center">
                    <form action ="/app/cargo/client/routes" method = "GET">
                        <input type = "submit" class="btn btn-primary" value = '<lang:print message = "header.jsp.place.an.order.for.delivery"/>'/>
                    </form>
                </td>
            </tr>
        </table><br>

        <div class="card">
            <div class="card-footer text-muted">
                <lang:print message = "clientWallet.jsp.here.you.may.replenish.balance.on.your.wallet"/>
            </div>
        </div>
    </body>
</html>
