package io.github.jinputprocessor.builder.base.types;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure.ValidationError.ObjectIsNotInstanceOf;
import io.github.jinputprocessor.ProcessFailure.ValidationError.ObjectIsNull;
import io.github.jinputprocessor.ProcessResultAssert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ObjectInputProcessorBuilderTest {

	@Nested
	class Sanitization {

		@Test
		void when_exception_then_return_failure() {
			var exception = new RuntimeException("any runtime exception happening :-/");
			var processor = InputProcessor.builder().forClass(Object.class)
				.sanitize(value -> {
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
			static final InputProcessor<Object, Object> PROCESSOR = InputProcessor.builder().forClass(Object.class)
				.sanitize().defaultIfNull(DEFAULT_OBJECT).then()
				.build();

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
			var processor = InputProcessor.builder().forClass(Object.class)
				.validate(value -> {
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

			static final InputProcessor<Object, Object> PROCESSOR = InputProcessor.builder().forClass(Object.class)
				.validateThat().isNotNull().then()
				.build();

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

		@Nested
		class IsInstanceOf {

			static final InputProcessor<Number, Number> PROCESSOR_FOR_NUMBER = InputProcessor.builder().forClass(Number.class)
				.validateThat().isInstanceOf(Number.class).then()
				.build();
			static final InputProcessor<Number, Number> PROCESSOR_FOR_INTEGER = InputProcessor.builder().forClass(Number.class)
				.validateThat().isInstanceOf(Integer.class).then()
				.build();

			@Test
			void when_null_then_failure() {
				// null is not instance of Integer
				var actual = PROCESSOR_FOR_INTEGER.process(null);
				ProcessResultAssert.assertThat(actual).isFailure(new ObjectIsNotInstanceOf(Integer.class));
			}

			@Test
			void when_sameType_then_success() {
				// Integer is instance of Integer
				final var intValue = Integer.valueOf(3);
				var actual = PROCESSOR_FOR_INTEGER.process(intValue);
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(intValue);
			}

			@Test
			void when_superType_then_success() {
				// Integer is instance of Number
				final var intValue = Integer.valueOf(3);
				var actual = PROCESSOR_FOR_NUMBER.process(intValue);
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(intValue);
			}

			@Test
			void when_otherType_then_failure() {
				// Long is not instance of Integer
				final var longValue = Long.valueOf(3L);
				var actual = PROCESSOR_FOR_INTEGER.process(longValue);
				ProcessResultAssert.assertThat(actual).isFailure(new ObjectIsNotInstanceOf(Integer.class));
			}

		}

	}

	@Nested
	class Mapping {

		@Test
		void when_exception_then_return_failure() {
			var exception = new RuntimeException("any runtime exception happening :-/");
			var processor = InputProcessor.builder().forClass(Object.class)
				.mapTo(value -> {
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
			final var outputValue = "newOutPut";
			var processor = InputProcessor.builder().forClass(Object.class)
				.mapTo(value -> outputValue)
				.build();

			var actual = processor.process(inputValue);

			ProcessResultAssert.assertThat(actual).isSuccessWithValue(outputValue);
		}

	}

}
