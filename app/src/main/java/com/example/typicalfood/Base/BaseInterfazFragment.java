package com.example.typicalfood.Base;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.typicalfood.Interface.Interfaz;

public abstract class BaseInterfazFragment extends Fragment {

    protected Interfaz mInterfaz;
    protected Activity actividad;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.actividad = (Activity) context;
            mInterfaz = (Interfaz) this.actividad;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
