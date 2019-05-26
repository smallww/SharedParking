package com.cat.entity;

public class SPuser implements java.io.Serializable {

    // Fields
    private SPspace sPspace;
    private Integer userId;
    private String userName;
    private String phone;
    private String password;
    private String gender;
    private String carNum;
    private Integer balance;
    private String headPic;
    private String devicetoken;


    // Constructors

    /** default constructor */
    public SPuser() {
    }

    /** full constructor */
    public SPuser( SPspace sPspace,String userName, String phone,
                   String password, String gender,String carNum, Integer balance, String headPic,
                   String devicetoken) {
        this.sPspace=sPspace;
        this.userName = userName;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.carNum=carNum;
        this.balance = balance;
        this.headPic = headPic;
        this.devicetoken = devicetoken;
    }

    // Property accessors

    public SPspace getSPspace() {
        return sPspace;
    }

    public void setSPspace(SPspace sPspace) {
        this.sPspace = sPspace;
    }
    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public Integer getBalance() {
        return this.balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getHeadPic() {
        return this.headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getDevicetoken() {
        return this.devicetoken;
    }

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }
}
