package io.github.jinputprocessor.builder.base;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.InputProcessors;
import io.github.jinputprocessor.ProcessFailure.ValidationFailure;
import io.github.jinputprocessor.builder.InputProcessorBuilder;
import io.github.jinputprocessor.builder.NullStrategy;
import io.github.jinputprocessor.builder.base.types.ObjectInputProcessorBuilder;
import io.github.jinputprocessor.processor.NullStrategyProcessor;
import jakarta.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Function;

public abstract class AbstractInputProcessorBuilder<IN, OUT, SELF extends AbstractInputProcessorBuilder<IN, OUT, SELF>> implements InputProcessorBuilder<IN, OUT, SELF> {

	private final @Nonnull InputProcessor<IN, OUT> process;

	public AbstractInputProcessorBuilder(InputProcessor<IN, OUT> previous) {
		this.process = Objects.requireNonNull(previous);
	}

	@Override
	public SELF nullStrategy(NullStrategy nullStrategy) {
		return newInstance(new NullStrategyProcessor<>(nullStrategy, this.build()));
	}

	@Override
	public SELF sanitize(Function<OUT, OUT> sanitizationFunction) {
		return apply(InputProcessors.sanitizationProcessor(sanitizationFunction));
	}

	@Override
	public SELF validate(@Nonnull Function<OUT, ValidationFailure> validationFunction) {
		return apply(InputProcessors.validationProcessor(validationFunction));
	}

	@Override
	public SELF apply(InputProcessor<OUT, OUT> processor) {
		return newInstance(this.build().andThen(processor));
	}

	protected abstract SELF newInstance(InputProcessor<IN, OUT> process);

	@Override
	public <NEW_OUT> InputProcessorBuilder<IN, NEW_OUT, ?> map(Function<OUT, NEW_OUT> mappingFunction) {
		return map(mappingFunction, ObjectInputProcessorBuilder::new);
	}

	@Override
	public <NEW_OUT, B extends InputProcessorBuilder<IN, NEW_OUT, B>> B map(
		Function<OUT, NEW_OUT> mappingFunction, Function<InputProcessor<IN, NEW_OUT>, B> builderFunction
	) {
		return builderFunction.apply(InputProcessors.mappingProcessor(this.build(), mappingFunction));
	}

	@Override
	public InputProcessor<IN, OUT> build() {
		return process;
	}

}
