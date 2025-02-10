package io.github.jinputprocessor.builder.base.types.collection;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.Path;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessFailure.ValidationError;
import io.github.jinputprocessor.ProcessResultAssert;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SetInputProcessorBuilderTest {

	@Nested
	class Sanitization {

		@Test
		void nominal() {
			var setProcessor = InputProcessor.builder().forSet(String.class)
				.sanitize(set -> set.stream().filter(str -> str.length() >= 2).collect(Collectors.toSet()))
				.build();

			var actualResult = setProcessor.process(Set.of("a", "bb", "ccc", "d", "eeee", "f", ""));

			ProcessResultAssert.assertThat(actualResult)
				.isSuccessWithValue(Set.of("bb", "ccc", "eeee"));
		}

	}

	@Nested
	class Validation {

		@Test
		void nominal_success() {
			var setProcessor = InputProcessor.builder().forSet(String.class)
				.validate(set -> set.isEmpty() ? new ValidationError.CollectionIsEmpty() : null)
				.build();

			var actualResult = setProcessor.process(Set.of("a"));

			ProcessResultAssert.assertThat(actualResult)
				.isSuccessWithValue(Set.of("a"));
		}

		@Test
		void nominal_failure() {
			var setProcessor = InputProcessor.builder().forSet(String.class)
				.validate(set -> set.isEmpty() ? new ValidationError.CollectionIsEmpty() : null)
				.build();

			var actualResult = setProcessor.process(Set.of());

			ProcessResultAssert.assertThat(actualResult)
				.isFailure(new ValidationError.CollectionIsEmpty());
		}

	}

	@Nested
	class Mapping {

		@Test
		void nominal() {
			var setProcessor = InputProcessor.builder().forSet(String.class)
				.mapTo(set -> set.stream().map(Integer::parseInt).collect(Collectors.toSet()))
				.build();

			var actualResult = setProcessor.process(Set.of("0", "1", "2"));

			ProcessResultAssert.assertThat(actualResult)
				.isSuccessWithValue(Set.of(0, 1, 2));
		}

	}

	@Nested
	class ProcessEach {

		@Nested
		class Sanitization {

			@Test
			void sanitization_all_success() {
				var itemProcessor = InputProcessor.builder().forString().sanitize().strip().then().build();
				var setProcessor = InputProcessor.builder().forSet(String.class)
					.processEach(/*NullStrategy.removeNulls()*/ itemProcessor)
					.build();

				var actualResult = setProcessor.process(Set.of("", " ", " a", "b ", " c "));

				ProcessResultAssert.assertThat(actualResult)
					.isSuccessWithValue(Set.of("", "a", "b", "c"));
			}

			@Test
			void sanitization_all_success_withCollector() {
				var itemProcessor = InputProcessor.builder().forString().sanitize().strip().then().build();
				var setProcessor = InputProcessor.builder().forSet(String.class)
					.processEach(/*NullStrategy.removeNulls()*/ itemProcessor, Collectors.toCollection(() -> new HashSet<>()))
					.build();

				var actualResult = setProcessor.process(Set.of("", " ", " a", "b ", " c "));

				ProcessResultAssert.assertThat(actualResult)
					.isSuccessWithValue(Set.of("", "a", "b", "c"));

				Assertions.assertThat(actualResult.get())
					.isExactlyInstanceOf(HashSet.class);
			}

		}

		@Nested
		class Validation {

			@Test
			void validation_all_success() {
				var itemProcessor = InputProcessor.builder().forString().validateThat().isNotNull().then().build();
				var setProcessor = InputProcessor.builder().forSet(String.class)
					.processEach(/*NullStrategy.removeNulls()*/ itemProcessor)
					.build();

				var actualResult = setProcessor.process(Set.of("", " ", " a", "b ", " c "));

				ProcessResultAssert.assertThat(actualResult)
					.isSuccessWithValue(Set.of("", " ", " a", "b ", " c "));
			}

			@Test
			void validation_fail() {
				var itemProcessor = InputProcessor.builder().forString().validateThat().isNotEmpty().then().build();
				var setProcessor = InputProcessor.builder().forSet(String.class)
					.processEach(/*NullStrategy.removeNulls()*/ itemProcessor)
					.build();

				var actualResult = setProcessor.process(Set.of("", "abc", "123"));

				var expectedFailure = new ProcessFailure.MultiFailure(
					List.of(
						new ValidationError.StringIsEmpty()
					)
				);
				ProcessResultAssert.assertThat(actualResult)
					.isFailure(expectedFailure);
			}

			@Test
			void validation_fail_atPath() {
				var itemProcessor = InputProcessor.builder().forString().validateThat().isNotEmpty().then().build();
				var setProcessor = InputProcessor.builder().forSet(String.class)
					.processEach(/*NullStrategy.removeNulls()*/ itemProcessor)
					.build();

				var actualResult = setProcessor.process(Set.of("", "abc", "123")).atPath(Path.createPropertyPath("mySet"));

				var expectedFailure = new ProcessFailure.MultiFailure(
					List.of(
						new ValidationError.StringIsEmpty()
					)
				).atPath(Path.createPropertyPath("mySet"));
				ProcessResultAssert.assertThat(actualResult)
					.isFailure(expectedFailure);
			}

		}

		@Nested
		class Mapping {

			@Test
			void mapping_all_success() {
				var itemProcessor = InputProcessor.builder().forString().mapToInteger().build();
				var setProcessor = InputProcessor.builder().forSet(String.class)
					.processEach(/*NullStrategy.removeNulls()*/ itemProcessor)
					.build();

				var actualResult = setProcessor.process(Set.of("0", "1", "2"));

				ProcessResultAssert.assertThat(actualResult)
					.isSuccessWithValue(Set.of(0, 1, 2));

				Assertions.assertThat(actualResult.get())
					.isUnmodifiable();
			}

		}

	}

}
