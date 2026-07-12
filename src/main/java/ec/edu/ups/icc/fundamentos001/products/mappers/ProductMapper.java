package ec.edu.ups.icc.fundamentos001.products.mappers;

import java.time.LocalDateTime;

import ec.edu.ups.icc.fundamentos001.categories.dtos.CategoryResponseDto;
import ec.edu.ups.icc.fundamentos001.categories.entity.CategoryEntity;
import ec.edu.ups.icc.fundamentos001.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos001.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos001.products.models.ProductModel;
import ec.edu.ups.icc.fundamentos001.users.mappers.UserMapper;

public class ProductMapper {

    public static ProductModel toModelFromDTO(CreateProductDto dto) {

        ProductModel model = new ProductModel();

        model.setName(dto.getName());
        model.setPrice(dto.getPrice());
        model.setStock(dto.getStock());
        model.setCreatedAt(LocalDateTime.now());

        return model;
    }

    public static ProductModel toModelFromEntity(ProductEntity entity) {

        ProductModel model = new ProductModel();

        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setPrice(entity.getPrice());
        model.setStock(entity.getStock());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setDeleted(entity.isDeleted());
        model.setCategories(entity.getCategories().stream().toList());

        return model;
    }

    public static ProductEntity toEntityFromModel(ProductModel model) {

        ProductEntity entity = new ProductEntity();

        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setPrice(model.getPrice());
        entity.setStock(model.getStock());

        return entity;
    }

    public static ProductResponseDto toResponse(ProductModel model) {

        ProductResponseDto response = new ProductResponseDto();

        response.setId(model.getId());
        response.setName(model.getName());
        response.setPrice(model.getPrice());
        response.setStock(model.getStock());
        response.setCategories(model.getCategories().stream().map(ProductMapper::toCategoryResponse).toList());
        

        return response;
    }




    public static ProductResponseDto toResponse(ProductEntity entity) {

        ProductResponseDto response = new ProductResponseDto();


        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setPrice(entity.getPrice());
        response.setStock(entity.getStock());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        response.setOwner(UserMapper.toResponse(
                UserMapper.toModelFromEntity(entity.getOwner())
     ));

        response.setCategories(entity.getCategories().stream().map(ProductMapper::toCategoryResponse).toList());


        return response;
    }

    private static CategoryResponseDto toCategoryResponse(CategoryEntity entity) {

        CategoryResponseDto response = new CategoryResponseDto();

        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());

        return response;
    }
}