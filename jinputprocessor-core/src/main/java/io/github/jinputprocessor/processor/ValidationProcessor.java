package io.github.jinputprocessor.processor;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessFailure.ValidationFailure;
import io.github.jinputprocessor.ProcessResult;
import jakarta.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Function;

public class ValidationProcessor<T> implements InputProcessor<T, T> {

	@Nonnull
	private final Function<T, ValidationFailure> validationFailureFunction;

	public ValidationProcessor(
		@Nonnull Function<T, ValidationFailure> validationFailureFunction
	) {
		this.validationFailureFunction = Objects.requireNonNull(validationFailureFunction);
	}

	@Override
	public ProcessResult<T> process(T value) {
		try {
			var failure = validationFailureFunction.apply(value);
			return failure == null
				? ProcessResult.success(value)
				: ProcessResult.failure(failure);
		} catch (Throwable t) {
			return ProcessResult.failure(new ProcessFailure.UnexpectedException(value, t));
		}
	}

	@Override
	public String toString() {
		return "ValidationProcessor -> " + validationFailureFunction.toString();
	}

}
