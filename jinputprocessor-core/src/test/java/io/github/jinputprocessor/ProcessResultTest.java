package io.github.jinputprocessor;

import io.github.jinputprocessor.result.BaseProcessorResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProcessResultTest {

	@Test
	void processResult_success_instance() {
		Assertions
			.assertThat(ProcessResult.success(""))
			.isInstanceOf(BaseProcessorResult.class);
	}

	@Test
	void processResult_failure_instance() {
		Assertions
			.assertThat(ProcessResult.failure(new ProcessFailure.ValidationError.ObjectIsNull()))
			.isInstanceOf(BaseProcessorResult.class);
	}

}
