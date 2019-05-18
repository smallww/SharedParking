package com.cat.entity;

import java.util.HashSet;
import java.util.Set;

public class SPspace implements java.io.Serializable{


    private Integer spaceId;
    private String spaceName;
    private Integer spaceNum;
    private String spacePic;
    private String spaceRemark;
    private String ownerName;
    private String contactNum;
    private String releaseTime;

    private Set users = new HashSet(0);

    /** default constructor */
    public SPspace() {
    }

    /** minimal constructor */
    public SPspace(Integer spaceId) {
        this.spaceId = spaceId;
    }


    public SPspace(Integer spaceId, String spaceName, Integer spaceNum, String spacePic,
                   String spaceRemark, String ownerName, String contactNum, String releaseTime, Set users) {
        this.spaceId = spaceId;
        this.spaceName = spaceName;
        this.spaceNum = spaceNum;
        this.spacePic = spacePic;
        this.spaceRemark = spaceRemark;
        this.ownerName = ownerName;
        this.contactNum = contactNum;
        this.releaseTime = releaseTime;
        this.users = users;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public Integer getSpaceNum() {
        return spaceNum;
    }

    public void setSpaceNum(Integer spaceNum) {
        this.spaceNum = spaceNum;
    }

    public String getSpacePic() {
        return spacePic;
    }

    public void setSpacePic(String spacePic) {
        this.spacePic = spacePic;
    }

    public String getSpaceRemark() {
        return spaceRemark;
    }

    public void setSpaceRemark(String spaceRemark) {
        this.spaceRemark = spaceRemark;
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

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Set getUsers() {
        return users;
    }

    public void setUsers(Set users) {
        this.users = users;
    }
}
