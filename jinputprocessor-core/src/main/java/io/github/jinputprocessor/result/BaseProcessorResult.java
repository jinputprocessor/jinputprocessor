package io.github.jinputprocessor.result;

import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessFailureMapper;
import io.github.jinputprocessor.ProcessResult;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Objects;

public class BaseProcessorResult<T> implements ProcessResult<T> {

	// ------------------------------------------------------------------------------------------------------------

	private static ProcessFailureMapper defaultFailureMapper = new ToIllegalArgumentProcessorFailureMapper();

	@Nonnull
	public static ProcessFailureMapper getDefaultFailureMapper() {
		return defaultFailureMapper;
	}

	public static void setDefaultFailureMapper(@Nonnull ProcessFailureMapper defaultFailureMapper) {
		BaseProcessorResult.defaultFailureMapper = Objects.requireNonNull(defaultFailureMapper, "defaultFailureMapper cannot be null");
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

	public static <OUT> BaseProcessorResult<OUT> failure(@Nonnull ProcessFailure failure) {
		Objects.requireNonNull(failure, "failure cannot be null");
		return new BaseProcessorResult<>(null, failure, defaultFailureMapper);
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
	public @Nonnull T get() {
		if (isFailure()) {
			throw new IllegalStateException("Cannot get the value as result is failure, please first test with isSuccess()/isFailure()");
		}
		return value;
	}

	@Override
	public T getOrThrow() {
		if (isFailure()) {
			throw failureMapper.mapFailure(failure);
		}
		return value;
	}

	@Override
	public ProcessResult<T> withName(String name) {
		var newFailure = failure;
		if (newFailure != null) {
			var cleanName = Objects.requireNonNull(name, "name cannot be null").strip();
			newFailure = failure.at(cleanName);
		}
		return new BaseProcessorResult<>(value, newFailure, failureMapper);
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
		return Objects.hash(value, failure, failureMapper);
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
		return Objects.equals(failure, other.failure)
			&& Objects.equals(value, other.value)
			&& Objects.equals(failureMapper, other.failureMapper);
	}

	@Override
	public String toString() {
		return "ProcessorResult" +
			(isSuccess()
				? "<Success>: " + value
				: "<Failure>: " + failure);
	}

}
