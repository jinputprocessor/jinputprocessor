package io.github.jinputprocessor.core.processor;

import io.github.jinputprocessor.ProcessResult;
import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure.ValidationError;
import io.github.jinputprocessor.core.processor.ChainedProcessor;
import java.util.concurrent.atomic.AtomicBoolean;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ChainedProcessorTest {

	@Test
	void when_error_in_first_then_second_is_not_processed() {

		ProcessResult<String> expectedResult = ProcessResult.failure(new ValidationError.CustomError("error.key"));
		InputProcessor<String, String> subProcessor1 = value -> {
			return expectedResult;
		};

		var secondProcessIsCalled = new AtomicBoolean(false);
		InputProcessor<String, String> subProcessor2 = value -> {
			secondProcessIsCalled.set(true);
			return ProcessResult.success("2");
		};

		var processor = new ChainedProcessor<>(subProcessor1, subProcessor2);
		var actualResult = processor.process("1");

		Assertions.assertThat(actualResult).isEqualTo(expectedResult);
		Assertions.assertThat(secondProcessIsCalled).isFalse();
	}

}
