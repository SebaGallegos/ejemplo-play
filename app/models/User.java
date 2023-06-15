package models;

public class User {
    public String name;
    public String email;

    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Nombre: " + this.name + ", Email: " + this.email;
    }
}
