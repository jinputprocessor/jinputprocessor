package io.github.jinputprocessor;

import io.github.jinputprocessor.ProcessFailure.ValidationError;
import io.github.jinputprocessor.core.processor.MappingProcessor;
import io.github.jinputprocessor.core.processor.SanitizationProcessor;
import io.github.jinputprocessor.core.processor.ValidationProcessor;
import jakarta.annotation.Nonnull;
import java.util.function.Function;
import java.util.function.Predicate;

public class InputProcessors {

	/**
	 * 
	 * @param <T>
	 * @return
	 */
	public static <T> InputProcessor<T, T> noOpProcessor() {
		return value -> ProcessResult.success(value);
	}

	/**
	 * 
	 * @param <T>
	 * @param sanitizationFunction
	 * @return
	 */
	public static <T> InputProcessor<T, T> sanitizationProcessor(Function<T, T> sanitizationFunction) {
		return new SanitizationProcessor<>(sanitizationFunction);
	}

	/**
	 * 
	 * @param <T>
	 * @param validationFunction
	 * @return
	 */
	public static <T> InputProcessor<T, T> validationProcessor(@Nonnull Function<T, ValidationError> validationFunction) {
		return new ValidationProcessor<>(validationFunction);
	}

	/**
	 * 
	 * @param <IN>
	 * @param <OUT>
	 * @param <NEW_OUT>
	 * @param initialProcessor
	 * @param mappingFunction
	 * @return
	 */
	public static <IN, OUT, NEW_OUT> InputProcessor<IN, NEW_OUT> mappingProcessor(InputProcessor<IN, OUT> initialProcessor, Function<OUT, NEW_OUT> mappingFunction) {
		var mappingProcess = new MappingProcessor<>(mappingFunction);
		return initialProcessor.andThen(mappingProcess);
	}

	public static <T> InputProcessor<T, T> ifProcessor(@Nonnull Predicate<T> predicate, @Nonnull InputProcessor<T, T> processIfTrue) {
		return ifProcessorOrElse(predicate, processIfTrue, noOpProcessor());
	}

	public static <T> InputProcessor<T, T> ifProcessorOrElse(
		@Nonnull Predicate<T> predicate, @Nonnull InputProcessor<T, T> processIfTrue, @Nonnull InputProcessor<T, T> processIfFalse
	) {
		return value -> predicate.test(value)
			? processIfTrue.process(value)
			: processIfFalse.process(value);
	}

}
