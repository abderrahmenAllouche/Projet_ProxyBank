package fr.poei.open.proxybanque;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.poei.open.proxybanque.repositories.ClientRepository;
import fr.poei.open.proxybanque.services.ClientService;


@SpringBootTest
public class TestClientService {
	
@Autowired
ClientService clientService;

@Autowired
ClientRepository clientRepository;



	
@Test
public void testVerficationSiIdExiste() {
		System.out.println( clientRepository.findById(39L) );

		assertFalse(clientService.verficationSiIdExiste(39L));
		
	}

}
