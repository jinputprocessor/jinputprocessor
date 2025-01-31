package io.github.jinputprocessor.core.processor;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessFailure.ValidationError;
import io.github.jinputprocessor.ProcessResult;
import jakarta.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Function;

public class ValidationProcessor<T> implements InputProcessor<T, T> {

	@Nonnull
	private final Function<T, ValidationError> validationErrorFunction;

	public ValidationProcessor(
		@Nonnull Function<T, ValidationError> validationErrorFunction
	) {
		this.validationErrorFunction = Objects.requireNonNull(validationErrorFunction);
	}

	@Override
	public ProcessResult<T> process(T value) {
		try {
			var error = validationErrorFunction.apply(value);
			return error == null
				? ProcessResult.success(value)
				: ProcessResult.failure(error);
		} catch (Throwable t) {
			return ProcessResult.failure(new ProcessFailure.UnexpectedException(value, t));
		}
	}

}
