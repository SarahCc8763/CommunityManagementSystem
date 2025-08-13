//package finalProj.domain.users;
//
//import java.time.LocalDateTime;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.MapsId;
//import jakarta.persistence.OneToOne;
//import jakarta.persistence.Table;
//
//@Entity
//@Table(name = "accounts")
//public class Accounts {
//
//    @Id
//    @Column(name = "accounts_id")
//    private Integer accountsId;
//
//    @OneToOne
//    @MapsId         // 共用主鍵
//    @JoinColumn(name = "accounts_id", referencedColumnName = "users_id")
//    private Users user;
//
//    @Column(name = "name", nullable = false, unique = true, length = 100)
//    private String name;
//
//    @Column(name = "password", nullable = false, length = 20)
//    @JsonIgnore  // 防止 password 被序列化回傳
//    private String password;
//
//    @Column(name = "states", columnDefinition = "INT DEFAULT 0")
//    private Integer states = 0;
//
//    @Column(name = "login_fail_times", columnDefinition = "INT DEFAULT 0")
//    private Integer loginFailTimes = 0;
//
//    @Column(name = "last_failed_login")
//    private LocalDateTime lastFailedLogin;
//
//    @Column(name = "account_locked_until")
//    private LocalDateTime accountLockedUntil;
//
//    @ManyToOne
//    @JoinColumn(name = "community_id", referencedColumnName = "community_id")
//    private Community community;
//
//    // --- Getters and Setters ---
//	public Integer getAccountsId() {
//		return accountsId;
//	}
//
//	public void setAccountsId(Integer accountsId) {
//		this.accountsId = accountsId;
//	}
//
//	public Users getUser() {
//		return user;
//	}
//
//	public void setUser(Users user) {
//		this.user = user;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public Integer getStates() {
//		return states;
//	}
//
//	public void setStates(Integer states) {
//		this.states = states;
//	}
//
//	public Integer getLoginFailTimes() {
//		return loginFailTimes;
//	}
//
//	public void setLoginFailTimes(Integer loginFailTimes) {
//		this.loginFailTimes = loginFailTimes;
//	}
//
//	public LocalDateTime getLastFailedLogin() {
//		return lastFailedLogin;
//	}
//
//	public void setLastFailedLogin(LocalDateTime lastFailedLogin) {
//		this.lastFailedLogin = lastFailedLogin;
//	}
//
//	public LocalDateTime getAccountLockedUntil() {
//		return accountLockedUntil;
//	}
//
//	public void setAccountLockedUntil(LocalDateTime accountLockedUntil) {
//		this.accountLockedUntil = accountLockedUntil;
//	}
//
//	public Community getCommunity() {
//		return community;
//	}
//
//	public void setCommunity(Community community) {
//		this.community = community;
//	}
//
//	// --- Optional: toString ---
//	@Override
//	public String toString() {
//		return "Accounts [accountsId=" + accountsId + ", user=" + user + ", name=" + name + ", password=" + password
//				+ ", states=" + states + ", loginFailTimes=" + loginFailTimes + ", lastFailedLogin=" + lastFailedLogin
//				+ ", accountLockedUntil=" + accountLockedUntil + ", community=" + community + ", getAccountsId()="
//				+ getAccountsId() + ", getUser()=" + getUser() + ", getName()=" + getName() + ", getPassword()="
//				+ getPassword() + ", getStates()=" + getStates() + ", getLoginFailTimes()=" + getLoginFailTimes()
//				+ ", getLastFailedLogin()=" + getLastFailedLogin() + ", getAccountLockedUntil()="
//				+ getAccountLockedUntil() + ", getCommunity()=" + getCommunity() + "]";
//	}
//
//}
