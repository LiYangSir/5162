package com.hrbeu5162.vo;

public class NavQuery {

    private String userName;
    private String avatar;
    private boolean isTeacher;
    private boolean isRoot;

    public NavQuery(String userName, String avatar, boolean isTeacher, boolean isRoot) {
        this.userName = userName;
        this.avatar = avatar;
        this.isTeacher = isTeacher;
        this.isRoot = isRoot;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    @Override
    public String toString() {
        return "NavQuery{" +
                "userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", isTeacher=" + isTeacher +
                ", isRoot=" + isRoot +
                '}';
    }
}
