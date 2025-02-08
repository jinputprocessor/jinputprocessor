package io.github.jinputprocessor;

import io.github.jinputprocessor.ProcessFailure.UnexpectedException;
import io.github.jinputprocessor.ProcessFailure.ValidationError;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;

public class ProcessFailureAssert extends AbstractAssert<ProcessFailureAssert, ProcessFailure> {

	private ProcessFailureAssert(ProcessFailure actual) {
		super(actual, ProcessFailureAssert.class);
	}

	public static ProcessFailureAssert assertThat(ProcessFailure actual) {
		return new ProcessFailureAssert(actual);
	}

	public UnexpectedExceptionAssert isUnexpectedException() {
		if (actual instanceof UnexpectedException err) {
			return UnexpectedExceptionAssert.assertThat(err);
		}
		return Assertions.fail("Failure expected to be " + UnexpectedException.class + ", but is " + actual.getClass() + ":\n" + actual);
	}

	public ValidationErrorAssert isValidationError() {
		if (actual instanceof ValidationError err) {
			return ValidationErrorAssert.assertThat(err);
		}
		return Assertions.fail("Failure expected to be " + ValidationError.class + ", but is " + actual.getClass() + ":\n" + actual);
	}

	/**
	 * 
	 * 
	 *
	 */
	public static class UnexpectedExceptionAssert extends AbstractAssert<UnexpectedExceptionAssert, UnexpectedException> {

		private UnexpectedExceptionAssert(UnexpectedException actual) {
			super(actual, UnexpectedExceptionAssert.class);
		}

		public static UnexpectedExceptionAssert assertThat(UnexpectedException actual) {
			return new UnexpectedExceptionAssert(actual);
		}

		public <T extends Throwable> UnexpectedExceptionAssert hasExceptionClass(Class<T> exceptionClass) {
			Assertions.assertThat(actual.exception()).isInstanceOf(exceptionClass);
			return this;
		}

		public AbstractThrowableAssert<?, Throwable> assertThatException() {
			return Assertions.assertThat(actual.exception());
		}

	}

	/**
	 * 
	 * 
	 *
	 */
	public static class ValidationErrorAssert extends AbstractAssert<ValidationErrorAssert, ValidationError> {

		private ValidationErrorAssert(ValidationError actual) {
			super(actual, ValidationErrorAssert.class);
		}

		public static ValidationErrorAssert assertThat(ValidationError actual) {
			return new ValidationErrorAssert(actual);
		}

	}

}
