<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
        <%@ include file="/view/header.jsp" %>
    </head>
    <body>
        <h2>
            Hello, ${sessionScope.user.login}
        </h2>

        <div>
            <img src="https://dnepr-more.com/wp-content/uploads/2018/02/gruzoperevozki.jpg" alt="cargo" style="float:left; margin-right:10px;" />
            <h1>About Cargo Application</h1>
            <p class="lh-lg" style="margin-left: 20px;">
                <h5>This is your home page.</h5><br>
                    At the top of the page, you can find the sections:<br>
                    &#34;<u>place an order for delivery</u>&#34; - here you can specify the necessary data to calculate the delivery cost,
                    build a route, and leave a delivery request. After that, our managers will process the order and become available for payment.
                    The shipping cost depends on the cargo&#8216;s distance, weight, and volume.<br>
                    &#34;<u>my orders</u>&#34; - here are all your orders. With the help of filters, you can find any order. A confirmed order is available for payment,
                    and by clicking the &#34;get a receipt&#34; button, you can pay for it by selecting the desired delivery date (delivery time takes two days
                    from the date of order).<br>
                    &#34;<u>my wallet</u>&#34; - here you can see the balance on your account and replenish it.
            </p>
        </div>

    <%@ include file="/view/footer.jsp" %>

    </body>
</html>
