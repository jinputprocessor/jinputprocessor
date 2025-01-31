package io.github.jinputprocessor.core.processor;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessResult;
import io.github.jinputprocessor.core.result.BaseProcessorResult;
import jakarta.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Function;

public class MappingProcessor<OUT, NEW_OUT> implements InputProcessor<OUT, NEW_OUT> {

	private final @Nonnull Function<OUT, NEW_OUT> mappingFunction;

	public MappingProcessor(Function<OUT, NEW_OUT> mappingFunction) {
		this.mappingFunction = Objects.requireNonNull(mappingFunction, "Mapping function cannot be null");
	}

	@Override
	public ProcessResult<NEW_OUT> process(OUT value) {
		try {
			NEW_OUT outValue = mappingFunction.apply(value);
			return BaseProcessorResult.success(outValue);
		} catch (Throwable t) {
			return ProcessResult.failure(new ProcessFailure.UnexpectedException(value, t));
		}
	}

}
