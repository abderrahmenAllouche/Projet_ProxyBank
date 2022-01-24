package fr.poei.open.ProxyBanque;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.poei.open.ProxyBanque.repositories.ClientRepository;
import fr.poei.open.ProxyBanque.services.ClientService;


@SpringBootTest
public class TestClientService {
	
@Autowired
ClientService clientService;

@Autowired
ClientRepository clientRepository;



	
@Test
public void testVerficationSiIdExiste() {
		System.out.println( clientRepository.findById(39L) );

		assertTrue(clientService.verficationSiIdExiste(39L));
		
	}

}
