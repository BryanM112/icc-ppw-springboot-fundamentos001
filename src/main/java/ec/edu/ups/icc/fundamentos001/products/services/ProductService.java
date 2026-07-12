package ec.edu.ups.icc.fundamentos001.products.services;

import java.util.List;

import ec.edu.ups.icc.fundamentos001.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.ProductFilterByCategoryDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.ProductFilterByUserDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.UpdateProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import ec.edu.ups.icc.fundamentos001.security.services.UserDetailsImpl;

import ec.edu.ups.icc.fundamentos001.core.dto.PaginationDto;

public interface ProductService {

    List<ProductResponseDto> findAll();

    ProductResponseDto findOne(Long id);

    ProductResponseDto create(CreateProductDto dto, UserDetailsImpl currentUser);

    ProductResponseDto update(Long id, UpdateProductDto dto, UserDetailsImpl currentUser);

    ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto, UserDetailsImpl currentUser);

    void delete(Long id, UserDetailsImpl currentUser);

    List<ProductResponseDto> findByUserId(Long userId);


    List<ProductResponseDto> findByUserIdWithFilters(
            Long userId,
            ProductFilterByUserDto filters
    );

        List<ProductResponseDto> findByCategoryIdWithFilters(
                Long categoryId,
                ProductFilterByCategoryDto filters
        );

        /*
 * Retorna productos activos usando Page.
 */
Page<ProductResponseDto> findAllPage(PaginationDto pagination);

/*
 * Retorna productos activos usando Slice.
 */
Slice<ProductResponseDto> findAllSlice(PaginationDto pagination, UserDetailsImpl currentUser);


Page<ProductResponseDto> findByCategoryIdWithFiltersPage(
        Long categoryId,
        ProductFilterByCategoryDto filters,
        PaginationDto pagination
);

Slice<ProductResponseDto> findByCategoryIdWithFiltersSlice(
        Long categoryId,
        ProductFilterByCategoryDto filters,
        PaginationDto pagination
);


}