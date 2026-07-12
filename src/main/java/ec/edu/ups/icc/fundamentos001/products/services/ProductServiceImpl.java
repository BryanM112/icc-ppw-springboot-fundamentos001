package ec.edu.ups.icc.fundamentos001.products.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos001.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.ProductFilterByCategoryDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.ProductFilterByUserDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos001.products.entities.ProductEntity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import ec.edu.ups.icc.fundamentos001.products.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos001.products.repositories.ProductRepository;
import ec.edu.ups.icc.fundamentos001.security.services.UserDetailsImpl;
import ec.edu.ups.icc.fundamentos001.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos001.users.repositories.UserRepository;
import ec.edu.ups.icc.fundamentos001.categories.entity.CategoryEntity;
import ec.edu.ups.icc.fundamentos001.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos001.core.exceptions.domain.BadRequestException;
import ec.edu.ups.icc.fundamentos001.core.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos001.core.exceptions.domain.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ec.edu.ups.icc.fundamentos001.core.dto.PaginationDto;

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
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponseDto findOne(Long id) {

    ProductEntity entity = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));

    if (entity.isDeleted()) {
        throw new NotFoundException("Product not found");
    }


    return ProductMapper.toResponse(entity);

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
                .map(ProductMapper::toResponse)
                .toList();
}


/*
 * Retorna los productos activos asociados a una categoría.
 *
 * Primero valida que la categoría exista y no esté eliminada.
 */



/*
 * Crea un producto asociado a un usuario y a una categoría.
 *
 * Valida:
 * - que el usuario exista
 * - que la categoría exista
 * - que no exista un producto activo con el mismo nombre
 */
@Override
public ProductResponseDto create(CreateProductDto dto, UserDetailsImpl currentUser) {

    

    UserEntity owner = findCurrentUserEntity(currentUser);

    Set<CategoryEntity> categories = validateAndGetCategories(dto.getCategoryIds());

    if (productRepository.findByNameIgnoreCaseAndDeletedFalse(dto.getName()).isPresent()) {
        throw new ConflictException("Product name already registered");
    }

    ProductEntity entity = new ProductEntity();

    entity.setName(dto.getName());
    entity.setPrice(dto.getPrice());
    entity.setStock(dto.getStock());
    entity.setOwner(owner);
    entity.setCategories(categories);

    ProductEntity savedEntity = productRepository.save(entity);

    return ProductMapper.toResponse(savedEntity);
}


/*
 * Actualiza completamente un producto activo.
 *
 * No permite cambiar el usuario propietario.
 * Sí permite cambiar la categoría.
 */
@Override
public ProductResponseDto update(
        Long id,
        UpdateProductDto dto,
        UserDetailsImpl currentUser
) {

    ProductEntity entity = productRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));

    validateOwnership(entity, currentUser);

    Set<CategoryEntity> categories =
            validateAndGetCategories(dto.getCategoryIds());

    entity.setName(dto.getName());
    entity.setPrice(dto.getPrice());
    entity.setStock(dto.getStock());
    entity.setCategories(categories);

    ProductEntity savedEntity = productRepository.save(entity);

    return ProductMapper.toResponse(savedEntity);
}


/*
 * Actualiza parcialmente un producto activo.
 *
 * Solo modifica los campos enviados en el DTO.
 */
@Override
public ProductResponseDto partialUpdate(
        Long id,
        PartialUpdateProductDto dto,
        UserDetailsImpl currentUser
) {

    ProductEntity entity = productRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));

    validateOwnership(entity, currentUser);

    if (dto.getName() != null) {
        entity.setName(dto.getName());
    }

    if (dto.getPrice() != null) {
        entity.setPrice(dto.getPrice());
    }

    if (dto.getStock() != null) {
        entity.setStock(dto.getStock());
    }

    if (dto.getCategoryIds() != null) {
        Set<CategoryEntity> categories =
                validateAndGetCategories(dto.getCategoryIds());

        entity.setCategories(categories);
    }

    ProductEntity savedEntity = productRepository.save(entity);

    return ProductMapper.toResponse(savedEntity);
}




