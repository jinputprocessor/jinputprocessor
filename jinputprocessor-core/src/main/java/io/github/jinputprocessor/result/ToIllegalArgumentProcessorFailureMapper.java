package io.github.jinputprocessor.result;

import io.github.jinputprocessor.Path;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessFailure.ValidationError;

public class ToIllegalArgumentProcessorFailureMapper implements ProcessFailureMapper {

	@Override
	public IllegalArgumentException mapFailure(ProcessFailure failure) {
		return mapFailure(Path.atRoot(), failure);
	}

	public IllegalArgumentException mapFailure(Path parentPath, ProcessFailure failure) {
		return switch (failure) {
			case ProcessFailure.PathFailure namedFail -> mapPathFailure(parentPath, namedFail);
			case ProcessFailure.MultiFailure multiFail -> mapMultiFailure(parentPath, multiFail);
			case ProcessFailure.UnexpectedException unexpFail -> mapUnexpectedFailure(parentPath, unexpFail);
			case ProcessFailure.ValidationError validationError -> mapValidationError(parentPath, validationError);
		};
	}

	private IllegalArgumentException mapPathFailure(Path parentPath, ProcessFailure.PathFailure failure) {
		return mapFailure(failure.getPath().atPath(parentPath), failure.failure());
	}

	private IllegalArgumentException mapMultiFailure(Path path, ProcessFailure.MultiFailure failure) {
		var exception = new IllegalArgumentException("Multiple failures while processing " + path.format());
		failure.failures().stream()
			.map(failureItem -> mapFailure(path, failureItem))
			.forEach(exception::addSuppressed);
		return exception;
	}

	private IllegalArgumentException mapUnexpectedFailure(Path path, ProcessFailure.UnexpectedException failure) {
		return new IllegalArgumentException("Unexpected exception while processing " + path.format(), failure.exception());
	}

	private IllegalArgumentException mapValidationError(Path path, ValidationError validationError) {
		return new IllegalArgumentException("Invalid " + path.format() + ": " + validationErrorToString(validationError));
	}

	private String validationErrorToString(ValidationError validationError) {
		return switch (validationError) {
			case ValidationError.ObjectIsNull err -> "must not be null";

			case ValidationError.StringIsEmpty err -> "must not be empty";
			case ValidationError.StringIsTooLong err -> "must be " + err.maxLength() + " chars max, but is " + err.currentLength();
			case ValidationError.StringIsNotParseableToInteger err -> "is not parseable to Integer";

			case ValidationError.NumberIsNotGreaterThan<?> err -> "must be greater than " + err.ref();
			case ValidationError.NumberIsNotGreaterOrEqualTo<?> err -> "must be greater or equal to " + err.ref();

			case ValidationError.CollectionIsEmpty err -> "collection is empty";

			case ValidationError.CustomValidationError err -> err.toString();
		};
	}

}