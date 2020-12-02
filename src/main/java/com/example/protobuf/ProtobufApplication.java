package com.example.protobuf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@SpringBootApplication
@RestController
public class ProtobufApplication {


	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(ProtobufApplication.class, args);
	}

	@PostMapping("generateProtoFile")
	public ResponseEntity<Void> generateProtofile() throws FileNotFoundException {
		userService.generateProtofile();

		return ResponseEntity.noContent().build();
	}

	@GetMapping("proto")
	public ResponseEntity<List<User>> getProto() {

		return ResponseEntity.ok(userService.findAllProtobuf());
	}

	@PostMapping("/proto")
	public ResponseEntity<Void> initializeProto() {

		userService.storeProtobuf();

		return ResponseEntity.noContent().build();
	}

	@Bean
	public RedisTemplate<String, byte[]> redisTemplateByte(LettuceConnectionFactory connectionFactory) {
		RedisTemplate<String, byte[]> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new GenericToStringSerializer<Object>(Object.class));
		template.setHashValueSerializer(new JdkSerializationRedisSerializer());
		template.setValueSerializer(new JdkSerializationRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}

}
