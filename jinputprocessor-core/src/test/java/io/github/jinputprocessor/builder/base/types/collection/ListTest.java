package io.github.jinputprocessor.builder.base.types.collection;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessFailure.ValidationError;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ListTest {

	@Nested
	class ApplyProcessOnEach {

		@Test
		void sanitization() {
			var stringProcessor = InputProcessor.builder().forString().sanitize(String::strip).build();

			var listProcessor = InputProcessor.builder()
				.forList(String.class)
				.processEach(stringProcessor)
				.build();

			var result = listProcessor.process(List.of(" ", " abc ", " 123 "));

			Assertions.assertThat(result.isSuccess()).isTrue();
			var actual = result.get();
			var expected = List.of("", "abc", "123");
			Assertions.assertThat(actual).containsExactlyElementsOf(expected);
			Assertions.assertThat(actual).isUnmodifiable();
		}

		@Test
		void validation_success() {
			var stringProcessor = InputProcessor.builder().forString().validateThat().isNotEmpty().then().build();

			var listProcessor = InputProcessor.builder()
				.forList(String.class)
				.processEach(stringProcessor)
				.build();

			var result = listProcessor.process(List.of(" ", " abc ", " 123 "));

			Assertions.assertThat(result.isSuccess()).isTrue();
			var actualList = result.get();
			var expectedList = List.of(" ", " abc ", " 123 ");
			Assertions.assertThat(actualList).containsExactlyElementsOf(expectedList);
			Assertions.assertThat(actualList).isUnmodifiable();
		}

		@Test
		void validation_fail() {
			var stringProcessor = InputProcessor.builder().forString().validateThat().isNotEmpty().then().build();

			var listProcessor = InputProcessor.builder()
				.forList(String.class)
				.processEach(stringProcessor)
				.build();

			var result = listProcessor.process(List.of("", "abc", "", "123", ""));

			Assertions.assertThat(result.isFailure()).isTrue();
			var actualFailure = result.getFailure();
			var expectedFailure = new ProcessFailure.MultiFailure(
				List.of(
					new ProcessFailure.IndexedFailure(0, new ValidationError.StringIsEmpty()),
					new ProcessFailure.IndexedFailure(2, new ValidationError.StringIsEmpty()),
					new ProcessFailure.IndexedFailure(4, new ValidationError.StringIsEmpty())
				)
			);
			Assertions.assertThat(actualFailure).isEqualTo(expectedFailure);

			Assertions.assertThatIllegalArgumentException()
				.isThrownBy(() -> result.getOrThrow())
				.withMessage("Multiple failures while processing value")
				.satisfies(e -> {
					Assertions.assertThat(e.getSuppressed()).hasSize(3);
					Assertions.assertThat(e.getSuppressed()[0]).hasMessage("Invalid index 0: must not be empty");
					Assertions.assertThat(e.getSuppressed()[1]).hasMessage("Invalid index 2: must not be empty");
					Assertions.assertThat(e.getSuppressed()[2]).hasMessage("Invalid index 4: must not be empty");
				});

		}

		@Test
		void mapping() {
			var stringProcessor = InputProcessor.builder().forString().mapToInteger().build();

			var listProcessor = InputProcessor.builder()
				.forList(String.class)
				.processEach(stringProcessor)
				.build();

			var result = listProcessor.process(List.of("1", "2", "3"));

			Assertions.assertThat(result.isSuccess()).isTrue();
			var actual = result.get();
			var expected = List.of(1, 2, 3);
			Assertions.assertThat(actual).containsExactlyElementsOf(expected);
			Assertions.assertThat(actual).isUnmodifiable();
		}

	}

}
