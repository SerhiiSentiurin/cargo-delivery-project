package cargo.delivery.epam.com.project.logic.services;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import cargo.delivery.epam.com.project.logic.dao.OrderDAO;
import cargo.delivery.epam.com.project.logic.entity.Route;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientOrderDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderService {
    private final OrderDAO orderDAO;

    public void createOrder(ClientOrderDto dto) {
        orderDAO.createOrder(dto);
    }

    public List<Route> getDistinctSenderCities() {
        return orderDAO.getDistinctSenderCities();
    }

    public List<Route> getDistinctRecipientCities() {
        return orderDAO.getDistinctRecipientCities();
    }

    public ClientOrderDto calculateDeliveryCost(ClientOrderDto dto) {
        Route route = orderDAO.getRoute(dto.getSenderCity(), dto.getRecipientCity());
        double distance = route.getDistance();
        double deliveryCost = calculateTax(dto.getWeight(), dto.getVolume(), distance);
        dto.setDistance(distance);
        dto.setDeliveryCost(deliveryCost);
        return dto;
    }

    private double calculateTax(Double weight, Double volume, Double distance) {
        final double fuelCostPerKilometer = 7.5d;
        final double volumeTax = getVolumeTax(volume);
        final double weightTax = getWeightTax(weight);

        return (fuelCostPerKilometer * distance) + (volumeTax * volume) + (weightTax * weight);
    }

    private double getVolumeTax(Double volume) {
        if (volume > 0d && volume <= 25d) {
            return 50d;
        } else {
            throw new AppException("Illegal volume for transportation");
        }
    }

    private double getWeightTax(Double weight) {
        if (weight > 0d && weight <= 500d) {
            return 4.5d;
        } else if (weight > 500d && weight <= 800d) {
            return 4d;
        } else if (weight > 800d && weight <= 1200d) {
            return 3.5d;
        } else if (weight > 1200d && weight <= 2000d) {
            return 3d;
        } else if (weight > 2000d && weight <= 3000d) {
            return 2.5d;
        } else if (weight > 3000d && weight <= 4000d) {
            return 2d;
        } else if (weight > 4000d && weight <= 5000d) {
            return 1.5d;
        } else {
            throw new AppException("Illegal weight for transportation");
        }
    }


}
