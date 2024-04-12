package andersen.parking.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("parking_space")
public class ParkingSpace {
    @Id
    private Integer id;
    @Column("parking_id")
    private Integer parkingId;
    @Column("car_id")
    private Integer carId;
    @Column("start_time")
    private LocalDateTime startTime;
    @Column("end_time")
    private LocalDateTime endTime;
    @Column("is_occupied")
    private boolean isOccupied;
}
