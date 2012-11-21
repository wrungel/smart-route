package com.smartroute.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;


@Entity
public class Account implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(length = 20)	
    private String login;

    @Column(length = 32)	
    private String passwd;

    @Column(length = 150)
    @NotNull
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")    
    private String name;

    @Column(length = 100)
    private String email;

    public Account() {
    }

    public Account(String login, String passwd, String name) {
        this.login = login;
        this.passwd = passwd;
        this.name = name;
    }

    public Integer getId() {
        return this.id;
    }


    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
