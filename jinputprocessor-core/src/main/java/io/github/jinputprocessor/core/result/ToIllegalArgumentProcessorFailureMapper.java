package io.github.jinputprocessor.core.result;

import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessFailure.ValidationError;
import io.github.jinputprocessor.ProcessFailureMapper;

public class ToIllegalArgumentProcessorFailureMapper implements ProcessFailureMapper {

	@Override
	public IllegalArgumentException mapFailure(ProcessFailure failure) {
		return mapFailure("", failure);
	}

	public IllegalArgumentException mapFailure(String inputName, ProcessFailure failure) {
		return switch (failure) {
			case ProcessFailure.NamedFailure namedFail -> mapNamedFailure(inputName, namedFail);
			case ProcessFailure.IndexedFailure idxFail -> mapIndexedFailure(inputName, idxFail);
			case ProcessFailure.MultiFailure multiFail -> mapMultiFailure(inputName, multiFail);
			case ProcessFailure.UnexpectedException unexpFail -> mapUnexpectedFailure(inputName, unexpFail);
			case ProcessFailure.ValidationError validationError -> mapValidationError(inputName, validationError);
		};
	}

	private IllegalArgumentException mapNamedFailure(String inputName, ProcessFailure.NamedFailure failure) {
		return mapFailure((inputName.isEmpty() ? "" : ".") + failure.name(), failure.failure());
	}

	private IllegalArgumentException mapIndexedFailure(String inputName, ProcessFailure.IndexedFailure failure) {
		return mapFailure(formatInputName(inputName, failure.index()), failure.failure());
	}

	private IllegalArgumentException mapMultiFailure(String inputName, ProcessFailure.MultiFailure failure) {
		var exception = new IllegalArgumentException("Multiple failures while processing " + formatInputName(inputName));
		failure.failures().stream()
			.map(failureItem -> mapFailure(inputName, failureItem))
			.forEach(exception::addSuppressed);
		return exception;
	}

	private IllegalArgumentException mapUnexpectedFailure(String inputName, ProcessFailure.UnexpectedException failure) {
		return new IllegalArgumentException("Unexpected exception while processing " + formatInputName(inputName), failure.exception());
	}

	private IllegalArgumentException mapValidationError(String inputName, ValidationError validationError) {
		return new IllegalArgumentException("Invalid " + formatInputName(inputName) + ": " + validationErrorToString(validationError));
	}

	private String formatInputName(String inputName, int index) {
		if (inputName.isEmpty()) {
			return "index " + index;
		}
		return formatInputName(inputName) + "[" + index + "]";
	}

	private String formatInputName(String inputName) {
		return inputName.isEmpty() ? "value" : inputName;
	}

	private String validationErrorToString(ValidationError validationError) {
		return switch (validationError) {
			case ValidationError.ObjectIsNull err -> "must not be null";
			case ValidationError.StringIsEmpty err -> "must not be empty";
			case ValidationError.StringTooLong err -> "must be " + err.maxLength() + " chars max, but is " + err.currentLength();
			case ValidationError.StringNotParseableToInteger err -> "is not parseable to Integer";
			case ValidationError.NumberMustBeGreaterThan err -> "must be greater than " + err.ref();
			case ValidationError.NumberMustBeGreaterOrEqualTo err -> "must be greater or equal to " + err.ref();

			case ValidationError.CustomValidationError err -> err.toString();
		};
	}

}