package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import cargo.delivery.epam.com.project.logic.dao.filtering.PreparerQueryToFiltering;
import cargo.delivery.epam.com.project.logic.dao.filtering.SetterFilteredFieldToPreparedStatement;
import cargo.delivery.epam.com.project.logic.entity.*;
import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReportFilteringDAOTest {
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
    ResultSet resultSet1;
    @Mock
    ResultSet resultSet2;
    @Mock
    ResultSet resultSet3;
    @Mock
    ResultSet resultSet4;
    @Mock
    PreparerQueryToFiltering preparerQuery;
    @Mock
    SetterFilteredFieldToPreparedStatement setterFilteredFieldToPreparedStatement;

    @InjectMocks
    ReportFilteringDAO reportFilteringDAO;

    private static final String GET_CLIENT_BY_ID = "SELECT amount, login FROM user join client on user.id = client.id WHERE client.id = ?";
    private static final String GET_ROUTE_BY_ID = "SELECT * FROM route WHERE id = ?";
    private static final String GET_DELIVERY_BY_ID = "SELECT * FROM delivery where id = ?";
    private static final String GET_ORDER_BY_ID = "select * from orders where id = ?";
    private static final String GET_INVOICE_BY_ID = "SELECT * FROM invoice WHERE id = ?";
    private final FilteringDto filteringDto = new FilteringDto(1L, "login", "type", 10d, 5d, "sender", "recipient", 100d, "2000-01-01", "2000-01-03", 15d, true, true, 1);
    private static final Long USER_ID = 1L;
    private static final Long ORDER_ID_1 = 1L;
    private static final Long ORDER_ID_2 = 2L;
    private static final Long ROUTE_ID = 1L;
    private static final Long DELIVERY_ID = 1L;
    private static final Long INVOICE_ID = 1L;

    @Before
    public void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void filterReportsTest() throws SQLException {
        String sqlQuery = preparerQuery.buildCheckedQueryToFiltering(filteringDto);
        List<Report> expectedList = createExpectedListReports();
        when(connection.prepareStatement(sqlQuery)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet1.getLong("client.id")).thenReturn(USER_ID);
        when(resultSet1.getLong("orders.id")).thenReturn(ORDER_ID_1).thenReturn(ORDER_ID_2);

        List<Report> resultList = reportFilteringDAO.filterReports(filteringDto);
        assertNotNull(resultList);
        assertEquals(expectedList, resultList);

        verify(setterFilteredFieldToPreparedStatement).setFieldsFromDtoToPreparedStatement(preparedStatement1, filteringDto);
    }

    @Test(expected = AppException.class)
    public void filterReportsExceptionTest() throws SQLException {
        String sqlQuery = preparerQuery.buildCheckedQueryToFiltering(filteringDto);
        List<Report> expectedList = new ArrayList<>();
        when(connection.prepareStatement(sqlQuery)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenThrow(AppException.class);

        List<Report> resultList = reportFilteringDAO.filterReports(filteringDto);
        assertNotNull(resultList);
        assertEquals(expectedList, resultList);

        verify(setterFilteredFieldToPreparedStatement).setFieldsFromDtoToPreparedStatement(preparedStatement1, filteringDto);
    }

    @Test
    public void filterReportsEmptyTest() throws SQLException {
        String sqlQuery = preparerQuery.buildCheckedQueryToFiltering(filteringDto);
        List<Report> expectedList = new ArrayList<>();
        when(connection.prepareStatement(sqlQuery)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(false);

        List<Report> resultList = reportFilteringDAO.filterReports(filteringDto);
        assertNotNull(resultList);
        assertEquals(expectedList, resultList);

        verify(setterFilteredFieldToPreparedStatement).setFieldsFromDtoToPreparedStatement(preparedStatement1, filteringDto);
    }

    @Test
    public void getClientByIdTest() throws SQLException {
        Client expectedClient = new Client();
        expectedClient.setId(USER_ID);
        expectedClient.setAmount(10d);
        expectedClient.setLogin("login");

        when(connection.prepareStatement(GET_CLIENT_BY_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true);
        when(resultSet1.getDouble("amount")).thenReturn(expectedClient.getAmount());
        when(resultSet1.getString("login")).thenReturn(expectedClient.getLogin());

        Client resultClient = reportFilteringDAO.getClientById(USER_ID);
        assertNotNull(resultClient);
        assertEquals(expectedClient, resultClient);

        verify(preparedStatement1).setLong(1, USER_ID);
    }

    @Test(expected = AppException.class)
    public void getClientByIdExceptionTest() throws SQLException {
        Client expectedClient = new Client();

        when(connection.prepareStatement(GET_CLIENT_BY_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenThrow(AppException.class);

        Client resultClient = reportFilteringDAO.getClientById(USER_ID);
        assertNotNull(resultClient);
        assertEquals(expectedClient, resultClient);

        verify(preparedStatement1).setLong(1, USER_ID);
    }

    @Test
    public void getClientByIdEmptyTest() throws SQLException {
        Client expectedClient = new Client();

        when(connection.prepareStatement(GET_CLIENT_BY_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(false);

        Client resultClient = reportFilteringDAO.getClientById(USER_ID);
        assertNotNull(resultClient);
        assertEquals(expectedClient, resultClient);

        verify(preparedStatement1).setLong(1, USER_ID);
    }

    @Test
    public void getRouteByIdTest() throws SQLException {
        Route expectedRoute = new Route(ROUTE_ID, 100d, "sender", "recipient");
        when(connection.prepareStatement(GET_ROUTE_BY_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true);
        when(resultSet1.getDouble("distance")).thenReturn(expectedRoute.getDistance());
        when(resultSet1.getString("sender_city")).thenReturn(expectedRoute.getSenderCity());
        when(resultSet1.getString("recipient_city")).thenReturn(expectedRoute.getRecipientCity());

        Route resultRoute = reportFilteringDAO.getRouteById(ROUTE_ID);
        assertNotNull(resultRoute);
        assertEquals(expectedRoute, resultRoute);

        verify(preparedStatement1).setLong(1, ROUTE_ID);
    }

    @Test(expected = AppException.class)
    public void getRouteByIdExceptionTest() throws SQLException {
        Route expectedRoute = new Route();
        when(connection.prepareStatement(GET_ROUTE_BY_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenThrow(AppException.class);

        Route resultRoute = reportFilteringDAO.getRouteById(ROUTE_ID);
        assertNotNull(resultRoute);
        assertEquals(expectedRoute, resultRoute);

        verify(preparedStatement1).setLong(1, ROUTE_ID);
    }

    @Test
    public void getRouteByIdEmptyTest() throws SQLException {
        Route expectedRoute = new Route();
        when(connection.prepareStatement(GET_ROUTE_BY_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(false);

        Route resultRoute = reportFilteringDAO.getRouteById(ROUTE_ID);
        assertNotNull(resultRoute);
        assertEquals(expectedRoute, resultRoute);

        verify(preparedStatement1).setLong(1, ROUTE_ID);
    }

    @Test
    public void getDeliveryByIdTest() throws SQLException {
        Delivery expectedDelivery = new Delivery(DELIVERY_ID, LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 3), new Route(ROUTE_ID, 10d, "seder", "recipient"));
        when(connection.prepareStatement(GET_DELIVERY_BY_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true);
        when(resultSet1.getDate("departure_date")).thenReturn(Date.valueOf(expectedDelivery.getDepartureDate()));
        when(resultSet1.getDate("arrival_date")).thenReturn(Date.valueOf(expectedDelivery.getArrivalDate()));
        when(resultSet1.getLong("route_id")).thenReturn(ROUTE_ID);

        when(connection.prepareStatement(GET_ROUTE_BY_ID)).thenReturn(preparedStatement2);
        when(preparedStatement2.executeQuery()).thenReturn(resultSet2);
        when(resultSet2.next()).thenReturn(true);
        when(resultSet2.getDouble("distance")).thenReturn(expectedDelivery.getRoute().getDistance());
        when(resultSet2.getString("sender_city")).thenReturn(expectedDelivery.getRoute().getSenderCity());
        when(resultSet2.getString("recipient_city")).thenReturn(expectedDelivery.getRoute().getRecipientCity());

        Delivery resultDelivery = reportFilteringDAO.getDeliveryById(DELIVERY_ID);
        assertNotNull(resultDelivery);
        assertEquals(expectedDelivery, resultDelivery);

        verify(preparedStatement1).setLong(1, DELIVERY_ID);
    }

    @Test
    public void getDeliveryByIdWithNullDateTest() throws SQLException {
        Delivery expectedDelivery = new Delivery(DELIVERY_ID, null, null, new Route(ROUTE_ID, 10d, "seder", "recipient"));

        when(connection.prepareStatement(GET_DELIVERY_BY_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true);
        when(resultSet1.getLong("route_id")).thenReturn(ROUTE_ID);

        when(connection.prepareStatement(GET_ROUTE_BY_ID)).thenReturn(preparedStatement2);
        when(preparedStatement2.executeQuery()).thenReturn(resultSet2);
        when(resultSet2.next()).thenReturn(true);
        when(resultSet2.getDouble("distance")).thenReturn(expectedDelivery.getRoute().getDistance());
        when(resultSet2.getString("sender_city")).thenReturn(expectedDelivery.getRoute().getSenderCity());
        when(resultSet2.getString("recipient_city")).thenReturn(expectedDelivery.getRoute().getRecipientCity());

        Delivery resultDelivery = reportFilteringDAO.getDeliveryById(DELIVERY_ID);
        assertNotNull(resultDelivery);
        assertEquals(expectedDelivery, resultDelivery);

        verify(preparedStatement1).setLong(1, DELIVERY_ID);
    }

    @Test
    public void getDeliveryByIdEmptyTest() throws SQLException {
        Delivery expectedDelivery = new Delivery();
        when(connection.prepareStatement(GET_DELIVERY_BY_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(false);

        Delivery resultDelivery = reportFilteringDAO.getDeliveryById(DELIVERY_ID);
        assertNotNull(resultDelivery);
        assertEquals(expectedDelivery, resultDelivery);

        verify(preparedStatement1).setLong(1, DELIVERY_ID);
    }

    @Test(expected = AppException.class)
    public void getDeliveryByIdExceptionTest() throws SQLException {
        Delivery expectedDelivery = new Delivery();
        when(connection.prepareStatement(GET_DELIVERY_BY_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenThrow(AppException.class);

        Delivery resultDelivery = reportFilteringDAO.getDeliveryById(DELIVERY_ID);
        assertNotNull(resultDelivery);
        assertEquals(expectedDelivery, resultDelivery);

        verify(preparedStatement1).setLong(1, DELIVERY_ID);
    }

    @Test
    public void getInvoiceByIdTest()throws SQLException{
        Invoice expectedInvoice = new Invoice(INVOICE_ID,20d,true);

        when(connection.prepareStatement(GET_INVOICE_BY_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true);
        when(resultSet1.getDouble("price")).thenReturn(expectedInvoice.getPrice());
        when(resultSet1.getBoolean("isPaid")).thenReturn(expectedInvoice.getIsPaid());

        Invoice resultInvoice = reportFilteringDAO.getInvoiceById(INVOICE_ID);
        assertNotNull(resultInvoice);
        assertEquals(expectedInvoice, resultInvoice);

        verify(preparedStatement1).setLong(1,INVOICE_ID);
    }

    @Test
    public void getInvoiceByIdEmptyTest()throws SQLException{
        Invoice expectedInvoice = new Invoice();

        when(connection.prepareStatement(GET_INVOICE_BY_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(false);

        Invoice resultInvoice = reportFilteringDAO.getInvoiceById(INVOICE_ID);
        assertNotNull(resultInvoice);
        assertEquals(expectedInvoice, resultInvoice);

        verify(preparedStatement1).setLong(1,INVOICE_ID);
    }

    @Test(expected = AppException.class)
    public void getInvoiceByIdExceptionTest()throws SQLException{
        Invoice expectedInvoice = new Invoice();

        when(connection.prepareStatement(GET_INVOICE_BY_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenThrow(AppException.class);

        Invoice resultInvoice = reportFilteringDAO.getInvoiceById(INVOICE_ID);
        assertNotNull(resultInvoice);
        assertEquals(expectedInvoice, resultInvoice);

        verify(preparedStatement1).setLong(1,INVOICE_ID);
    }

    @Test
    public void getOrderByIdTest() throws SQLException {
        Delivery delivery = new Delivery(DELIVERY_ID, LocalDate.of(2000,1,1), LocalDate.of(2000,1,3), new Route(ROUTE_ID, 10d, "seder", "recipient"));
        Invoice invoice = new Invoice(INVOICE_ID,200d,true);
        Order expectedOrder = new Order(ORDER_ID_1, "type", 10d, 10d, delivery, invoice, true);

        when(connection.prepareStatement(GET_ORDER_BY_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true);
        when(resultSet1.getString("type")).thenReturn(expectedOrder.getType());
        when(resultSet1.getDouble("weight")).thenReturn(expectedOrder.getWeight());
        when(resultSet1.getDouble("volume")).thenReturn(expectedOrder.getVolume());
        when(resultSet1.getBoolean("isConfirmed")).thenReturn(expectedOrder.getIsConfirmed());
        when(resultSet1.getLong("delivery_id")).thenReturn(expectedOrder.getDelivery().getId());
        when(resultSet1.getLong("invoice_id")).thenReturn(expectedOrder.getInvoice().getId());

        when(connection.prepareStatement(GET_ROUTE_BY_ID)).thenReturn(preparedStatement2);
        when(preparedStatement2.executeQuery()).thenReturn(resultSet2);
        when(resultSet2.next()).thenReturn(true);
        when(resultSet2.getDouble("distance")).thenReturn(delivery.getRoute().getDistance());
        when(resultSet2.getString("sender_city")).thenReturn(delivery.getRoute().getSenderCity());
        when(resultSet2.getString("recipient_city")).thenReturn(delivery.getRoute().getRecipientCity());

        when(connection.prepareStatement(GET_DELIVERY_BY_ID)).thenReturn(preparedStatement3);
        when(preparedStatement3.executeQuery()).thenReturn(resultSet3);
        when(resultSet3.next()).thenReturn(true);
        when(resultSet3.getDate("departure_date")).thenReturn(Date.valueOf(delivery.getDepartureDate()));
        when(resultSet3.getDate("arrival_date")).thenReturn(Date.valueOf(delivery.getArrivalDate()));
        when(resultSet3.getLong("route_id")).thenReturn(ROUTE_ID);

        when(connection.prepareStatement(GET_INVOICE_BY_ID)).thenReturn(preparedStatement4);
        when(preparedStatement4.executeQuery()).thenReturn(resultSet4);
        when(resultSet4.next()).thenReturn(true);
        when(resultSet4.getDouble("price")).thenReturn(invoice.getPrice());
        when(resultSet4.getBoolean("isPaid")).thenReturn(invoice.getIsPaid());

        Order resultOrder = reportFilteringDAO.getOrderById(ORDER_ID_1);
        assertNotNull(resultOrder);
        assertEquals(expectedOrder,resultOrder);

        verify(preparedStatement1).setLong(1, ORDER_ID_1);
    }

    @Test
    public void getOrderByIdEmptyTest() throws SQLException {
        Order expectedOrder = new Order();

        when(connection.prepareStatement(GET_ORDER_BY_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(false);

        Order resultOrder = reportFilteringDAO.getOrderById(ORDER_ID_1);
        assertNotNull(resultOrder);
        assertEquals(expectedOrder,resultOrder);

        verify(preparedStatement1).setLong(1, ORDER_ID_1);
    }

    @Test(expected = AppException.class)
    public void getOrderByIdExceptionTest() throws SQLException {
        Order expectedOrder = new Order();

        when(connection.prepareStatement(GET_ORDER_BY_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenThrow(AppException.class);

        Order resultOrder = reportFilteringDAO.getOrderById(ORDER_ID_1);
        assertNotNull(resultOrder);
        assertEquals(expectedOrder,resultOrder);

        verify(preparedStatement1).setLong(1, ORDER_ID_1);
    }

    @Test
    public void getCountOfFilteredRowsTest()throws SQLException{
        Double expectedCount = 10d;
        FilteringDto filteringDto = Mockito.mock(FilteringDto.class);
        String query = preparerQuery.buildCheckedQueryToCountRows(filteringDto);
        when(connection.prepareStatement(query)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true);
        when(resultSet1.getDouble(1)).thenReturn(expectedCount);

        Double resultCount = reportFilteringDAO.getCountFilteredRows(filteringDto);
        assertNotNull(resultCount);
        assertEquals(expectedCount,resultCount);

        verify(filteringDto).setPage(null);
        verify(setterFilteredFieldToPreparedStatement).setFieldsFromDtoToPreparedStatement(preparedStatement1,filteringDto);
    }

    @Test
    public void getCountOfFilteredRowsEmptyTest()throws SQLException{
        Double expectedCount = 0d;
        FilteringDto filteringDto = Mockito.mock(FilteringDto.class);
        String query = preparerQuery.buildCheckedQueryToCountRows(filteringDto);
        when(connection.prepareStatement(query)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(false);

        Double resultCount = reportFilteringDAO.getCountFilteredRows(filteringDto);
        assertNotNull(resultCount);
        assertEquals(expectedCount,resultCount);

        verify(filteringDto).setPage(null);
        verify(setterFilteredFieldToPreparedStatement).setFieldsFromDtoToPreparedStatement(preparedStatement1,filteringDto);
    }

    @Test(expected = AppException.class)
    public void getCountOfFilteredRowsExceptionTest()throws SQLException{
        Double expectedCount = 10d;
        FilteringDto filteringDto = Mockito.mock(FilteringDto.class);
        String query = preparerQuery.buildCheckedQueryToCountRows(filteringDto);
        when(connection.prepareStatement(query)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(resultSet1.next()).thenThrow(AppException.class);

        Double resultCount = reportFilteringDAO.getCountFilteredRows(filteringDto);
        assertNotNull(resultCount);
        assertEquals(expectedCount,resultCount);

        verify(filteringDto).setPage(null);
        verify(setterFilteredFieldToPreparedStatement).setFieldsFromDtoToPreparedStatement(preparedStatement1,filteringDto);
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
