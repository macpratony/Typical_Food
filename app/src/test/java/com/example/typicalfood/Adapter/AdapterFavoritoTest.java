package com.example.typicalfood.Adapter;

import android.content.Context;
import android.view.View;

import com.example.typicalfood.Entity.FavoritosPlatos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AdapterFavoritoTest {

    @Mock
    private Context mockContext;

    @Mock
    private View.OnClickListener mockListener;

    private ArrayList<FavoritosPlatos> platosList;
    private AdapterFavorito adapter;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        platosList = new ArrayList<>();
        adapter = new AdapterFavorito(mockContext, 0, platosList);
    }

    @Test
    public void testGetItemCountEmpty() {
        assertEquals(0, adapter.getItemCount());
    }

    @Test
    public void testGetItemCountWithItems() {
        platosList.add(new FavoritosPlatos("Paella", "http://foto.jpg", "Desc", "Valencia"));
        platosList.add(new FavoritosPlatos("Gazpacho", "http://foto2.jpg", "Desc2", "Sevilla"));
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testGetItemCountAfterAddingItems() {
        assertEquals(0, adapter.getItemCount());
        platosList.add(new FavoritosPlatos("Paella", "http://foto.jpg", "Desc", "Valencia"));
        assertEquals(1, adapter.getItemCount());
        platosList.add(new FavoritosPlatos("Gazpacho", "http://foto2.jpg", "Desc2", "Sevilla"));
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testGetItemCountAfterRemovingItems() {
        platosList.add(new FavoritosPlatos("Paella", "http://foto.jpg", "Desc", "Valencia"));
        platosList.add(new FavoritosPlatos("Gazpacho", "http://foto2.jpg", "Desc2", "Sevilla"));
        assertEquals(2, adapter.getItemCount());
        platosList.remove(0);
        assertEquals(1, adapter.getItemCount());
    }

    @Test
    public void testSetOnClickListener() {
        // Should not throw
        adapter.setOnclickListener(mockListener);
    }

    @Test
    public void testOnClickWithNullListener() {
        // Should not throw when listener is null
        adapter.onClick(null);
    }

    @Test
    public void testAdapterCreationWithNonEmptyList() {
        ArrayList<FavoritosPlatos> list = new ArrayList<>();
        list.add(new FavoritosPlatos("Tortilla", "http://foto.jpg", "Desc", "Madrid"));
        list.add(new FavoritosPlatos("Cocido", "http://foto2.jpg", "Desc2", "Madrid"));
        list.add(new FavoritosPlatos("Fabada", "http://foto3.jpg", "Desc3", "Asturias"));
        AdapterFavorito adp = new AdapterFavorito(mockContext, 0, list);
        assertEquals(3, adp.getItemCount());
    }

    @Test
    public void testGetItemCountReflectsListState() {
        ArrayList<FavoritosPlatos> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(new FavoritosPlatos("Plato " + i, "http://foto" + i + ".jpg", "Desc " + i, "Provincia " + i));
        }
        AdapterFavorito adp = new AdapterFavorito(mockContext, 0, list);
        assertEquals(50, adp.getItemCount());
    }

    @Test
    public void testClearListUpdatesItemCount() {
        platosList.add(new FavoritosPlatos("Paella", "http://foto.jpg", "Desc", "Valencia"));
        platosList.add(new FavoritosPlatos("Gazpacho", "http://foto2.jpg", "Desc2", "Sevilla"));
        assertEquals(2, adapter.getItemCount());
        platosList.clear();
        assertEquals(0, adapter.getItemCount());
    }
}
