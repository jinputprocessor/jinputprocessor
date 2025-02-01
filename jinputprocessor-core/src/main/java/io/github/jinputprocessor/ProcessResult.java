package io.github.jinputprocessor;

import io.github.jinputprocessor.result.BaseProcessorResult;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

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
public interface ProcessResult<T> {

	public boolean isSuccess();

	public boolean isFailure();

	/**
	 * Get the value if process has succeed.
	 * 
	 * @return	The processed value
	 * 
	 * @throws IllegalStateException if process result is failure
	 */
	public @Nonnull T get();

	/**
	 * Get the value if process has succeed, or throws an exception if failure.
	 * 
	 * @return
	 */
	public @Nonnull T getOrThrow();

	/**
	 * Get the failure if process has failed.
	 * 
	 * @return	The failure
	 * 
	 * @throws IllegalStateException if process result is success
	 */
	public ProcessFailure getFailure();

	// ===========================================================================================================

	/**
	 * 
	 * @param name
	 * @return
	 */
	public ProcessResult<T> withName(String name);

	// ===========================================================================================================

	/**
	 * Create a successful process result.
	 * 
	 * @param <OUT> Expected type of the value
	 * @param value	The value
	 * 
	 * @return	A successful process result
	 */
	public static <OUT> ProcessResult<OUT> success(@Nullable OUT value) {
		return BaseProcessorResult.success(value);
	}

	/**
	 * Create a failed process result, using the given failure reason.
	 * 
	 * @param <OUT> Expected type of the value (not used in this method)
	 * @param value	The failure, cannot be <code>null</code>
	 * 
	 * @return	A failed process result
	 */
	public static <OUT> ProcessResult<OUT> failure(@Nonnull ProcessFailure failuret) {
		return BaseProcessorResult.failure(failuret);
	}

}
