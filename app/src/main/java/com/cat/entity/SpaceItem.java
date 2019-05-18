package com.cat.entity;

public class SpaceItem {
    private Integer spaceId;
    private String spaceName;
    private Integer spaceNum;
    private String ownerName;
    private String innerTime;
    private String contactNum;

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public Integer getSpaceNum() {
        return spaceNum;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
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

    public String getInnerTime() {
        return innerTime;
    }

    public void setInnerTime(String innerTime) {
        this.innerTime = innerTime;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    @Override
    public String toString() {
        return "SpaceItem{" +
                "spaceId=" + spaceId +
                ", spaceNum=" + spaceNum +
                ", ownerName='" + ownerName + '\'' +
                ", innerTime='" + innerTime + '\'' +
                ", contactNum='" + contactNum + '\'' +
                '}';
    }
}
