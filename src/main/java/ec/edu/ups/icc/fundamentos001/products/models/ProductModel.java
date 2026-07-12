package ec.edu.ups.icc.fundamentos001.products.models;

import java.time.LocalDateTime;
import java.util.List;

import ec.edu.ups.icc.fundamentos001.categories.entity.CategoryEntity;

public class ProductModel {
    private Long id;
    private String name;
    private Double price;
    private Integer stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;
    private List<CategoryEntity> categories;
    
    public ProductModel() {
    }
    
    







    public ProductModel(Long id, String name, Double price, Integer stock, LocalDateTime createdAt,
            LocalDateTime updatedAt, boolean deleted, List<CategoryEntity> categories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
        this.categories = categories;
    }









    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    public List<CategoryEntity> getCategories() {
        return categories;
    }


    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }


    

    

    

    

    
}
