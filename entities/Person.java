package entities;

public class Person {
    private String name;
    private String email;
    private String phonenum;

    public Person(String name, String email, String phonenum) {
        this.name = name;
        this.email = email;
        this.phonenum = phonenum;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public String getPhonenum() {
        return phonenum;
    }

}