package socar.user.domain;

public class User {
    private String userId;
    private String userName;
    private String password;
    private int licenseNum;
    private char isAdmin;

    // 생성자
    public User(String userId, String userName, String password, int licenseNum, char isAdmin) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.licenseNum = licenseNum;
        this.isAdmin = isAdmin;
    }

    // getter, setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLicenseNum() {
        return licenseNum;
    }

    public void setLicenseNum(int licenseNum) {
        this.licenseNum = licenseNum;
    }

    public char getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(char isAdmin) {
        this.isAdmin = isAdmin;
    }

    // toString 메서드에서 isAdmin을 'Y' 또는 'N'으로 변환
    @Override
    public String toString() {
        return "User [userId=" + userId + ", userName=" + userName + ", licenseNum=" + licenseNum + ", isAdmin=" + isAdmin + "]";
    }
}
