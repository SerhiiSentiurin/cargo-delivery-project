<html>
    <body>
        <head>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
        </head>
        <h2>
            This is manager home page! user id: ${user.id}
        </h2>

        <form action = "/app/cargo/logout" method = "POST">
            <input type = "submit" class="btn btn-primary" value = 'Logout'>
        </form>
    </body>
</html>
