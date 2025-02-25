package io.github.jinputprocessor.builder.base.types;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure.ValidationFailure;
import io.github.jinputprocessor.ProcessFailure.ValidationFailure.StringIsNotParseableToInteger;
import io.github.jinputprocessor.ProcessResultAssert;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class StringInputProcessorBuilderTest {

	@Nested
	class Sanitization {

		private static class StringSanitizationArgumentsProvider implements ArgumentsProvider {

			@Override
			public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
				return Stream.of(
					// Test description, sanitizerCustomizer, input, expectedOutput
					Arguments.of("strip", build(builder -> builder.strip()), " value ", "value"),
					Arguments.of("toLowerCase", build(builder -> builder.toLowerCase()), "THIS IS Éé", "this is éé"),
					Arguments.of("toUpperCase", build(builder -> builder.toUpperCase()), "this is Éé", "THIS IS ÉÉ"),
					Arguments.of("prefix-nominal", build(builder -> builder.prefix("p-")), "value", "p-value"),
					Arguments.of("prefix-already-prefixed", build(builder -> builder.prefix("p-")), "p-value", "p-value"),
					Arguments.of("prefix-already-prefixedUppercase", build(builder -> builder.prefix("p-")), "P-value", "p-P-value"),
					Arguments.of("suffix-nominal", build(builder -> builder.suffix("-s")), "value", "value-s"),
					Arguments.of("suffix-already-suffixed", build(builder -> builder.suffix("-s")), "value-s", "value-s"),
					Arguments.of("suffix-already-suffixedUppercase", build(builder -> builder.suffix("-s")), "value-S", "value-S-s")
				);
			}

			// Convenient method to avoid ugly cast in provideArguments()
			private static Function<StringSanitizationBuilder<String>, StringSanitizationBuilder<String>> build(
				Function<StringSanitizationBuilder<String>, StringSanitizationBuilder<String>> function
			) {
				return function;
			}

		}

		@ParameterizedTest()
		@ArgumentsSource(StringSanitizationArgumentsProvider.class)
		void sanitize(
			String testDesc, Function<StringSanitizationBuilder<String>, StringSanitizationBuilder<String>> sanitizerCustomizer, String input, String expectedOutput
		) {
			var sanitizerBuilder = InputProcessor.builder().forString().sanitize();
			sanitizerBuilder = sanitizerCustomizer.apply(sanitizerBuilder);
			var processor = sanitizerBuilder.then().build();

			var actualResult = processor.process(input);

			ProcessResultAssert.assertThat(actualResult)
				.as("Error in sanitization test: " + testDesc)
				.isSuccessWithValue(expectedOutput);
		}

	}

	@Nested
	class Validation {

		@Nested
		class IsNotNull {

			private static final InputProcessor<String, String> PROCESSOR = InputProcessor.builder().forString()
				.validateThat().isNotNull().then()
				.build();

			@Test
			void when_empty_then_success() {
				var actual = PROCESSOR.process("");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue("");
			}

			@Test
			void when_blank_then_success() {
				var actual = PROCESSOR.process("  ");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue("  ");
			}

			@Test
			void when_anyString_then_success() {
				var actual = PROCESSOR.process("plop");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue("plop");
			}

		}

		@Nested
		class IsMaxLength {

			private static final InputProcessor<String, String> PROCESSOR = InputProcessor.builder().forString()
				.validateThat().isMaxLength(5).then()
				.build();

			@Test
			void when_shorter_then_success() {
				var actual = PROCESSOR.process("1234");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue("1234");
			}

			@Test
			void when_exactMaxLength_then_success() {
				var actual = PROCESSOR.process("12345");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue("12345");
			}

			@Test
			void when_longer_then_failure() {
				var actual = PROCESSOR.process("123456");
				ProcessResultAssert.assertThat(actual).isFailure(new ValidationFailure.StringIsTooLong(6, 5));
			}

		}

		@Nested
		class CanBeParsedToInteger {

			private static final InputProcessor<String, String> PROCESSOR = InputProcessor.builder().forString()
				.validateThat().canBeParsedToInteger().then()
				.build();

			@Test
			void when_negative_then_success() {
				var actual = PROCESSOR.process("-1");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue("-1");
			}

			@Test
			void when_zero_then_success() {
				var actual = PROCESSOR.process("0");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue("0");
			}

			@Test
			void when_positive_then_success() {
				var actual = PROCESSOR.process("1");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue("1");
			}

			@Test
			void when_positiveWithSign_then_success() {
				var actual = PROCESSOR.process("+1");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue("+1");
			}

			@Test
			void when_minValue_then_success() {
				var actual = PROCESSOR.process(String.valueOf(Integer.MIN_VALUE));
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(String.valueOf(Integer.MIN_VALUE));
			}

			@Test
			void when_maxValue_then_success() {
				var actual = PROCESSOR.process(String.valueOf(Integer.MAX_VALUE));
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(String.valueOf(Integer.MAX_VALUE));
			}

			@Test
			void when_beforeMinValue_then_failure() {
				var actual = PROCESSOR.process("-21474836481"); // Integer.MIN_VALUE - 1 (64 bits)
				ProcessResultAssert.assertThat(actual).isFailure()
					.assertThatFailure().isValidationFailure()
					.isInstanceOf(StringIsNotParseableToInteger.class);
			}

			@Test
			void when_beyondMaxValue_then_failure() {
				var actual = PROCESSOR.process("2147483648"); // Integer.MAX_VALUE + 1 (64 bits)
				ProcessResultAssert.assertThat(actual).isFailure()
					.assertThatFailure().isValidationFailure()
					.isInstanceOf(StringIsNotParseableToInteger.class);
			}

		}

	}

	@Nested
	class Mapping {

		@Nested
		class MapToInteger {

			static final InputProcessor<String, Integer> PROCESSOR = InputProcessor.builder().forString().mapToInteger().build();

			@Test
			void when_negative_then_success() {
				var actual = PROCESSOR.process("-1");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(-1);
			}

			@Test
			void when_zero_then_success() {
				var actual = PROCESSOR.process("0");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(0);
			}

			@Test
			void when_positive_then_success() {
				var actual = PROCESSOR.process("1");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(1);
			}

			@Test
			void when_positiveWithSign_then_success() {
				var actual = PROCESSOR.process("+1");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(1);
			}

			@Test
			void when_minValue_then_success() {
				var actual = PROCESSOR.process(String.valueOf(Integer.MIN_VALUE));
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(Integer.MIN_VALUE);
			}

			@Test
			void when_maxValue_then_success() {
				var actual = PROCESSOR.process(String.valueOf(Integer.MAX_VALUE));
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(Integer.MAX_VALUE);
			}

			@Test
			void when_beforeMinValue_then_failure() {
				var actual = PROCESSOR.process("-21474836481"); // Integer.MIN_VALUE - 1 (64 bits)
				ProcessResultAssert.assertThat(actual).isFailure()
					.assertThatFailure().isUnexpectedException()
					.assertThatException().isInstanceOf(NumberFormatException.class);
			}

			@Test
			void when_beyondMaxValue_then_failure() {
				var actual = PROCESSOR.process("2147483648"); // Integer.MAX_VALUE + 1 (64 bits)
				ProcessResultAssert.assertThat(actual).isFailure()
					.assertThatFailure().isUnexpectedException()
					.assertThatException().isInstanceOf(NumberFormatException.class);
			}

		}

		@Nested
		class MapToLong {

			static final InputProcessor<String, Long> PROCESSOR = InputProcessor.builder().forString().mapToLong().build();

			@Test
			void when_negative_then_success() {
				var actual = PROCESSOR.process("-1");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(-1L);
			}

			@Test
			void when_zero_then_success() {
				var actual = PROCESSOR.process("0");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(0L);
			}

			@Test
			void when_positive_then_success() {
				var actual = PROCESSOR.process("1");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(1L);
			}

			@Test
			void when_positiveWithSign_then_success() {
				var actual = PROCESSOR.process("+1");
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(1L);
			}

			@Test
			void when_minValue_then_success() {
				var actual = PROCESSOR.process(String.valueOf(Long.MIN_VALUE));
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(Long.MIN_VALUE);
			}

			@Test
			void when_maxValue_then_success() {
				var actual = PROCESSOR.process(String.valueOf(Long.MAX_VALUE));
				ProcessResultAssert.assertThat(actual).isSuccessWithValue(Long.MAX_VALUE);
			}

			@Test
			void when_beforeMinValue_then_failure() {
				var actual = PROCESSOR.process("-9223372036854775809"); // Long.MIN_VALUE - 1 (64 bits)
				ProcessResultAssert.assertThat(actual).isFailure()
					.assertThatFailure().isUnexpectedException()
					.assertThatException().isInstanceOf(NumberFormatException.class);
			}

			@Test
			void when_beyondMaxValue_then_failure() {
				var actual = PROCESSOR.process("9223372036854775808"); // Long.MAX_VALUE + 1 (64 bits)
				ProcessResultAssert.assertThat(actual).isFailure()
					.assertThatFailure().isUnexpectedException()
					.assertThatException().isInstanceOf(NumberFormatException.class);
			}

		}

	}

}
