<html>
    <head>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
        <%@ include file="/view/header.jsp" %>
    </head>
    <body>

        <table id="manageWallet" class="table table-bordered" vertical-align = "center" style = "width: 600px ">
            <caption align = "top">
                <h3>Wallet balance: ${client.amount} (UAH)</h3>
            </caption>
            <tr align="center">
                <th>Top up your wallet</th>
                <th>Choose delivery</th>
            </tr>
            <tr>
                <td align="center">
                    <form action ="/app/cargo/client/topUpWallet" method = "POST">
                        <input type = "hidden" name="clientId" value = "${sessionScope.user.id}"/>
                        <input type="number" min = "0" step = 0.01 name="amount" required><br><br>
                        <input type = "submit" class = "btn btn-primary" value ='Top up!'>
                    </form>
                </td>
                <td align="center">
                    <input type = "submit" action ="/app/client/clientHome.jsp" class="btn btn-primary" value = 'Order delivery!'/>
                </td>
            </tr>
        </table><br>

        <div class="card">
            <div class="card-footer text-muted">
                Here you may replenish balance on your wallet!
            </div>
        </div>
    </body>
</html>
