package bank.web;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bank.model.Account;
import bank.service.BankServiceLocal;

/**
 * Servlet implementation class CreateClientServlet
 */
@WebServlet("/TransferServlet")
public class TransferServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;       
	
	@EJB
	BankServiceLocal bank;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int from = Integer.parseInt(request.getParameter("from"));
		int to = Integer.parseInt(request.getParameter("to"));
		double amount = Double.parseDouble(request.getParameter("amount"));
		
		
		String message = null;
		
		try {			
//			bank.transfer(from, to, amount);
			bank.scheduleTransfer(from, to, amount, 10);
//			message = "Transfer was successful";
			message = "Transfer was successfully scheduled.";
		} catch (Exception e) {
			e.printStackTrace();
//			message = "Transfer failed: " + e.getMessage();
			message = "Transfer schedule failed: " + e.getMessage();
		}
		
		request.setAttribute("resultOfTransfer", message);
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
	}

	
}
