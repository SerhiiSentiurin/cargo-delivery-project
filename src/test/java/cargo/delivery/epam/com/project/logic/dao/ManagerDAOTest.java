package cargo.delivery.epam.com.project.logic.dao;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@RunWith(MockitoJUnitRunner.class)
public class ManagerDAOTest {
    @Mock
    DataSource dataSource;
    @Mock
    Connection connection;
    @Mock
    PreparedStatement preparedStatement;
    @Mock
    ResultSet resultSet;

    @InjectMocks
    ClientDAO clientDAO;

    private static final String sql = "select count(*) from report";

}
