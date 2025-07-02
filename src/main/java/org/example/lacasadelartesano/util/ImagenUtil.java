package org.example.lacasadelartesano.util;

import javafx.scene.image.Image;

import java.io.*;

public class ImagenUtil {

    // Convertir archivo a byte[]
    public static byte[] convertirArchivoABytes(File archivo) {
        try (FileInputStream fis = new FileInputStream(archivo);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int leido;
            while ((leido = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, leido);
            }

            return baos.toByteArray();
        } catch (IOException e) {
            System.out.println("Error al convertir archivo a bytes:");
            e.printStackTrace();
            return null;
        }
    }

    // Convertir byte[] a Image (para mostrar en JavaFX)
    public static Image convertirBytesAImagen(byte[] datos) {
        if (datos == null || datos.length == 0) {
            return null;
        }

        try (ByteArrayInputStream bais = new ByteArrayInputStream(datos)) {
            return new Image(bais);
        } catch (Exception e) {
            System.out.println("Error al convertir bytes a imagen:");
            e.printStackTrace();
            return null;
        }
    }

    // Cargar imagen por defecto desde recursos o archivo
    public static Image cargarImagenPorDefecto() {
        try (InputStream is = ImagenUtil.class.getResourceAsStream("/img/default.png")) {
            if (is != null) {
                return new Image(is);
            } else {
                System.out.println("Imagen por defecto no encontrada.");
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error al cargar imagen por defecto:");
            e.printStackTrace();
            return null;
        }




    }

    public static byte[] obtenerBytesImagenPorDefecto() {
        try (InputStream is = ImagenUtil.class.getResourceAsStream("/img/default.png")) {
            return is != null ? is.readAllBytes() : new byte[0];
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }


}
