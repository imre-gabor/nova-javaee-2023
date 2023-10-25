package bank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import bank.dao.AccountDao;
import bank.dao.ClientDao;
import bank.model.Account;
import bank.model.Client;

@ExtendWith(ArquillianExtension.class)
public class BankServiceIT {

	@EJB
	BankServiceLocal bankService;
	
	@EJB
	ClientDao clientDao;
	
	@EJB
	AccountDao accountDao;
	
	@Deployment
	public static WebArchive createDeployment() {
		
		File[] assertJDeps = Maven.resolver()
			.loadPomFromFile("pom.xml")
			.resolve("org.assertj:assertj-core")
			.withTransitivity()
			.asFile();
		
		return ShrinkWrap.create(WebArchive.class)
			.addPackages(false, "bank.model", "bank.dao", "bank.service")
			.addAsResource("META-INF/persistence.xml")
			.addAsLibraries(assertJDeps)
			;
	}

	@Test
	public void testCannotCreateAccountForNonExistingClient() {

		BankException ex = assertThrows(BankException.class, () -> 
			bankService.createAccountForClient(new Account(100.0), -1)
		);
//		assertEquals("Client with id -1 does not exist.", ex.getMessage());
		assertThat(ex.getMessage()).isEqualTo("Client with id -1 does not exist.");
	}
	
	
	@Test
	public void testAccountProperlyCreatedForExsitingClient() throws Exception {
		//ARRANGE
		Client client = new Client();
		client.setName("client1");
		client.setAddress("address1");
		clientDao.create(client);
		int clientId = client.getClientid();
		Account account = new Account(100.0);
		
		//ACT
		bankService.createAccountForClient(account, clientId);
		
		//ASSERT
		Account accountInDb = accountDao.findById(account.getAccountid());
		assertThat(accountInDb.getBalance()).isEqualTo(100);
		assertThat(accountInDb.getCreatedate()).isToday();
		assertThat(accountInDb.getClient()).isEqualTo(client);
	}

}