@Override
public void delete(
        Long id,
        UserDetailsImpl currentUser
) {

    ProductEntity entity = productRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));

    validateOwnership(entity, currentUser);

    entity.setDeleted(true);

    productRepository.save(entity);
}


    /*
 * Retorna productos activos de un usuario aplicando filtros opcionales.
 *
 * Primero valida que el usuario exista y no esté eliminado.
 * Luego valida el rango de precios.
 * Finalmente consulta los productos desde ProductRepository.
 */
@Override
public List<ProductResponseDto> findByUserIdWithFilters(
        Long userId,
        ProductFilterByUserDto filters
) {
    if (!userRepository.existsByIdAndDeletedFalse(userId)) {
        throw new NotFoundException("User not found");
    }

    validateUserFilters(filters);

    String name = normalizeName(filters.getName());

    return productRepository.findByOwnerIdWithFilters(
                    userId,
                    name,
                    filters.getMinPrice(),
                    filters.getMaxPrice()
            )
            .stream()
            .map(ProductMapper::toResponse)
            .toList();
}

/*
 * Retorna productos activos de una categoría aplicando filtros opcionales.
 *
 * Primero valida que la categoría exista y no esté eliminada.
 * Luego valida el rango de precios.
 * Finalmente consulta los productos desde ProductRepository.
 */
//----------------------------------------------------------


/*
 * Valida reglas de negocio relacionadas con filtros.
 */
private void validateUserFilters(ProductFilterByUserDto filters) {

    if (filters == null) {
        return;
    }

    if (!filters.hasValidPriceRange()) {
        throw new BadRequestException("El precio máximo debe ser mayor o igual al precio mínimo");
    }



}


/*
 * Convierte un texto vacío en null.
 *
 * Esto permite que el repositorio ignore el filtro por nombre
 * cuando el query param llega vacío.
 */
private String normalizeName(String name) {

    if (name == null || name.isBlank()) {
        return null;
    }

    return name.trim();
}

@Override
public List<ProductResponseDto> findByCategoryIdWithFilters(
        Long categoryId,
        ProductFilterByCategoryDto filters
) {
    if (!categoryRepository.existsByIdAndDeletedFalse(categoryId)) {
        throw new NotFoundException("Category not found");
    }

    validateCategoryFilters(filters);

    String name = normalizeName(filters.getName());

    return productRepository.findByCategoryIdWithFilters(
                    categoryId,
                    name,
                    filters.getMinPrice(),
                    filters.getMaxPrice(),
                    filters.getUserId()
            )
            .stream()
            .map(ProductMapper::toResponse)
            .toList();
}

private void validateCategoryFilters(ProductFilterByCategoryDto filters) {

    if (filters == null) {
        return;
    }

    if (!filters.hasValidPriceRange()) {
        throw new BadRequestException("El precio máximo debe ser mayor o igual al precio mínimo");
    }

    if (filters.getUserId() != null &&
            !userRepository.existsByIdAndDeletedFalse(filters.getUserId())) {
        throw new NotFoundException("User not found");
    }
}


private Set<CategoryEntity> validateAndGetCategories(Set<Long> categoryIds) {

    if (categoryIds == null || categoryIds.isEmpty()) {
        throw new BadRequestException("Debe seleccionar al menos una categoría");
    }

    Set<CategoryEntity> categories = new HashSet<>();

    for (Long categoryId : categoryIds) {

        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (category.isDeleted()) {
            throw new NotFoundException("Category not found");
        }

        categories.add(category);
    }

    return categories;
}


@Override
@Transactional(readOnly = true)
public Page<ProductResponseDto> findAllPage(PaginationDto pagination) {

    Pageable pageable = createPageable(pagination);

    return productRepository.findActivePage(pageable)
            .map(ProductMapper::toResponse);
}

@Override
@Transactional(readOnly = true)
public Slice<ProductResponseDto> findAllSlice(
        PaginationDto pagination,
        UserDetailsImpl currentUser
) {

    if (currentUser == null) {
        throw new AccessDeniedException("Usuario no autenticado");
    }

    Pageable pageable = createPageable(pagination);

    return productRepository.findActiveSliceByOwnerId(
                    currentUser.getId(),
                    pageable
            )
            .map(ProductMapper::toResponse);
}




