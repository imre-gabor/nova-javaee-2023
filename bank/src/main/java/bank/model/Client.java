package bank.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * The persistent class for the client database table.
 * 
 */
@Entity
@Table(name = "client", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c")
public class Client implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer clientid;

	private String address;

	private String name;

	// bi-directional many-to-one association to Account
	@OneToMany(mappedBy = "client")
	private List<Account> accounts;

	public Client() {
	}

	public Client(String address, String name) {
		super();
		this.address = address;
		this.name = name;
	}

	public Integer getClientid() {
		return this.clientid;
	}

	public void setClientid(Integer clientid) {
		this.clientid = clientid;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Account> getAccounts() {
		if (this.accounts == null)
			this.accounts = new ArrayList<Account>();
		return this.accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public Account addAccount(Account account) {
		getAccounts().add(account);
		account.setClient(this);

		return account;
	}

	public Account removeAccount(Account account) {
		getAccounts().remove(account);
		account.setClient(null);

		return account;
	}

	@Override
	public int hashCode() {
		return Objects.hash(clientid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return Objects.equals(clientid, other.clientid);
	}

}