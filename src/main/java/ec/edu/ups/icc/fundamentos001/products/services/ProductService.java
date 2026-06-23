package ec.edu.ups.icc.fundamentos001.products.services;

import java.util.List;

import ec.edu.ups.icc.fundamentos001.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.UpdateProductDto;

public interface ProductService {
    List<ProductResponseDto> findAll();

    Object findOne(Long id);

    ProductResponseDto create(CreateProductDto dto);

    Object update(Long id, UpdateProductDto dto);

    Object partialUpdate(Long id, PartialUpdateProductDto dto);

    Object delete(Long id);
}
