package io.github.jinputprocessor;

import io.github.jinputprocessor.ProcessResult;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ProcessResultAssert<T> extends AbstractAssert<ProcessResultAssert<T>, ProcessResult<T>> {

	private ProcessResultAssert(ProcessResult<T> actual) {
		super(actual, ProcessResultAssert.class);
	}

	public static <T> ProcessResultAssert<T> assertThat(ProcessResult<T> actual) {
		return new ProcessResultAssert<>(actual);
	}

	public ProcessResultAssert<T> isSuccess() {
		Assertions.assertThat(actual.isSuccess())
			.overridingErrorMessage(() -> "Expected process result to be success, but is failure: " + actual.getFailure())
			.isTrue();
		return this;
	}

	public ProcessResultAssert<T> isSuccessWithValue(T expectedValue) {
		isSuccess();
		Assertions.assertThat(actual.getValue())
			.overridingErrorMessage("Expected process result to have value <%s>, but has <%s>", expectedValue, actual.getValue())
			.isEqualTo(expectedValue);
		return this;
	}

	public ProcessResultAssert<T> isFailure() {
		Assertions.assertThat(actual.isFailure())
			.overridingErrorMessage("Expected process result to be failure, but is success with value <%s>", actual.getValue())
			.isFalse();
		return this;
	}

}
