package bank.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


/**
 * The persistent class for the client database table.
 * 
 */
@Entity
@Table(name = "client", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c")
@NamedQuery(name = "Client.findAllWithAccounts", query = "SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.accounts")
@NamedEntityGraph(
		name = "Client.egWithRelationships",
		attributeNodes = { 
			@NamedAttributeNode("accounts")
			,
			//@NamedAttributeNode("historyLogs")	//ha ez is itt van: cannot simultaneously fetch multiple bags
			/*
			 * első megoldás: List-ek helyett Set-ek 
			 * --> működik, de a Descartes szorzat miatt a DB-ből  N*M sor jön vissza (ha egy clienthez N account és M history)
			 * --> teljesítmény probléma lehet
			 * második megoldás: legeljebb 1 többes kapcsolatot töltünk be joinnal, a többit külön selecttel
			 */
		}
)
@NamedEntityGraph(
		name = "Client.egWithHistory",
		attributeNodes = { 
			@NamedAttributeNode("historyLogs")
		}
)

public class Client implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer clientid;

	private String address;

	private String name;

	// bi-directional many-to-one association to Account
	//2. megoldás LazyInitException ellen: EAGER fetch --> itt is N+1 SELECT!
	//3. megoldás: 
	//@Fetch(FetchMode.JOIN) //--> csak egyes entitás (find id alapján) betöltésekor hat
	//4. megoldás
	@Fetch(FetchMode.SUBSELECT) //--> N+1 SELECT helyett 2 Select, de működik findAll-ra is	
	//2., 3., 4. esetében is minden queryre eager fetch lesz
	@OneToMany(mappedBy = "client"/*, fetch = FetchType.EAGER*/)
	private Set<Account> accounts;
	
	@OneToMany(mappedBy = "client")
	private Set<History> historyLogs;

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

	public Set<Account> getAccounts() {
		if (this.accounts == null)
			this.accounts = new HashSet<Account>();
		return this.accounts;
	}

	public void setAccounts(Set<Account> accounts) {
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
	
	public Set<History> getHistoryLogs() {
		return historyLogs;
	}

	public void setHistoryLogs(Set<History> historyLogs) {
		this.historyLogs = historyLogs;
	}
	
	public void addHistory(History history) {
		getHistoryLogs().add(history);
		history.setClient(this);
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