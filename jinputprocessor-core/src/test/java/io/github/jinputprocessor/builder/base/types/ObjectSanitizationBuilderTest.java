package io.github.jinputprocessor.builder.base.types;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessResultAssert;
import org.junit.jupiter.api.Test;

public class ObjectSanitizationBuilderTest {

	@Test
	void defaultIfNull() {
		final var expected = new Object();
		var processor = InputProcessor.builder().forClass(Object.class)
			.sanitize()
			.defaultIfNull(expected)
			.then()
			.build();

		var actualResult = processor.process(null);

		ProcessResultAssert.assertThat(actualResult).isSuccessWithValue(expected);
	}

}
