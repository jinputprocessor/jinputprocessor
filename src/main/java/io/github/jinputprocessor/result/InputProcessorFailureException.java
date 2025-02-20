package io.github.jinputprocessor.result;

import io.github.jinputprocessor.ProcessFailure;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Objects;

public class InputProcessorFailureException extends IllegalArgumentException {

	@Nonnull
	private final ProcessFailure failure;

	public InputProcessorFailureException(String message, @Nonnull ProcessFailure failure) {
		this(message, failure, null);
	}

	public InputProcessorFailureException(String message, @Nonnull ProcessFailure failure, @Nullable Throwable cause) {
		super(message, cause);
		this.failure = Objects.requireNonNull(failure, "failure cannot be null");
	}

	public ProcessFailure getFailure() {
		return failure;
	}

}
