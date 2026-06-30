package com.example.typicalfood.Administrador;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.typicalfood.Base.BaseInterfazFragment;
import com.example.typicalfood.R;
import com.example.typicalfood.Utils.FirebaseUserHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginAdminFragment extends BaseInterfazFragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private String message;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_admin, container, false);

        inicializar();
        comprobateUser();

        return view;
    }

    public void inicializar(){

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    public void comprobateUser(){
        if(mAuth.getCurrentUser() != null){
            FirebaseUserHelper.fetchCurrentUserInfo(mAuth, mFirestore, new FirebaseUserHelper.UserInfoCallback() {
                @Override
                public void onUserLoaded(String name, String email) {
                    mInterfaz.accesAdministrator(name, email);
                }

                @Override
                public void onError(Exception e) {
                    message = getString(R.string.mensaje13);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
