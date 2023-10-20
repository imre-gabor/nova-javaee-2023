package bank.model;

import java.io.Serializable;
import javax.persistence.*;

import bank.service.BankException;

import java.util.Date;
import java.util.Objects;


/**
 * The persistent class for the account database table.
 * 
 */
@Entity
@NamedQuery(name="Account.findAll", query="SELECT a FROM Account a")
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer accountid;

	private double balance;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	//bi-directional many-to-one association to Client
	@ManyToOne
	@JoinColumn(name="clientid")
	private Client client;

	public Account() {
	}

	public Account(double balance) {
		this.balance = balance;
	}
	
	
	public void increase(double amount) {
		if(amount < 0)
			throw new IllegalArgumentException("amount should positive");
		this.balance += amount;
	}
	
	public void decrease(double amount) throws BankException {
		if(amount < 0)
			throw new IllegalArgumentException("amount should positive");
		if(this.balance < amount)
			throw new BankException("Balance is not enough");
		this.balance -= amount;
	}

	public Integer getAccountid() {
		return this.accountid;
	}

	public void setAccountid(Integer accountid) {
		this.accountid = accountid;
	}

	public double getBalance() {
		return this.balance;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		return Objects.equals(accountid, other.accountid);
	}
	
	

}