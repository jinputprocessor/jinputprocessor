package io.github.jinputprocessor.builder.base;

import io.github.jinputprocessor.InputProcessors;
import io.github.jinputprocessor.builder.InputProcessorBuilder;
import io.github.jinputprocessor.processor.NullStrategyProcessor.NullStrategy;

public class NullStrategyBuilder<IN, OUT, B extends InputProcessorBuilder<IN, OUT, B>>
	extends AbstractIntermediateBuilder<IN, OUT, B, NullStrategyBuilder<IN, OUT, B>> {

	public NullStrategyBuilder(B builder) {
		super(builder);
	}

	public B then() {
		return builder;
	}

	public final B processNull() {
		builder = builder.apply(InputProcessors.nullStrategyProcessor(NullStrategy.PROCESS, InputProcessors.noOpProcessor()));
		return builder;
	}

	public final B ignoreNull() {
		builder = builder.apply(InputProcessors.nullStrategyProcessor(NullStrategy.IGNORE, InputProcessors.noOpProcessor()));
		return builder;
	}

}
