<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <body>
        <head>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
            <%@ include file="/view/header.jsp" %>
        </head>

        <c:if test = "${reports.size() == 0}">
            <h3> All orders confirmed :) </h3>
        </c:if>

        <c:if test = "${reports.size() > 0}">
            <table class="table table-bordered caption-top" >
                <caption>Manage Orders</caption>
                <tr valign = "middle" align = "center">
                    <th>Order <br> number</th>
                    <th>Client</th>
                    <th>Cargo type</th>
                    <th>Cargo weight (kg)</th>
                    <th>Cargo volume (m³)</th>
                    <th>Distance (km)</th>
                    <th>Delivery from:</th>
                    <th>Delivery to:</th>
                    <th>Price (UAH)</th>
                    <th>Status</th>
                    <th>Confirm order and <br> send invoice</th>
                </tr>
                <c:forEach items="${reports}" var="report">
                    <tr valign = "middle" align = "center">
                        <td>${report.order.id}</td>
                        <td>${report.client.login}</td>
                        <td>${report.order.type}</td>
                        <td>${report.order.weight}</td>
                        <td>${report.order.volume}</td>
                        <td>${report.order.delivery.route.distance}</td>
                        <td>${report.order.delivery.route.senderCity}</td>
                        <td>${report.order.delivery.route.recipientCity}</td>
                        <td>${report.order.invoice.price}</td>
                        <td>
                            <c:if test="${report.order.isConfirmed == false}">
                                <h6 style = "color:red">Not confirmed</h6>
                            </c:if>
                            <c:if test="${report.order.isConfirmed == true}">
                                <h6 style = "color:green">Confirmed</h6>
                            </c:if>
                        </td>
                        <c:if test = "${report.order.isConfirmed == false}">
                            <td>
                                <form action = "/app/cargo/manager/confirmOrder" method = "POST">
                                    <input type = "hidden" name = "orderId" value = "${report.order.id}" />
                                    <button type = "submit"  class="btn btn-success btn-sm">Confirm</button>
                                </form>
                            </td>
                        </c:if>
                        <c:if test = "${report.order.isConfirmed == true}">
                            <td> </td>
                        </c:if>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </body>
</html>
