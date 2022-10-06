<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <body>
        <head>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
            <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&libraries=places&key=AIzaSyAyM6orVGgY2a7NzZPgLnYm2FmyrqFcQYc"></script>
            <%@ include file="/view/header.jsp" %>
        </head>

        <table class="table table-striped">
            <caption>Your orders</caption>
                <tr>
                    <th>Cargo type</th>
                    <th>Cargo weight</th>
                    <th>Cargo volume</th>
                    <th>Delivery from</th>
                    <th>Delivery to</th>
                    <th>Distance</th>
                    <th>Departure date</th>
                    <th>Arrival date</th>
                    <th>Price</th>
                    <th>Confirmation</th>
                    <th>Payment</th>

                </tr>

                <tr>
                    <td>${client.order.type}</td>
                    <td>${client.order.weight}</td>
                    <td>${client.order.volume}</td>
                    <td>${client.order.delivery.route.senderCity}</td>
                    <td>${client.order.delivery.route.recipientCity}</td>
                    <td>${client.order.delivery.route.distance}</td>
                    <td>${client.order.delivery.departureDate}</td>
                    <td>${client.order.delivery.arrivalDate}</td>
                    <td>${client.invoice.price}</td>
                    <td>${client.order.isConfirmed}</td>
                    <td>${client.invoice.isPaid}</td>
                </tr>


        </table>


    </body>
</html>
