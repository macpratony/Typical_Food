package com.example.typicalfood.Entity;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FavoritosPlatosTest {

    private FavoritosPlatos favPlatos;

    @Before
    public void setUp() {
        favPlatos = new FavoritosPlatos();
    }

    @Test
    public void testDefaultConstructor() {
        FavoritosPlatos fp = new FavoritosPlatos();
        assertNotNull(fp);
        assertNull(fp.getTitulo());
        assertNull(fp.getFoto());
        assertNull(fp.getDescripcion());
        assertNull(fp.getProvincia());
    }

    @Test
    public void testParameterizedConstructor() {
        FavoritosPlatos fp = new FavoritosPlatos(
                "Paella",
                "http://example.com/paella.jpg",
                "Plato tipico de Valencia",
                "Valencia"
        );
        assertEquals("Paella", fp.getTitulo());
        assertEquals("http://example.com/paella.jpg", fp.getFoto());
        assertEquals("Plato tipico de Valencia", fp.getDescripcion());
        assertEquals("Valencia", fp.getProvincia());
    }

    @Test
    public void testSetAndGetTitulo() {
        favPlatos.setTitulo("Gazpacho");
        assertEquals("Gazpacho", favPlatos.getTitulo());
    }

    @Test
    public void testSetAndGetFoto() {
        favPlatos.setFoto("http://example.com/gazpacho.jpg");
        assertEquals("http://example.com/gazpacho.jpg", favPlatos.getFoto());
    }

    @Test
    public void testSetAndGetDescripcion() {
        favPlatos.setDescripcion("Sopa fria tipica de Andalucia");
        assertEquals("Sopa fria tipica de Andalucia", favPlatos.getDescripcion());
    }

    @Test
    public void testSetAndGetProvincia() {
        favPlatos.setProvincia("Sevilla");
        assertEquals("Sevilla", favPlatos.getProvincia());
    }

    @Test
    public void testSetTituloNull() {
        favPlatos.setTitulo(null);
        assertNull(favPlatos.getTitulo());
    }

    @Test
    public void testSetFotoNull() {
        favPlatos.setFoto(null);
        assertNull(favPlatos.getFoto());
    }

    @Test
    public void testSetDescripcionNull() {
        favPlatos.setDescripcion(null);
        assertNull(favPlatos.getDescripcion());
    }

    @Test
    public void testSetProvinciaNull() {
        favPlatos.setProvincia(null);
        assertNull(favPlatos.getProvincia());
    }

    @Test
    public void testSetTituloEmpty() {
        favPlatos.setTitulo("");
        assertEquals("", favPlatos.getTitulo());
    }

    @Test
    public void testSetProvinciaEmpty() {
        favPlatos.setProvincia("");
        assertEquals("", favPlatos.getProvincia());
    }

    @Test
    public void testSettersOverwriteConstructorValues() {
        FavoritosPlatos fp = new FavoritosPlatos("Paella", "http://foto.jpg", "Desc original", "Valencia");
        fp.setTitulo("Tortilla");
        fp.setFoto("http://nueva.jpg");
        fp.setDescripcion("Desc nueva");
        fp.setProvincia("Madrid");
        assertEquals("Tortilla", fp.getTitulo());
        assertEquals("http://nueva.jpg", fp.getFoto());
        assertEquals("Desc nueva", fp.getDescripcion());
        assertEquals("Madrid", fp.getProvincia());
    }

    @Test
    public void testSerializable() {
        FavoritosPlatos fp = new FavoritosPlatos("Paella", "http://foto.jpg", "Desc", "Valencia");
        assertTrue(fp instanceof java.io.Serializable);
    }

    @Test
    public void testSpecialCharactersInFields() {
        FavoritosPlatos fp = new FavoritosPlatos(
                "Jamón ibérico",
                "http://example.com/foto?id=1&size=large",
                "Descripción con acentos y ñ",
                "Cádiz"
        );
        assertEquals("Jamón ibérico", fp.getTitulo());
        assertEquals("http://example.com/foto?id=1&size=large", fp.getFoto());
        assertEquals("Descripción con acentos y ñ", fp.getDescripcion());
        assertEquals("Cádiz", fp.getProvincia());
    }

    @Test
    public void testAllFieldsIndependent() {
        favPlatos.setTitulo("A");
        favPlatos.setFoto("B");
        favPlatos.setDescripcion("C");
        favPlatos.setProvincia("D");

        assertEquals("A", favPlatos.getTitulo());
        assertEquals("B", favPlatos.getFoto());
        assertEquals("C", favPlatos.getDescripcion());
        assertEquals("D", favPlatos.getProvincia());

        favPlatos.setTitulo("X");
        assertEquals("X", favPlatos.getTitulo());
        assertEquals("B", favPlatos.getFoto());
        assertEquals("C", favPlatos.getDescripcion());
        assertEquals("D", favPlatos.getProvincia());
    }

    @Test
    public void testSerializationRoundTrip() throws Exception {
        FavoritosPlatos original = new FavoritosPlatos("Paella", "http://foto.jpg", "Rica paella", "Valencia");

        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bos);
        oos.writeObject(original);
        oos.flush();

        java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
        java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bis);
        FavoritosPlatos deserialized = (FavoritosPlatos) ois.readObject();

        assertEquals(original.getTitulo(), deserialized.getTitulo());
        assertEquals(original.getFoto(), deserialized.getFoto());
        assertEquals(original.getDescripcion(), deserialized.getDescripcion());
        assertEquals(original.getProvincia(), deserialized.getProvincia());
    }
}
