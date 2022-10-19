<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <body>
        <head>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
            <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&libraries=places&key=AIzaSyAyM6orVGgY2a7NzZPgLnYm2FmyrqFcQYc"></script>
            <%@ include file="/view/header.jsp" %>
        </head>

        <c:if test = "${reports.size() == 0}">
            <h3>Sorry, you have not orders yet :( </h3>
        </c:if>

        <c:if test = "${reports.size() > 0}">

            <table class="table caption-top">
                <caption>Orders</caption>
                <tr>
                    <th>
                        <input type = "number" name = "orderId" min= "1" size = "3" form = "filter" /><br>
                    </th>
                    <th>
                        <input type = "text" name = "type" size = "12" form ="filter" pattern = "([a-zA-Z ])*" title='Must contain only latin letters' /><br>
                    </th>
                    <th>
                        <input type = "number" name = "weight" min= "1" step = "0.1" size = "12" form = "filter" /><br>
                    </th>
                    <th>
                        <input type = "number" name = "volume" step = "0.1" min= "1" size = "12" form = "filter"  /><br>
                    </th>
                    <th>
                        <input type = "text" name = "senderCity" size = "8" form = "filter" pattern = "([a-zA-Z ])*" title='Must contain only latin letters' /><br>
                    </th>
                    <th>
                        <input type = "text" name = "recipientCity" size = "8" form = "filter" pattern = "([a-zA-Z ])*" title='Must contain only latin letters' /><br>
                    </th>
                    <th>
                        <input type = "number" name = "distance" min= "1" step = "0.1" size = "8" form = "filter" /><br>
                    </th>
                    <th>
                        <input type = "text" name = "departureDate" size = "10" form = "filter" pattern = "^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$" title='Format expected YYYY-MM-DD' /><br>
                    </th>
                    <th>
                        <input type = "text" name = "arrivalDate" size = "10" form = "filter" pattern = "^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$" title='Format expected YYYY-MM-DD' /><br>
                    </th>
                    <th>
                        <input type = "number" name = "price" size = "5" min= "0" step = "0.1" form = "filter" /><br>
                    </th>
                    <th>
                        <select name = "isConfirmed" form = "filter" >
                            <option value="" selected>Choose...</option>
                            <option value="true">Confirmed</option>
                            <option value="false">Not confirmed</option>
                        </select>
                    </th>
                    <th>
                        <select name = "isPaid" form = "filter" >
                            <option value="" selected>Choose...</option>
                            <option value='true'>Paid</option>
                            <option value='false'>Not paid</option>
                        </select>
                    </th>
                    <th>
                        <form action = "/app/cargo/client/getAllOrders/filter" method = "GET" id = "filter">
                            <button type = "submit"  class="btn btn-dark btn-sm">Find Order</button>
                            <input type = "hidden" name = "clientId" value = "${sessionScope.user.id}">
                        </form>
                    </th>
                </tr>
                <tr>
                    <th>Order <br> number</th>
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
                <c:if test = "${reports.size() > 0}">
                    <c:forEach items="${reports}" var="report">
                        <tr>
                            <td>${report.order.id}</td>
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
                            <c:if test = "${report.order.isConfirmed == true}">
                                <c:if test= "${report.order.invoice.isPaid == false}">
                                    <td>
                                        <form action ="/app/cargo/client/getInvoice" method = "GET">
                                            <input type = "hidden" name = "clientId" value = "${sessionScope.user.id}" />
                                            <input type = "hidden" name = "orderId" value = "${report.order.id}" />
                                            <button type = "submit"  class = "btn btn-secondary">Get invoice</button>
                                        </form>
                                    </td>
                                </c:if>
                            </c:if>

                            <c:if test = "${report.order.isConfirmed == false}">
                                <c:if test = "${report.order.invoice.isPaid == false}">
                                    <td></td>
                                </c:if>
                            </c:if>
                        </tr>
                    </c:forEach>
                </c:if>
            </table>
        </c:if>
    </body>
</html>
