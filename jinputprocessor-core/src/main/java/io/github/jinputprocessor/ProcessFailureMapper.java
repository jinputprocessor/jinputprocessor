package io.github.jinputprocessor;

@FunctionalInterface
public interface ProcessFailureMapper {

	RuntimeException mapFailure(ProcessFailure failure);

}
