package io.github.jinputprocessor.core.processor;

import io.github.jinputprocessor.ProcessResult;
import io.github.jinputprocessor.assertions.ProcessResultAssert;
import io.github.jinputprocessor.core.processor.SanitizationProcessor;
import java.util.function.Function;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class SanitizationProcessorTest {

	@Test
	void null_is_accepted() {
		Function<String, String> function = value -> String.valueOf(value);

		var processor = new SanitizationProcessor<>(function);

		var actualResult = processor.process(null);

		ProcessResultAssert.assertThat(actualResult).isSuccessWithValue("null");
		Assertions.assertThat(actualResult).extracting(ProcessResult::getValue).isEqualTo("null");
	}

}
