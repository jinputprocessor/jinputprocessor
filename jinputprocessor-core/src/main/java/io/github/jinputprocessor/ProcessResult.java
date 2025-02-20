package io.github.jinputprocessor;

import io.github.jinputprocessor.result.InputProcessorFailureExceptionMapper;
import io.github.jinputprocessor.result.ProcessFailureMapper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Objects;

/**
 * The result produced by a {@link InputProcessor}, either successful or failed.
 * 
 * The state of a result can be checked using {@link #isSuccess()} and {@link #isFailure()} methods.
 *
 * If successful, the processed value can be obtained using {@link #get()}. Note that this method will throw 
 * an {@link IllegalStateException} if result is failure.
 * 
 * @param <T>
 */
public class ProcessResult<T> {

	// ------------------------------------------------------------------------------------------------------------

	private static ProcessFailureMapper defaultFailureMapper = new InputProcessorFailureExceptionMapper();

	@Nonnull
	public static ProcessFailureMapper getDefaultFailureMapper() {
		return defaultFailureMapper;
	}

	public static void setDefaultFailureMapper(@Nonnull ProcessFailureMapper defaultFailureMapper) {
		ProcessResult.defaultFailureMapper = Objects.requireNonNull(defaultFailureMapper, "defaultFailureMapper cannot be null");
	}

	// ------------------------------------------------------------------------------------------------------------

	private final ProcessFailureMapper failureMapper;
	private final @Nullable T value;
	private final @Nullable ProcessFailure failure;

	private ProcessResult(@Nullable T value, @Nullable ProcessFailure failure, @Nonnull ProcessFailureMapper failureMapper) {
		this.value = value;
		this.failure = failure;
		this.failureMapper = Objects.requireNonNull(failureMapper, "failureMapper cannot be null");
	}

	/**
	 * Create a successful process result.
	 * 
	 * @param <OUT> Expected type of the value
	 * @param value	The value
	 * 
	 * @return	A successful process result
	 */
	public static <OUT> ProcessResult<OUT> success(@Nullable OUT value) {
		return new ProcessResult<OUT>(value, null, defaultFailureMapper);
	}

	/**
	 * Create a failed process result, using the given failure reason.
	 * 
	 * @param <OUT> Expected type of the value (not used in this method)
	 * @param value	The failure, cannot be <code>null</code>
	 * 
	 * @return	A failed process result
	 */
	public static <OUT> ProcessResult<OUT> failure(@Nonnull ProcessFailure failure) {
		Objects.requireNonNull(failure, "failure cannot be null");
		return new ProcessResult<>(null, failure, defaultFailureMapper);
	}

	// ------------------------------------------------------------------------------------------------------------

	/**
	 * Test if this result is a success.
	 * 
	 * @return	<code>true</code> if this result is a success, <code>false</code> if it is a failure
	 */
	public boolean isSuccess() {
		return failure == null;
	}

	/**
	 * Test if this result is a failure.
	 * 
	 * @return	<code>true</code> if this result is a failure, <code>false</code> if it is a success
	 */
	public boolean isFailure() {
		return !isSuccess();
	}

	/**
	 * Get the value if process has succeed.
	 * 
	 * @return	The processed value
	 * 
	 * @throws IllegalStateException if process result is failure
	 */
	public @Nonnull T get() {
		if (isFailure()) {
			throw new IllegalStateException("Cannot get the value as result is failure, please first test with isSuccess()/isFailure()");
		}
		return value;
	}

	/**
	 * Get the value if process has succeed, or throws an exception if failure.
	 * 
	 * @return
	 */
	public @Nonnull T getOrThrow() {
		if (isFailure()) {
			throw failureMapper.mapFailure(failure);
		}
		return value;
	}

	/**
	 * Get the failure if process has failed.
	 * 
	 * @return	The failure
	 * 
	 * @throws IllegalStateException if process result is success
	 * 
	 * @see ProcessFailure
	 */
	public ProcessFailure getFailure() {
		if (isSuccess()) {
			throw new IllegalStateException("Cannot get the failure as result is success, please first test with isSuccess()/isFailure()");
		}
		return failure;
	}

	// ===========================================================================================================

	/**
	 * 
	 * @param property
	 * @return
	 */
	public ProcessResult<T> atPath(Path path) {
		Objects.requireNonNull(path, "path cannot be null");
		if (failure == null) {
			return this;
		}
		return new ProcessResult<>(value, failure.atPath(path), failureMapper);
	}

	// ===========================================================================================================

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
		ProcessResult<?> other = (ProcessResult<?>) obj;
		return Objects.equals(failure, other.failure)
			&& Objects.equals(value, other.value)
			&& Objects.equals(failureMapper, other.failureMapper);
	}

	@Override
	public String toString() {
		return "ProcessResult" +
			(isSuccess()
				? "<Success>: " + value
				: "<Failure>: " + failure);
	}

}
