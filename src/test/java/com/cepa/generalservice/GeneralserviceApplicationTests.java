package com.cepa.generalservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.cepa.generalservice.controllers.AuthenticationController;

@SpringBootTest
class GeneralserviceApplicationTests {

	@MockBean 
	private AuthenticationController authenticationController;

	@Test
	void contextLoads() {
		Assertions.assertThat(authenticationController).isNotNull();
	}

}
