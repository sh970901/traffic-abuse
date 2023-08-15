package com.totoro.AntiAbuse;

import com.totoro.AntiAbuse.utils.RequestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class IpValidTests {
	@Test
	public void testValidIPv4Addresses() {
		String[] ipv4Addresses = {
				"192.168.0.1",
				"10.0.0.1",
				"172.16.0.1"
		};
		assertTrue(isValidIPAddresses(ipv4Addresses));
	}

	@Test
	public void testValidIPv6Addresses() {
		String[] ipv6Addresses = {
				"2001:0db8:85a3:0000:0000:8a2e:0370:7334",
				"fe80::1",
				"::1"
		};

		assertTrue(isValidIPAddresses(ipv6Addresses));
	}

	@Test
	public void testInvalidIPAddresses() {
		String[] invalidAddresses = {
				"256.256.256.256",  // Invalid IPv4
				"not_an_ip_address", // Invalid
				"2001:::1" // Invalid IPv6
		};

		assertFalse(isValidIPAddresses(invalidAddresses));
	}
	public static boolean isValidIPAddresses(String[] ipAddresses) {
		return Stream.of(ipAddresses).allMatch(RequestUtils::isValidIPAddress);
	}

}
