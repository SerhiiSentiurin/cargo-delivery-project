<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <body>
        <head>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
            <%@ include file="/view/header.jsp" %>
        </head>

        <c:if test = "${reports.size() == 0}">
            <h3> <lang:print message = "manageOrders.jsp.all.orders.confirmed"/></h3>
        </c:if>

        <c:if test = "${reports.size() > 0}">
            <table class="table table-bordered caption-top" >
                <caption><lang:print message = "manageOrders.jsp.manage.orders"/></caption>
                <tr valign = "middle" align = "center">
                    <th><lang:print message = "common.message.table.order"/><br><lang:print message = "common.message.table.number"/></th>
                    <th><lang:print message = "common.message.table.client"/></th>
                    <th><lang:print message = "common.message.table.cargo.type"/></th>
                    <th><lang:print message = "common.message.table.cargo.weight"/> <lang:print message = "common.message.table.(kg)"/></th>
                    <th><lang:print message = "common.message.table.cargo.volume"/> <lang:print message = "common.message.table.(m³)"/></th>
                    <th><lang:print message = "common.message.table.distance"/> <lang:print message = "common.message.table.(km)"/></th>
                    <th><lang:print message = "common.message.table.delivery.from"/></th>
                    <th><lang:print message = "common.message.table.delivery.to"/></th>
                    <th><lang:print message = "common.message.table.price"/> <lang:print message = "common.message.(uah)"/></th>
                    <th><lang:print message = "common.message.table.status"/></th>
                    <th><lang:print message = "manageOrders.jsp.confirm.order.and"/><br><lang:print message = "manageOrders.jsp.send.invoice"/></th>
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
                                <h6 style = "color:red"><lang:print message = "common.message.table.not.confirmed"/></h6>
                            </c:if>
                            <c:if test="${report.order.isConfirmed == true}">
                                <h6 style = "color:green"><lang:print message = "common.message.table.confirmed"/></h6>
                            </c:if>
                        </td>
                        <c:if test = "${report.order.isConfirmed == false}">
                            <td>
                                <form action = "/app/cargo/manager/orders/confirm" method = "POST">
                                    <input type = "hidden" name = "orderId" value = "${report.order.id}" />
                                    <button type = "submit"  class="btn btn-success btn-sm"><lang:print message = "manageOrders.jsp.confirm"/></button>
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
