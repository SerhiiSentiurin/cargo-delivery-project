package cargo.delivery.epam.com.project.logic.services;

import cargo.delivery.epam.com.project.logic.dao.OrderDAO;
import cargo.delivery.epam.com.project.logic.entity.Route;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientOrderDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {
    @Mock
    OrderDAO orderDAO;

    @InjectMocks
    OrderService orderService;

    private static final ClientOrderDto dto = Mockito.mock(ClientOrderDto.class);
    private static final Route route1 = Mockito.mock(Route.class);
    private static final Route route2 = Mockito.mock(Route.class);

    @Test
    public void createOrderTest(){
        orderService.createOrder(dto);
        verify(orderDAO).createOrder(dto);
    }

    @Test
    public void getDistinctSenderCitiesTest(){
        List<Route> expectedList = new ArrayList<>();
        expectedList.add(route1);
        expectedList.add(route2);
        when(orderDAO.getDistinctSenderCities()).thenReturn(expectedList);

        List<Route> resultList = orderService.getDistinctSenderCities();
        assertEquals(expectedList,resultList);
        assertNotNull(resultList);

        verify(orderDAO).getDistinctSenderCities();
    }

    @Test
    public void getDistinctSenderCitiesEmptyTest(){
        List<Route> expectedList = new ArrayList<>();
        when(orderDAO.getDistinctSenderCities()).thenReturn(expectedList);

        List<Route> resultList = orderService.getDistinctSenderCities();
        assertEquals(expectedList,resultList);
        assertNotNull(resultList);
        verify(orderDAO).getDistinctSenderCities();
    }

    @Test
    public void getDistinctRecipientCitiesTest(){
        List<Route> expectedList = new ArrayList<>();
        expectedList.add(route1);
        expectedList.add(route2);
        when(orderDAO.getDistinctRecipientCities()).thenReturn(expectedList);

        List<Route> resultList = orderService.getDistinctRecipientCities();
        assertEquals(expectedList,resultList);
        assertNotNull(resultList);

        verify(orderDAO).getDistinctRecipientCities();
    }

    @Test
    public void getDistinctRecipientCitiesEmptyTest(){
        List<Route> expectedList = new ArrayList<>();
        when(orderDAO.getDistinctRecipientCities()).thenReturn(expectedList);

        List<Route> resultList = orderService.getDistinctRecipientCities();
        assertEquals(expectedList,resultList);
        assertNotNull(resultList);

        verify(orderDAO).getDistinctRecipientCities();
    }

    @Test
    public void calculateDeliveryCost(){
        ClientOrderDto userInsertDto = new ClientOrderDto(1L, "type", 100d, 10d, "sender", "recipient",100d, null);
        ClientOrderDto expectedDto = new ClientOrderDto(1L, "type", 100d, 10d, "sender", "recipient",100d, 1700d);
        Route route = new Route(1L, expectedDto.getDistance(), expectedDto.getSenderCity(),expectedDto.getRecipientCity());
        when(orderDAO.getRouteByCities(userInsertDto.getSenderCity(),userInsertDto.getRecipientCity())).thenReturn(route);

        ClientOrderDto resultDto = orderService.calculateDeliveryCost(userInsertDto);
        assertEquals(expectedDto,resultDto);

        verify(orderDAO).getRouteByCities(userInsertDto.getSenderCity(),userInsertDto.getRecipientCity());
    }
}
