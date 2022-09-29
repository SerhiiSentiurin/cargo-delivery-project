<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <body>
        <head>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
            <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&libraries=places&key=AIzaSyAyM6orVGgY2a7NzZPgLnYm2FmyrqFcQYc"></script>
        </head>
        <div align = "left">
            <button class = "btn btn-primary" onclick="location.href='/app/index.jsp'">Back to registration</button>
            <h2 align = "center">
                Calculate the delivery cost!
            </h2>
        </div>

        <div class="input-group mb-3">
            <label class="input-group-text" for="senderCity">Choose departure city</label>
            <select class="form-select"  name = "senderCity" form = "calculateCost" required>
                <option value="" selected>Choose...</option>
                <c:forEach items="${routeSender}" var="route">
                <option value="${route.senderCity}">${route.senderCity}</option>
                </c:forEach>
            </select>
        </div>

        <div class="input-group mb-3">
            <label class="input-group-text" for="recipientCity">Choose arrival city</label>
            <select class="form-select"  name = "recipientCity" form = "calculateCost" required>
                <option value="" selected>Choose...</option>
                <c:forEach items="${routeRecipient}" var="route">
                <option value="${route.recipientCity}">${route.recipientCity}</option>
                </c:forEach>
            </select>
        </div>

        <table class="table table-striped">
            <td align = "center">
                <form action = "/app/cargo/calculateDelivery" method = "GET" id = "calculateCost">
                    <label for = "Weight" >Weight (kg)</label>
                    <input type = "number" min = "0" max= "5000" step = 0.01 name="weight" id = "Weight" required><br><br>
                    <label for = "Volume">Volume (m³)</label>
                    <input type = "number" min = "0" max= "25" step = 0.01 name="volume" id = "Volume" required><br><br>
                    <input type = "hidden" name = "senderCity" value = "${senderCity}">
                    <input type = "hidden" name = "recipientCity" value = "${recipientCity}">
                    <button type = "submit"  class = "btn btn-secondary">Get cost</button>
                </form>

            </td>
            <td>
                <c:if test = "${order != null}">
                    <div id = "results">
                        <h3>Results: </h4>
                        <h5>Sender city: ${order.senderCity}</h5>
                        <h5>Arrival city: ${order.recipientCity}</h5>
                        <h5>Distance: ${order.distance}</h5>
                        <h5>Delivery cost: ${order.deliveryCost}</h5>

                        <input type = "hidden" id = "from" value = "${order.senderCity}">
                        <input type = "hidden" id = "to" value = "${order.recipientCity}">
                        <button type = "submit" onclick="calcRoutee()" class = "btn btn-secondary">Get route</button>
                    </div>
                </c:if>
            </td>
            <td align = "center">
                <div id="mapa">
                </div>
            </td>
            <td>
                <h5>
                    Some rules how to use this application...
                </h5>
            </td>
        </table>
        <br>




<style>
#mapa {
	height: 550px;
	width: 800px;
	max-width: 800px;
	display:block;
}
</style>
    </body>


<script>
var calcCoordX = 50.4019514; var calcCoordY = 30.3926095;

var directionsDisplayy;
var directionsServicee = new google.maps.DirectionsService();
var geocoder = new google.maps.Geocoder();
var map;

function initialize() {

directionsDisplayy = new google.maps.DirectionsRenderer();
var chicagot = new google.maps.LatLng(calcCoordX, calcCoordY);
  var myOptions = {
    zoom:10,
    mapTypeId: google.maps.MapTypeId.ROADMAP,
    center: chicagot
  }
  map = new google.maps.Map(document.getElementById("mapa"), myOptions);
  directionsDisplayy.setMap(map);
}

function calcRoutee() {
  var from = document.getElementById("from").value;
  var to = document.getElementById("to").value;
  var request = {
    origin:from,
    destination:to,
    travelMode: google.maps.TravelMode.DRIVING
  };
  directionsServicee.route(request, function(response, status) {
    if (status == google.maps.DirectionsStatus.OK) {
      directionsDisplayy.setDirections(response);
      var route = response.routes[0];
    }
  });
}

function addHandler(ev, handler){
	try{
		window.addEventListener(ev, handler, false);
	}catch(e){
		window.attachEvent('on'+ev, handler);
	}
}
addHandler('load', initialize);
</script>

</html>