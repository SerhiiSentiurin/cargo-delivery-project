<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <body>
        <head>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
            <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&libraries=places&key=AIzaSyAyM6orVGgY2a7NzZPgLnYm2FmyrqFcQYc"></script>
            <%@ include file="/view/header.jsp" %>
        </head>

        <div class="input-group mb-3">
            <label class="input-group-text" for="senderCity"><lang:print message = "common.message.choose.departure.city"/></label>
            <select class="form-select"  name = "senderCity" form = "calculateCost" required>
                <option value="" selected><lang:print message = "common.message.choose"/></option>
                <c:forEach items="${routeSender}" var="route">
                <option value="${route.senderCity}">${route.senderCity}</option>
                </c:forEach>
            </select>
        </div>

        <div class="input-group mb-3">
            <label class="input-group-text" for="recipientCity"><lang:print message = "common.message.choose.arrival.city"/></label>
            <select class="form-select"  name = "recipientCity" form = "calculateCost" required>
                <option value="" selected><lang:print message = "common.message.choose"/></option>
                <c:forEach items="${routeRecipient}" var="route">
                <option value="${route.recipientCity}">${route.recipientCity}</option>
                </c:forEach>
            </select>
        </div>

        <div class="input-group mb-3">
            <label class="input-group-text" for="type"><lang:print message = "common.message.choose.cargo.type"/></label>
            <select class="form-select"  name = "type" form = "calculateCost"  required>
                <option value="" selected><lang:print message = "common.message.choose"/></option>
                <option value="metal products"><lang:print message = "common.message.metalwork"/></option>
                <option value="wood products"><lang:print message = "common.message.woodwork"/></option>
                <option value="furniture"><lang:print message = "common.message.furniture"/></option>
                <option value="spare parts for cars"><lang:print message = "common.message.spare.parts.for.cars"/></option>
                <option value="goods in boxes"><lang:print message = "common.message.goods.in.boxes"/></option>
                <option value="goods on pallets"><lang:print message = "common.message.fragile.cargo"/></option>
                <option value="other"><lang:print message = "common.message.other"/></option>
            </select>
        </div>

        <table class="table table-striped">
            <td align = "center">
                <form action = "/app/cargo/client/calculate/delivery" method = "GET" id = "calculateCost">
                    <label for = "Weight" ><lang:print message = "common.message.table.cargo.weight"/> <lang:print message = "common.message.table.(kg)"/></label>
                    <input type = "number" min = "0" max= "5000" step = 0.01 name="weight" id = "Weight" required/><br><br>
                    <label for = "Volume"><lang:print message = "common.message.table.cargo.volume"/> <lang:print message = "common.message.table.(m³)"/></label>
                    <input type = "number" min = "0" max= "25" step = 0.01 name="volume" id = "Volume" required/><br><br>
                    <input type = "hidden" name = "clientId" value = "${user.id}"/>
                    <button type = "submit"  class = "btn btn-secondary"><lang:print message = "getOrder.jsp.get.cost"/></button>
                </form>

            </td>
            <td>
                <c:if test = "${order != null}">
                    <div id = "results">
                        <h3><lang:print message = "common.message.results"/></h4>
                        <h5><lang:print message = "common.message.sender.city"/> ${order.senderCity}</h5>
                        <h5><lang:print message = "common.message.arrival.city"/> ${order.recipientCity}</h5>
                        <h5><lang:print message = "common.message.table.distance"/> ${order.distance} <lang:print message = "common.message.table.(km)"/></h5>
                        <h5><lang:print message = "common.message.delivery.cost"/> ${order.deliveryCost} <lang:print message = "common.message.(uah)"/></h5>
                        <h5><lang:print message = "common.message.table.cargo.type"/> ${order.type}</h5>

                        <input type = "hidden" id = "from" value = "${order.senderCity}"/>
                        <input type = "hidden" id = "to" value = "${order.recipientCity}"/>
                        <button type = "submit" onclick="calcRoutee()" class = "btn btn-secondary"><lang:print message = "common.message.build.a.route"/></button>
                    </div>

                    <br></br>
                    <form action = "/app/cargo/client/create/order" method = "POST" id = "createOrder">
                        <input type = "hidden" name = "clientId" value = "${user.id}" />
                        <input type = "hidden" name = "senderCity" value = "${order.senderCity}"/>
                        <input type = "hidden" name = "recipientCity" value = "${order.recipientCity}"/>
                        <input type = "hidden" name = "distance" value = "${order.distance}"/>
                        <input type = "hidden" name = "deliveryCost" value = "${order.deliveryCost}"/>
                        <input type = "hidden" name = "type" value = "${order.type}"/>
                        <input type = "hidden" name = "weight" value = "${order.weight}" />
                        <input type = "hidden" name = "volume" value = "${order.volume}" />
                        <input type="hidden" name="page" value="1"/>
                        <button type = "submit"  class = "btn btn-primary"><lang:print message = "getOrder.jsp.get.delivery.order"/></button>
                    </form>
                </c:if>
            </td>

            <td align = "right">
                <div id="mapa"> </div>
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