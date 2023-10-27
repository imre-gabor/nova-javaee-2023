package bank.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bank.dao.ClientDao;
import bank.model.Account;
import bank.model.Client;
import bank.model.Client_;
import bank.service.BankService;
import bank.service.BankServiceLocal;

/**
 * Servlet implementation class CreateClientServlet
 */
@WebServlet("/FindClientsServlet")
public class FindClientsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;       
	
	@EJB
	ClientDao clientDao;
	
	@EJB
	BankServiceLocal bankService;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//List<Client> clients = clientDao.findAll();
			//7. megoldás LazyInit ellen
//			List<Client> clients = clientDao.findAllWithFetch(Client_.accounts);
			//8. megoldás LazyInit ellen
//			List<Client> clients = clientDao.findAllWithEntityGraph("Client.egWithRelationships");
			//több kapcsolat esetén, Descartes-szorzat elkerülésére
			List<Client> clients = bankService.findAllWithAllRelationships();
			request.setAttribute("clients", clients);
//			System.out.println("calling findById");
//			request.setAttribute("client", clientDao.findById(1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
	
}
