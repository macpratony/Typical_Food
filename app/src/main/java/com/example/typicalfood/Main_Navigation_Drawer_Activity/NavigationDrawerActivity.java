package com.example.typicalfood.Main_Navigation_Drawer_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.typicalfood.Administrador.AdministradorFragment;
import com.example.typicalfood.Fragments.DetallePlatoFragment;
import com.example.typicalfood.Interface.Interfaz;
import com.example.typicalfood.PlatosFavoritos.FavoritosFragment;
import com.example.typicalfood.Entity.Platos;
import com.example.typicalfood.Adapter.AdapterPlatos;
import com.example.typicalfood.Fragments.PlatosFragment;
import com.example.typicalfood.Fragments.ProvinciasFragment;
import com.example.typicalfood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener, Interfaz {
    private DrawerLayout drawerLayout;
    private FirebaseFirestore db;
    private AdapterPlatos adapterPlatos;

    private RecyclerView mRecyclerView;
    private ArrayList<Platos> platosList;
    private PlatosFragment platosFragment;
    private DetallePlatoFragment detallePlatoFragment;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        db = FirebaseFirestore.getInstance();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // mRecyclerView.setLayoutManager(layoutManager);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuItem menuItem = navigationView.getMenu().getItem(0);
        onNavigationItemSelected(menuItem);
        menuItem.setChecked(true);

        drawerLayout.addDrawerListener(this);

    }

    //Este metodo hace que si está el menu desplegado al darle a la tecla atras se oculte el menú
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
        //el drawer se ha abierto completamente
//        Toast.makeText(this, getString(R.string.navigation_drawer_open),
//                Toast.LENGTH_SHORT).show();
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.nav_provincia:
                title = R.string.menu_provincia;
                fragmentManager.beginTransaction().replace(R.id.home_content, new ProvinciasFragment()).commit();
                break;
            case R.id.nav_favorito:
                title = R.string.menu_platos;
                fragmentManager.beginTransaction().replace(R.id.home_content, new FavoritosFragment()).commit();
                break;
            case R.id.nav_admin:
                title = R.string.menu_administrador;
                fragmentManager.beginTransaction().replace(R.id.home_content, new AdministradorFragment()).commit();
                break;
            case R.id.nav_logout:
                title = R.string.menu_cerrar_sesion;
                break;
            default:
                throw new IllegalArgumentException("menu option not implemented!!");
        }

        setTitle(getString(title));
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Se le pasa como parametros el nombre de la ciudad
     *
     * @param ciudad
     */
    private String titulo = "";
    private String foto = "";
    private String descripcion = "";
    @Override
    public AdapterPlatos getAllUser(String ciudad) {
        platosList = null;
        platosList = new ArrayList<>();
        platosFragment = new PlatosFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();


        Bundle bundle = new Bundle();
        bundle.putSerializable("city",platosList);
        bundle.putSerializable("provincia", ciudad);
        platosFragment.setArguments(bundle);

        //Method get collection dataBase of Firebase
        // [START get_all_users]
        db.collection("Provincias")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    if (document.getId().equals(ciudad)) {

                                        fragmentTransaction.replace(R.id.home_content, platosFragment);
                                        fragmentTransaction.commit();

                                        //Recoge los datos que hay en el arraylist de firebase para recorrerlo y extraer su valor
                                        ArrayList a = (ArrayList) document.getData().get("platos");
                                        for (int i = 0; i < a.size(); i++) {
                                            Map<String, String> map = (Map<String, String>) a.get(i);
                                            for (Map.Entry<String, String> entrada : map.entrySet()) {

                                                //Variables para recoger los datos al recorrer el map
                                                String clave = entrada.getKey();
                                                String valor = entrada.getValue();

                                                Log.d(TAG, clave+" "+valor);

                                                if(clave.equals("descripcion")){
                                                    descripcion = valor;
                                                }
                                                if (clave.equals("foto")) {
                                                    foto = valor;
                                                }
                                                if (clave.equals("titulo")) {
                                                    titulo = valor;
                                                }
                                            }
                                            if(titulo != "" && foto != "" && descripcion != ""){
                                                platosList.add(new Platos(titulo,foto, descripcion));
                                            }

                                            adapterPlatos = new AdapterPlatos(getBaseContext(),R.layout.item_platos_provincia, platosList);
                                        }
                                        // Toast.makeText(getContext(),  titulo, Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(NavigationDrawerActivity.this, "No es madrid", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(NavigationDrawerActivity.this, "No hay datos que cargar", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });

        return adapterPlatos;
        // [END get_all_users]
    }

    @Override
    public void enviarPlatos(Platos platos) {
        detallePlatoFragment = new DetallePlatoFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putSerializable("objeto", platos);
        detallePlatoFragment.setArguments(bundle);


        fragmentTransaction.replace(R.id.home_content, detallePlatoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void regresar() {
        ProvinciasFragment provinciasFragment = new ProvinciasFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_content, provinciasFragment);
        fragmentTransaction.commit();
    }

}