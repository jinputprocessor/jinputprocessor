package io.github.jinputprocessor;

import io.github.jinputprocessor.ProcessFailure.UnexpectedException;
import io.github.jinputprocessor.ProcessFailure.ValidationFailure;
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

	public ValidationFailureAssert isValidationFailure() {
		if (actual instanceof ValidationFailure fail) {
			return ValidationFailureAssert.assertThat(fail);
		}
		return Assertions.fail("Failure expected to be " + ValidationFailure.class + ", but is " + actual.getClass() + ":\n" + actual);
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
	public static class ValidationFailureAssert extends AbstractAssert<ValidationFailureAssert, ValidationFailure> {

		private ValidationFailureAssert(ValidationFailure actual) {
			super(actual, ValidationFailureAssert.class);
		}

		public static ValidationFailureAssert assertThat(ValidationFailure actual) {
			return new ValidationFailureAssert(actual);
		}

	}

}
