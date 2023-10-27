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

import bank.model.Account;
import bank.model.Client;
import bank.service.BankServiceLocal;

/**
 * Servlet implementation class CreateClientServlet
 */
@WebServlet("/SearchAccountServlet")
public class SearchAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;       
	
	@EJB
	BankServiceLocal bank;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer clientId = getParameterAsInteger(request, "clientid");
		Integer accountId = getParameterAsInteger(request, "accountid");
		double balance = getParameterAsDouble(request, "balance");
		Date createdate = getParameterAsDate(request, "createdate");
			
		Account account = new Account(balance);
		account.setAccountid(accountId);
		account.setCreatedate(createdate);
		if(clientId != null) {
			Client client = new Client();
			client.setClientid(clientId);
			account.setClient(client);
		}
		
		try {
			List<Account> accounts = bank.findByExample(account);
			request.setAttribute("accounts", accounts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
	}


	private Integer getParameterAsInteger(HttpServletRequest request, String parName) {
		String parString = request.getParameter(parName);
		return isEmpty(parString) ? null : Integer.parseInt(parString);
	}


	private boolean isEmpty(String parString) {
		return parString == null || parString.length() == 0;
	}
	
	private double getParameterAsDouble(HttpServletRequest request, String parName) {
		String parString = request.getParameter(parName);
		return isEmpty(parString) ? 0.0 : Double.parseDouble(parString);
	}
		

	private Date getParameterAsDate(HttpServletRequest request, String parName) {
		String parString = request.getParameter(parName);
		try {
			return isEmpty(parString) ? null : sdf.parse(parString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
