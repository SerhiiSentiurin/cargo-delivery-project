package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import cargo.delivery.epam.com.project.logic.entity.*;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientCreateDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientDAOTest {
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
    ResultSet resultSet;

    @InjectMocks
    ClientDAO clientDAO;

    private static final String INSERT_INTO_USER = "INSERT INTO user (login, password, role) VALUES (?,?,?)";
    private static final String INSERT_INTO_CLIENT = "INSERT INTO client (id, amount) VALUES(?, 0)";
    private static final String TOP_UP_CLIENT_WALLET = "UPDATE client SET amount = ? where id = ?";
    private static final String GET_REPORTS_BY_CLIENT_ID = "select client_id, order_id from report join orders on report.order_id=orders.id join invoice on orders.invoice_id=invoice.id where client_id = ? order by isConfirmed asc, isPaid asc, order_id desc limit ?, 10";
    private static final String UPDATE_DELIVERY = "update delivery join orders on delivery.id = orders.delivery_id set departure_date = ?, arrival_date = ? where orders.id = ?";
    private static final String UPDATE_INVOICE = "update invoice join orders on invoice.id = orders.invoice_id set isPaid=true where orders.id = ?";
    private static final String UPDATE_CLIENT = "update client set amount = ? where id = ?";
    private static final String GET_COUNT_OF_ROWS_ALL_CLIENT_ORDERS = "select count(*) from report where client_id = ?";
    private static final Long USER_ID = 1L;
    private static final Long ORDER_ID_1 = 1L;
    private static final Long ORDER_ID_2 = 2L;
    private static final ClientCreateDto dto = new ClientCreateDto(USER_ID, "login", "password");
    private static final int INDEX = 0;

    @Before
    public void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void insertIntoUserTest() throws SQLException {
        when(connection.prepareStatement(INSERT_INTO_USER, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement1);
        when(preparedStatement1.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(USER_ID);

        Long expectedUserId = clientDAO.insertIntoUser(connection, dto);
        assertEquals(expectedUserId, USER_ID);

        verify(preparedStatement1).setString(1, dto.getLogin());
        verify(preparedStatement1).setString(2, dto.getPassword());
        verify(preparedStatement1).setString(3, dto.getUserRole().toString());
        verify(preparedStatement1).execute();
        verify(resultSet).close();
        verify(preparedStatement1).close();
    }

    @Test(expected = AppException.class)
    public void insertIntoUserCatchExceptionTest() throws SQLException {
        ClientCreateDto dto = new ClientCreateDto(USER_ID, "login", "password");
        when(connection.prepareStatement(INSERT_INTO_USER, Statement.RETURN_GENERATED_KEYS)).thenThrow(AppException.class);
        Long expectedUserId = clientDAO.insertIntoUser(connection, dto);
        verify(resultSet).close();
        verify(preparedStatement1).close();
    }

    @Test
    public void insertIntoClientTest() throws SQLException {
        ClientCreateDto dto = new ClientCreateDto(USER_ID, "login", "password");
        when(connection.prepareStatement(INSERT_INTO_CLIENT)).thenReturn(preparedStatement1);

        when(connection.prepareStatement(INSERT_INTO_USER, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement2);
        when(preparedStatement2.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(USER_ID);

        clientDAO.insertIntoClient(connection, dto);

        verify(preparedStatement1).setLong(1, USER_ID);
        verify(preparedStatement1).execute();
        verify(preparedStatement1).close();
    }

    @Test(expected = AppException.class)
    public void insertIntoClientCatchExceptionTest() throws SQLException {
        ClientCreateDto dto = new ClientCreateDto(USER_ID, "login", "password");
        clientDAO.insertIntoClient(connection, dto);
        verify(preparedStatement1).close();
    }

    @Test
    public void createNewClientTest() throws SQLException {
        ClientCreateDto dto = new ClientCreateDto(USER_ID, "login", "password");

        when(connection.prepareStatement(INSERT_INTO_USER, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement2);
        when(preparedStatement2.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(USER_ID);

        when(connection.prepareStatement(INSERT_INTO_CLIENT)).thenReturn(preparedStatement1);

        Long expectedUserId = clientDAO.insertIntoUser(connection, dto);
        assertEquals(expectedUserId, USER_ID);
        clientDAO.insertIntoClient(connection, dto);
        clientDAO.createNewClient(dto);

        verify(connection).setAutoCommit(false);
        verify(connection).commit();
        verify(connection).close();
    }

    @Test(expected = AppException.class)
    public void createNewClientTransactionRollbackByInsertUserTest() throws SQLException {
        ClientCreateDto dto = new ClientCreateDto(USER_ID, "login", "password");

        when(connection.prepareStatement(INSERT_INTO_USER, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement2);
        when(preparedStatement2.getGeneratedKeys()).thenThrow(AppException.class);
        clientDAO.createNewClient(dto);

        verify(connection).setAutoCommit(false);
        verify(connection).rollback();
        verify(connection).close();
    }

    @Test(expected = AppException.class)
    public void createNewClientTransactionRollbackByInsertClientTest() throws SQLException {
        ClientCreateDto dto = new ClientCreateDto(USER_ID, "login", "password");

        clientDAO.createNewClient(dto);
        verify(connection).setAutoCommit(false);
        verify(connection).rollback();
        verify(connection).close();
    }

    @Test
    public void topUpClientWalletTest() throws SQLException {
        when(connection.prepareStatement(TOP_UP_CLIENT_WALLET)).thenReturn(preparedStatement1);
        Double amountForTopUp = 15d;
        Double clientAmount = 100d;
        clientDAO.topUpClientWallet(amountForTopUp, USER_ID, clientAmount);

        verify(preparedStatement1).setDouble(1, amountForTopUp + clientAmount);
        verify(preparedStatement1).setLong(2, USER_ID);
        verify(preparedStatement1).execute();
    }

    @Test
    public void getReportsByClientIdTest() throws SQLException {
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

        when(connection.prepareStatement(GET_REPORTS_BY_CLIENT_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("order_id")).thenReturn(ORDER_ID_1).thenReturn(ORDER_ID_2);

        List<Report> resultList = clientDAO.getReportsByClientId(USER_ID, INDEX);
        assertEquals(expectedList, resultList);

        verify(preparedStatement1).setLong(1, USER_ID);
        verify(preparedStatement1).setInt(2, INDEX);
    }

    @Test
    public void getReportsByClientIdEmptyTest() throws SQLException {
        List<Report> expectedList = new ArrayList<>();

        when(connection.prepareStatement(GET_REPORTS_BY_CLIENT_ID)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Report> resultList = clientDAO.getReportsByClientId(USER_ID, INDEX);
        assertEquals(expectedList, resultList);

        verify(preparedStatement1).setLong(1, USER_ID);
        verify(preparedStatement1).setInt(2, INDEX);
    }

    @Test
    public void payInvoiceTransactionCommit() throws SQLException {
        when(connection.prepareStatement(UPDATE_DELIVERY)).thenReturn(preparedStatement1);
        when(connection.prepareStatement(UPDATE_INVOICE)).thenReturn(preparedStatement2);
        when(connection.prepareStatement(UPDATE_CLIENT)).thenReturn(preparedStatement3);
        LocalDate date1 = Date.valueOf("2000-01-01").toLocalDate();
        LocalDate date2 = Date.valueOf("2000-01-03").toLocalDate();
        double amountAfterPaid = 100d;

        clientDAO.payInvoice(ORDER_ID_1, USER_ID, date1, date2, amountAfterPaid);
        verify(connection).setAutoCommit(false);
        verify(preparedStatement1).setDate(1, Date.valueOf(date1));
        verify(preparedStatement1).setDate(2, Date.valueOf(date2));
        verify(preparedStatement1).setLong(3, ORDER_ID_1);
        verify(preparedStatement1).execute();
        verify(preparedStatement2).setLong(1, ORDER_ID_1);
        verify(preparedStatement2).execute();
        verify(preparedStatement3).setDouble(1, amountAfterPaid);
        verify(preparedStatement3).setLong(2, USER_ID);
        verify(preparedStatement3).execute();
        verify(connection).commit();
        verify(connection).close();
    }

    @Test(expected = AppException.class)
    public void payInvoiceTransactionRollbackFailOnUpdateDeliveryTest() throws SQLException {
        when(connection.prepareStatement(UPDATE_DELIVERY)).thenThrow(AppException.class);
        LocalDate date1 = Date.valueOf("2000-01-01").toLocalDate();
        LocalDate date2 = Date.valueOf("2000-01-03").toLocalDate();
        double amountAfterPaid = 100d;

        clientDAO.payInvoice(ORDER_ID_1, USER_ID, date1, date2, amountAfterPaid);
        verify(connection).setAutoCommit(false);
        verify(preparedStatement2).setLong(1, ORDER_ID_1);
        verify(preparedStatement2).execute();
        verify(preparedStatement3).setDouble(1, amountAfterPaid);
        verify(preparedStatement3).setLong(2, USER_ID);
        verify(preparedStatement3).execute();
        verify(connection).rollback();
        verify(connection).close();
    }

    @Test(expected = AppException.class)
    public void payInvoiceTransactionRollbackFailOnUpdateInvoiceTest() throws SQLException {
        when(connection.prepareStatement(UPDATE_DELIVERY)).thenReturn(preparedStatement1);
        when(connection.prepareStatement(UPDATE_INVOICE)).thenThrow(AppException.class);
        LocalDate date1 = Date.valueOf("2000-01-01").toLocalDate();
        LocalDate date2 = Date.valueOf("2000-01-03").toLocalDate();
        double amountAfterPaid = 100d;

        clientDAO.payInvoice(ORDER_ID_1, USER_ID, date1, date2, amountAfterPaid);
        verify(connection).setAutoCommit(false);
        verify(preparedStatement1).setDate(1, Date.valueOf(date1));
        verify(preparedStatement1).setDate(2, Date.valueOf(date2));
        verify(preparedStatement1).setLong(3, ORDER_ID_1);
        verify(preparedStatement1).execute();
        verify(preparedStatement3).setDouble(1, amountAfterPaid);
        verify(preparedStatement3).setLong(2, USER_ID);
        verify(preparedStatement3).execute();
        verify(connection).rollback();
        verify(connection).close();
    }

    @Test(expected = AppException.class)
    public void payInvoiceTransactionRollbackFailOnUpdateClientTest() throws SQLException {
        when(connection.prepareStatement(UPDATE_DELIVERY)).thenReturn(preparedStatement1);
        when(connection.prepareStatement(UPDATE_INVOICE)).thenReturn(preparedStatement2);
        when(connection.prepareStatement(UPDATE_CLIENT)).thenThrow(AppException.class);
        LocalDate date1 = Date.valueOf("2000-01-01").toLocalDate();
        LocalDate date2 = Date.valueOf("2000-01-03").toLocalDate();
        double amountAfterPaid = 100d;

        clientDAO.payInvoice(ORDER_ID_1, USER_ID, date1, date2, amountAfterPaid);
        verify(connection).setAutoCommit(false);
        verify(preparedStatement1).setDate(1, Date.valueOf(date1));
        verify(preparedStatement1).setDate(2, Date.valueOf(date2));
        verify(preparedStatement1).setLong(3, ORDER_ID_1);
        verify(preparedStatement1).execute();
        verify(preparedStatement2).setLong(1, ORDER_ID_1);
        verify(preparedStatement2).execute();
        verify(connection).rollback();
        verify(connection).close();
    }

    @Test
    public void getCountOfRowsAllOrdersClientTest()throws SQLException{
        Double expectedCountOfRows = 10d;
        when(connection.prepareStatement(GET_COUNT_OF_ROWS_ALL_CLIENT_ORDERS)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getDouble(1)).thenReturn(expectedCountOfRows);

        Double resultCountOfRows = clientDAO.getCountOfRowsAllOrdersClient(USER_ID);
        assertEquals(expectedCountOfRows,resultCountOfRows);

        verify(preparedStatement1).setLong(1,USER_ID);
    }

    @Test
    public void getCountOfRowsAllOrdersClientEmptyTest()throws SQLException{
        Double expectedCountOfRows = 0d;
        when(connection.prepareStatement(GET_COUNT_OF_ROWS_ALL_CLIENT_ORDERS)).thenReturn(preparedStatement1);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Double resultCountOfRows = clientDAO.getCountOfRowsAllOrdersClient(USER_ID);
        assertEquals(expectedCountOfRows,resultCountOfRows);

        verify(preparedStatement1).setLong(1,USER_ID);
    }
}
