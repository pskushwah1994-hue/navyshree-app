package com.navyshreecomputereducation.app;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public final class FirebaseHelper {
    private FirebaseHelper() {}

    public static void signInAnonymously(Runnable onSuccess, Runnable onFailure) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            onSuccess.run();
            return;
        }
        auth.signInAnonymously()
                .addOnSuccessListener(result -> onSuccess.run())
                .addOnFailureListener(error -> onFailure.run());
    }

    public static void saveStudent(String name, String mobile, String course,
                                   Runnable onSuccess, Runnable onFailure) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            onFailure.run();
            return;
        }

        String uid = auth.getCurrentUser().getUid();
        Map<String, Object> data = new HashMap<>();
        data.put("uid", uid);
        data.put("name", name);
        data.put("mobile", mobile);
        data.put("course", course);
        data.put("updatedAt", System.currentTimeMillis());

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("students")
                .child(uid);
        ref.updateChildren(data)
                .addOnSuccessListener(v -> onSuccess.run())
                .addOnFailureListener(error -> onFailure.run());
    }
}
