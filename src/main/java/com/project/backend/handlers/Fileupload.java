package com.project.backend.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.annotation.PostConstruct;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.project.backend.Model.*;

@Component
public class Fileupload {

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region).build();
    }

    public Mono<ServerResponse> fileupload(ServerRequest req) {
        Flux<String> then = req.multipartData().map(it -> it.get("file")).flatMapMany(Flux::fromIterable)
            .cast(FilePart.class).flatMap(it -> {
                File f = null;
 
                String suffix = it.filename();
                String fileName = "images/" + it.filename();
                Path path = null;
                try {
                     path = Files.createTempFile("tempimg", suffix.substring(1, suffix.length()));
                     it.transferTo(path);
                } catch (IOException e) {
                // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                f = path.toFile();
                
                s3Client.putObject(bucket, it.filename(), f );
                s3Client.setObjectAcl(bucket, it.filename(),CannedAccessControlList.PublicRead);
                suffix = s3Client.getUrl(bucket, it.filename()).toString();
                f.delete();
            return Flux.just(suffix);
        });
    
        return ok().body(then, String.class);

    }

}