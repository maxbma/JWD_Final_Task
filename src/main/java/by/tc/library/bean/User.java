package by.tc.library.bean;

public class User {
    private final UserRole userRole;
    private final int userID;

    public User(String role, int id){
        this.userRole = UserRole.valueOf(role);
        this.userID = id;
    }

    @Override
    public boolean equals(Object obj){
        if(null == obj || obj.getClass()!=this.getClass()){
            return false;
        }
        if(this == obj){
            return true;
        }
        User user = (User)obj;
        return this.userID == user.userID &&
                this.userRole == user.userRole;
    }

    public UserRole getUserRole(){
        return userRole;
    }

    public int getUserID() {
        return userID;
    }
}
