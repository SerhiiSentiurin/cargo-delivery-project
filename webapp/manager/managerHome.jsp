<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <body>
        <head>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
            <%@ include file="/view/header.jsp" %>
        </head>
        <h2>
            <lang:print message = "managerHome.jsp.this.is.manager.home.page"/> ${sessionScope.user.login}!
        </h2>

        <br>
        <form action = "/app/cargo/manager/getReport" method "GET">
            <label for = "date"><lang:print message = "managerHome.jsp.enter.arrivalDate"/></label><br>
            <input type = "date" name = "arrivalDate" style="width: 12%" required><br><br>
            <label for = "departureCity"><lang:print message = "managerHome.jsp.departure.city"/></label><br>
            <input type = "text" name = "senderCity" pattern="([A-Za-z](-*?)([A-Za-z]?)){4,15}" style="width: 12%" title='<lang:print message = "common.message.title.must.contain.only.latin.letters"/>' required><br><br>
            <label for = "recipientCity"><lang:print message = "managerHome.jsp.recipient.city"/> </label><br>
            <input type = "text" name = "recipientCity" pattern="([A-Za-z](-*?)([A-Za-z]?)){4,15}" style="width: 12%" title='<lang:print message = "common.message.title.must.contain.only.latin.letters"/>' required><br>
            <input type = "submit" class="btn btn-primary" value = '<lang:print message = "managerHome.jsp.get.report"/>'/>
        </form>

        <c:if test = "${reports.size() > 0}">
            <table class="table table-bordered caption-top">
                <caption><lang:print message = "managerHome.jsp.report"/></caption>
                <tr>
                    <th><lang:print message = "common.message.table.order"/> <br> <lang:print message = "common.message.table.number"/><br></th>
                    <th><lang:print message = "common.message.table.client"/></th>
                    <th><lang:print message = "common.message.table.cargo.type"/></th>
                    <th><lang:print message = "common.message.table.cargo.weight"/> <lang:print message = "common.message.table.(kg)"/></th>
                    <th><lang:print message = "common.message.table.cargo.volume"/> <lang:print message = "common.message.table.(m³)"/></th>
                    <th><lang:print message = "common.message.table.delivery.from"/></th>
                    <th><lang:print message = "common.message.table.delivery.to"/></th>
                    <th><lang:print message = "common.message.table.distance"/> <lang:print message = "common.message.table.(km)"/></th>
                    <th><lang:print message = "common.message.table.departure.date"/></th>
                    <th><lang:print message = "common.message.table.arrival.date"/></th>
                    <th><lang:print message = "common.message.table.price"/> <lang:print message = "common.message.(uah)"/></th>
                    <th><lang:print message = "common.message.table.confirmation"/></th>
                    <th><lang:print message = "common.message.table.payment"/></th>
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
                                <h6 style = "color:red"><lang:print message = "common.message.table.not.confirmed"/></h6>
                            </c:if>
                            <c:if test="${report.order.isConfirmed == true}">
                                <h6 style = "color:green"><lang:print message = "common.message.table.confirmed"/></h6>
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${report.order.invoice.isPaid == false}">
                                <h6 style = "color:red"><lang:print message = "common.message.table.not.paid"/></h6>
                            </c:if>
                            <c:if test="${report.order.invoice.isPaid == true}">
                                <h6 style = "color:green"><lang:print message = "common.message.table.paid"/></h6>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </body>
</html>
