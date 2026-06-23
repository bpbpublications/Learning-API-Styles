package com.apistyle.pbprestapi;

import com.apistyle.pbprestapi.dto.Profile;
import com.apistyle.pbprestapi.servcies.UsersServcie;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class UsersGraphQLController {
    @Autowired
    private UsersServcie usersServcie;


    @QueryMapping
    @GetMapping
    public Profile getUserProfileById(@Argument int id) throws Exception {
        Profile profiles = usersServcie.getUserProfiles(id);
        if (profiles != null) {
            return profiles;
        } else {
            throw new BadRequestException();
        }
    }

    @MutationMapping
    public Profile createUserProfile(@Argument int id,
                                         @Argument String name,
                                         @Argument String email) {
        return usersServcie.createUserProfile(id, name, email);
    }
}
