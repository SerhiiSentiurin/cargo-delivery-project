<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <head>
        <meta charset="UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
        <div class="card" >
            <div class="card-header" >
                <div class="d-grid gap-2 d-md-flex justify-content-md-end" style = "display: flex; justify-content: center; align-items: center; height: 100px;">

                    <div class="card-body">
                        <div class="card-title">
                            <h5><lang:print message = "header.jsp.cargo.application"/></h5>
                        </div>
                    </div>

                    <c:if test = "${sessionScope.user.userRole == 'MANAGER'}">
                        <form action ="/app/manager/home.jsp" method = "GET">
                            <input type = "submit" class="btn btn-primary" value = '<lang:print message = "header.jsp.home"/>'/>
                        </form>
                    </c:if>

                    <c:if test = "${sessionScope.user.userRole == 'MANAGER'}">
                        <form action = "/app/cargo/manager/orders" method "GET">
                            <input type="hidden" name="page" value="1">
                            <input type = "submit" class="btn btn-primary" value = '<lang:print message = "header.jsp.get.all.orders"/>'/>
                        </form>
                    </c:if>

                    <c:if test = "${sessionScope.user.userRole == 'MANAGER'}">
                        <form action = "/app/cargo/manager/orders/notConfirmed" method "GET">
                            <input type = "submit" class="btn btn-primary" value = '<lang:print message = "header.jsp.get.not.confirmed.orders"/>'/>
                        </form>
                    </c:if>

                    <c:if test = "${sessionScope.user.userRole == 'CLIENT'}">
                        <form action ="/app/cargo/client/routes" method = "GET">
                            <input type = "submit" class="btn btn-primary" value = '<lang:print message = "header.jsp.place.an.order.for.delivery"/>'/>
                        </form>
                    </c:if>

                    <c:if test = "${sessionScope.user.userRole == 'CLIENT'}">
                        <form action ="/app/client/home.jsp" method = "GET">
                            <input type = "submit" class="btn btn-primary" value = '<lang:print message = "header.jsp.home"/>'/>
                        </form>
                    </c:if>


                    <c:if test = "${sessionScope.user.userRole == 'CLIENT'}">
                        <form action ="/app/cargo/client/wallet" method = "GET">
                            <input type = "hidden" name="clientId" value = "${sessionScope.user.id}"/>
                            <input type = "submit" class="btn btn-primary" value = '<lang:print message = "header.jsp.my.wallet"/>'/>
                        </form>
                    </c:if>

                    <c:if test = "${sessionScope.user.userRole == 'CLIENT'}">
                        <form action ="/app/cargo/client/orders" method = "GET">
                            <input type="hidden" name="page" value="1">
                            <input type = "hidden" name="clientId" value = "${sessionScope.user.id}"/>
                            <input type = "submit" class="btn btn-primary" value = '<lang:print message = "header.jsp.my.orders"/>'/>
                        </form>
                    </c:if>

                    <form action = "/app/cargo/logout" method = "POST" >
                        <input type = "submit" class="btn btn-primary" value = '<lang:print message = "header.jsp.logout"/>'/>
                    </form>

                    <c:if test = "${sessionScope.user.id != null}">
                        <div class="icon" style = "text-align: center; width: 100px; ">
                            <img src="https://cdn-icons-png.flaticon.com/512/2206/2206200.png" alt="user" style = "width: 60px; height: 60px; line-height: 60px; border-radius: 50%; font-size: 20px; background: #000" />
                            <p>${sessionScope.user.login}</p>
                        </div>
                    </c:if>

                    <form action = "/app/cargo/changeLocale" method = "POST">
                        <input type = "submit" class="btn btn-secondary btn-sm" value = '<lang:print message = "common.message.update"/>'><br>
                        <select name = "selectedLocale" >
                            <c:forEach var = "locale" items = "${sessionScope.locales}">
                                <option value = "${locale}">
                                    ${locale}
                                </option>
                            </c:forEach>
                        </select>
                    </form>
                </div>
            </div>
        </div>
    </head>
</html>