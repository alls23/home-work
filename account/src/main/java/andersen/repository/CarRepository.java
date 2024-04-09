package andersen.repository;

import andersen.model.Car;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CarRepository extends R2dbcRepository<Car, Integer> {
    Mono<Car> findByUserEmail(String email);
}
