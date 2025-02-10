package io.github.jinputprocessor.processor;

import io.github.jinputprocessor.ProcessResult;
import io.github.jinputprocessor.ProcessResultAssert;
import java.util.function.Function;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SanitizationProcessorTest {

	@Test
	void null_is_accepted() {
		Function<String, String> function = value -> String.valueOf(value);

		var processor = new SanitizationProcessor<>(function);

		var actualResult = processor.process(null);

		ProcessResultAssert.assertThat(actualResult).isSuccessWithValue("null");
		Assertions.assertThat(actualResult).extracting(ProcessResult::get).isEqualTo("null");
	}

}
