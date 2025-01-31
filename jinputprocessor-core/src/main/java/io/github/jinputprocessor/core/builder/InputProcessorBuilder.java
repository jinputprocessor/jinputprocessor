package io.github.jinputprocessor.core.builder;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure.ValidationError;
import io.github.jinputprocessor.core.result.BaseProcessorResult;
import jakarta.annotation.Nonnull;
import java.util.function.Function;

/**
 * @param <IN>
 * @param <OUT>
 */
public interface InputProcessorBuilder<IN, OUT, SELF extends InputProcessorBuilder<IN, OUT, SELF>> {

	/**
	 * 
	 * @return
	 */
	public InputProcessor<IN, OUT> build();

	/**
	 * 
	 * @param sanitizationFunction
	 * @return
	 */
	public SELF sanitize(Function<OUT, OUT> sanitizationFunction);

	/**
	 * 
	 * @param validationFunction
	 * @return
	 */
	public SELF validate(@Nonnull Function<OUT, ValidationError> validationFunction);

	/**
	 * 
	 * @param process
	 * @return
	 */
	public SELF applyProcessor(InputProcessor<OUT, OUT> processor);

	/**
	 * 
	 * @param <NEW_OUT>
	 * @param mappingFunction
	 * @return
	 */
	public <NEW_OUT> InputProcessorBuilder<IN, NEW_OUT, ?> mapTo(Function<OUT, NEW_OUT> mappingFunction);

	/**
	 * 
	 * @param <NEW_OUT>
	 * @param <B>
	 * @param mappingFunction
	 * @param builderFunction
	 * @return
	 */
	public <NEW_OUT, B extends InputProcessorBuilder<IN, NEW_OUT, B>> B mapTo(
		Function<OUT, NEW_OUT> mappingFunction, Function<InputProcessor<IN, NEW_OUT>, B> builderFunction
	);

	// ===========================================================================================================

	/**
	 * 
	 * @param <T>
	 * @return
	 */
	public static <T> InputProcessor<T, T> noOpProcessor() {
		return value -> BaseProcessorResult.success(value);
	}

}
