package ec.edu.ups.icc.fundamentos001.products.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.fundamentos001.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos001.products.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import ec.edu.ups.icc.fundamentos001.security.config.OpenApiConfig;
import ec.edu.ups.icc.fundamentos001.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ec.edu.ups.icc.fundamentos001.core.dto.PaginationDto;

import jakarta.validation.Valid;




@RestController
@RequestMapping("/products")
@Tag(
    name = "Productos",
    description = "Operaciones para consultar, crear, modificar y eliminar productos"
)
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }
        @Operation(
        summary = "Obtener todos los productos",
        description = "Busca todoso los productos existentes"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Producto encontrado correctamente"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No existe un token válido"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<ProductResponseDto> findAll() {
        return service.findAll();
    }

    @Operation(
        summary = "Obtener producto por ID",
        description = "Busca un único producto utilizando su identificador"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Producto encontrado correctamente"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No existe un token válido"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        )
    })
    @GetMapping("/{id}")
    public ProductResponseDto findOne(@PathVariable Long id) {
        return service.findOne(id);
    }

        @Operation(
        summary = "Crear",
        description = "Crea un producto"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Producto creado correctamente"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No existe un token válido"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto create(
            @Valid @RequestBody CreateProductDto dto,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return service.create(dto, currentUser);
    }

@Operation(
        summary = "Actualizar",
        description = "Actualiza todo un producto"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Producto actualizado correctamente"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No existe un token válido"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        )
    })
    @PutMapping("/{id}")
    public ProductResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductDto dto,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return service.update(id, dto, currentUser);
    }

    @Operation(
        summary = "Actualizar parcialmente",
        description = "Actualiza uno o varios campos de un producto"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Producto actualizado correctamente"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No existe un token válido"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        )
    })
    @PatchMapping("/{id}")
    public ProductResponseDto partialUpdate(
         @PathVariable Long id,
         @Valid @RequestBody PartialUpdateProductDto dto,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return service.partialUpdate(id, dto, currentUser);
    }

        @Operation(
        summary = "Eliminar producto por ID",
        description = "Elimina el producto utilizando su identificador"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Producto encontrado correctamente"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No existe un token válido"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        service.delete(id, currentUser);
}


        // Otros endpoints ...

     /*
     * Endpoint para buscar productos por id de usuario.
     *
     * GET /products/user/{userId}
     */
    @Operation(
        summary = "Buscar por id",
        description = "buscar productos por id de usuario"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Producto encontrado correctamente"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No existe un token válido"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        )
    })
    @GetMapping("/user/{userId}")
    public List<ProductResponseDto> findByUserId(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    /*
 * Endpoint paginado usando Page.
 *
 * GET /api/products/page
 * GET /api/products/page?page=0&size=5
 * 
 */

    @Operation(
    summary = "Listar productos paginados",
    description = "Devuelve una página de productos activos."
)
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Página obtenida correctamente"
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Parámetros de paginación inválidos"
    ),
    @ApiResponse(
        responseCode = "401",
        description = "Usuario no autenticado"
    )
})
    @GetMapping("/page")
    public Page<ProductResponseDto> findAllPage(
            @Valid @ModelAttribute PaginationDto pagination
    ) {
        return service.findAllPage(pagination);
    }

/*
 * Endpoint paginado usando Slice.
 *
 * GET /api/products/slice
 * GET /api/products/slice?page=0&size=5
 */
    @Operation(
    summary = "Listar productos con slice",
    description = "Devuelve una página de productos activos"
)
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Página obtenida correctamente"
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Parámetros de paginación inválidos"
    ),
    @ApiResponse(
        responseCode = "401",
        description = "Usuario no autenticado"
    )
})
@GetMapping("/slice")
public Slice<ProductResponseDto> findAllSlice(
        @Valid @ModelAttribute PaginationDto pagination,
        @AuthenticationPrincipal UserDetailsImpl currentUser
) {
    return service.findAllSlice(pagination, currentUser);
}

}