package com.cat.entity;

public class TaskShareItem {
    private int taskId;
    private String startTime;
    private String endTime;
    private Integer spaceId;
    private String createTime;
    private String taskType;
    private String fixedTime;
    private String address;
    private Integer spaceNum;
    private String ownerName;
    private String contactNum;
    private String nameTV;
    private String phoneTV;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getFixedTime() {
        return fixedTime;
    }

    public void setFixedTime(String fixedTime) {
        this.fixedTime = fixedTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSpaceNum() {
        return spaceNum;
    }

    public void setSpaceNum(Integer spaceNum) {
        this.spaceNum = spaceNum;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getNameTV() {
        return nameTV;
    }

    public void setNameTV(String nameTV) {
        this.nameTV = nameTV;
    }

    public String getPhoneTV() {
        return phoneTV;
    }

    public void setPhoneTV(String phoneTV) {
        this.phoneTV = phoneTV;
    }

    @Override
    public String toString() {
        return "TaskShareItem{" +
                "taskId=" + taskId +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", spaceId=" + spaceId +
                ", createTime='" + createTime + '\'' +
                ", taskType='" + taskType + '\'' +
                ", fixedTime='" + fixedTime + '\'' +
                ", address='" + address + '\'' +
                ", spaceNum=" + spaceNum +
                ", ownerName='" + ownerName + '\'' +
                ", contactNum='" + contactNum + '\'' +
                ", nameTV='" + nameTV + '\'' +
                ", phoneTV='" + phoneTV + '\'' +
                '}';
    }
}
