package teamworks.server.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import teamworks.server.service.TimeService;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "products")
public class Product implements Serializable{
    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, name = "id")
    @JsonIgnore
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_info_id", nullable = false)
    private ProductInfo info;

    @Column(nullable = false, name = "price")
    private double price;

    @Column(nullable = false, name = "unit")
    private String unit;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeService.DATE_FORMAT)
    @Column(nullable = false, name = "creation_time")
    @JsonIgnore
    private Date creationDate;

    @Column(name = "country")
    private String country;

    protected Product() {

    }

    public Product(ProductInfo info, double price, String unit, Store store, String country) {
        this.info = info;
        this.price = price;
        this.unit = unit;
        this.store = store;
        this.country = country;
        this.creationDate = new Date();
    }

    public Product(long id, ProductInfo info, double price, String unit, Store store, Date creationDate, String country) {
        this.id = id;
        this.info = info;
        this.price = price;
        this.unit = unit;
        this.store = store;
        this.creationDate = new Date();
        this.country = country;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProductInfo getInfo() {
        return info;
    }

    public void setInfo(ProductInfo info) {
        this.info = info;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}