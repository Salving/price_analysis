package teamworks.server.repository;

import org.springframework.data.repository.CrudRepository;
import teamworks.server.domain.Product;
import teamworks.server.domain.ProductInfo;
import teamworks.server.domain.Tag;

import java.util.List;

public interface ProductInfoRepository extends CrudRepository<ProductInfo, Long> {
    ProductInfo findById(long id);
    ProductInfo findByName(String name);
    ProductInfo findByEan(String ean);
    List<ProductInfo> findByTagsIn(List<Tag> tags);
    List<Product> findByNameContains(String name);

    boolean existsByName(String name);
    boolean existsByEan(String ean);
    boolean existsByTagsIn(List<Tag> tags);

}