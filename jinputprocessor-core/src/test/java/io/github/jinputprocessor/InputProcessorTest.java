package io.github.jinputprocessor;

import io.github.jinputprocessor.ProcessFailure.ValidationError;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class InputProcessorTest {

	@Nested
	class Process {

		@Test
		void nominal_valid() {
			var expectedResult = ProcessResult.success("OK");
			InputProcessor<String, String> processStr = value -> expectedResult;

			var actualResult = processStr.process("test");

			Assertions.assertThat(actualResult).isSameAs(expectedResult);
		}

		@Test
		void nominal_error() {
			var validationError = new ValidationError.CustomError("error.key");
			ProcessResult<String> expectedResult = ProcessResult.failure(validationError);
			InputProcessor<String, String> processStr = value -> expectedResult;

			var actualResult = processStr.process("test");

			Assertions.assertThat(actualResult).isSameAs(expectedResult);
		}

	}

	@Nested
	class DefaultFailureMapper {

		@Test
		void setDefaultFailureMapper() {
			var defaultFailureMapper = InputProcessor.getDefaultFailureMapper();
			synchronized (InputProcessor.class) {
				try {
					InputProcessor.setDefaultFailureMapper((inputName, failure) -> new NullPointerException("NPE for " + inputName));
					var processor = InputProcessor.builder().forString()
						.validate(value -> new ValidationError.ObjectIsNull())
						.build();

					Assertions.assertThatNullPointerException()
						.isThrownBy(() -> processor.process(null).getOrThrow("myVal"))
						.withMessage("NPE for myVal");
				} finally {
					InputProcessor.setDefaultFailureMapper(defaultFailureMapper);
					Assertions.assertThat(InputProcessor.getDefaultFailureMapper()).isSameAs(defaultFailureMapper);
				}
			}

		}

	}

	@Nested
	class AndThen {

		@Test
		void andThen_instance() {
			InputProcessor<String, String> subProcessor1 = value -> ProcessResult.success(value + "-1");
			InputProcessor<String, String> subProcessor2 = value -> ProcessResult.success(value + "-2");

			var processor = subProcessor1.andThen(subProcessor2);

			InputProcessorAssert.assertThat(processor)
				.isChainedProcessor()
				.hastFirstProcessor(subProcessor1)
				.hastSecondProcessor(subProcessor2);
		}

	}

}
