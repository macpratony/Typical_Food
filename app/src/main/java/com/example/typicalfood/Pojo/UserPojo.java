package com.example.typicalfood.Pojo;

import com.google.firebase.firestore.DocumentReference;
import java.util.List;

public class UserPojo {
    private String email;
    private String name;
    private List<DocumentReference> favorites;


    public UserPojo() {
    }

    public UserPojo(String email, String name, List<DocumentReference> favorites) {
        this.email = email;
        this.name = name;
        this.favorites = favorites;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DocumentReference> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<DocumentReference> favorites) {
        this.favorites = favorites;
    }

}
