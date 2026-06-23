package com.apistyle.pbprestapi.dto;

import java.util.List;

public class Profiles {
    List<Profile> users;

    public Profiles(List<Profile> users) {
        this.users = users;
    }

    public List<Profile> getUsers() {
        return users;
    }

    public void setUsers(List<Profile> users) {
        this.users = users;
    }
}
