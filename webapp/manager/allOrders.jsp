<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <body>
        <head>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
            <%@ include file="/view/header.jsp" %>
        </head>

        <table class="table table-bordered caption-top">
            <caption>
                <form action = "/app/cargo/manager/getAllOrders/select" method = "GET" id = "sort">
                    <button type = "submit"  class="btn btn-dark btn-sm">Find Order</button>
                </form>
            </caption>
            <tr>
                <th>
                        <input type = "number" name = "orderId" min= "1" size = "3" form = "sort" /><br>
                </th>
                <th>
                        <input type = "text" name = "login" size = "5" form = "sort" /><br>
                </th>
                <th>
                        <input type = "text" name = "type" size = "12" form ="sort" /><br>
                </th>
                <th>
                        <input type = "number" name = "weight" min= "1" size = "12" form = "sort" /><br>
                </th>
                <th>
                        <input type = "number" name = "volume" min= "1" size = "12" form = "sort"  /><br>
                </th>
                <th>
                        <input type = "text" name = "senderCity" size = "8" form = "sort" /><br>
                </th>
                <th>
                        <input type = "text" name = "recipientCity" size = "8" form = "sort" /><br>
                </th>
                <th>
                        <input type = "number" name = "distance" size = "8" form = "sort" /><br>
                </th>
                <th>
                        <input type = "text" name = "departureDate" size = "10" form = "sort" /><br>
                </th>
                <th>
                        <input type = "text" name = "arrivalDate" size = "10" form = "sort" /><br>
                </th>
                <th>
                        <input type = "number" name = "price" size = "5" min= "0" form = "sort" /><br>
                </th>
                <th>
                        <select name = "isConfirmed" form = "sort" >
                            <option value="" selected>Choose...</option>
                            <option value="true">Confirmed</option>
                            <option value="false">Not confirmed</option>
                        </select>
                </th>
                <th>
                        <select name = "isPaid" form = "sort" >
                            <option value="" selected>Choose...</option>
                            <option value='true'>Paid</option>
                            <option value='false'>Not paid</option>
                        </select>
                </th>
            </tr>
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
    </body>
</html>