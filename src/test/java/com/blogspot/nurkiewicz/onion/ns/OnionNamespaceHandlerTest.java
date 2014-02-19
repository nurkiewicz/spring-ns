package com.blogspot.nurkiewicz.onion.ns;

import com.blogspot.nurkiewicz.onion.BarController;
import com.blogspot.nurkiewicz.onion.BuzzController;
import com.blogspot.nurkiewicz.onion.FooController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class OnionNamespaceHandlerTest {

	@Resource
	private FooController fooController;

	@Resource
	private BarController barController;

	@Resource
	private BuzzController buzzController;

	@Test
	public void test() {
		assertThat(fooController).isNotNull();
		assertThat(barController).isNotNull();
		assertThat(buzzController).isNotNull();
	}

}