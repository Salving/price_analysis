package teamworks.server.domain;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, name = "id")
    private long id;

    @Column(unique = true, nullable = false, name = "login")
    private String login;

    @Column(unique = true, nullable = false, name = "mail")
    private String mail;

    @Column(nullable = false, name = "password")
    private String password;

    protected User() {

    }

    public User(long id, String login, String mail, String password) {
        this.id = id;
        this.login = login;
        this.mail = mail;
        this.password = password;
    }

    public User(String login, String mail, String password) {
        this.login = login;
        this.mail = mail;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
