package ec.edu.ups.icc.fundamentos001.products.dtos;

import java.util.Set;

import javax.swing.text.html.parser.Entity;

import ec.edu.ups.icc.fundamentos001.categories.entity.CategoryEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class UpdateProductDto {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String name;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double price;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    //@NotNull(message = "El ID de la categoría es obligatorio")
    @NotEmpty(message = "Debe seleccionar al menos una categoría")
    private Set<Long> categoryIds;

    public UpdateProductDto() {
    }

    




    






    public UpdateProductDto(
            @NotBlank(message = "El nombre es obligatorio") @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres") String name,
            @NotNull(message = "El precio es obligatorio") @Min(value = 0, message = "El precio no puede ser negativo") Double price,
            @NotNull(message = "El stock es obligatorio") @Min(value = 0, message = "El stock no puede ser negativo") Integer stock,
            @NotEmpty(message = "Debe seleccionar al menos una categoría") Set<Long> categoryIds) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.categoryIds = categoryIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }



    public Set<Long> getCategoryIds() {
        return categoryIds;
    }


    public void setCategoryIds(Set<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    
}
