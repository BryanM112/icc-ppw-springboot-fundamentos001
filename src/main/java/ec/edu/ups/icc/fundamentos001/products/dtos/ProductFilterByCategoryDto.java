package ec.edu.ups.icc.fundamentos001.products.dtos;

public class ProductFilterByCategoryDto {
    private String name;

    private Double minPrice;

    private Double maxPrice;

    private Long userId;


    public boolean hasValidPriceRange() {

    if (minPrice == null || maxPrice == null) {
        return true;
    }

        return maxPrice >= minPrice;
    }


    public ProductFilterByCategoryDto() {
    }


    public ProductFilterByCategoryDto(String name, Double minPrice, Double maxPrice, Long userId) {
        this.name = name;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.userId = userId;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Double getMinPrice() {
        return minPrice;
    }


    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }


    public Double getMaxPrice() {
        return maxPrice;
    }


    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }


    public Long getUserId() {
        return userId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }

    


}
