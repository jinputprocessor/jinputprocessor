package io.github.jinputprocessor.core.processor;

import io.github.jinputprocessor.InputProcessor;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ChainedProcessorAssert<IN, NEW_OUT> extends AbstractAssert<ChainedProcessorAssert<IN, NEW_OUT>, ChainedProcessor<IN, ?, NEW_OUT>> {

	private ChainedProcessorAssert(ChainedProcessor<IN, ?, NEW_OUT> actual) {
		super(actual, ChainedProcessorAssert.class);
	}

	public static <IN, NEW_OUT> ChainedProcessorAssert<IN, NEW_OUT> assertThat(ChainedProcessor<IN, ?, NEW_OUT> actual) {
		return new ChainedProcessorAssert<>(actual);
	}

	public ChainedProcessorAssert<IN, NEW_OUT> hastFirstProcessor(InputProcessor<IN, ?> firstProcessor) {
		Assertions.assertThat(actual.getFirstProcessor())
			.as(() -> "ChainedProcessor first processor is not the same as expected")
			.isSameAs(firstProcessor);
		return this;
	}

	public ChainedProcessorAssert<IN, NEW_OUT> hastSecondProcessor(InputProcessor<?, NEW_OUT> secondProcessor) {
		Assertions.assertThat(actual.getSecondProcessor())
			.as(() -> "ChainedProcessor second processor is not the same as expected")
			.isSameAs(secondProcessor);
		return this;
	}

}
