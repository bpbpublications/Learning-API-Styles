package com.PBP.APIstyle;


import pbp.apistyle.soap.GetProfilesRequest;
import pbp.apistyle.soap.GetProfilesResponse;
import pbp.apistyle.soap.Profile;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.ArrayList;
import java.util.List;

@Endpoint
public class ProfileEndpoint {

    private static final String NAMESPACE_URI = "http://apistyle.pdb/soap";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProfilesRequest")
    @ResponsePayload
    public GetProfilesResponse getProfiles(@RequestPayload GetProfilesRequest request) {
        GetProfilesResponse response = new GetProfilesResponse();
        List<Profile> profiles = new ArrayList<>();

        Profile p1 = new Profile();
        p1.setId(1);
        p1.setName("John Doe");
        p1.setEmail("john@example.com");

        Profile p2 = new Profile();
        p2.setId(2);
        p2.setName("Jane Smith");
        p2.setEmail("jane@example.com");

        profiles.add(p1);
        profiles.add(p2);

        response.getProfile().addAll(profiles);
        return response;
    }
}
