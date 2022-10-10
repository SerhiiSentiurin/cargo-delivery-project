package cargo.delivery.epam.com.project.logic.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;

@Log4j2
@RequiredArgsConstructor
public class ManagerDAO {
    private final DataSource dataSource;


}
