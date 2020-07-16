package com.example.covid;

import java.util.ArrayList;
import java.util.List;

public class user {
    private long Phone_no;
    private List<Long> interacted_user;

    public user()
    {
        Phone_no=0;
        interacted_user=new ArrayList<>();
    }
    public user(long phone_no, List<Long> interacted_user) {
        Phone_no = phone_no;
        this.interacted_user = interacted_user;
    }

    public long getPhone_no() {
        return Phone_no;
    }

    public void setPhone_no(long phone_no) {
        Phone_no = phone_no;
    }

    public List<Long> getInteracted_user() {
        return interacted_user;
    }

    public void setInteracted_user(List<Long> interacted_user) {
        this.interacted_user = interacted_user;
    }
}
