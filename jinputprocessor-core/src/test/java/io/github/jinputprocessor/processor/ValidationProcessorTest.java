package io.github.jinputprocessor.processor;

import io.github.jinputprocessor.ProcessFailure.ValidationFailure;
import java.util.function.Function;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ValidationProcessorTest {

	@Nested
	class ToStringTest {

		@Test
		void nominal() {
			Function<String, ValidationFailure> function = t -> null;
			var processor = new ValidationProcessor<>(function);

			var actual = processor.toString();

			Assertions.assertThat(actual)
				.startsWith("ValidationProcessor")
				.contains(function.toString());
		}

	}

}
