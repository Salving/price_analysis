package teamworks.server.repository;

import org.springframework.data.repository.CrudRepository;
import teamworks.server.domain.Tag;

public interface TagRepository extends CrudRepository<Tag, Long> {
    Tag findById(long id);
    Tag findByName(String name);

    boolean existsByName(String name);
}
