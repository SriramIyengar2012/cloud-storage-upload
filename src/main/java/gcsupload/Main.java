package gcsupload;


import com.google.cloud.storage.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.codec.CharEncoding.UTF_8;

public class Main {

    public static void main(String args[]) {
        storeReports(args[0], args[1], args[2], args[3],args[4]);

    }

    private static void storeReports(String path, String project, String bucket, String buildVersion, String appVersion) {



            String filepath = path;
            Storage storage = StorageOptions.newBuilder().setProjectId(project).build().getService();

            try (Stream<Path> walk = Files.walk(Paths.get(filepath))) {

                  List<String> result = walk.filter(Files::isRegularFile)
                        .map(x -> x.toString()).collect(Collectors.toList());

/*
                List<String> result = walk.filter(Files::isRegularFile)
                        .map(p -> p.getFileName()
                                .toString())
                        .collect(Collectors.toList());
*/


                result.forEach(re -> {
                    try {
                        System.out.println(re);
                        String bucketName = bucket;
                    //    BlobId blobid = BlobId.of(bucketName, re);
                        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName ,buildVersion+"/"+appVersion+"/"+re).build();
                        storage.create(blobInfo, Files.readAllBytes(Paths.get(re)));
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();

                    }
                });

                //  BlobId blobId = BlobId.of(bucketName,"/GaugeReports");


            } catch (IOException e) {
                e.printStackTrace();

            }


        }

    }