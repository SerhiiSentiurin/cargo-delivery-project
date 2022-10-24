<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <body>
        <head>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
            <%@ include file="/view/header.jsp" %>
        </head>
        <table class="table table-bordered caption-top">
            <caption>
                <form action = "/app/cargo/manager/getAllOrders/filter" method = "GET" id = "filter">
                    <input type="hidden" name="page" value="1"/>
                    <button type = "submit"  class="btn btn-dark btn-sm"><lang:print message = "common.message.button.find.order"/></button>
                </form>
            </caption>
            <tr>
                <th>
                        <input type = "number" name = "orderId" min= "1" size = "3" form = "filter" /><br>
                </th>
                <th>
                        <input type = "text" name = "login" size = "5" form = "filter" /><br>
                </th>
                <th>
                        <input type = "text" name = "type" size = "12" form ="filter" pattern = "([a-zA-Z ])*" title='<lang:print message = "common.message.title.must.contain.only.latin.letters"/>' /><br>
                </th>
                <th>
                        <input type = "number" name = "weight" min= "1" step = "0.1" size = "12" form = "filter" /><br>
                </th>
                <th>
                        <input type = "number" name = "volume" step = "0.1" min= "1" size = "12" form = "filter"  /><br>
                </th>
                <th>
                        <input type = "text" name = "senderCity" size = "8" form = "filter" pattern = "([a-zA-Z ])*" title='<lang:print message = "common.message.title.must.contain.only.latin.letters"/>' /><br>
                </th>
                <th>
                        <input type = "text" name = "recipientCity" size = "8" form = "filter" pattern = "([a-zA-Z ])*" title='<lang:print message = "common.message.title.must.contain.only.latin.letters"/>' /><br>
                </th>
                <th>
                        <input type = "number" name = "distance" min= "1" step = "0.1" size = "8" form = "filter" /><br>
                </th>
                <th>
                        <input type = "text" name = "departureDate" size = "10" form = "filter" pattern = "^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$" title='<lang:print message = "common.message.title.format.expected.yyyy.mm.dd"/>' /><br>
                </th>
                <th>
                        <input type = "text" name = "arrivalDate" size = "10" form = "filter" pattern = "^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$" title='<lang:print message = "common.message.title.format.expected.yyyy.mm.dd"/>' /><br>
                </th>
                <th>
                        <input type = "number" name = "price" size = "5" min= "0" step = "0.1" form = "filter" /><br>
                </th>
                <th>
                        <select name = "isConfirmed" form = "filter" >
                            <option value="" selected>Choose...</option>
                            <option value="true"><lang:print message = "common.message.table.confirmed"/></option>
                            <option value="false"><lang:print message = "common.message.table.not.confirmed"/></option>
                        </select>
                </th>
                <th>
                        <select name = "isPaid" form = "filter" >
                            <option value="" selected>Choose...</option>
                            <option value='true'><lang:print message = "common.message.table.paid"/></option>
                            <option value='false'><lang:print message = "common.message.table.not.paid"/></option>
                        </select>
                </th>
            </tr>
            <tr>
                <th><lang:print message = "common.message.table.order"/><br><lang:print message = "common.message.table.number"/><br></th>
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
            <c:if test = "${reports.size() > 0}">
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
            </c:if>
        </table>

            <div class="d-flex justify-content-center">
                <c:if test = "${!empty paginationLinks}">
                <c:set var="count" value="0" scope="page" />
                  <c:forEach var = "link" items="${paginationLinks}">
                  <c:set var="count" value="${count + 1}" scope="page"/>
                  <ul>
                     <a href="${link}">${count}</a>
                  </ul>
                  </c:forEach>
                </c:if>
            </div>
    </body>
</html>
