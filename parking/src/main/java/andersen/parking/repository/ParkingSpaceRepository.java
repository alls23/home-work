package andersen.parking.repository;

import andersen.parking.model.ParkingSpace;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ParkingSpaceRepository extends R2dbcRepository<ParkingSpace, Integer> {
    Flux<ParkingSpace> findAllByParkingId(Integer parkingId);
}