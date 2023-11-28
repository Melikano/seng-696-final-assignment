package entities;
public class User {
    private String name;
    private String password;
    private String email;
    private String phonenum;

    public User(String name, String password, String email, String phonenum) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phonenum = phonenum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhonenum() {
        return phonenum;
    }

}
