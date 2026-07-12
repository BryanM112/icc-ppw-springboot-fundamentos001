package ec.edu.ups.icc.fundamentos001.categories.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.fundamentos001.categories.dtos.CategoryResponseDto;
import ec.edu.ups.icc.fundamentos001.categories.dtos.CreateCategoryDto;
import ec.edu.ups.icc.fundamentos001.categories.dtos.UpdateCategoryDto;
import ec.edu.ups.icc.fundamentos001.categories.services.CategoryService;
import jakarta.validation.Valid;
import ec.edu.ups.icc.fundamentos001.products.dtos.ProductFilterByCategoryDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.ProductFilterByUserDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos001.products.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import ec.edu.ups.icc.fundamentos001.core.dto.PaginationDto;

/*
 * Controlador REST encargado de exponer los endpoints HTTP
 * para la gestión de categorías.
 */
@RestController
@RequestMapping("/categories")
public class CategoriesController {

    private final CategoryService service;  
    private final ProductService productService;

    public CategoriesController(CategoryService service, ProductService productService) {
        this.service = service;
        this.productService = productService;
    }

    @GetMapping
    public List<CategoryResponseDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public CategoryResponseDto findOne(@PathVariable Long id) {
        return service.findOne(id);
    }

    @PostMapping
    public CategoryResponseDto create(@Valid @RequestBody CreateCategoryDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public CategoryResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryDto dto
    ) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


    @GetMapping("/{id}/products")
    public List<ProductResponseDto> findProductsByCategory(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductFilterByCategoryDto filters
    ) {
        return productService.findByCategoryIdWithFilters(id, filters);
    }


    @GetMapping("/{id}/products/page")
public Page<ProductResponseDto> findProductsByCategoryPage(
        @PathVariable Long id,
        @Valid @ModelAttribute ProductFilterByCategoryDto filters,
        @Valid @ModelAttribute PaginationDto pagination
) {
    return productService.findByCategoryIdWithFiltersPage(
            id,
            filters,
            pagination
    );
}


@GetMapping("/{id}/products/slice")
public Slice<ProductResponseDto> findProductsByCategorySlice(
        @PathVariable Long id,
        @Valid @ModelAttribute ProductFilterByCategoryDto filters,
        @Valid @ModelAttribute PaginationDto pagination
) {
    return productService.findByCategoryIdWithFiltersSlice(
            id,
            filters,
            pagination
    );
}
}