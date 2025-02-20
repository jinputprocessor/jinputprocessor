package io.github.jinputprocessor.processor;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessFailure;
import io.github.jinputprocessor.ProcessResult;
import jakarta.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Function;

public class SanitizationProcessor<IN, OUT> implements InputProcessor<IN, OUT> {

	private final @Nonnull Function<IN, OUT> sanitizationFunction;

	public SanitizationProcessor(@Nonnull Function<IN, OUT> sanitizationFunction) {
		this.sanitizationFunction = Objects.requireNonNull(sanitizationFunction, "sanitizationFunction cannot be null");
	}

	@Override
	public ProcessResult<OUT> process(IN value) {
		try {
			OUT newValue = sanitizationFunction.apply(value);
			return ProcessResult.success(newValue);
		} catch (Exception t) {
			return ProcessResult.failure(new ProcessFailure.UnexpectedException(value, t));
		}
	}

	@Override
	public String toString() {
		return "SanitizationProcessor -> " + sanitizationFunction.toString();
	}

}
