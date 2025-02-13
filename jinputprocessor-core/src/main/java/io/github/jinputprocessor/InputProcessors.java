package io.github.jinputprocessor;

import io.github.jinputprocessor.ProcessFailure.ValidationFailure;
import io.github.jinputprocessor.processor.MappingProcessor;
import io.github.jinputprocessor.processor.SanitizationProcessor;
import io.github.jinputprocessor.processor.ValidationProcessor;
import jakarta.annotation.Nonnull;
import java.util.function.Function;

/**
 * Utility class to create various processors.
 */
public class InputProcessors {

	private InputProcessors() {
		// Utility class
	}

	/**
	 * Creates a processor that will always return the same input, as a success.
	 * 
	 * @param <T>	The type of the input
	 * 
	 * @return	A processor ready to be used or combined with other processor(s)
	 */
	public static <T> InputProcessor<T, T> noOpProcessor() {
		return value -> ProcessResult.success(value);
	}

	/**
	 * Creates a processor that will sanitize/transform the input using the given function, and return the result as a success.
	 * 
	 * @param <T>					The type of the input
	 * 
	 * @param sanitizationFunction	The function to apply to the input
	 * 
	 * @return						A processor ready to be used or combined with other processor(s)
	 * 
	 * @see SanitizationProcessor
	 */
	public static <T> InputProcessor<T, T> sanitizationProcessor(@Nonnull Function<T, T> sanitizationFunction) {
		return new SanitizationProcessor<>(sanitizationFunction);
	}

	/**
	 * Creates a processor that will validate the input against the given function.
	 * The validation function must return <code>null</code> if the input is valid,
	 * or an appropriate {@link ValidationFailure} if not.
	 * 
	 * @param <T>					The type of the input
	 * 
	 * @param validationFunction	The validation function, returns <code>null</code> if the input is valid or an appropriate {@link ValidationFailure} if not.
	 * 
	 * @return						A processor ready to be used or combined with other processor(s)
	 * 
	 * @see ValidationProcessor
	 */
	public static <T> InputProcessor<T, T> validationProcessor(@Nonnull Function<T, ValidationFailure> validationFunction) {
		return new ValidationProcessor<>(validationFunction);
	}

	/**
	 * Creates a processor that will map the input into another type, using the given mapping function.
	 * <pre>
	 * InputProcessor<String, String> stringOnlyProc = InputProcessors.noOpProcessor();
	 * InputProcessor<String, Integer> stringToIntProc = InputProcessors.mappingProcessor(stringOnlyProc, Integer::parseInt);
	 * Integer i = stringToIntProc.process("123").get(); // i = 123
	 * </pre>
	 * 
	 * @param <IN>					The type of the input
	 * @param <OUT>					The type of the initial outpout (=the type to map from)
	 * @param <NEW_OUT>				The new type of the outpout, after mapping (=the type to map to)
	 * 
	 * @param initialProcessor		The initial processor, from which the output will be mapped
	 * @param mappingFunction		The mapping function
	 * 
	 * @return						A processor ready to be used or combined with other processor(s)
	 * 
	 * @see MappingProcessor
	 */
	public static <IN, OUT, NEW_OUT> InputProcessor<IN, NEW_OUT> mappingProcessor(
		@Nonnull InputProcessor<IN, OUT> initialProcessor, @Nonnull Function<OUT, NEW_OUT> mappingFunction
	) {
		var mappingProcess = new MappingProcessor<>(mappingFunction);
		return initialProcessor.andThen(mappingProcess);
	}

}
