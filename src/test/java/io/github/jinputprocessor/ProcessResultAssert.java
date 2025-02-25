package io.github.jinputprocessor;

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
			.as(descriptionText())
			.overridingErrorMessage(() -> "Expected process result to be success, but is failure: " + actual.getFailure())
			.isTrue();
		return this;
	}

	public ProcessResultAssert<T> isSuccessWithValue(T expectedValue) {
		isSuccess();
		Assertions.assertThat(actual.get())
			.as(descriptionText())
			.overridingErrorMessage("Expected process result to have value:\n  %s\nbut has:\n  %s", expectedValue, actual.get())
			.isEqualTo(expectedValue);
		return this;
	}

	public ProcessResultAssert<T> isFailure() {
		Assertions.assertThat(actual.isFailure())
			.as(descriptionText())
			.overridingErrorMessage(() -> "Expected process result to be failure, but is success with value: " + actual.get())
			.isTrue();
		return this;
	}

	public ProcessResultAssert<T> isFailure(ProcessFailure expectedFailure) {
		isFailure();
		Assertions.assertThat(actual.getFailure())
			.as(descriptionText())
			.overridingErrorMessage(() -> "Expected process result failed because of:\n  " + expectedFailure + "\nbut failed because of:\n  " + actual.getFailure())
			.isEqualTo(expectedFailure);
		return this;
	}

	public ProcessFailureAssert assertThatFailure() {
		return ProcessFailureAssert.assertThat(actual.getFailure());
	}

}
