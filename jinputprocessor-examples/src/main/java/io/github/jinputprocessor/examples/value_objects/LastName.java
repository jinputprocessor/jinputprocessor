package io.github.jinputprocessor.examples.value_objects;

import io.github.jinputprocessor.InputProcessor;

public class LastName {

	private static final int MAX_LENGTH = 200;

	/*
	 * A simple processor that will first apply some sanitization:
	 *   - If the input string is null, it defaults to "N/A"
	 *   - Removes leading and trailing whitespace
	 *   - Converts the string to uppercase
	 * Some examples of such sanitization:
	 *   null      -> "N/A"
	 *   " Smith " -> "SMITH"
	 *   
	 * Then it perform a few validations:
	 *   - Ensures the string is not empty
	 *   - Ensures the string does not exceed a specified maximum length (MAX_LENGTH)
	 * 
	 * Finally, it maps the input string to the LastName value object, using its constructor.
	 */

	// @formatter:off
	private static final InputProcessor<String, LastName> PROCESSOR = InputProcessor.builder()
		.forString()
		.sanitize()
			.defaultIfNull("N/A")
			.strip()
			.toUpperCase()
			.then()
		.validateThat()
			.isNotEmpty()
			.isMaxLength(MAX_LENGTH)
			.then()
		.mapTo(LastName::new)
		.build();
	// @formatter:on

	private final String value;

	private LastName(String value) {
		this.value = value;
	}

	public static LastName of(String value) {
		return PROCESSOR.process(value).getOrThrow("lastName");
	}

	@Override
	public String toString() {
		return value;
	}

}
