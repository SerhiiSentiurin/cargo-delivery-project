<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <body>
        <head>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
            <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&libraries=places&key=AIzaSyAyM6orVGgY2a7NzZPgLnYm2FmyrqFcQYc"></script>
            <%@ include file="/view/header.jsp" %>
        </head>

        <c:if test = "${order != null}">
        <table class="table table-bordered" vertical-align = "center" style = "width: 600px ">
            <caption align = "top">
                <h3 align = "center">Invoice</h3>
            </caption>
            <tr>
                <th>Cargo type</th>
                <th>Delivery from</th>
                <th>Delivery to</th>
                <th>Distance (km)</th>
                <th>Price (UAH)</th>
                <th>Choose arrival date</th>
            </tr>
            <tr>
                <td>${order.type}</td>
                <td>${order.delivery.route.senderCity}</td>
                <td>${order.delivery.route.recipientCity}</td>
                <td>${order.delivery.route.distance}</td>
                <td>${order.invoice.price}</td>
                <td>
                    <input type="date" name="arrivalDate" value="${date.plusDays(3)}" min="${date.plusDays(3)}" max = "${date.plusYears(1)}" form = "payInvoice">
                </td>
            </tr>
            <tr>
                <td>
                    <form action ="/app/cargo/client/payInvoice" method = "POST" id = "payInvoice">
                        <input type = "hidden" name = "clientId" value = "${sessionScope.user.id}" />
                        <input type = "hidden" name = "orderId" value = "${order.id}" />
                        <button type = "submit"  class = "btn btn-primary">Pay invoice</button>
                    </form>
                </td>
            </tr>
        </table>
        </c:if>

    </body>
</html>
