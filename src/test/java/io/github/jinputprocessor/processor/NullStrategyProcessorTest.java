package io.github.jinputprocessor.processor;

import io.github.jinputprocessor.InputProcessors;
import io.github.jinputprocessor.processor.NullStrategyProcessor.NullStrategy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NullStrategyProcessorTest {

	@Nested
	class ToStringTest {

		@Test
		void nominal() {
			var nextProcessor = InputProcessors.noOpProcessor();
			var processor = new NullStrategyProcessor<>(NullStrategy.process(), nextProcessor);

			var actual = processor.toString();

			Assertions.assertThat(actual)
				.startsWith("NullStrategyProcessor")
				.contains(NullStrategy.process().toString())
				.contains(nextProcessor.toString());
		}

	}

}
