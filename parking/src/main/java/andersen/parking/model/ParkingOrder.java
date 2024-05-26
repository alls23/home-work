package andersen.parking.model;

import andersen.parking.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("parking_order")
public class ParkingOrder {
    @Id
    private Integer id;

    private OrderStatus status;

    public static ParkingOrder create() {
        return ParkingOrder.builder()
                .status(OrderStatus.CREATED)
                .build();
    }
}
