<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <body>
        <head>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
            <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&libraries=places&key=AIzaSyAyM6orVGgY2a7NzZPgLnYm2FmyrqFcQYc"></script>
            <%@ include file="/view/header.jsp" %>
        </head>

        <c:if test = "${orders.size() == 0}">
            <h3>Sorry, you have not orders yet :( </h3>
        </c:if>

        <c:if test = "${orders.size() > 0}">
        <table class="table caption-top">
            <caption>Your orders</caption>
                <tr>
                    <th>Cargo type</th>
                    <th>Cargo weight (kg)</th>
                    <th>Cargo volume (m³)</th>
                    <th>Delivery from</th>
                    <th>Delivery to</th>
                    <th>Distance (km)</th>
                    <th>Departure date</th>
                    <th>Arrival date</th>
                    <th>Price (UAH)</th>
                    <th>Confirmation</th>
                    <th>Payment</th>
                    <th>Pay</th>

                </tr>
                <c:forEach items="${orders}" var="order">
                <tr>
                    <td>${order.type}</td>
                    <td>${order.weight}</td>
                    <td>${order.volume}</td>
                    <td>${order.delivery.route.senderCity}</td>
                    <td>${order.delivery.route.recipientCity}</td>
                    <td>${order.delivery.route.distance}</td>
                    <td>${order.delivery.departureDate}</td>
                    <td>${order.delivery.arrivalDate}</td>
                    <td>${order.invoice.price}</td>
                    <td>
                        <c:if test="${order.isConfirmed == false}">
                            <h6 style = "color:red">Not confirmed</h6>
                        </c:if>
                        <c:if test="${order.isConfirmed == true}">
                            <h6 style = "color:green">Confirmed</h6>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${order.invoice.isPaid == false}">
                            <h6 style = "color:red">Not paid :( </h6>
                        </c:if>
                        <c:if test="${order.invoice.isPaid == true}">
                            <h6 style = "color:green">Paid :) </h6>
                        </c:if>
                    </td>
                    <c:if test = "${order.isConfirmed == true}">
                    <c:if test= "${order.invoice.isPaid == false}">
                    <td>
                        <form action ="/app/cargo/client/getInvoice" method = "GET">
                            <input type = "hidden" name = "clientId" value = "${sessionScope.user.id}" />
                            <input type = "hidden" name = "orderId" value = "${order.id}" />
                            <button type = "submit"  class = "btn btn-secondary">Get invoice</button>
                        </form>
                    </td>
                    </c:if>
                    </c:if>

                    <c:if test = "${order.isConfirmed == false}">
                    <c:if test = "${order.invoice.isPaid == true}">
                    <td></td>
                    </c:if>
                    </c:if>
                </tr>
                </c:forEach>
        </table>

        </c:if>
    </body>
</html>
