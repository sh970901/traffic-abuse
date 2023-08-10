package com.totoro.AntiAbuse;

import com.totoro.AntiAbuse.couchbase.config.CouchbaseConfig;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AntiAbuseApplicationTests {
	@Value("${spring.datasource.username}")
	String username;

	@Autowired
	private CouchbaseConfig couchbaseConfig;

	@Test
	void contextLoads() {
	}

	@Test
	void jasypt() {
//		assertThat(couchbaseConfig.getUserName()).isEqualTo(username);
		System.out.println(couchbaseConfig.getUserName());
	}
}
