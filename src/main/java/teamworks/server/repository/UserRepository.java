package teamworks.server.repository;

import org.springframework.data.repository.CrudRepository;
import teamworks.server.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findById(String id);
    User findByLogin(String login);
    User findByMail(String mail);
    boolean existsByLogin(String login);
    boolean existsByMail(String mail);
}
