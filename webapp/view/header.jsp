<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <meta charset="UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
        <div class="card">
            <div class="card-header" >
                <div class="d-grid gap-2 d-md-flex justify-content-md-end">

                    <div class="card-body">
                        <div class="card-title">
                            <h5> Cargo Application  </h5>
                        </div>
                    </div>



                    <c:if test = "${user.userRole == 'MANAGER'}">
                        <form action ="/app/manager/managerHome.jsp" method = "GET">
                            <input type = "submit" class="btn btn-primary" value = ' Home '/>
                        </form>
                    </c:if>

                    <c:if test = "${user.userRole == 'CLIENT'}">
                        <form action ="/app/cargo/client/getInfoToOder" method = "GET">
                            <input type = "hidden" name = "clientId" value = "${sessionScope.user.id}"/>
                            <input type = "submit" class="btn btn-primary" value = ' Place an order for delivery '/>
                        </form>
                    </c:if>

                    <c:if test = "${user.userRole == 'CLIENT'}">
                        <form action ="/app/client/clientHome.jsp" method = "GET">
                            <input type = "submit" class="btn btn-primary" value = ' Home '/>
                        </form>
                    </c:if>


                    <c:if test = "${user.userRole == 'CLIENT'}">
                        <form action ="/app/cargo/client/getWalletInfo" method = "GET">
                            <input type = "hidden" name="clientId" value = "${sessionScope.user.id}"/>
                            <input type = "submit" class="btn btn-primary" value = 'My Wallet'/>
                        </form>
                    </c:if>

                    <c:if test = "${user.userRole == 'CLIENT'}">
                        <form action ="/app/cargo/client/getClientOrders" method = "GET">
                            <input type = "hidden" name="clientId" value = "${sessionScope.user.id}"/>
                            <input type = "submit" class="btn btn-primary" value = ' My orders '/>
                        </form>
                    </c:if>

                    <form action = "/app/cargo/logout" method = "POST" >
                        <input type = "submit" class="btn btn-primary" value = 'Logout'/>
                    </form>
                </div>
            </div>
        </div>
    </head>
</html>