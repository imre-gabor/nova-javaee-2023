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
import bank.service.BankServiceLocal;

/**
 * Servlet implementation class CreateClientServlet
 */
@WebServlet("/FindClientsServlet")
public class FindClientsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;       
	
	@EJB
	ClientDao clientDao;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<Client> clients = clientDao.findAll();
			request.setAttribute("clients", clients);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
	
}
