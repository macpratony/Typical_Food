package com.example.typicalfood.Utils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public final class FirebaseUserHelper {

    private FirebaseUserHelper() {}

    public interface UserInfoCallback {
        void onUserLoaded(String name, String email);
        void onError(Exception e);
    }

    public static void fetchCurrentUserInfo(FirebaseAuth auth, FirebaseFirestore firestore,
                                            UserInfoCallback callback) {
        if (auth.getCurrentUser() == null) {
            return;
        }
        String id = auth.getCurrentUser().getUid();
        firestore.collection("Users").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String nombre = documentSnapshot.getString("name");
                            String correo = documentSnapshot.getString("email");
                            callback.onUserLoaded(nombre, correo);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError(e);
                    }
                });
    }
}
