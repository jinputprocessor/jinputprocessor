package io.github.jinputprocessor.result;

import io.github.jinputprocessor.Path;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessFailure.ValidationFailure;

public class InputProcessorFailureExceptionMapper implements ProcessFailureMapper {

	@Override
	public InputProcessorFailureException mapFailure(ProcessFailure failure) {
		return mapFailure(Path.root(), failure);
	}

	public InputProcessorFailureException mapFailure(Path parentPath, ProcessFailure failure) {
		return switch (failure) {
			case ProcessFailure.PathFailure namedFail -> mapPathFailure(parentPath, namedFail);
			case ProcessFailure.MultiFailure multiFail -> mapMultiFailure(parentPath, multiFail);
			case ProcessFailure.UnexpectedException unexpFail -> mapUnexpectedFailure(parentPath, unexpFail);
			case ProcessFailure.ValidationFailure validationFail -> mapValidationFailure(parentPath, validationFail);
		};
	}

	private InputProcessorFailureException mapPathFailure(Path parentPath, ProcessFailure.PathFailure failure) {
		return mapFailure(failure.getPath().atPath(parentPath), failure.failure());
	}

	private InputProcessorFailureException mapMultiFailure(Path path, ProcessFailure.MultiFailure failure) {
		var exception = new InputProcessorFailureException("Multiple failures while processing " + path.format(), failure);
		failure.failures().stream()
			.map(failureItem -> mapFailure(path, failureItem))
			.forEach(exception::addSuppressed);
		return exception;
	}

	private InputProcessorFailureException mapUnexpectedFailure(Path path, ProcessFailure.UnexpectedException failure) {
		return new InputProcessorFailureException("Unexpected exception while processing " + path.format(), failure, failure.exception());
	}

	private InputProcessorFailureException mapValidationFailure(Path path, ValidationFailure validationFail) {
		return new InputProcessorFailureException("Invalid " + path.format() + ": " + validationFailureToString(validationFail), validationFail);
	}

	private String validationFailureToString(ValidationFailure validationFail) {
		return switch (validationFail) {
			case ValidationFailure.ObjectIsNull err -> "must not be null";
			case ValidationFailure.ObjectIsNotInstanceOf(var clazz) -> "is not an instance of " + clazz;

			case ValidationFailure.StringIsEmpty err -> "must not be empty";
			case ValidationFailure.StringIsTooLong(var curr, var max) -> "must be " + max + " chars max, but is " + curr;
			case ValidationFailure.StringIsNotParseableToInteger err -> "is not parseable to Integer";

			case ValidationFailure.NumberIsLowerOrEqualTo<?>(var ref) -> "must be greater than " + ref;
			case ValidationFailure.NumberIsLowerThan<?>(var ref) -> "must be greater or equal to " + ref;

			case ValidationFailure.CollectionIsEmpty err -> "is empty";

			case ValidationFailure.CustomValidationFailure err -> err.toString();
		};
	}

}
