<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <body>
        <head>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
            <%@ include file="/view/header.jsp" %>
        </head>
        <h2>
            This is manager home page! Hello, ${sessionScope.user.login}!
        </h2>

        <br>
        <form action = "/app/cargo/manager/getReport" method "GET">
            <label for = "date">Enter arrivalDate: </label><br>
            <input type = "date" name = "arrivalDate" style="width: 12%" required><br><br>
            <label for = "departureCity">Departure City: </label><br>
            <input type = "text" name = "senderCity" pattern="([A-Za-z](-*?)([A-Za-z]?)){4,15}" style="width: 12%" title='Must contain only latin letters' required><br><br>
            <label for = "recipientCity">Recipient City: </label><br>
            <input type = "text" name = "recipientCity" pattern="([A-Za-z](-*?)([A-Za-z]?)){4,15}" style="width: 12%" title='Must contain only latin letters' required><br>
            <input type = "submit" class="btn btn-primary" value = 'Get report'/>
        </form>

        <c:if test = "${reports.size() > 0}">
            <table class="table table-bordered caption-top">
                <caption>Report</caption>
                <tr>
                    <th>Order <br> number <br></th>
                    <th>Client</th>
                    <th>Cargo type</th>
                    <th>Cargo weight (kg)</th>
                    <th>Cargo volume (m³)</th>
                    <th>Delivery from</th>
                    <th>Delivery to</th>
                    <th>Distance (km)</th>
                    <th>Departure date</th>
                    <th>Arrival date</th>
                    <th>Price</th>
                    <th>Confirmation</th>
                    <th>Payment</th>
                </tr>
                <c:forEach items="${reports}" var="report">
                    <tr>
                        <td>${report.order.id}</td>
                        <td>${report.client.login}</td>
                        <td>${report.order.type}</td>
                        <td>${report.order.weight}</td>
                        <td>${report.order.volume}</td>
                        <td>${report.order.delivery.route.senderCity}</td>
                        <td>${report.order.delivery.route.recipientCity}</td>
                        <td>${report.order.delivery.route.distance}</td>
                        <td>${report.order.delivery.departureDate}</td>
                        <td>${report.order.delivery.arrivalDate}</td>
                        <td>${report.order.invoice.price}</td>
                        <td>
                            <c:if test="${report.order.isConfirmed == false}">
                                <h6 style = "color:red">Not confirmed</h6>
                            </c:if>
                            <c:if test="${report.order.isConfirmed == true}">
                                <h6 style = "color:green">Confirmed</h6>
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${report.order.invoice.isPaid == false}">
                                <h6 style = "color:red">Not paid :( </h6>
                            </c:if>
                            <c:if test="${report.order.invoice.isPaid == true}">
                                <h6 style = "color:green">Paid :) </h6>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>


    </body>
</html>
