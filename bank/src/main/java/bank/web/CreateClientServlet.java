package bank.web;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bank.model.Client;
import bank.service.BankServiceLocal;

/**
 * Servlet implementation class CreateClientServlet
 */
@WebServlet("/CreateClientServlet")
public class CreateClientServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;       
	
	@EJB
	BankServiceLocal bank;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		
		Client client = new Client(address, name);
		String message = null;
		
		try {
			bank.createClient(client);
			message = "Client created with id " + client.getClientid();
		} catch (Exception e) {
			e.printStackTrace();
			message = "Client could not be created: " + e.getMessage();
		}
		
		request.setAttribute("resultOfClientCreation", message);
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
	}

	
}
