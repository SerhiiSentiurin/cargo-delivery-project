<html>
    <body>
        <head>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
            <%@ include file="/view/header.jsp" %>
        </head>

        <table>
            <caption>Your orders</caption>
            <tr>
                <th>Cargo type</th>
                <th>Cargo weight (kg)</th>
                <th>Cargo volume (m³)</th>
                <th>Delivery from</th>
                <th>Delivery to</th>
                <th>login</th>
            </tr>
            <c:forEach items="${reports}" var="report">
            <tr>
                <td>${report.order.type}</td>
                <td>${report.order.weight}</td>
                <td>${report.order.volume}</td>
                <td>${report.order.delivery.route.senderCity}</td>
                <td>${report.order.delivery.route.recipientCity}</td>
                <td>${report.client.login}</td>

            </tr>
            </c:forEach>
        </table>
    </body>
</html>
