package andersen.configuration.aws;

import java.time.Duration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@EnableConfigurationProperties(S3ClientConfigurationProperties.class)
public class S3ClientConfiguration {

    @Bean
    public S3Configuration s3Configuration() {
        return S3Configuration.builder()
                .checksumValidationEnabled(false)
                .chunkedEncodingEnabled(true)
                .pathStyleAccessEnabled(true)
                .build();
    }

    @Bean
    public S3AsyncClient s3client(S3ClientConfigurationProperties s3props,
            AwsCredentialsProvider credentialsProvider,
            S3Configuration configuration) {
        SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
                .writeTimeout(Duration.ZERO)
                .maxConcurrency(64)
                .build();

        S3AsyncClientBuilder builder = S3AsyncClient.builder()
                .httpClient(httpClient)
                .region(s3props.getRegion())
                .credentialsProvider(credentialsProvider)
                .endpointOverride(s3props.getEndpoint())
                .serviceConfiguration(configuration);

        return builder.build();
    }

    @Bean
    public S3Presigner s3Presigner(S3ClientConfigurationProperties s3props,
            AwsCredentialsProvider credentialsProvider,
            S3Configuration configuration) {
        return S3Presigner.builder()
                .serviceConfiguration(configuration)
                .region(s3props.getRegion())
                .credentialsProvider(credentialsProvider)
                .endpointOverride(s3props.getEndpoint())
                .build();
    }

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider(S3ClientConfigurationProperties s3props) {
        return () -> AwsBasicCredentials.create(s3props.getAccessKeyId(), s3props.getSecretAccessKey());
    }

}
