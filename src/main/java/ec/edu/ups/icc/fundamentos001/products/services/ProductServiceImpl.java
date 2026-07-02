package ec.edu.ups.icc.fundamentos001.products.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos001.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos001.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos001.products.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos001.products.models.ProductModel;
import ec.edu.ups.icc.fundamentos001.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos001.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos001.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos001.users.repositories.UserRepository;
import ec.edu.ups.icc.fundamentos001.categories.dtos.CategoryResponseDto;
import ec.edu.ups.icc.fundamentos001.categories.entity.CategoryEntity;
import ec.edu.ups.icc.fundamentos001.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos001.core.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos001.core.exceptions.domain.NotFoundException;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(
            ProductRepository productRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ProductResponseDto> findAll() {
        return productRepository.findByDeletedFalse()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductResponseDto findOne(Long id) {

    ProductEntity entity = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));

    if (entity.isDeleted()) {
        throw new NotFoundException("Product not found");
    }

    ProductModel model = ProductMapper.toModelFromEntity(entity);

    return toResponse(entity);

    }

    /*
 * Retorna los productos activos creados por un usuario.
 *
 * Primero valida que el usuario exista y no esté eliminado.
 */
@Override
public List<ProductResponseDto> findByUserId(Long userId) {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) {
            throw new NotFoundException("User not found");
        }

        List<ProductEntity> list = productRepository.findByOwner_IdAndDeletedFalse(userId);

        return list
                .stream()
                .map(this::toResponse)
                .toList();
}


/*
 * Retorna los productos activos asociados a una categoría.
 *
 * Primero valida que la categoría exista y no esté eliminada.
 */
@Override
public List<ProductResponseDto> findByCategoryId(Long categoryId) {

    if (!categoryRepository.existsByIdAndDeletedFalse(categoryId)) {
        throw new NotFoundException("Category not found");
    }

    return productRepository.findByCategory_IdAndDeletedFalse(categoryId)
            .stream()
            .map(this::toResponse)
            .toList();
}


/*
 * Crea un producto asociado a un usuario y a una categoría.
 *
 * Valida:
 * - que el usuario exista
 * - que la categoría exista
 * - que no exista un producto activo con el mismo nombre
 */
@Override
public ProductResponseDto create(CreateProductDto dto) {

        // 1 Encontramos el user
    UserEntity owner = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new NotFoundException("User not found"));

    if (owner.isDeleted()) {
        throw new NotFoundException("User not found");
    }

        // 2 Encontramos la categoria
    CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new NotFoundException("Category not found"));

    if (category.isDeleted()) {
        throw new NotFoundException("Category not found");
    }

// validadacion de negocio, por ejemplo que no exista un producto  con el mismo nombre
    if (productRepository.findByNameIgnoreCaseAndDeletedFalse(dto.getName()).isPresent()) {
        throw new ConflictException("Product name already registered");
    }


        // Genereamos la entidad a partir del DTO

    ProductEntity entity = new ProductEntity();

    entity.setName(dto.getName());
    entity.setPrice(dto.getPrice());
    entity.setStock(dto.getStock());
    entity.setOwner(owner);
    entity.setCategory(category);

 ProductEntity savedEntity = productRepository.save(entity);

        ProductModel savedModel = ProductMapper.toModelFromEntity(savedEntity);

        return toResponse(savedEntity);
}


/*
 * Actualiza completamente un producto activo.
 *
 * No permite cambiar el usuario propietario.
 * Sí permite cambiar la categoría.
 */
@Override
public ProductResponseDto update(Long id, UpdateProductDto dto) {

    ProductEntity entity = productRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));

    CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new NotFoundException("Category not found"));

    if (category.isDeleted()) {
        throw new NotFoundException("Category not found");
    }

    entity.setName(dto.getName());
    entity.setPrice(dto.getPrice());
    entity.setStock(dto.getStock());
    entity.setCategory(category);

        ProductEntity savedEntity = productRepository.save(entity);


        return toResponse(savedEntity);
}


/*
 * Actualiza parcialmente un producto activo.
 *
 * Solo modifica los campos enviados en el DTO.
 */
@Override
public ProductResponseDto partialUpdate(Long id, PartialUpdateProductDto dto) {

    ProductEntity entity = productRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));

    if (dto.getName() != null) {
        entity.setName(dto.getName());
    }

    if (dto.getPrice() != null) {
        entity.setPrice(dto.getPrice());
    }

    if (dto.getStock() != null) {
        entity.setStock(dto.getStock());
    }

    if (dto.getCategoryId() != null) {
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (category.isDeleted()) {
            throw new NotFoundException("Category not found");
        }

        entity.setCategory(category);
    }

        ProductEntity savedEntity = productRepository.save(entity);


        return toResponse(savedEntity);
}




    @Override
    public void delete(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (entity.isDeleted()) {
            throw new NotFoundException("Product not found");
        }

        entity.setDeleted(true);
        productRepository.save(entity);
    }


    private ProductResponseDto toResponse(ProductEntity entity) {

    ProductResponseDto dto = new ProductResponseDto();

    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setPrice(entity.getPrice());
    dto.setStock(entity.getStock());
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setUpdatedAt(entity.getUpdatedAt());

    dto.setOwner(UserMapper.toResponse(
            UserMapper.toModelFromEntity(entity.getOwner())
    ));

    dto.setCategory(categoryToResponse(entity.getCategory()));

    return dto;
}

private CategoryResponseDto categoryToResponse(CategoryEntity entity) {

    CategoryResponseDto dto = new CategoryResponseDto();

    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());

    return dto;
}
}