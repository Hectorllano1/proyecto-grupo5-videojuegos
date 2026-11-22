package es.gamestore.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Servicio que guarda y elimina imágenes en el sistema de ficheros del servidor.
 * <p>
 * Los ficheros se almacenan en la carpeta configurada en {@code app.upload-dir} con un nombre
 * aleatorio (UUID) para evitar colisiones y ataques de path traversal. Después se sirven
 * públicamente en la ruta {@code /uploads/**} (ver {@code WebConfig}).
 * </p>
 */
@Service
public class FileStorageService {

    /** Extensiones de imagen permitidas. */
    private static final Set<String> EXTENSIONES = Set.of("jpg", "jpeg", "png", "gif", "webp");

    /** Carpeta raíz donde se guardan los ficheros. */
    private final Path raiz;

    /**
     * Crea el servicio y la carpeta de subidas si no existe.
     *
     * @param directorio ruta de la carpeta de subidas (propiedad {@code app.upload-dir})
     */
    public FileStorageService(@Value("${app.upload-dir:uploads}") String directorio) {
        this.raiz = Paths.get(directorio).toAbsolutePath().normalize();
        try {
            Files.createDirectories(raiz);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo crear la carpeta de subidas", e);
        }
    }

    /**
     * Guarda una imagen en el servidor.
     *
     * @param archivo fichero recibido del formulario
     * @return el nombre con el que se ha guardado (para almacenarlo en la base de datos)
     * @throws IllegalArgumentException si el fichero no es una imagen válida
     */
    public String guardar(MultipartFile archivo) {
        String original = archivo.getOriginalFilename() == null ? "" : archivo.getOriginalFilename();
        int punto = original.lastIndexOf('.');
        String extension = punto >= 0 ? original.substring(punto + 1).toLowerCase(Locale.ROOT) : "";
        String tipo = archivo.getContentType();

        if (!EXTENSIONES.contains(extension) || tipo == null || !tipo.startsWith("image/")) {
            throw new IllegalArgumentException("El fichero debe ser una imagen (jpg, png, gif o webp)");
        }

        String nombre = UUID.randomUUID() + "." + extension;
        try {
            Files.copy(archivo.getInputStream(), raiz.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar la imagen", e);
        }
        return nombre;
    }

    /**
     * Elimina una imagen del servidor. Si no existe, no hace nada.
     *
     * @param nombre nombre del fichero guardado
     */
    public void eliminar(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return;
        }
        try {
            Files.deleteIfExists(raiz.resolve(nombre).normalize());
        } catch (IOException e) {
            // No es crítico: se ignora para no impedir el borrado del registro
        }
    }

    /**
     * @return la ruta absoluta de la carpeta de subidas
     */
    public Path getRaiz() {
        return raiz;
    }
}
