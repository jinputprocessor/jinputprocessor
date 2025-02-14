package io.github.jinputprocessor.processor;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessResult;
import io.github.jinputprocessor.builder.NullStrategy;
import jakarta.annotation.Nonnull;
import java.util.Objects;

public class NullStrategyProcessor<IN, OUT> implements InputProcessor<IN, OUT> {

	private final @Nonnull NullStrategy strategy;
	private final @Nonnull InputProcessor<IN, OUT> nextProcessor;

	public NullStrategyProcessor(@Nonnull NullStrategy strategy, @Nonnull InputProcessor<IN, OUT> nextProcessor) {
		this.strategy = Objects.requireNonNull(strategy, "null strategy cannot be null");
		this.nextProcessor = Objects.requireNonNull(nextProcessor, "next processor cannot be null");
	}

	@Override
	@SuppressWarnings("unchecked")
	public <NEW_OUT> InputProcessor<IN, NEW_OUT> andThen(InputProcessor<OUT, NEW_OUT> after) {
		if (after instanceof NullStrategyProcessor nullStrategyProcessor) {
			return new ChainedProcessor<>(this, nullStrategyProcessor);
		}
		return new NullStrategyProcessor<>(strategy, nextProcessor.andThen(after));
	}

	@Override
	public ProcessResult<OUT> process(IN value) {
		return switch (strategy) {
			case IGNORE -> value == null ? ProcessResult.success(null) : nextProcessor.process(value);
			case PROCESS -> nextProcessor.process(value);
		};
	}

	@Override
	public String toString() {
		return "NullStrategyProcessor / " + strategy + "\n"
			+ nextProcessor.toString().indent(2);
	}

}
