package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import cargo.delivery.epam.com.project.logic.entity.Route;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientOrderDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderDAOTest {
    @Mock
    DataSource dataSource;
    @Mock
    Connection connection;
    @Mock
    PreparedStatement preparedStatement1;
    @Mock
    PreparedStatement preparedStatement2;
    @Mock
    PreparedStatement preparedStatement3;
    @Mock
    PreparedStatement preparedStatement4;
    @Mock
    Statement statement;
    @Mock
    ResultSet resultSet1;
    @Mock
    ResultSet resultSet2;
    @Mock
    ResultSet resultSet3;

    @InjectMocks
    OrderDAO orderDAO;

    private static final String GET_ROUTE = "SELECT * FROM route WHERE sender_city = ? AND recipient_city = ?";
    private static final String GET_DISTINCT_SENDER_CITIES = "select distinct sender_city from route";
    private static final String GET_DISTINCT_RECIPIENT_CITIES = "select distinct recipient_city from route";
    private static final String INSERT_INTO_INVOICE = "insert into invoice (price) value (?)";
    private static final String INSERT_INTO_DELIVERY = "insert into delivery (route_id) value ((select id from route where sender_city = ? and recipient_city = ?))";
    private static final String INSERT_INTO_ORDER = "insert into orders (type, weight, volume, delivery_id, invoice_id) values (?, ?, ?, ?, ?)";
    private static final String INSERT_INTO_REPORT = "insert into report (client_id, order_id) values(?, ?)";
    private static final Long DELIVERY_ID = 1L;
    private static final Long INVOICE_ID = 1L;
    private static final Long ORDER_ID = 1L;
    private final Route route1 = new Route();
    private final Route route2 = new Route();
    private static final ClientOrderDto clientDto = new ClientOrderDto(1L, "type", 10d, 5d, "sender", "recipient", 100d, 10d);


    @Before
    public void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void GetRouteTest() throws SQLException {
        route1.setId(1L);
        route1.setDistance(10d);
        route1.setSenderCity("sender");
        route1.setRecipientCity("recipient");

        when(connection.prepareStatement(GET_ROUTE)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true);
        when(resultSet1.getLong("id")).thenReturn(route1.getId());
        when(resultSet1.getDouble("distance")).thenReturn(route1.getDistance());
        when(resultSet1.getString("sender_city")).thenReturn(route1.getSenderCity());
        when(resultSet1.getString("recipient_city")).thenReturn(route1.getRecipientCity());

        Route resultRoute = orderDAO.getRoute("sender", "recipient");
        assertNotNull(resultRoute);
        assertEquals(route1, resultRoute);

        verify(preparedStatement1).setString(1, route1.getSenderCity());
        verify(preparedStatement1).setString(2, route1.getRecipientCity());
    }

    @Test
    public void getDistinctSenderCitiesTest() throws SQLException {
        List<Route> expectedList = new ArrayList<>();
        route1.setSenderCity("sender1");
        route2.setSenderCity("sender2");
        expectedList.add(route1);
        expectedList.add(route2);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(GET_DISTINCT_SENDER_CITIES)).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet1.getString("sender_city")).thenReturn(route1.getSenderCity()).thenReturn(route2.getSenderCity());

        List<Route> resultList = orderDAO.getDistinctSenderCities();
        assertNotNull(resultList);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void getDistinctRecipientCitiesTest() throws SQLException {
        List<Route> expectedList = new ArrayList<>();
        route1.setRecipientCity("recipient1");
        route2.setRecipientCity("recipient2");
        expectedList.add(route1);
        expectedList.add(route2);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(GET_DISTINCT_RECIPIENT_CITIES)).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet1.getString("recipient_city")).thenReturn(route1.getRecipientCity()).thenReturn(route2.getRecipientCity());

        List<Route> resultList = orderDAO.getDistinctRecipientCities();
        assertNotNull(resultList);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void getDistinctSenderCitiesEmptyTest() throws SQLException {
        List<Route> expectedList = new ArrayList<>();

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(GET_DISTINCT_SENDER_CITIES)).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(false);

        List<Route> resultList = orderDAO.getDistinctSenderCities();
        assertNotNull(resultList);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void getDistinctRecipientCitiesEmptyTest() throws SQLException {
        List<Route> expectedList = new ArrayList<>();

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(GET_DISTINCT_RECIPIENT_CITIES)).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(false);

        List<Route> resultList = orderDAO.getDistinctRecipientCities();
        assertNotNull(resultList);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void createOrderWhenTransactionCommitTest() throws SQLException {
        when(connection.prepareStatement(INSERT_INTO_INVOICE, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement1);
        when(preparedStatement1.getGeneratedKeys()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true);
        when(resultSet1.getLong(1)).thenReturn(INVOICE_ID);

        when(connection.prepareStatement(INSERT_INTO_DELIVERY, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement2);
        when(preparedStatement2.getGeneratedKeys()).thenReturn(resultSet2);
        when(resultSet2.next()).thenReturn(true);
        when(resultSet2.getLong(1)).thenReturn(DELIVERY_ID);

        when(connection.prepareStatement(INSERT_INTO_ORDER, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement3);
        when(preparedStatement3.getGeneratedKeys()).thenReturn(resultSet3);
        when(resultSet3.next()).thenReturn(true);
        when(resultSet3.getLong(1)).thenReturn(ORDER_ID);

        when(connection.prepareStatement(INSERT_INTO_REPORT)).thenReturn(preparedStatement4);

        orderDAO.createOrder(clientDto);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement1).setDouble(1, clientDto.getDeliveryCost());
        verify(preparedStatement1).execute();
        verify(preparedStatement2).setString(1, clientDto.getSenderCity());
        verify(preparedStatement2).setString(2, clientDto.getRecipientCity());
        verify(preparedStatement2).execute();
        verify(preparedStatement3).setString(1, clientDto.getType());
        verify(preparedStatement3).setDouble(2, clientDto.getWeight());
        verify(preparedStatement3).setDouble(3, clientDto.getVolume());
        verify(preparedStatement3).setLong(4, DELIVERY_ID);
        verify(preparedStatement3).setLong(5, INVOICE_ID);
        verify(preparedStatement3).execute();
        verify(preparedStatement4).setLong(1, clientDto.getClientId());
        verify(preparedStatement4).setLong(2, ORDER_ID);
        verify(preparedStatement4).execute();
        verify(connection).commit();
        verify(resultSet1).close();
        verify(resultSet2).close();
        verify(resultSet3).close();
        verify(preparedStatement1).close();
        verify(preparedStatement2).close();
        verify(preparedStatement3).close();
        verify(preparedStatement4).close();
        verify(connection).close();
    }

    @Test(expected = AppException.class)
    public void createOrderWhenTransactionRollbackOnInsertIntoInvoiceTest() throws SQLException {
        when(connection.prepareStatement(INSERT_INTO_INVOICE, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement1);
        when(preparedStatement1.getGeneratedKeys()).thenReturn(resultSet1);
        when(resultSet1.next()).thenThrow(AppException.class);

        orderDAO.createOrder(clientDto);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement1).setDouble(1, clientDto.getDeliveryCost());
        verify(preparedStatement1).execute();
        verify(connection).rollback();
        verify(resultSet1).close();
        verify(preparedStatement1).close();
        verify(connection).close();
    }

    @Test(expected = AppException.class)
    public void createOrderWhenTransactionRollbackOnInsertIntoDeliveryTest() throws SQLException {
        when(connection.prepareStatement(INSERT_INTO_INVOICE, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement1);
        when(preparedStatement1.getGeneratedKeys()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true);
        when(resultSet1.getLong(1)).thenReturn(INVOICE_ID);

        when(connection.prepareStatement(INSERT_INTO_DELIVERY, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement2);
        when(preparedStatement2.getGeneratedKeys()).thenReturn(resultSet2);
        when(resultSet2.next()).thenThrow(AppException.class);

        orderDAO.createOrder(clientDto);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement1).setDouble(1, clientDto.getDeliveryCost());
        verify(preparedStatement1).execute();
        verify(preparedStatement2).setString(1, clientDto.getSenderCity());
        verify(preparedStatement2).setString(2, clientDto.getRecipientCity());
        verify(preparedStatement2).execute();
        verify(connection).rollback();
        verify(resultSet1).close();
        verify(resultSet2).close();
        verify(preparedStatement1).close();
        verify(preparedStatement2).close();
        verify(connection).close();
    }

    @Test(expected = AppException.class)
    public void createOrderWhenTransactionRollbackOnInsertIntoOrderTest() throws SQLException {
        when(connection.prepareStatement(INSERT_INTO_INVOICE, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement1);
        when(preparedStatement1.getGeneratedKeys()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true);
        when(resultSet1.getLong(1)).thenReturn(INVOICE_ID);

        when(connection.prepareStatement(INSERT_INTO_DELIVERY, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement2);
        when(preparedStatement2.getGeneratedKeys()).thenReturn(resultSet2);
        when(resultSet2.next()).thenReturn(true);
        when(resultSet2.getLong(1)).thenReturn(DELIVERY_ID);

        when(connection.prepareStatement(INSERT_INTO_ORDER, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement3);
        when(preparedStatement3.getGeneratedKeys()).thenReturn(resultSet3);
        when(resultSet3.next()).thenThrow(AppException.class);

        orderDAO.createOrder(clientDto);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement1).setDouble(1, clientDto.getDeliveryCost());
        verify(preparedStatement1).execute();
        verify(preparedStatement2).setString(1, clientDto.getSenderCity());
        verify(preparedStatement2).setString(2, clientDto.getRecipientCity());
        verify(preparedStatement2).execute();
        verify(preparedStatement3).setString(1, clientDto.getType());
        verify(preparedStatement3).setDouble(2, clientDto.getWeight());
        verify(preparedStatement3).setDouble(3, clientDto.getVolume());
        verify(preparedStatement3).setLong(4, DELIVERY_ID);
        verify(preparedStatement3).setLong(5, INVOICE_ID);
        verify(preparedStatement3).execute();
        verify(connection).rollback();
        verify(resultSet1).close();
        verify(resultSet2).close();
        verify(resultSet3).close();
        verify(preparedStatement1).close();
        verify(preparedStatement2).close();
        verify(preparedStatement3).close();
        verify(connection).close();
    }

    @Test(expected = AppException.class)
    public void createOrderWhenTransactionRollbackOnInsertIntoReportTest() throws SQLException {
        when(connection.prepareStatement(INSERT_INTO_INVOICE, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement1);
        when(preparedStatement1.getGeneratedKeys()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true);
        when(resultSet1.getLong(1)).thenReturn(INVOICE_ID);

        when(connection.prepareStatement(INSERT_INTO_DELIVERY, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement2);
        when(preparedStatement2.getGeneratedKeys()).thenReturn(resultSet2);
        when(resultSet2.next()).thenReturn(true);
        when(resultSet2.getLong(1)).thenReturn(DELIVERY_ID);

        when(connection.prepareStatement(INSERT_INTO_ORDER, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement3);
        when(preparedStatement3.getGeneratedKeys()).thenReturn(resultSet3);
        when(resultSet3.next()).thenReturn(true);
        when(resultSet3.getLong(1)).thenReturn(ORDER_ID);

        when(connection.prepareStatement(INSERT_INTO_REPORT)).thenThrow(AppException.class);

        orderDAO.createOrder(clientDto);

        verify(connection).setAutoCommit(false);
        verify(preparedStatement1).setDouble(1, clientDto.getDeliveryCost());
        verify(preparedStatement1).execute();
        verify(preparedStatement2).setString(1, clientDto.getSenderCity());
        verify(preparedStatement2).setString(2, clientDto.getRecipientCity());
        verify(preparedStatement2).execute();
        verify(preparedStatement3).setString(1, clientDto.getType());
        verify(preparedStatement3).setDouble(2, clientDto.getWeight());
        verify(preparedStatement3).setDouble(3, clientDto.getVolume());
        verify(preparedStatement3).setLong(4, DELIVERY_ID);
        verify(preparedStatement3).setLong(5, INVOICE_ID);
        verify(preparedStatement3).execute();
        verify(preparedStatement4).setLong(1, clientDto.getClientId());
        verify(preparedStatement4).setLong(2, ORDER_ID);
        verify(preparedStatement4).execute();
        verify(connection).rollback();
        verify(resultSet1).close();
        verify(resultSet2).close();
        verify(resultSet3).close();
        verify(preparedStatement1).close();
        verify(preparedStatement2).close();
        verify(preparedStatement3).close();
        verify(preparedStatement4).close();
        verify(connection).close();
    }

}
