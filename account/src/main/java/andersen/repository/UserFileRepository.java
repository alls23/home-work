package andersen.repository;

import andersen.model.UserFile;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFileRepository extends R2dbcRepository<UserFile, Integer>  {

}
