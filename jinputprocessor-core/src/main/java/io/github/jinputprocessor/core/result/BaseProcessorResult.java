package io.github.jinputprocessor.core.result;

import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessFailureMapper;
import io.github.jinputprocessor.ProcessResult;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Objects;

public class BaseProcessorResult<T> implements ProcessResult<T> {

	// ------------------------------------------------------------------------------------------------------------

	private static ProcessFailureMapper defaultFailureMapper = new ToIllegalArgumentProcessorFailureMapper();

	public static ProcessFailureMapper getDefaultFailureMapper() {
		return defaultFailureMapper;
	}

	public static void setDefaultFailureMapper(ProcessFailureMapper defaultFailureMapper) {
		BaseProcessorResult.defaultFailureMapper = defaultFailureMapper;
	}

	// ------------------------------------------------------------------------------------------------------------

	private final ProcessFailureMapper failureMapper;
	private final @Nullable T value;
	private final @Nullable ProcessFailure failure;

	private BaseProcessorResult(@Nullable T value, @Nullable ProcessFailure failure, @Nonnull ProcessFailureMapper failureMapper) {
		this.value = value;
		this.failure = failure;
		this.failureMapper = Objects.requireNonNull(failureMapper, "failureMapper cannot be null");
	}

	public static <OUT> BaseProcessorResult<OUT> success(@Nullable OUT value) {
		return new BaseProcessorResult<OUT>(value, null, defaultFailureMapper);
	}

	public static <OUT> BaseProcessorResult<OUT> failure(@Nonnull ProcessFailure error) {
		Objects.requireNonNull(error);
		return new BaseProcessorResult<>(null, error, defaultFailureMapper);
	}

	@Override
	public ProcessResult<T> withFailureMapper(@Nonnull ProcessFailureMapper failureMapper) {
		return new BaseProcessorResult<T>(value, failure, failureMapper);
	}

	@Override
	public boolean isSuccess() {
		return failure == null;
	}

	@Override
	public boolean isFailure() {
		return !isSuccess();
	}

	@Override
	public @Nonnull T getValue() {
		if (isFailure()) {
			throw new IllegalStateException("Cannot get the value as result is failure, please first test with isSuccess()/isFailure()");
		}
		return value;
	}

	@Override
	public T getValueOrThrow(String inputName) {
		if (isFailure()) {
			throw failureMapper.mapFailure(inputName, failure);
		}
		return value;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public ProcessFailure getFailure() {
		if (isSuccess()) {
			throw new IllegalStateException("Cannot get the failure as result is success, please first test with isSuccess()/isFailure()");
		}
		return failure;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value, failure);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseProcessorResult<?> other = (BaseProcessorResult<?>) obj;
		return Objects.equals(failure, other.failure) && Objects.equals(value, other.value);
	}

}
