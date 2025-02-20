package io.github.jinputprocessor.processor;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessResult;
import jakarta.annotation.Nonnull;
import java.util.Objects;

public class NullStrategyProcessor<IN, OUT> implements InputProcessor<IN, OUT> {

	private final @Nonnull NullStrategy<IN> strategy;
	private final @Nonnull InputProcessor<IN, OUT> nextProcessor;

	public NullStrategyProcessor(@Nonnull NullStrategy<IN> strategy, @Nonnull InputProcessor<IN, OUT> nextProcessor) {
		this.strategy = Objects.requireNonNull(strategy, "null strategy cannot be null");
		this.nextProcessor = Objects.requireNonNull(nextProcessor, "next processor cannot be null");
	}

	@Override
	public <NEW_OUT> InputProcessor<IN, NEW_OUT> andThen(InputProcessor<OUT, NEW_OUT> after) {
		if (after instanceof NullStrategyProcessor<OUT, NEW_OUT> nullStrategyProcessor) {
			return new ChainedProcessor<>(this, nullStrategyProcessor);
		}
		return new NullStrategyProcessor<>(strategy, nextProcessor.andThen(after));
	}

	@Override
	public ProcessResult<OUT> process(IN value) {
		return switch (strategy) {
			case Process<IN> str -> nextProcessor.process(value);
			case SkipProcess<IN> str -> value == null ? ProcessResult.success(null) : nextProcessor.process(value);
			case Fail<IN>(var failure) -> value == null ? ProcessResult.failure(failure) : nextProcessor.process(value);
			case UseDefault<IN>(var defaultValue) -> value == null ? nextProcessor.process(defaultValue) : nextProcessor.process(value);
		};
	}

	@Override
	public String toString() {
		return "NullStrategyProcessor / " + strategy + "\n"
			+ nextProcessor.toString().indent(2);
	}

	@SuppressWarnings("unused")
	public static sealed interface NullStrategy<T> {

		static <T> NullStrategy<T> process() {
			return new Process<>();
		}

		static <T> NullStrategy<T> skipProcess() {
			return new SkipProcess<>();
		}

		static <T> NullStrategy<T> fail() {
			return new Fail<>();
		}

		static <T> NullStrategy<T> useDefault(@Nonnull T value) {
			return new UseDefault<>(value);
		}

	}

	private static record Process<T>() implements NullStrategy<T> {

	}

	private static record SkipProcess<T>() implements NullStrategy<T> {

	}

	private static record Fail<T>(ProcessFailure failure) implements NullStrategy<T> {

		public Fail(@Nonnull ProcessFailure failure) {
			this.failure = Objects.requireNonNull(failure, "failure cannot be null");
		}

		public Fail() {
			this(new ProcessFailure.ValidationFailure.ObjectIsNull());
		}

	}

	private static record UseDefault<T>(@Nonnull T value) implements NullStrategy<T> {

		public UseDefault(@Nonnull T value) {
			this.value = Objects.requireNonNull(value, "default value cannot be null");
		}

	}

}
