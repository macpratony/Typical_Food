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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.typicalfood.Administrador.AdministradorFragment;
import com.example.typicalfood.Administrador.AgregarPlatosAdminFragment;
import com.example.typicalfood.AutenticacionActivity;
import com.example.typicalfood.Entity.FavoritosPlatos;
import com.example.typicalfood.Fragments.DetallePlatoFragment;
import com.example.typicalfood.Interface.Interfaz;
import com.example.typicalfood.PlatosFavoritos.FavoritosFragment;
import com.example.typicalfood.Entity.Platos;
import com.example.typicalfood.Adapter.AdapterPlatos;
import com.example.typicalfood.Fragments.PlatosFragment;
import com.example.typicalfood.Fragments.ProvinciasFragment;
import com.example.typicalfood.R;
import com.example.typicalfood.ScreenMainActivity;
import com.example.typicalfood.ViewModel.ViewModelFavorites;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static android.content.ContentValues.TAG;



@SuppressWarnings("ALL")
public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener, Interfaz {
    private DrawerLayout drawerLayout;
    private AdapterPlatos adapterPlatos;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private DocumentReference mDocumentReference;

    private RecyclerView mRecyclerView;
    private ArrayList<Platos> platosList;
    private PlatosFragment platosFragment;
    private DetallePlatoFragment detallePlatoFragment;


    private FragmentTransaction fragmentTransaction;
    private FavoritosFragment favoritoFragment;
    private DatabaseReference mDatabase;
    private DocumentReference documentRef;
    private ArrayList<DocumentReference> ref;

    private List<FavoritosPlatos> platosListFav = new ArrayList<FavoritosPlatos>();
    private FavoritosPlatos fav;

    private View header;
    protected ViewModelFavorites viewModel;

    private TextView nameUsuario;
    private TextView emailUsuario;
    private Menu menu;
    private String message;
    private String message1;
    private String message2;
    private String message3;

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

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();
        //viewModel = new ViewModelProvider(this).get(ViewModelFavorites.class);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        header = navigationView.getHeaderView(0);
        nameUsuario = (TextView) header.findViewById(R.id.nombreUsuario);
        emailUsuario = (TextView) header.findViewById(R.id.correoUsuario);

        navigationView.setNavigationItemSelectedListener(this);

        MenuItem menuItem = navigationView.getMenu().getItem(0);
        onNavigationItemSelected(menuItem);
        menuItem.setChecked(true);

        //Buscar item para ocultarlo dependiendo si ha iniciado sesión o no...
        menu = navigationView.getMenu();
        MenuItem login = menu.findItem(R.id.nav_login);
        MenuItem loguot = menu.findItem(R.id.nav_logout);

        drawerLayout.addDrawerListener(this);
        if(mAuth.getCurrentUser() != null){
            loguot.setVisible(true);
            login.setVisible(false);
            getUserInfo();
        }else{
            loguot.setVisible(false);
            login.setVisible(true);
        }

    }

    public void getUserInfo(){
        if(mAuth.getCurrentUser() != null){
            String id = mAuth.getCurrentUser().getUid();
            mFirestore.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        String nombre = documentSnapshot.getString("name");
                        String correo = documentSnapshot.getString("email");
                        nameUsuario.setText(nombre);
                        emailUsuario.setText(correo);
                    }
                }
            }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {
                    message = getString(R.string.mensaje13);
                    Toast.makeText(NavigationDrawerActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });

        }

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
                accesoAdmin();
               // fragmentManager.beginTransaction().replace(R.id.home_content, new AdministradorFragment()).commit();
                break;
            case R.id.nav_logout:
                title = R.string.menu_cerrar_sesion;
                //Cierra sesión y regresa a la pagina de autenticacion
                mAuth.signOut();
                startActivity(new Intent(NavigationDrawerActivity.this, NavigationDrawerActivity.class));
                finish();//Finaliza la tarea para que no pueda volver atras
                break;

            case R.id.nav_login:
                title = R.string.menu_iniciar_sesion;
                startActivity(new Intent(NavigationDrawerActivity.this, ScreenMainActivity.class));
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
    public AdapterPlatos getAllProvince(String ciudad) {
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
        mFirestore.collection("Provincias")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    if (document.getId().toLowerCase().equals(ciudad.toLowerCase())) {

                                        fragmentTransaction.replace(R.id.home_content, platosFragment);
                                        fragmentTransaction.addToBackStack(null); //Permite que al darle al boton de atras del movil regrese a la pagina anterior
                                        fragmentTransaction.commit();

                                        //Recoge los datos que hay en el arraylist de firebase para recorrerlo y extraer su valor
                                        ArrayList a = (ArrayList) document.getData().get("platos");
                                        for (int i = 0; i < a.size(); i++) {

                                            Log.d("", "*** "+((ArrayList) document.getData().get("platos")).get(i).getClass());
                                            Map<String, String> map = (Map<String, String>) a.get(i);
                                            for (Map.Entry<String, String> entrada : map.entrySet()) {
                                                //Variables para recoger los datos al recorrer el map
                                                String clave = entrada.getKey();
                                                String valor = entrada.getValue();

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
    }

    @Override
    public void enviarPlatos(Platos platos, String ciudad) {

        detallePlatoFragment = new DetallePlatoFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(ciudad.equals("A Coruña")){
            ciudad = ciudad.toUpperCase();
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("objeto", platos);
        bundle.putSerializable("provincia", ciudad);
        detallePlatoFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.home_content, detallePlatoFragment);
        fragmentTransaction.addToBackStack(null); //Permite que al darle al boton de atras del movil regrese a la pagina anterior
        fragmentTransaction.commit();

    }

    @Override
    public void enviarPlatosFavoritos(FavoritosPlatos platos) {
        detallePlatoFragment = new DetallePlatoFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putSerializable("objeto", platos);
        detallePlatoFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.home_content, detallePlatoFragment);
        fragmentTransaction.addToBackStack(null); //Permite que al darle al boton de atras del movil regrese a la pagina anterior
        fragmentTransaction.commit();
    }

    @Override
    public void regresar() {
        ProvinciasFragment provinciasFragment = new ProvinciasFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_content, provinciasFragment);
        fragmentTransaction.commit();
    }

    public void accesoAdmin(){
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(mAuth.getCurrentUser() != null){
            String id = mAuth.getCurrentUser().getUid();
            mFirestore.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        String nombre = documentSnapshot.getString("name");
                        String correo = documentSnapshot.getString("email");
                        if(nombre.equals("Marco") && correo.equals("marcoaph29@gmail.com")){
                            fragmentManager.beginTransaction().replace(R.id.home_content, new AdministradorFragment()).commit();
                        }else{
                            //Si no existe usuario registrado sale una ventana de alerta
                            alertDialogAdmin();
                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {
                    message = getString(R.string.mensaje13);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            alertDialogAdmin();
            Intent i = new Intent(getApplicationContext(), AutenticacionActivity.class);
           // startActivity(i);
        }

    }

    @Override
    public void accesAdministrator(String nombre, String correo) {
         AdministradorFragment admin = new AdministradorFragment();

        if(nombre.equals("Marco") && correo.equals("marcoaph29@gmail.com")){
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.home_content, admin);
            fragmentTransaction.commit();
        }else{
            //Si no existe usuario registrado sale una ventana de alerta
            alertDialogAdmin();
        }
    }

    @Override
    public void agregarPlatos() {
        AgregarPlatosAdminFragment agregar = new AgregarPlatosAdminFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_content, agregar);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    //Metodo que avisa al usuario que si desea entrar al item de administrador debe contar con una cuenta admin
    public void alertDialogAdmin(){
        //Si no existe usuario registrado sale una ventana de alerta
        AlertDialog.Builder alerta = new AlertDialog.Builder(NavigationDrawerActivity.this);
        message = getString(R.string.titulo);
        message1 = getString(R.string.mensaje_alert_dialog);
        message2 = getString(R.string.mensaje_si);
        message3 = getString(R.string.mensaje_no);

        alerta.setTitle(message)
                .setMessage(message1)
                .setPositiveButton(message2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), AutenticacionActivity.class);
                         startActivity(i);
                    }
                })
                .setNegativeButton(message3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        message = getString(R.string.mensaje_admin);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
        alerta.show();
    }

}