package com.example.foodie.service;

import com.google.firebase.firestore.FirebaseFirestore;

public class Firebase {
    public FirebaseFirestore db;

    public Firebase() {
         db = FirebaseFirestore.getInstance();
    }
}
