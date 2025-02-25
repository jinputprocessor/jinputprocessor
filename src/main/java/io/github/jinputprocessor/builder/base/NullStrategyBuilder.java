package io.github.jinputprocessor.builder.base;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.InputProcessors;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessResult;
import io.github.jinputprocessor.builder.InputProcessorBuilder;
import io.github.jinputprocessor.processor.NullStrategyProcessor.NullStrategy;

public class NullStrategyBuilder<IN, OUT, B extends InputProcessorBuilder<IN, OUT, B>>
	extends AbstractIntermediateBuilder<IN, OUT, B, NullStrategyBuilder<IN, OUT, B>> {

	public NullStrategyBuilder(B builder) {
		super(builder);
	}

	/**
	 * Continue process of <code>null</code> value. This may raise NullPointerException inside process.
	 * This is the default behavior when no null strategy is defined.
	 * 
	 * @return the calling builder
	 */
	public final B process() {
		builder = builder.apply(InputProcessors.nullStrategyProcessor(NullStrategy.process(), InputProcessors.noOpProcessor()));
		return builder;
	}

	/**
	 * Skip the process of <code>null</code> value, meaning a <code>null</code> input will provide a <code>null</code> output.
	 * 
	 * @return the calling builder
	 */
	public final B skipProcess() {
		builder = builder.apply(InputProcessors.nullStrategyProcessor(NullStrategy.skipProcess(), InputProcessors.noOpProcessor()));
		return builder;
	}

	/**
	 * Fail the process of <code>null</code> value, meaning the {@link ProcessResult} will be a failure with cause {@link ProcessFailure.ValidationFailure.ObjectIsNull}.
	 * 
	 * @return the calling builder
	 */
	public final B fail() {
		builder = builder.apply(InputProcessors.nullStrategyProcessor(NullStrategy.fail(), InputProcessors.noOpProcessor()));
		return builder;
	}

	/**
	 * Use the provided default value if the input value is <code>null</code>.
	 * 
	 * @param defaultValue	The default value to use in case of <code>null</code> input, cannot be <code>null</code>.
	 * 
	 * @return the calling builder
	 */
	@SuppressWarnings("unchecked")
	public final B useDefault(IN defaultValue) {
		builder = builder.apply((InputProcessor<OUT, OUT>) InputProcessors.nullStrategyProcessor(NullStrategy.useDefault(defaultValue), InputProcessors.noOpProcessor()));
		return builder;
	}

}
