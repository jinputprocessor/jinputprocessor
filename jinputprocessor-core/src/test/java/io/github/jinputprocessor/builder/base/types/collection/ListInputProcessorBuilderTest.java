package io.github.jinputprocessor.builder.base.types.collection;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.Path;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessFailure.ValidationFailure;
import io.github.jinputprocessor.ProcessResultAssert;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ListInputProcessorBuilderTest {

	@Nested
	class Sanitization {

		@Test
		void nominal() {
			var listProcessor = InputProcessor.builder().forList(String.class)
				.sanitize(list -> list.stream().filter(str -> str.length() >= 2).toList())
				.build();

			var actualResult = listProcessor.process(List.of("a", "bb", "ccc", "d", "eeee", "f", ""));

			ProcessResultAssert.assertThat(actualResult)
				.isSuccessWithValue(List.of("bb", "ccc", "eeee"));
		}

	}

	@Nested
	class Validation {

		@Test
		void nominal_success() {
			var listProcessor = InputProcessor.builder().forList(String.class)
				.validate(list -> list.isEmpty() ? new ValidationFailure.CollectionIsEmpty() : null)
				.build();

			var actualResult = listProcessor.process(List.of("a"));

			ProcessResultAssert.assertThat(actualResult)
				.isSuccessWithValue(List.of("a"));

			Assertions.assertThat(actualResult.get())
				.isUnmodifiable();
		}

		@Test
		void nominal_failure() {
			var listProcessor = InputProcessor.builder().forList(String.class)
				.validate(list -> list.isEmpty() ? new ValidationFailure.CollectionIsEmpty() : null)
				.build();

			var actualResult = listProcessor.process(List.of());

			ProcessResultAssert.assertThat(actualResult)
				.isFailure(new ValidationFailure.CollectionIsEmpty());
		}

	}

	@Nested
	class Mapping {

		@Test
		void nominal() {
			var listProcessor = InputProcessor.builder().forList(String.class)
				.map(list -> list.stream().map(Integer::parseInt).toList())
				.build();

			var actualResult = listProcessor.process(List.of("0", "1", "2"));

			ProcessResultAssert.assertThat(actualResult)
				.isSuccessWithValue(List.of(0, 1, 2));

			Assertions.assertThat(actualResult.get())
				.isUnmodifiable();
		}

	}

	@Nested
	class ProcessEach {

		@Nested
		class Sanitization {

			@Test
			void sanitization_all_success() {
				var itemProcessor = InputProcessor.builder().forString().sanitize().strip().then().build();
				var listProcessor = InputProcessor.builder().forList(String.class)
					.processEach(/*NullStrategy.removeNulls()*/ itemProcessor)
					.build();

				var actualResult = listProcessor.process(List.of("", " ", " a", "b ", " c "));

				ProcessResultAssert.assertThat(actualResult)
					.isSuccessWithValue(List.of("", "", "a", "b", "c"));

				Assertions.assertThat(actualResult.get())
					.isUnmodifiable();
			}

			@Test
			void sanitization_all_success_withCollector() {
				var itemProcessor = InputProcessor.builder().forString().sanitize().strip().then().build();
				var listProcessor = InputProcessor.builder().forList(String.class)
					.processEach(/*NullStrategy.removeNulls()*/ itemProcessor, Collectors.toCollection(() -> new LinkedList<>()))
					.build();

				var actualResult = listProcessor.process(List.of("", " ", " a", "b ", " c "));

				ProcessResultAssert.assertThat(actualResult)
					.isSuccessWithValue(List.of("", "", "a", "b", "c"));

				Assertions.assertThat(actualResult.get())
					.isExactlyInstanceOf(LinkedList.class);
			}

		}

		@Nested
		class Validation {

			@Test
			void validation_all_success() {
				var itemProcessor = InputProcessor.builder().forString().validateThat().isNotNull().then().build();
				var listProcessor = InputProcessor.builder().forList(String.class)
					.processEach(/*NullStrategy.removeNulls()*/ itemProcessor)
					.build();

				var actualResult = listProcessor.process(List.of("", " ", " a", "b ", " c "));

				ProcessResultAssert.assertThat(actualResult)
					.isSuccessWithValue(List.of("", " ", " a", "b ", " c "));

				Assertions.assertThat(actualResult.get())
					.isUnmodifiable();
			}

			@Test
			void validation_fail() {
				var itemProcessor = InputProcessor.builder().forString().validateThat().isNotEmpty().then().build();
				var listProcessor = InputProcessor.builder().forList(String.class)
					.processEach(/*NullStrategy.removeNulls()*/ itemProcessor)
					.build();

				var actualResult = listProcessor.process(List.of("", "abc", "", "123", ""));

				var expectedFailure = new ProcessFailure.MultiFailure(
					List.of(
						(new ValidationFailure.StringIsEmpty()).atPath(Path.createIndexPath(0)),
						(new ValidationFailure.StringIsEmpty()).atPath(Path.createIndexPath(2)),
						(new ValidationFailure.StringIsEmpty()).atPath(Path.createIndexPath(4))
					)
				);
				ProcessResultAssert.assertThat(actualResult)
					.isFailure(expectedFailure);
			}

			@Test
			void validation_fail_atPath() {
				var itemProcessor = InputProcessor.builder().forString().validateThat().isNotEmpty().then().build();
				var listProcessor = InputProcessor.builder().forList(String.class)
					.processEach(/*NullStrategy.removeNulls()*/ itemProcessor)
					.build();

				var actualResult = listProcessor.process(List.of("", "abc", "", "123", "")).atPath(Path.createPropertyPath("myList"));

				var expectedFailure = new ProcessFailure.MultiFailure(
					List.of(
						(new ValidationFailure.StringIsEmpty()).atPath(Path.createIndexPath(0)),
						(new ValidationFailure.StringIsEmpty()).atPath(Path.createIndexPath(2)),
						(new ValidationFailure.StringIsEmpty()).atPath(Path.createIndexPath(4))
					)
				).atPath(Path.createPropertyPath("myList"));
				ProcessResultAssert.assertThat(actualResult)
					.isFailure(expectedFailure);
			}

		}

		@Nested
		class Mapping {

			@Test
			void mapping_all_success() {
				var itemProcessor = InputProcessor.builder().forString().mapToInteger().build();
				var listProcessor = InputProcessor.builder().forList(String.class)
					.processEach(/*NullStrategy.removeNulls()*/ itemProcessor)
					.build();

				var actualResult = listProcessor.process(List.of("0", "1", "2"));

				ProcessResultAssert.assertThat(actualResult)
					.isSuccessWithValue(List.of(0, 1, 2));

				Assertions.assertThat(actualResult.get())
					.isUnmodifiable();
			}

		}

	}

}
