package teamworks.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, name = "id")
    @JsonIgnore
    private long id;

    @Column(unique = true, nullable = false, name = "name")
    private String name;

    @JsonIgnore
    @ManyToMany(targetEntity = ProductInfo.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "product_info_id", nullable = false)
    private List<ProductInfo> productInfo;

    protected Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }


    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
        this.productInfo = new ArrayList<>();
    }

    public Tag(long id, String name, List<ProductInfo> productInfo) {
        this.id = id;
        this.name = name;
        this.productInfo = productInfo;
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

    public List<ProductInfo> getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(List<ProductInfo> productInfo) {
        this.productInfo = productInfo;
    }
}
