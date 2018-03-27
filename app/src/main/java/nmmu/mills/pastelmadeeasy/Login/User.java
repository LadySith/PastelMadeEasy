package nmmu.mills.pastelmadeeasy.Login;

/**
 * Created by s214301427 on 2017/09/20.
 *
 */
public class User {

    //Feb 2018 replaced Username, Password model with Email, Password, Fname, LName

    private int id;
    private String Email;
    private String Password;
    private String FName;
    private String LName;


/*    public User(String  email, String pword, String fname, String lname) {

        this.Password = pword;
        this.Email = email;
        this.FName = fname;
        this.LName = lname;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }
}
