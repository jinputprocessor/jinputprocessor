package io.github.jinputprocessor;

import io.github.jinputprocessor.processor.ChainedProcessor;
import io.github.jinputprocessor.processor.ChainedProcessorAssert;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class InputProcessorAssert<IN, OUT> extends AbstractAssert<InputProcessorAssert<IN, OUT>, InputProcessor<IN, OUT>> {

	private InputProcessorAssert(InputProcessor<IN, OUT> actual) {
		super(actual, InputProcessorAssert.class);
	}

	public static <IN, OUT> InputProcessorAssert<IN, OUT> assertThat(InputProcessor<IN, OUT> actual) {
		return new InputProcessorAssert<>(actual);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public <X> ChainedProcessorAssert<IN, OUT> isChainedProcessor() {
		Assertions.assertThat(actual).isInstanceOf(ChainedProcessor.class);
		return ChainedProcessorAssert.assertThat((ChainedProcessor<IN, X, OUT>) actual);
	}

}
