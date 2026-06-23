package com.apistyle.pbprestapi.servcies;

import com.apistyle.pbprestapi.dto.Profile;
import com.apistyle.pbprestapi.dto.Profiles;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersServcie {
    static Profiles profiles;
    public Profiles getUserProfiles() {

        if(profiles==null)
            loadprofiles();

        return profiles;
    }

    private void loadprofiles() {
        List<Profile> users = new ArrayList<Profile>();
        Profile p1 = new Profile();
        p1.setId(1);
        p1.setName("John Doe");
        p1.setEmail("john@example.com");

        Profile p2 = new Profile();
        p2.setId(2);
        p2.setName("Jane Smith");
        p2.setEmail("jane@example.com");

        users.add(p1);
        users.add(p2);
        profiles=new Profiles(users);
    }

    public Profile getUserProfiles(int id) {
        if(profiles==null)
            loadprofiles();

        return profiles.getUsers().stream().filter(index -> index.getId() == id).findAny().get();
    }

    public Profile createUserProfile(int id, String name, String email) {
        if(profiles==null)
            loadprofiles();
        Profile profile = new Profile(name, email, id);
        profiles.getUsers().add(profile);
        return profile;
    }
}
