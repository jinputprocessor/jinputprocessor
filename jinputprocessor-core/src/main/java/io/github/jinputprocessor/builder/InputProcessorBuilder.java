package io.github.jinputprocessor.builder;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessFailure.ValidationFailure;
import io.github.jinputprocessor.builder.base.NullStrategyBuilder;
import jakarta.annotation.Nonnull;
import java.util.function.Function;

/**
 * @param <IN>
 * @param <OUT>
 */
public interface InputProcessorBuilder<IN, OUT, SELF extends InputProcessorBuilder<IN, OUT, SELF>> {

	/**
	 * Build the final {@link InputProcessor}.
	 * 
	 * @return
	 */
	@Nonnull
	InputProcessor<IN, OUT> build();

	/**
	 * 
	 * @param nullStrategy
	 * @return
	 */
	NullStrategyBuilder<IN, OUT, SELF> nullStrategy();

	/**
	 * Sanitize the value: any transformation function can be applied.
	 * 
	 * @param sanitizationFunction	Any transformation function, to be applied to the value
	 * 
	 * @return	a new builder
	 */
	@Nonnull
	SELF sanitize(@Nonnull Function<OUT, OUT> sanitizationFunction);

	/**
	 * Validate the value using the given validation function.
	 * 
	 * @param validationFunction	A function that returns <code>null</code> if the value is valid, a {@link ProcessFailure.ValidationFailure} otherwise.
	 * 
	 * @return	a new builder
	 * 
	 * @see ValidationFailure
	 */
	@Nonnull
	SELF validate(@Nonnull Function<OUT, ValidationFailure> validationFunction);

	/**
	 * Apply the given processor, i.e. include it into the current one.
	 * 
	 * @param processor	The processor to apply to the value.
	 * 
	 * @return	a new builder
	 */
	@Nonnull
	SELF apply(@Nonnull InputProcessor<OUT, OUT> processor);

	/**
	 * Map the output value of this processor into another type, and return a new {@link InputProcessorBuilder} for this new type.
	 * 
	 * @param <NEW_OUT>	The new output value type
	 * 
	 * @param mappingFunction	The mapping function to apply to this processor's output value
	 * 
	 * @return	a new builder
	 */
	@Nonnull
	<NEW_OUT> InputProcessorBuilder<IN, NEW_OUT, ?> map(@Nonnull Function<OUT, NEW_OUT> mappingFunction);

	/**
	 * Map the output value of this processor into another type, and return a new {@link InputProcessorBuilder} for this new type,
	 * using the provided function.
	 * 
	 * @param <NEW_OUT>	The new output value type
	 * @param <B>		The new processor builder type
	 * 
	 * @param mappingFunction	The mapping function to apply to this processor's output value
	 * @param builderFunction	The function to create a new processor builder from the created processor
	 * @return
	 */
	@Nonnull
	<NEW_OUT, B extends InputProcessorBuilder<IN, NEW_OUT, B>> B map(
		@Nonnull Function<OUT, NEW_OUT> mappingFunction, @Nonnull Function<InputProcessor<IN, NEW_OUT>, B> builderFunction
	);

}
