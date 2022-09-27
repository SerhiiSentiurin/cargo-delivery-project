package cargo.delivery.epam.com.project.logic.services;

import cargo.delivery.epam.com.project.logic.dao.ClientDAO;
import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientCreateDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientService {
    private final ClientDAO clientDAO;

    public void createNewClient(ClientCreateDto dto){
        clientDAO.createNewClient(dto);
    }


}
