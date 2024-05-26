package andersen.parking.repository;

import andersen.parking.model.ParkingOrder;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ParkingOrderRepository extends R2dbcRepository<ParkingOrder, Integer> {

}
