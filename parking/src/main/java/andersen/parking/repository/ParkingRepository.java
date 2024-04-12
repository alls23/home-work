package andersen.parking.repository;

import andersen.parking.model.Parking;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository extends R2dbcRepository<Parking, Integer> {

}
