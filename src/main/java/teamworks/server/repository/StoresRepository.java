package teamworks.server.repository;

import org.springframework.data.repository.CrudRepository;
import teamworks.server.domain.Product;
import teamworks.server.domain.Store;

import java.util.List;

public interface StoresRepository extends CrudRepository<Store, Long> {
    Store findById(long id);
    Store findByName(String name);

    boolean existsByName(String name);
}
