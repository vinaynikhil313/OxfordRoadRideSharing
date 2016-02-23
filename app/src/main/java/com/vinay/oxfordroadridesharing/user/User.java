package com.vinay.oxfordroadridesharing.user;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vinay Nikhil Pabba on 17-01-2016.
 * POJO for retrieving all the User related information from Firebase
 */
public class User {
    private String accessToken;
    private String displayName;
    private String email;
    private boolean isTemporaryPassword;
    private boolean temporaryPassword;
    Map<String, Integer> petitions = new HashMap<> ();
    private String profileImageURL;
    private String provider;
    private String id;
    Map<String, String> preferences = new HashMap<> ();
    private String uid;

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public User () {
    }

    public String getAccessToken () {
        return accessToken;
    }

    public void setAccessToken (String accessToken) {
        this.accessToken = accessToken;
    }

    public String getDisplayName () {
        return displayName;
    }

    public void setDisplayName (String displayName) {
        this.displayName = displayName;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public void setTemporaryPassword (boolean temporaryPassword) {
        this.temporaryPassword = temporaryPassword;
    }

    public boolean isTemporaryPassword () {
        return isTemporaryPassword;
    }

    public void setIsTemporaryPassword (boolean isTemporaryPassword) {
        this.isTemporaryPassword = isTemporaryPassword;
    }

    public Map<String, Integer> getPetitions () {
        return petitions;
    }

    public void setPetitions (Map<String, Integer> petitions) {
        this.petitions = petitions;
    }

    public String getProfileImageURL () {
        return profileImageURL;
    }

    public void setProfileImageURL (String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getProvider () {
        return provider;
    }

    public void setProvider (String provider) {
        this.provider = provider;
    }

    public Map<String, String> getPreferences () {
        return preferences;
    }

    public void setPreferences (Map<String, String> preferences) {
        this.preferences = preferences;
    }

    public String getUid () {
        return uid;
    }

    public void setUid (String uid) {
        this.uid = uid;
    }
}
