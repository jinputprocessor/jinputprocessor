package io.github.jinputprocessor.core.builder.base;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.InputProcessors;
import io.github.jinputprocessor.ProcessFailure.ValidationError;
import io.github.jinputprocessor.core.builder.InputProcessorBuilder;
import io.github.jinputprocessor.core.builder.base.types.ObjectInputProcessorBuilder;
import jakarta.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Function;

public abstract class AbstractInputProcessorBuilder<IN, OUT, SELF extends AbstractInputProcessorBuilder<IN, OUT, SELF>> implements InputProcessorBuilder<IN, OUT, SELF> {

	protected @Nonnull InputProcessor<IN, OUT> process;

	public AbstractInputProcessorBuilder(InputProcessor<IN, OUT> previous) {
		this.process = Objects.requireNonNull(previous);
	}

	@Override
	public SELF sanitize(Function<OUT, OUT> sanitizationFunction) {
		return applyProcessor(InputProcessors.sanitizationProcessor(sanitizationFunction));
	}

	@Override
	public SELF validate(@Nonnull Function<OUT, ValidationError> validationFunction) {
		return applyProcessor(InputProcessors.validationProcessor(validationFunction));
	}

	@Override
	public SELF applyProcessor(InputProcessor<OUT, OUT> processor) {
		this.process = this.process.andThen(processor);
		return cast();
	}

	protected abstract SELF cast();

	@Override
	public <NEW_OUT> InputProcessorBuilder<IN, NEW_OUT, ?> mapTo(Function<OUT, NEW_OUT> mappingFunction) {
		return mapTo(mappingFunction, ObjectInputProcessorBuilder::new);
	}

	@Override
	public <NEW_OUT, B extends InputProcessorBuilder<IN, NEW_OUT, B>> B mapTo(
		Function<OUT, NEW_OUT> mappingFunction, Function<InputProcessor<IN, NEW_OUT>, B> builderFunction
	) {
		return builderFunction.apply(InputProcessors.mappingProcessor(this.process, mappingFunction));
	}

	@Override
	public InputProcessor<IN, OUT> build() {
		return process;
	}

}
