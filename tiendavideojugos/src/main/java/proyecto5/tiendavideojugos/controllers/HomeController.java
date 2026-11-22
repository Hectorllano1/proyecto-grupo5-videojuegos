package proyecto5.tiendavideojugos.controllers;

import proyecto5.tiendavideojugos.services.CategoriaService;
import proyecto5.tiendavideojugos.services.FichaTecnicaService;
import proyecto5.tiendavideojugos.services.VideojuegoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de la página de inicio con un pequeño resumen de datos.
 */
@Controller
public class HomeController {

    private final VideojuegoService videojuegoService;
    private final CategoriaService categoriaService;
    private final FichaTecnicaService fichaService;

    /**
     * @param videojuegoService servicio de videojuegos
     * @param categoriaService  servicio de categorías
     * @param fichaService      servicio de fichas técnicas
     */
    public HomeController(VideojuegoService videojuegoService, CategoriaService categoriaService,
                          FichaTecnicaService fichaService) {
        this.videojuegoService = videojuegoService;
        this.categoriaService = categoriaService;
        this.fichaService = fichaService;
    }

    /**
     * Muestra la portada.
     *
     * @param model modelo de la vista
     * @return nombre de la plantilla
     */
    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("totalVideojuegos", videojuegoService.contar());
        model.addAttribute("totalCategorias", categoriaService.contar());
        model.addAttribute("totalFichas", fichaService.contar());
        return "index";
    }
}

