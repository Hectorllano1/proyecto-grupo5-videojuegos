package proyecto5.tiendavideojugos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import proyecto5.tiendavideojugos.entities.Videojuego;
import proyecto5.tiendavideojugos.services.FileStorageService;

/**
 * Configuración de Spring MVC:
 * <ul>
 *   <li>Publica la carpeta de subidas en la URL {@code /uploads/**} para poder mostrar las carátulas.</li>
 *   <li>Registra un conversor {@code String -> Videojuego} para que el formulario de pedidos pueda enviar
 *       solo los ids de los videojuegos seleccionados y Spring los convierta en objetos.</li>
 * </ul>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final FileStorageService almacenamiento;

    public WebConfig(FileStorageService almacenamiento) {
        this.almacenamiento = almacenamiento;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String ubicacion = almacenamiento.getRaiz().toUri().toString();
        if (!ubicacion.endsWith("/")) {
            ubicacion += "/";
        }
        registry.addResourceHandler("/uploads/**").addResourceLocations(ubicacion);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, Videojuego.class, id -> {
            if (id == null || id.isBlank()) {
                return null;
            }
            Videojuego videojuego = new Videojuego();
            videojuego.setId(Long.valueOf(id.trim()));
            return videojuego;
        });
    }
}
