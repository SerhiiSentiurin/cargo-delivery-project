package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.logic.entity.Route;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OrderDAO {
    private final DataSource dataSource;

    @SneakyThrows
    public Route getRoute(String senderCity, String recipientCity) {
        String sql = "SELECT * FROM route WHERE sender_city = ? AND recipient_city = ?";
        Route route = new Route();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, senderCity);
            preparedStatement.setString(2, recipientCity);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                route.setId(resultSet.getLong("id"));
                route.setDistance(resultSet.getDouble("distance"));
                route.setSenderCity(resultSet.getString("sender_city"));
                route.setRecipientCity(resultSet.getString("recipient_city"));
            }
        }
        return route;
    }

//    @SneakyThrows
//    public List<Route> getAllRoutes() {
//        String sql = "SELECT * FROM route";
//        List<Route> routesList = new ArrayList<>();
//        try (Connection connection = dataSource.getConnection();
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(sql);) {
//            while (resultSet.next()) {
//                 long id = resultSet.getLong("id");
//                 double distance = resultSet.getDouble("distance");
//                 String senderCity = resultSet.getString("sender_city");
//                 String recipientCity = resultSet.getString("recipient_city");
//                 Route route = new Route(id,distance,senderCity,recipientCity);
//                 routesList.add(route);
//            }
//        }
//        return routesList;
//    }

    @SneakyThrows
    public List<Route> getDistinctSenderCities(){
        String sql = "select distinct sender_city from route";
        List<Route> routesList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql);) {
            while (resultSet.next()) {
                String senderCity = resultSet.getString("sender_city");
                Route route = new Route();
                route.setSenderCity(senderCity);
                routesList.add(route);
            }
        }
        return routesList;
    }

    @SneakyThrows
    public List<Route> getDistinctRecipientCities(){
        String sql = "select distinct recipient_city from route";
        List<Route> routesList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql);) {
            while (resultSet.next()) {
                String recipientCity = resultSet.getString("recipient_city");
                Route route = new Route();
                route.setRecipientCity(recipientCity);
                routesList.add(route);
            }
        }
        return routesList;
    }

    @SneakyThrows
    public void createOrder(ClientOrderDto dto){

    }
}
