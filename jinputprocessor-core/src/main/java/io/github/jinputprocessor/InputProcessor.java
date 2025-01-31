package io.github.jinputprocessor;

import io.github.jinputprocessor.core.builder.Builder;
import io.github.jinputprocessor.core.processor.ChainedProcessor;
import io.github.jinputprocessor.core.result.BaseProcessorResult;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @param <IN>
 * @param <OUT>
 */
@FunctionalInterface
public interface InputProcessor<IN, OUT> {

	/**
	 * 
	 * @param input
	 * @return
	 */
	public ProcessResult<OUT> process(@Nullable IN value);

	// ===========================================================================================================

	/**
	 * 
	 * @param <NEW_OUT>
	 * @param after
	 * @return
	 */
	public default <NEW_OUT> InputProcessor<IN, NEW_OUT> andThen(InputProcessor<OUT, NEW_OUT> after) {
		return new ChainedProcessor<>(this, after);
	}

	// ===========================================================================================================

	/**
	 * 
	 * @return
	 */
	public static Builder builder() {
		return Builder.INSTANCE;
	}

	// ===========================================================================================================

	public static @Nonnull ProcessFailureMapper getDefaultFailureMapper() {
		return BaseProcessorResult.getDefaultFailureMapper();
	}

	/**
	 * 
	 * @param defaultFailureMapper
	 */
	public static void setDefaultFailureMapper(@Nonnull ProcessFailureMapper defaultFailureMapper) {
		BaseProcessorResult.setDefaultFailureMapper(defaultFailureMapper);
	}

}
