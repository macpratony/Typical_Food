package com.example.typicalfood;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MenuFragment extends Fragment {


    private static final String TEXT = "text";

    public static MenuFragment newInstance(String text) {
        MenuFragment frag = new MenuFragment();

        Bundle args = new Bundle();
        args.putString(TEXT, text);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_menu, container, false);

        if (getArguments() != null) {
            ((TextView) layout.findViewById(R.id.text)).setText(getArguments().getString(TEXT));
        }

        return layout;
    }
}