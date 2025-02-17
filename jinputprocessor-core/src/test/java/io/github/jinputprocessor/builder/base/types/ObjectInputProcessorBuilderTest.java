package io.github.jinputprocessor.builder.base.types;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure.ValidationFailure.ObjectIsNotInstanceOf;
import io.github.jinputprocessor.ProcessFailure.ValidationFailure.ObjectIsNull;
import io.github.jinputprocessor.ProcessResultAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ObjectInputProcessorBuilderTest {

	@Nested
	class NullStrategyTest {

		@Test
		void when_no_nullStrategy_then_nullIsProcessed() {
			var processor = InputProcessor.builder().forString()
				// no null strategy
				.sanitize(value -> value + "-1")
				.build();

			var actualResult = processor.process(null);

			ProcessResultAssert.assertThat(actualResult).isSuccessWithValue("null-1");
		}

		@Test
		void process() {
			var processor = InputProcessor.builder().forString()
				.ifNullThen().process()
				.sanitize(value -> value + "-1")
				.build();

			ProcessResultAssert.assertThat(processor.process(null)).isSuccessWithValue("null-1");
			ProcessResultAssert.assertThat(processor.process("val")).isSuccessWithValue("val-1");
		}

		@Test
		void skipProcess() {
			var processor = InputProcessor.builder().forString()
				.ifNullThen().skipProcess()
				.sanitize(value -> value + "-1")
				.build();

			ProcessResultAssert.assertThat(processor.process(null)).isSuccessWithValue(null);
			ProcessResultAssert.assertThat(processor.process("val")).isSuccessWithValue("val-1");
		}

		@Test
		void useDefault() {
			var processor = InputProcessor.builder().forString()
				.ifNullThen().useDefault("default")
				.sanitize(value -> value + "-1")
				.build();

			ProcessResultAssert.assertThat(processor.process(null)).isSuccessWithValue("default-1");
			ProcessResultAssert.assertThat(processor.process("val")).isSuccessWithValue("val-1");
		}

		@Test
		void when_useDefault_with_null_then_NPE() {
			Assertions.assertThatNullPointerException()
				.isThrownBy(
					() -> InputProcessor.builder().forString()
						.ifNullThen().useDefault(null) // null default value is not allowed
						.build()
				);
		}

		@Test
		void mixedStrategies_1() {
			var processor = InputProcessor.builder().forString()
				.ifNullThen().skipProcess()
				.ifNullThen().process()
				.ifNullThen().skipProcess()
				.sanitize(value -> value + "-1")
				.build();

			ProcessResultAssert.assertThat(processor.process(null)).isSuccessWithValue(null);
			ProcessResultAssert.assertThat(processor.process("val")).isSuccessWithValue("val-1");
		}

		@Test
		void mixedStrategies_2() {
			var processor = InputProcessor.builder().forString()
				.ifNullThen().skipProcess()
				.ifNullThen().process()
				.ifNullThen().useDefault("plop")
				.ifNullThen().skipProcess()
				.ifNullThen().process()
				.sanitize(value -> value + "-1")
				.build();

			ProcessResultAssert.assertThat(processor.process(null)).isSuccessWithValue("plop-1");
			ProcessResultAssert.assertThat(processor.process("val")).isSuccessWithValue("val-1");
		}

		@Test
		void mixedStrategies_3() {
			var processor = InputProcessor.builder().forString()
				.ifNullThen().skipProcess()
				.sanitize(value -> value + "-1")
				.ifNullThen().process()
				.sanitize(value -> value + "-2")
				.ifNullThen().skipProcess()
				.sanitize(value -> value + "-3")
				.ifNullThen().process()
				.sanitize(value -> value + "-4")
				.ifNullThen().useDefault("default")
				.sanitize(value -> value + "-5")
				.build();

			ProcessResultAssert.assertThat(processor.process(null)).isSuccessWithValue("null-2-3-4-5");
			ProcessResultAssert.assertThat(processor.process("val")).isSuccessWithValue("val-1-2-3-4-5");
		}

	}

	@Nested
	class SanitizationTest {

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
		class ApplyTest {

			static final Object NEW_OBJECT = new Object();
			static final InputProcessor<Object, Object> PROCESSOR = InputProcessor.builder().forClass(Object.class)
				.sanitize().apply(v -> NEW_OBJECT).then()
				.build();

			@Test
			void when_applyWithNull_then_function_is_called() {
				var actualResult = PROCESSOR.process(null);
				ProcessResultAssert.assertThat(actualResult).isSuccessWithValue(NEW_OBJECT);
			}

			@Test
			void when_apply_then_function_is_called() {
				var nonNullObject = new Object();
				var actualResult = PROCESSOR.process(nonNullObject);
				ProcessResultAssert.assertThat(actualResult).isSuccessWithValue(NEW_OBJECT);
			}

		}

	}

	@Nested
	class ValidationTest {

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
		class IsNotNullTest {

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
		class IsInstanceOfTest {

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
	class MappingTest {

		@Test
		void when_exception_then_return_failure() {
			var exception = new RuntimeException("any runtime exception happening :-/");
			var processor = InputProcessor.builder().forClass(Object.class)
				.map(value -> {
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
				.map(value -> outputValue)
				.build();

			var actual = processor.process(inputValue);

			ProcessResultAssert.assertThat(actual).isSuccessWithValue(outputValue);
		}

	}

}
