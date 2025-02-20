package io.github.jinputprocessor.result;

import io.github.jinputprocessor.ProcessFailure;

@FunctionalInterface
public interface ProcessFailureMapper {

	RuntimeException mapFailure(ProcessFailure failure);

}
