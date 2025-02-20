package io.github.jinputprocessor.processor;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessResult;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SequencedCollectionIterationProcessorTest {

	@Nested
	class ToStringTest {

		@Test
		void nominal() {
			InputProcessor<String, String> subProcessor = value -> ProcessResult.success(value + "-1");
			var processor = new SequencedCollectionIterationProcessor<>(subProcessor, Collectors.toList());

			var actual = processor.toString();

			Assertions.assertThat(actual)
				.startsWith("SequencedCollectionIterationProcessor")
				.contains(subProcessor.toString());
		}

	}

}
