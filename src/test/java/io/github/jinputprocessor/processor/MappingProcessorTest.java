package io.github.jinputprocessor.processor;

import java.util.function.Function;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MappingProcessorTest {

	@Nested
	class ToStringTest {

		@Test
		void nominal() {
			Function<String, Integer> function = Integer::parseInt;
			var processor = new MappingProcessor<>(function);

			var actual = processor.toString();

			Assertions.assertThat(actual)
				.startsWith("MappingProcessor")
				.contains(function.toString());
		}

	}

}
