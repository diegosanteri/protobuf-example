package com.example.protobuf;

import com.example.protobuf.protobuf.UserProtobuf;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.Descriptors;

public class User {

    private long id;

    @JsonProperty("first_name")
    private String name;

    @JsonProperty("last_name")
    private String lastname;

    @JsonProperty("email")
    private String email;

    private Gender gender;

    @JsonProperty("ip_address")
    private String ipAddress;



    public static UserProtobuf.User domainToProtobuf(User user) {


        var gender = UserProtobuf.User.Gender.valueOf(user.getGender().name());

        UserProtobuf.User userResponse = UserProtobuf.User.newBuilder()
                    .setGender(gender)
                    .setName(user.getName())
                    .setLastname(user.getLastname())
                    .setEmail(user.getEmail())
                    .setIpAddress(user.getIpAddress())
                    .setId(user.getId())
                .build();

        return userResponse;
    }

    public static UserProtobuf.User domainToProtobufImproved(User user) {

        var gender = UserProtobuf.User.Gender.valueOf(user.getGender().name());
        UserProtobuf.User userResponse = UserProtobuf.User.newBuilder()
                .setGender(gender)
                .setName(user.getName())
                .setLastname(user.getLastname())
                .setEmail(user.getEmail())
                .setIpAddress(user.getIpAddress())
                .setId(user.getId())
                .build();

        return userResponse;
    }

    public static User protobufToDomain(UserProtobuf.User protoUser) {
        User response = new User();
        response.setEmail(protoUser.getEmail());
        response.setGender(Gender.valueOf(protoUser.getGender().name()));
        response.setId(protoUser.getId());
        response.setName(protoUser.getName());
        response.setLastname(protoUser.getLastname());
        response.setIpAddress(protoUser.getIpAddress());
        return response;
    }

    public static User domainToProtobufImproved(UserProtobuf.User protoUser) {

        final Descriptors.FieldDescriptor idField = protoUser.getDescriptorForType().findFieldByName("id");
        final Descriptors.FieldDescriptor emailField = protoUser.getDescriptorForType().findFieldByName("email");
        final Descriptors.FieldDescriptor genderField = protoUser.getDescriptorForType().findFieldByName("gender");
        final Descriptors.FieldDescriptor nameField = protoUser.getDescriptorForType().findFieldByName("name");
        final Descriptors.FieldDescriptor lastnameField = protoUser.getDescriptorForType().findFieldByName("lastname");
        final Descriptors.FieldDescriptor ipAddressField = protoUser.getDescriptorForType().findFieldByName("ipAddress");

        User response = new User();
        if(protoUser.hasField(emailField))
            response.setEmail(protoUser.getEmail());

        if(protoUser.hasField(genderField))
            response.setGender(Gender.valueOf(protoUser.getGender().name()));

        if(protoUser.hasField(idField))
            response.setId(protoUser.getId());

        if(protoUser.hasField(nameField))
            response.setName(protoUser.getName());

        if(protoUser.hasField(lastnameField))
            response.setLastname(protoUser.getLastname());

        if(protoUser.hasField(ipAddressField))
            response.setIpAddress(protoUser.getIpAddress());

        return response;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
