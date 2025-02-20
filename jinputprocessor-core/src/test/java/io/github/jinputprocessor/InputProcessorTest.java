package io.github.jinputprocessor;

import io.github.jinputprocessor.ProcessFailure.ValidationFailure;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class InputProcessorTest {

	@Nested
	class Process {

		@Test
		void nominal_valid() {
			var expectedResult = ProcessResult.success("OK");
			InputProcessor<String, String> processStr = value -> expectedResult;

			var actualResult = processStr.process("test");

			Assertions.assertThat(actualResult).isEqualTo(expectedResult);
		}

		@Test
		void nominal_valid_atProperty() {
			var expectedResult = ProcessResult.success("OK");
			InputProcessor<String, String> processStr = value -> expectedResult;

			var actualResult = processStr.process("myVal", "test");

			Assertions.assertThat(actualResult).isEqualTo(expectedResult.atPath(Path.createPropertyPath("myVal")));
		}

		@Test
		void nominal_valid_atPath() {
			Path path = Path.createPropertyPath("myVal").atIndex(0);
			var expectedResult = ProcessResult.success("OK");
			InputProcessor<String, String> processStr = value -> expectedResult;

			var actualResult = processStr.process(path, "test");

			Assertions.assertThat(actualResult).isEqualTo(expectedResult.atPath(path));
		}

		@Test
		void nominal_error() {
			var validationFailure = new ValidationFailure.ObjectIsNull();
			ProcessResult<String> expectedResult = ProcessResult.failure(validationFailure);
			InputProcessor<String, String> processStr = value -> expectedResult;

			var actualResult = processStr.process("test");

			Assertions.assertThat(actualResult).isEqualTo(expectedResult);
		}

		@Test
		void nominal_error_atProperty() {
			var validationFailure = new ValidationFailure.ObjectIsNull();
			ProcessResult<String> expectedResult = ProcessResult.failure(validationFailure);
			InputProcessor<String, String> processStr = value -> expectedResult;

			var actualResult = processStr.process("myVal", "test");

			Assertions.assertThat(actualResult).isEqualTo(expectedResult.atPath(Path.createPropertyPath("myVal")));
		}

		@Test
		void nominal_error_atPath() {
			Path path = Path.createPropertyPath("myVal").atIndex(0);
			var validationFailure = new ValidationFailure.ObjectIsNull();
			ProcessResult<String> expectedResult = ProcessResult.failure(validationFailure);
			InputProcessor<String, String> processStr = value -> expectedResult;

			var actualResult = processStr.process(path, "test");

			Assertions.assertThat(actualResult).isEqualTo(expectedResult.atPath(path));
		}

	}

	@Nested
	class DefaultFailureMapper {

		@Test
		void setDefaultFailureMapper() {
			var defaultFailureMapper = InputProcessor.getDefaultFailureMapper();
			synchronized (InputProcessor.class) {
				try {
					InputProcessor.setDefaultFailureMapper(failure -> new NullPointerException("NPE!"));
					var processor = InputProcessor.builder().forString()
						.validate(value -> new ValidationFailure.ObjectIsNull())
						.build();

					Assertions.assertThatNullPointerException()
						.isThrownBy(() -> processor.process(null).getOrThrow())
						.withMessage("NPE!");
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
