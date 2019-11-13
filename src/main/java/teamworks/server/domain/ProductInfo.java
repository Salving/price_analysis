package teamworks.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_info")
public class ProductInfo {
    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, name = "id")
    @JsonIgnore
    private long id;

    @Column(unique = true, nullable = false, name = "name")
    private String name;

    @Column(name = "ean")
    private String ean;

    @JsonIgnore
    @OneToMany(mappedBy = "info")
    private List<Product> products;

    @JsonIgnore
    @ManyToMany(mappedBy = "productInfo")
    private List<Tag> tags;

    protected ProductInfo() {
    }

    public ProductInfo(String name) {
        this.name = name;
    }


    public ProductInfo(String name, String ean) {
        this.name = name;
        this.ean = ean;
        this.products = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public ProductInfo(long id, String name, String ean, List<Product> products) {
        this.id = id;
        this.name = name;
        this.ean = ean;
        this.products = products;
    }

    public ProductInfo(long id, String name, String ean, List<Product> products, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.ean = ean;
        this.products = products;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
