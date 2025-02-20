package io.github.jinputprocessor.processor;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessResult;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CollectionIterationProcessorTest {

	@Nested
	class ToStringTest {

		@Test
		void nominal() {
			InputProcessor<String, String> subProcessor = value -> ProcessResult.success(value + "-1");
			var processor = new CollectionIterationProcessor<>(subProcessor, Collectors.toSet());

			var actual = processor.toString();

			Assertions.assertThat(actual)
				.startsWith("CollectionIterationProcessor")
				.contains(subProcessor.toString());
		}

	}

}
