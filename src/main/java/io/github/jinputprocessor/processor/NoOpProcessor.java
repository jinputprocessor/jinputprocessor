package io.github.jinputprocessor.processor;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.ProcessResult;

/**
 * Default processor that does nothing with the value.
 * 
 * @param <T>
 */
public class NoOpProcessor<T> implements InputProcessor<T, T> {

	@Override
	public ProcessResult<T> process(T value) {
		return ProcessResult.success(value);
	}

	@Override
	public <NEW_OUT> InputProcessor<T, NEW_OUT> andThen(InputProcessor<T, NEW_OUT> after) {
		return after;
	}

	@Override
	public String toString() {
		return "NoOpProcessor";
	}

}
