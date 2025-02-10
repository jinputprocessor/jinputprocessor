package io.github.jinputprocessor.builder.base.types;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure.ValidationError.ObjectIsNull;
import io.github.jinputprocessor.ProcessResultAssert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ObjectInputProcessorBuilderTest {

	static final ObjectInputProcessorBuilder<Object, Object> BUILDER = InputProcessor.builder().forClass(Object.class);

	@Nested
	class Sanitization {

		@Test
		void when_exception_then_return_failure() {
			var exception = new RuntimeException("any runtime exception happening :-/");
			var processor = BUILDER.sanitize(value -> {
				throw exception;
			})
				.build();
			var actualResult = processor.process(new Object());
			ProcessResultAssert.assertThat(actualResult).isFailure()
				.assertThatFailure().isUnexpectedException()
				.assertThatException().hasMessage("any runtime exception happening :-/");
		}

		@Nested
		class DefaultIfNull {

			static final Object DEFAULT_OBJECT = new Object();
			static final InputProcessor<Object, Object> PROCESSOR = BUILDER.sanitize().defaultIfNull(DEFAULT_OBJECT).then().build();

			@Test
			void when_nullInput_then_return_default() {
				var actualResult = PROCESSOR.process(null);
				ProcessResultAssert.assertThat(actualResult).isSuccessWithValue(DEFAULT_OBJECT);
			}

			@Test
			void when_nonNullInput_then_return_input() {
				var nonNullObject = new Object();
				var actualResult = PROCESSOR.process(nonNullObject);
				ProcessResultAssert.assertThat(actualResult).isSuccessWithValue(nonNullObject);
			}

		}

	}

	@Nested
	class Validation {

		@Test
		void when_exception_then_return_failure() {
			var exception = new RuntimeException("any runtime exception happening :-/");
			var processor = BUILDER.validate(value -> {
				throw exception;
			})
				.build();
			var actualResult = processor.process(new Object());
			ProcessResultAssert.assertThat(actualResult).isFailure()
				.assertThatFailure().isUnexpectedException()
				.assertThatException().hasMessage("any runtime exception happening :-/");
		}

		@Nested
		class IsNotNull {

			static final InputProcessor<Object, Object> PROCESSOR = BUILDER.validateThat().isNotNull().then().build();

			@Test
			void when_null_then_failure() {
				var actual = PROCESSOR.process(null);
				ProcessResultAssert.assertThat(actual).isFailure(new ObjectIsNull());
			}

			@Test
			void when_nonNull_then_success() {
				final var nonNullObject = new Object();
				var actual = PROCESSOR.process(nonNullObject);
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(nonNullObject);
			}

		}

	}

	@Nested
	class Mapping {

		@Test
		void when_exception_then_return_failure() {
			var exception = new RuntimeException("any runtime exception happening :-/");
			var processor = BUILDER.mapTo(value -> {
				throw exception;
			})
				.build();
			var actualResult = processor.process(new Object());
			ProcessResultAssert.assertThat(actualResult).isFailure()
				.assertThatFailure().isUnexpectedException()
				.assertThatException().hasMessage("any runtime exception happening :-/");
		}

		@Test
		void when_map_then_success() {
			final var inputValue = new Object();
			final var outputValue = new Object();
			var actual = BUILDER.mapTo(value -> outputValue).build().process(inputValue);
			ProcessResultAssert.assertThat(actual).isSuccessWithValue(outputValue);
		}

	}

}
