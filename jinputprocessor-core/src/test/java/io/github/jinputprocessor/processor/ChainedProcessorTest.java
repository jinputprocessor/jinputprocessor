package io.github.jinputprocessor.processor;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure.ValidationError;
import io.github.jinputprocessor.processor.ChainedProcessor;
import io.github.jinputprocessor.ProcessResult;
import io.github.jinputprocessor.ProcessResultAssert;
import java.util.concurrent.atomic.AtomicBoolean;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ChainedProcessorTest {

	@Test
	void nominal() {
		InputProcessor<String, String> subProcessor1 = value -> ProcessResult.success(value + "-1");
		InputProcessor<String, String> subProcessor2 = value -> ProcessResult.success(value + "-2");

		var processor = new ChainedProcessor<>(subProcessor1, subProcessor2);
		var actualResult = processor.process("0");

		ProcessResultAssert.assertThat(actualResult).isSuccessWithValue("0-1-2");
	}

	@Test
	void when_error_in_first_then_second_is_not_processed() {

		var validationError = new ValidationError.ObjectIsNull();
		InputProcessor<String, String> subProcessor1 = value -> ProcessResult.failure(validationError);

		var secondProcessIsCalled = new AtomicBoolean(false);
		InputProcessor<String, String> subProcessor2 = value -> {
			secondProcessIsCalled.set(true);
			return ProcessResult.success("2");
		};

		var processor = new ChainedProcessor<>(subProcessor1, subProcessor2);
		var actualResult = processor.process("1");

		ProcessResultAssert.assertThat(actualResult).isFailure()
			.assertThatFailure().isEqualTo(validationError);
		Assertions.assertThat(secondProcessIsCalled).isFalse();
	}

}
