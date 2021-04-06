package com.example.typicalfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuItem menuItem = navigationView.getMenu().getItem(0);
        onNavigationItemSelected(menuItem);
        menuItem.setChecked(true);

        drawerLayout.addDrawerListener(this);

        View header = navigationView.getHeaderView(0);
        /*header.findViewById(R.id.header_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NavigationDrawerActivity.this, getString(R.string.title_click),
                        Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int title;
        switch (item.getItemId()) {
            case R.id.nav_favorito:
                title = R.string.menu_platos;
                break;
            case R.id.nav_admin:
                title = R.string.menu_administrador;
                break;
            case R.id.nav_logout:
                title = R.string.menu_cerrar_sesion;
                break;
            default:
                throw new IllegalArgumentException("menu option not implemented!!");
        }
        Fragment fragment = MenuFragment.newInstance(getString(title));
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_enter, R.anim.nav_exit)
                .replace(R.id.home_content, fragment)
                .commit();

        setTitle(getString(title));

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}