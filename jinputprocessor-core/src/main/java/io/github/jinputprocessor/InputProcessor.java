package io.github.jinputprocessor;

import io.github.jinputprocessor.builder.Builder;
import io.github.jinputprocessor.processor.ChainedProcessor;
import io.github.jinputprocessor.result.ProcessFailureMapper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * A processor that will process an input: sanitization, validation and mapping, any number of times, in any order.
 * 
 * <pre>
 * InputProcessor<String, String> processor = InputProcessor.builder().forClass(String.class)
 *   .validate(value -> value == null ? new ValidationFailure.ObjectIsNull() : null)
 *   .sanitize(String::strip)
 *   .build();
 * ProcessResult<String> result = processor.process(" some input  ");
 * String safeValue = result.getOrThrow(); // "some input"
 * </pre>
 * 
 * The same behavior can be obtained using handy pre-defined builders, discoverable using your IDE auto-completion feature.
 * The above example can then be simplified into:
 * <pre>
 * InputProcessor<String, String> processor = InputProcessor.builder().forString()  // "forString()" instead of "forClass(String.class)"
 *   .validateThat().isNotNull().then()
 *   .sanitize().strip().then()
 *   .build();
 * ProcessResult<String> result = processor.process(" some input  ");
 * String safeValue = result.getOrThrow(); // "some input"
 * </pre>
 * 
 * 
 * @param <IN>	The initial input value type
 * @param <OUT>	The output value type, may be different than the input type if some mapping were applied
 */
@FunctionalInterface
public interface InputProcessor<IN, OUT> {

	/**
	 * 
	 * @param input
	 * @return
	 */
	@Nonnull
	ProcessResult<OUT> process(@Nullable IN value);

	// ===========================================================================================================

	/**
	 * 
	 * @param property
	 * @param value
	 * @return
	 */
	@Nonnull
	default ProcessResult<OUT> process(@Nonnull String property, @Nullable IN value) {
		return process(Path.root().atProperty(property), value);
	}

	/**
	 * 
	 * @param path
	 * @param value
	 * @return
	 */
	@Nonnull
	default ProcessResult<OUT> process(@Nonnull Path path, @Nullable IN value) {
		return process(value).atPath(path);
	}

	// ===========================================================================================================

	/**
	 * 
	 * @param <NEW_OUT>
	 * @param after
	 * @return
	 */
	@Nonnull
	default <NEW_OUT> InputProcessor<IN, NEW_OUT> andThen(InputProcessor<OUT, NEW_OUT> after) {
		return new ChainedProcessor<>(this, after);
	}

	// ===========================================================================================================

	/**
	 * 
	 * @return
	 */
	@Nonnull
	static Builder builder() {
		return Builder.INSTANCE;
	}

	// ===========================================================================================================

	@Nonnull
	static ProcessFailureMapper getDefaultFailureMapper() {
		return ProcessResult.getDefaultFailureMapper();
	}

	/**
	 * 
	 * @param defaultFailureMapper
	 */
	static void setDefaultFailureMapper(@Nonnull ProcessFailureMapper defaultFailureMapper) {
		ProcessResult.setDefaultFailureMapper(defaultFailureMapper);
	}

}
