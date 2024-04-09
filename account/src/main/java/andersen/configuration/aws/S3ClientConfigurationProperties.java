package andersen.configuration.aws;

import java.net.URI;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import software.amazon.awssdk.regions.Region;

@Data
@ConfigurationProperties(prefix = "s3")
public class S3ClientConfigurationProperties {

    private Region region = Region.of("us-east-1");
    private URI endpoint = null;
    private String accessKeyId;
    private String secretAccessKey;
    private String bucket;
    private int linkDuration;
    // AWS S3 requires that file parts must have at least 5MB, except
    // for the last part. This may change for other S3-compatible services
    private int multipartMinPartSize = 5 * 1024 * 1024;

}
