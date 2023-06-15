package controllers;

import play.data.validation.Constraints;

public class UserProcess {
    @Constraints.Required protected String name;
    protected String email;

    public UserProcess(){

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
