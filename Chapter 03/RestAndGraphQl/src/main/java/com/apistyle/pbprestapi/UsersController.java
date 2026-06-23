package com.apistyle.pbprestapi;

import com.apistyle.pbprestapi.dto.Profile;
import com.apistyle.pbprestapi.dto.Profiles;
import com.apistyle.pbprestapi.servcies.UsersServcie;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {
    @Autowired
    private UsersServcie usersServcie;

    @GetMapping("/profiles")
    public ResponseEntity<Profiles> getUserProfiles() throws Exception {
        Profiles profiles = usersServcie.getUserProfiles();
        if (profiles != null) {
            return ResponseEntity.ok(profiles);
        } else {
            throw new BadRequestException();
        }
    }
    @GetMapping("/profiles/{id}")
    public ResponseEntity<Profile> getUserProfiles(@PathVariable int id) throws Exception {
        Profile profiles = usersServcie.getUserProfiles(id);
        if (profiles != null) {
            return ResponseEntity.ok(profiles);
        } else {
            throw new BadRequestException();
        }
    }
}
