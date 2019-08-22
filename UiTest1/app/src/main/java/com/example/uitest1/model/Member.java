package com.example.uitest1.model;

import java.io.Serializable;

public class Member implements Serializable {
    private String name;    // 이름
    private String email;   // 이메일
    private String tel;     // 전화번호
    private String addr;    // 주소
    private String image;   // 이미지

    public Member() {
    }

    public Member(String name, String email, String tel, String addr) {
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.addr = addr;
    }

    public Member(String name, String email, String tel, String addr, String image) {
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.addr = addr;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
