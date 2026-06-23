package ec.edu.ups.icc.fundamentos001.products.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.fundamentos001.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos001.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos001.products.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos001.products.models.ProductModel;



@RestController
@RequestMapping("/products")
public class ProductController {
    private List<ProductModel> products = new ArrayList<>();

    private int currentId = 1;

    @GetMapping
    public List<ProductResponseDto> findAll() {

        // Programación tradicional iterativa para mapear cada User a UserResponseDto
        //List<UserResponseDto> dtos = new ArrayList<>();
        //for (UserModel user : users) {
        //    dtos.add(UserMapper.toResponse(user));
        //}
        //return dtos;

        // Programación funcional para mapear cada User a UserResponseDto
        return products.stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public Object findOne(@PathVariable Long id) {

      // Programación tradicional iterativa para mapear cada User a UserResponseDto
      // Busqueda Lineal
       // for (UserModel user : users) {
        //    if (user.getId().equals(id)) {
        //        return UserMapper.toResponse(user);
        //    }
        //}
        //return new Object() {
        //    public String error = "User not found";
        //};

      // Programación funcional para mapear cada User a UserResponseDto
      // Busqueda Lineal
    return products.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
              .map(product -> (Object) ProductMapper.toResponse(product))
            .orElseGet(() -> new Object() {
                public String error = "Product not found";
            });
    }



    @PostMapping
    public ProductResponseDto create(@RequestBody CreateProductDto dto) {

    ProductModel product = ProductMapper.toModel(dto);

    product.setId((long) currentId++);

    products.add(product);

    return ProductMapper.toResponse(product);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable Long id, @RequestBody UpdateProductDto dto) {

        // Programacion tradicional iterativa
        //for (UserModel user : users) {
        //if (user.getId().equals(id)) {
        //user.setName(dto.getName());
        //user.setEmail(dto.getEmail());
        //return UserMapper.toResponse(user);
        //}
        //}
        //return new Object() {
        //public String error = "UserModel not found";
        //};

        // Programacion funcional
        ProductModel product = products.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
        if (product == null)
            return new Object() {
                public String error = "ProductModel not found";
            };

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        return ProductMapper.toResponse(product);
    }


    @PatchMapping("/{id}")
    public Object partialUpdate(@PathVariable Long id, @RequestBody PartialUpdateProductDto dto) {

        // Programacion tradicional iterativa
        //for (UserModel user : users) {
            // ESTE ES EL CAMBIO pero deberia estar en un metodo aparte para evitar
            // duplicacion de codigo y mejorar mantenibilidad con separacion de
            // responsabilidades.
        //    if (user.getId().equals(id)) {
        //        if (dto.getName() != null)
        //            user.setName(dto.getName());
        //        if (dto.getEmail() != null)
        //            user.setEmail(dto.getEmail());
        //        return UserMapper.toResponse(user);
        //    }
        //}
        //return new Object() {
        //    public String error = "UserModel not found";
        //};

        // Programación funcional
        // Búsqueda lineal del usuario por id
        ProductModel product = products.stream().filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (product == null)
            return new Object() {
                public String error = "ProductModel not found";
            };

        if (dto.getName() != null)
            product.setName(dto.getName());
        if (dto.getStock() != null)
            product.setStock(dto.getStock());
        if (dto.getPrice() != null)
            product.setPrice(dto.getPrice());

        return ProductMapper.toResponse(product);
    }


   @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id) {
        
        // Programacion funcional
        boolean exists = products.removeIf(u -> u.getId().equals(id));
        if (!exists)
            return new Object() {
                public String error = "Product not found";
            };

        return new Object() {
            public String message = "Deleted successfully";
        };
    }
}
