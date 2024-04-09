package andersen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    private Integer id;
    @Column("user_email")
    private String userEmail;
    @Column("car_number")
    private String number;
    @Column("car_type")
    private String type;
    @Column("car_brand")
    private String brand;
    @Column("car_model")
    private String model;
}