private Pageable createPageable(PaginationDto pagination) {

    String sortBy = normalizeSortBy(pagination.getSortBy());

    Sort.Direction direction = normalizeDirection(pagination.getDirection());

    Sort sort = Sort.by(direction, sortBy);

    return PageRequest.of(
            pagination.getPage(),
            pagination.getSize(),
            sort
    );
}

private String normalizeSortBy(String sortBy) {

    if (sortBy == null || sortBy.isBlank()) {
        return "id";
    }

    Set<String> allowedFields = Set.of(
            "id",
            "name",
            "price",
            "stock",
            "createdAt",
            "updatedAt"
    );

    if (!allowedFields.contains(sortBy)) {
        throw new BadRequestException("Campo de ordenamiento no permitido: " + sortBy);
    }

    return sortBy;
}

private Sort.Direction normalizeDirection(String direction) {

    if (direction == null || direction.isBlank()) {
        return Sort.Direction.ASC;
    }

    if (direction.equalsIgnoreCase("asc")) {
        return Sort.Direction.ASC;
    }

    if (direction.equalsIgnoreCase("desc")) {
        return Sort.Direction.DESC;
    }

    throw new BadRequestException("Dirección de ordenamiento no válida: " + direction);
}


@Override
@Transactional(readOnly = true)
public Page<ProductResponseDto> findByCategoryIdWithFiltersPage(
        Long categoryId,
        ProductFilterByCategoryDto filters,
        PaginationDto pagination
) {
    if (!categoryRepository.existsByIdAndDeletedFalse(categoryId)) {
        throw new NotFoundException("Category not found");
    }

    validateCategoryFilters(filters);

    String name = normalizeName(filters.getName());

    Pageable pageable = createPageable(pagination);

    return productRepository.findByCategoryIdWithFiltersPage(
                    categoryId,
                    name,
                    filters.getMinPrice(),
                    filters.getMaxPrice(),
                    filters.getUserId(),
                    pageable
            )
            .map(ProductMapper::toResponse);
}


@Override
@Transactional(readOnly = true)
public Slice<ProductResponseDto> findByCategoryIdWithFiltersSlice(
        Long categoryId,
        ProductFilterByCategoryDto filters,
        PaginationDto pagination
) {
    if (!categoryRepository.existsByIdAndDeletedFalse(categoryId)) {
        throw new NotFoundException("Category not found");
    }

    validateCategoryFilters(filters);

    String name = normalizeName(filters.getName());

    Pageable pageable = createPageable(pagination);

    return productRepository.findByCategoryIdWithFiltersSlice(
                    categoryId,
                    name,
                    filters.getMinPrice(),
                    filters.getMaxPrice(),
                    filters.getUserId(),
                    pageable
            )
            .map(ProductMapper::toResponse);
}

private UserEntity findCurrentUserEntity(UserDetailsImpl currentUser) {

    if (currentUser == null) {
        throw new AccessDeniedException("Usuario no autenticado");
    }

    return userRepository.findByIdAndDeletedFalse(currentUser.getId())
            .orElseThrow(() ->
                    new AccessDeniedException("Usuario no autorizado"));
}



private void validateOwnership(
        ProductEntity product,
        UserDetailsImpl currentUser
) {

    if (currentUser == null) {
        throw new AccessDeniedException("Usuario no autenticado");
    }

    if (hasRole(currentUser, "ROLE_ADMIN")) {
        return;
    }

    if (product.getOwner() == null || product.getOwner().getId() == null) {
        throw new AccessDeniedException(
                "El producto no tiene propietario válido");
    }

    if (!product.getOwner().getId().equals(currentUser.getId())) {
        throw new AccessDeniedException(
                "No puedes modificar productos ajenos");
    }
}



private boolean hasRole(
        UserDetailsImpl user,
        String role
) {
    return user.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authority -> authority.equals(role));
}   





}