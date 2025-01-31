package io.github.jinputprocessor;

@FunctionalInterface
public interface ProcessFailureMapper {

	RuntimeException mapFailure(String inputName, ProcessFailure failure);

}
