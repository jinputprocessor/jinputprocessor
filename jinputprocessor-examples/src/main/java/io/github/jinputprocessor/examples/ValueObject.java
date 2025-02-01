package io.github.jinputprocessor.examples;

import io.github.jinputprocessor.InputProcessor;

public class ValueObject {

	public static void main(String[] args) {
		/*
		 * In this example, we try to build various "LastName" value objects,
		 * using the LastName.PROCESSOR instanciated below.
		 */
		processAndPrint(null); // "N/A"
		processAndPrint(" Smith  "); // "SMITH"
		processAndPrint("DOE"); // "DOE"
		processAndPrint(""); // IllegalArgumentException: must not be empty
		processAndPrint("ThisIsAWayTooLongLastName"); // IllegalArgumentException: must be 20 chars max, but is 25
	}

	private static void processAndPrint(String value) {
		try {
			var lastName = LastName.of(value);
			printFormatted(value, lastName.toString());
		} catch (IllegalArgumentException e) {
			printFormatted(value, e.getMessage());
		}
	}

	private static void printFormatted(String before, String after) {
		System.out.println((before == null ? "null" : "'" + before + "'") + " --> " + "'" + after + "'");
	}

	/**
	 * A simple value object representing lastname of a user.
	 */
	private static class LastName {

		private static final int MAX_LENGTH = 20; // Keep low for the sake of this example. Real production code would of course allow greater value!

		/*
		 * A processor that will first apply some sanitization:
		 *   - If the input string is null, it defaults to "N/A"
		 *   - Removes leading and trailing whitespace
		 *   - Converts the string to uppercase
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
			return PROCESSOR.process(value).withName("lastName").getOrThrow();
		}

		@Override
		public String toString() {
			return value;
		}

	}

}
