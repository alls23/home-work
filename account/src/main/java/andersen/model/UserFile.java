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
public class UserFile {
    @Id
    private Integer id;
    @Column("car_id")
    private Integer carId;
    @Column("file_key")
    private String fileKey;
    @Column("presigned_url")
    private String presignedUrl;
}
