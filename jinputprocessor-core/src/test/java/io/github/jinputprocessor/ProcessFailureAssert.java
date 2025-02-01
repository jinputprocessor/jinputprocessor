package io.github.jinputprocessor;

import org.assertj.core.api.AbstractAssert;

public class ProcessFailureAssert extends AbstractAssert<ProcessFailureAssert, ProcessFailure> {

	private ProcessFailureAssert(ProcessFailure actual) {
		super(actual, ProcessFailureAssert.class);
	}

	public static ProcessFailureAssert assertThat(ProcessFailure actual) {
		return new ProcessFailureAssert(actual);
	}

}
