package com.example.protobuf;

import com.example.protobuf.protobuf.UserProtobuf;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    @Qualifier("redisTemplateByte")
    private RedisTemplate redisTemplateByte;

    @Autowired
    private ObjectMapper mapper;

    private static final String redisProtoHash = "protobuf:proto";

    public List<User> findAllProtobuf(){

        var users = fetchProtobufFromResources();

        var response = users.stream().map(user -> {
            byte[] binary = (byte[]) redisTemplateByte.opsForHash().get("proto-" + user.getId(), redisProtoHash);
            try {
                return User.protobufToDomain(UserProtobuf.User.parseFrom(binary));
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

        return response;
    }


    public void storeProtobuf() {
        List<User> users = fetchProtobufFromResources();

        users.forEach(user -> {
            persistProtobuf(User.domainToProtobuf(user));
        });
    }

    private List<User> fetchProtobufFromResources(){
        List<User> users = new ArrayList<>();

        try {
            FileInputStream input = new FileInputStream("src/main/resources/users.protobuf");
            while (true) {

                UserProtobuf.User protoUser = UserProtobuf.User.parseDelimitedFrom(input);
                if (protoUser == null)
                    break;

                users.add(User.protobufToDomain(protoUser));
            }
            return users;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void persistProtobuf(UserProtobuf.User user) {
        BoundHashOperations<String, String, byte[]> boundValueOperations = redisTemplateByte.boundHashOps("proto-" + user.getId());
        boundValueOperations.put(redisProtoHash, user.toByteArray());
        boundValueOperations.expire(1000000, TimeUnit.MILLISECONDS);
        boundValueOperations.persist();
    }

    public void generateProtofile() throws FileNotFoundException {

        FileOutputStream output = new FileOutputStream("src/main/resources/users.protobuf");

        var users = fetchJsonFromResources();
        users.forEach(user -> {
            try {
                var userProto =  User.domainToProtobuf(user);
                userProto.writeDelimitedTo(output);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    private List<User> fetchJsonFromResources(){
        TypeReference<List<User>> typeReference = new TypeReference<List<User>>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/users.json");
        try {

            return mapper.readValue(inputStream,typeReference);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
