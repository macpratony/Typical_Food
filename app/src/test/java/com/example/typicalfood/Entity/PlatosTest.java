package com.example.typicalfood.Entity;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlatosTest {

    private Platos platos;

    @Before
    public void setUp() {
        platos = new Platos();
    }

    @Test
    public void testDefaultConstructor() {
        Platos p = new Platos();
        assertNotNull(p);
        assertNull(p.getTitulo());
        assertNull(p.getFoto());
        assertNull(p.getDescripcion());
    }

    @Test
    public void testParameterizedConstructor() {
        Platos p = new Platos("Paella", "http://example.com/paella.jpg", "Plato tipico de Valencia");
        assertEquals("Paella", p.getTitulo());
        assertEquals("http://example.com/paella.jpg", p.getFoto());
        assertEquals("Plato tipico de Valencia", p.getDescripcion());
    }

    @Test
    public void testSetAndGetTitulo() {
        platos.setTitulo("Gazpacho");
        assertEquals("Gazpacho", platos.getTitulo());
    }

    @Test
    public void testSetAndGetFoto() {
        platos.setFoto("http://example.com/gazpacho.jpg");
        assertEquals("http://example.com/gazpacho.jpg", platos.getFoto());
    }

    @Test
    public void testSetAndGetDescripcion() {
        platos.setDescripcion("Sopa fria tipica de Andalucia");
        assertEquals("Sopa fria tipica de Andalucia", platos.getDescripcion());
    }

    @Test
    public void testSetTituloNull() {
        platos.setTitulo(null);
        assertNull(platos.getTitulo());
    }

    @Test
    public void testSetFotoNull() {
        platos.setFoto(null);
        assertNull(platos.getFoto());
    }

    @Test
    public void testSetDescripcionNull() {
        platos.setDescripcion(null);
        assertNull(platos.getDescripcion());
    }

    @Test
    public void testSetTituloEmpty() {
        platos.setTitulo("");
        assertEquals("", platos.getTitulo());
    }

    @Test
    public void testSetFotoEmpty() {
        platos.setFoto("");
        assertEquals("", platos.getFoto());
    }

    @Test
    public void testSetDescripcionEmpty() {
        platos.setDescripcion("");
        assertEquals("", platos.getDescripcion());
    }

    @Test
    public void testSettersOverwriteConstructorValues() {
        Platos p = new Platos("Paella", "http://foto.jpg", "Descripcion original");
        p.setTitulo("Tortilla");
        p.setFoto("http://nueva_foto.jpg");
        p.setDescripcion("Descripcion nueva");
        assertEquals("Tortilla", p.getTitulo());
        assertEquals("http://nueva_foto.jpg", p.getFoto());
        assertEquals("Descripcion nueva", p.getDescripcion());
    }

    @Test
    public void testSerializable() {
        Platos p = new Platos("Paella", "http://foto.jpg", "Descripcion");
        assertTrue(p instanceof java.io.Serializable);
    }

    @Test
    public void testSpecialCharactersInFields() {
        Platos p = new Platos("Jamón ibérico", "http://example.com/foto?id=1&size=large", "Descripción con acentos y ñ");
        assertEquals("Jamón ibérico", p.getTitulo());
        assertEquals("http://example.com/foto?id=1&size=large", p.getFoto());
        assertEquals("Descripción con acentos y ñ", p.getDescripcion());
    }

    @Test
    public void testLongStrings() {
        StringBuilder sbTitle = new StringBuilder();
        for (int i = 0; i < 1000; i++) sbTitle.append("A");
        String longTitle = sbTitle.toString();

        StringBuilder sbFoto = new StringBuilder("http://example.com/");
        for (int i = 0; i < 1000; i++) sbFoto.append("x");
        String longFoto = sbFoto.toString();

        StringBuilder sbDesc = new StringBuilder();
        for (int i = 0; i < 5000; i++) sbDesc.append("B");
        String longDesc = sbDesc.toString();

        Platos p = new Platos(longTitle, longFoto, longDesc);
        assertEquals(longTitle, p.getTitulo());
        assertEquals(longFoto, p.getFoto());
        assertEquals(longDesc, p.getDescripcion());
    }
}
