package bank.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;

import org.mockito.junit.jupiter.MockitoExtension;

import bank.dao.AccountDao;
import bank.dao.ClientDao;
import bank.model.Account;
import bank.model.Client;

@ExtendWith(MockitoExtension.class)
public class BankServiceTest {
	
	@InjectMocks
	BankServiceLocal bankService;
	
	@Mock
	ClientDao clientDao;
	
	@Mock
	AccountDao accountDao;

	//mockito használata annotációk nélkül
//	@Test
//	public void testCannotCreateAccountForNonExistingClient() {
//		//ARRANGE
//		BankService bankService = new BankService();
//		bankService.clientDao = Mockito.mock(ClientDao.class);
//		Mockito.when(bankService.clientDao.findById(1)).thenReturn(null);
//		
//		//ACT + ASSERT
//		BankException ex = assertThrows(BankException.class, () -> 
//			bankService.createAccountForClient(new Account(100.0), 1)
//		); 
//		
//		//ASSERT
//		assertEquals("Client with id 1 does not exist.", ex.getMessage());
//	}
	
		
	@Test
	public void testCannotCreateAccountForNonExistingClient() {
		//ARRANGE
		when(clientDao.findById(1)).thenReturn(null);
		
		//ACT + ASSERT
		BankException ex = assertThrows(BankException.class, () -> 
			bankService.createAccountForClient(new Account(100.0), 1)
		); 
		
		//ASSERT
		//JUnit stílusú assert
//		assertEquals("Client with id 1 does not exist.", ex.getMessage());
		//AssertJ-vel:
		assertThat(ex.getMessage()).isEqualTo("Client with id 1 does not exist.");
	}
	
	@Test
	public void testAccountProperlyCreatedForExsitingClient() throws Exception {
		//ARRANGE
		Client client = new Client();
		client.setClientid(1);
		client.setAccounts(new ArrayList<>());
		when(clientDao.findById(1)).thenReturn(client);
		Account account = new Account(100.0);
		
		//ACT
		bankService.createAccountForClient(account, 1);
		
		//ASSERT
		assertThat(client.getAccounts()).contains(account);
		assertThat(account.getClient()).isSameAs(client);
		assertThat(account.getBalance()).isEqualTo(100.0);
		//assertThat(account.getCreatedate()).isToday();
		assertThat(account.getCreatedate()).isCloseTo(new Date(), 100);
		verify(accountDao).create(account);
	}
	
	
}
