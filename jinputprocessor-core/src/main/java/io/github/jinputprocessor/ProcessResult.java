package io.github.jinputprocessor;

import io.github.jinputprocessor.core.result.BaseProcessorResult;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface ProcessResult<T> {

	public boolean isSuccess();

	public boolean isFailure();

	public @Nonnull T getValue();

	public @Nonnull T getValueOrThrow(String inputName);

	public ProcessFailure getFailure();

	public ProcessResult<T> withFailureMapper(ProcessFailureMapper failureMapper);

	// ===========================================================================================================

	public static <OUT> ProcessResult<OUT> success(@Nullable OUT value) {
		return BaseProcessorResult.success(value);
	}

	public static <OUT> ProcessResult<OUT> failure(@Nonnull ProcessFailure error) {
		return BaseProcessorResult.failure(error);
	}

}
