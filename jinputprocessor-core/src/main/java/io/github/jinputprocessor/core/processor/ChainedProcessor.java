package io.github.jinputprocessor.core.processor;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessResult;
import io.github.jinputprocessor.core.result.BaseProcessorResult;
import jakarta.annotation.Nonnull;
import java.util.Objects;

public class ChainedProcessor<IN, OUT, NEW_OUT> implements InputProcessor<IN, NEW_OUT> {

	private final @Nonnull InputProcessor<IN, OUT> firstProcessor;
	private final @Nonnull InputProcessor<OUT, NEW_OUT> secondProcessor;

	public ChainedProcessor(@Nonnull InputProcessor<IN, OUT> firstProcessor, @Nonnull InputProcessor<OUT, NEW_OUT> secondProcessor) {
		this.firstProcessor = Objects.requireNonNull(firstProcessor, "first processor cannot be null");
		this.secondProcessor = Objects.requireNonNull(secondProcessor, "second processor cannot be null");
	}

	@Override
	public ProcessResult<NEW_OUT> process(IN value) {
		var resultOut = firstProcessor.process(value);
		if (resultOut.isFailure()) {
			return BaseProcessorResult.failure(resultOut.getFailure());
		}
		var outValue = resultOut.getValue();
		var resultNewOut = secondProcessor.process(outValue);
		return resultNewOut;
	}

	public InputProcessor<IN, OUT> getFirstProcessor() {
		return firstProcessor;
	}

	public InputProcessor<OUT, NEW_OUT> getSecondProcessor() {
		return secondProcessor;
	}

}
