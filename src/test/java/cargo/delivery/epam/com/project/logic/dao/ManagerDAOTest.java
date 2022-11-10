package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.Order;
import cargo.delivery.epam.com.project.logic.entity.Report;
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


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ManagerDAOTest {
    @Mock
    DataSource dataSource;
    @Mock
    Connection connection;
    @Mock
    PreparedStatement preparedStatement;
    @Mock
    Statement statement;
    @Mock
    ResultSet resultSet;

    @InjectMocks
    ManagerDAO managerDAO;

    private static final String GET_COUNT_OF_ROWS_ALL_ORDERS = "select count(*) from report";
    private static final String GET_ALL_ORDERS = "select client_id, order_id from report join orders on report.order_id = orders.id join invoice on orders.invoice_id = invoice.id order by isConfirmed asc, isPaid asc, order_id desc limit ?, 10;";
    private static final String GET_NOT_CONFIRMED_ORDERS = "select client_id, order_id from report join orders on report.order_id = orders.id where orders.isConfirmed = false order by order_id desc";
    private static final String GET_REPORTS_BY_DAY_AND_DIRECTION = "select client_id, order_id from report join orders on report.order_id = orders.id join delivery on orders.delivery_id = delivery.id join route on delivery.route_id = route.id where arrival_date = ? and sender_city = ? and recipient_city = ?";
    private static final String CONFIRM_ORDER = "update orders set isConfirmed = true where id = ?";
    private static final int INDEX = 0;
    private static final Long ORDER_ID_1 = 1L;
    private static final Long ORDER_ID_2 = 2L;
    private static final Long USER_ID = 1L;

    @Before
    public void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void getCountOfRowsAllOrdersTest() throws SQLException {
        when(connection.prepareStatement(GET_COUNT_OF_ROWS_ALL_ORDERS)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getDouble(1)).thenReturn(10d);

        double expectedCountOfRows = 10d;
        double resultCountOfRowsAllOrders = managerDAO.getCountOfRowsAllOrders();
        assertEquals(expectedCountOfRows, resultCountOfRowsAllOrders);
    }

    @Test
    public void getCountOfRowsAllOrdersEmptyTest() throws SQLException {
        when(connection.prepareStatement(GET_COUNT_OF_ROWS_ALL_ORDERS)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        double expectedCountOfRows = 0d;
        double resultCountOfRowsAllOrders = managerDAO.getCountOfRowsAllOrders();
        assertEquals(expectedCountOfRows, resultCountOfRowsAllOrders);
    }

    @Test
    public void getAllOrdersTest() throws SQLException {
        List<Report> expectedList = createExpectedListReports();

        when(connection.prepareStatement(GET_ALL_ORDERS)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("order_id")).thenReturn(ORDER_ID_1).thenReturn(ORDER_ID_2);
        when(resultSet.getLong("client_id")).thenReturn(USER_ID);

        List<Report> resultList = managerDAO.getAllOrders(INDEX);
        assertNotNull(resultList);
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setInt(1, INDEX);
    }

    @Test
    public void getNotConfirmedOrdersTest() throws SQLException {
        List<Report> expectedList = createExpectedListReports();

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(GET_NOT_CONFIRMED_ORDERS)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("order_id")).thenReturn(ORDER_ID_1).thenReturn(ORDER_ID_2);
        when(resultSet.getLong("client_id")).thenReturn(USER_ID);

        List<Report> resultList = managerDAO.getNotConfirmedOrders();
        assertNotNull(resultList);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void getReportsByDayAndDirectionTest() throws SQLException {
        List<Report> expectedList = createExpectedListReports();
        String arrivalDate = "2000-01-01";
        String senderCity = "Kyiv";
        String recipientCity = "Lviv";

        when(connection.prepareStatement(GET_REPORTS_BY_DAY_AND_DIRECTION)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("client_id")).thenReturn(USER_ID);
        when(resultSet.getLong("order_id")).thenReturn(ORDER_ID_1).thenReturn(ORDER_ID_2);

        List<Report> resultList = managerDAO.getReportByDayAndDirection(arrivalDate, senderCity, recipientCity);
        assertNotNull(resultList);
        assertEquals(expectedList, resultList);

        verify(preparedStatement).setDate(1, Date.valueOf(arrivalDate));
        verify(preparedStatement).setString(2, senderCity);
        verify(preparedStatement).setString(3, recipientCity);
    }

    @Test
    public void confirmOrderTest() throws SQLException {
        when(connection.prepareStatement(CONFIRM_ORDER)).thenReturn(preparedStatement);
        managerDAO.confirmOrder(ORDER_ID_1);
        verify(preparedStatement).setLong(1, ORDER_ID_1);
        verify(preparedStatement).execute();
    }

    private static List<Report> createExpectedListReports() {
        List<Report> expectedList = new ArrayList<>();
        Client client = new Client();
        client.setId(USER_ID);
        Order order1 = new Order();
        Order order2 = new Order();
        order1.setId(ORDER_ID_1);
        order2.setId(ORDER_ID_2);
        Report report1 = new Report(client, order1);
        Report report2 = new Report(client, order2);
        expectedList.add(report1);
        expectedList.add(report2);
        return expectedList;
    }
}
