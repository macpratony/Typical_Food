package com.example.typicalfood.Adapter;

import android.content.Context;
import android.view.View;

import com.example.typicalfood.Entity.Platos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AdapterPlatosTest {

    @Mock
    private Context mockContext;

    @Mock
    private View.OnClickListener mockListener;

    private ArrayList<Platos> platosList;
    private AdapterPlatos adapter;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        platosList = new ArrayList<>();
        adapter = new AdapterPlatos(mockContext, 0, platosList);
    }

    @Test
    public void testGetItemCountEmpty() {
        assertEquals(0, adapter.getItemCount());
    }

    @Test
    public void testGetItemCountWithItems() {
        platosList.add(new Platos("Paella", "http://foto.jpg", "Desc"));
        platosList.add(new Platos("Gazpacho", "http://foto2.jpg", "Desc2"));
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testGetItemCountAfterAddingItems() {
        assertEquals(0, adapter.getItemCount());
        platosList.add(new Platos("Paella", "http://foto.jpg", "Desc"));
        assertEquals(1, adapter.getItemCount());
        platosList.add(new Platos("Gazpacho", "http://foto2.jpg", "Desc2"));
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testGetItemCountAfterRemovingItems() {
        platosList.add(new Platos("Paella", "http://foto.jpg", "Desc"));
        platosList.add(new Platos("Gazpacho", "http://foto2.jpg", "Desc2"));
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
        ArrayList<Platos> list = new ArrayList<>();
        list.add(new Platos("Tortilla", "http://foto.jpg", "Tortilla espanola"));
        list.add(new Platos("Cocido", "http://foto2.jpg", "Cocido madrileno"));
        list.add(new Platos("Fabada", "http://foto3.jpg", "Fabada asturiana"));
        AdapterPlatos adp = new AdapterPlatos(mockContext, 0, list);
        assertEquals(3, adp.getItemCount());
    }

    @Test
    public void testGetItemCountReflectsListState() {
        ArrayList<Platos> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(new Platos("Plato " + i, "http://foto" + i + ".jpg", "Desc " + i));
        }
        AdapterPlatos adp = new AdapterPlatos(mockContext, 0, list);
        assertEquals(50, adp.getItemCount());
    }
}
