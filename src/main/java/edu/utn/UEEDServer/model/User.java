package edu.utn.UEEDServer.model;

public class User {
    private String username;
    private String password;
    private String name;
    private String surname;
    private Boolean employee;

    public User(String username, String password, String name, String surname, Boolean employee) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.employee = employee;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Boolean getEmployee() {
        return employee;
    }

    public void setEmployee(Boolean employee) {
        this.employee = employee;
    }
}