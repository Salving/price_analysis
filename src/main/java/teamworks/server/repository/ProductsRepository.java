package teamworks.server.repository;

import org.springframework.data.repository.CrudRepository;
import teamworks.server.domain.Product;
import teamworks.server.domain.Store;

import java.util.List;

public interface ProductsRepository extends CrudRepository<Product, Long> {
    List<Product> findByInfo_Name(String name);
    Product findProductById(long id);
    List<Product> findByInfo_Ean(String ean);
    Product findByInfo_NameAndStore(String name, Store store);

    boolean existsByInfo_Name(String name);
    boolean existsByInfo_NameAndStoreAndPrice(String name, Store store, Double price);
    boolean existsByInfo_Ean(String ean);
    boolean existsByInfo_NameAndStore(String name, Store store);

    List<Product> findByInfo_NameContains(String name);
    List<Product> findByInfo_NameContainsIgnoreCase(String name);
}
