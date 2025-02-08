package io.github.jinputprocessor;

public sealed interface Path {

	String format();

	static Path createPropertyPath(String property) {
		return new PropertyPath(null, property);
	}

	static Path createIndexPath(int index) {
		return new IndexPath(null, index);
	}

	default Path atProperty(String property) {
		return new PropertyPath(this, property);
	}

	default Path atIndex(int index) {
		return new IndexPath(this, index);
	}

	record PropertyPath(Path parent, String property) implements Path {

		@Override
		public String format() {
			return parent == null
				? property
				: parent.format() + "." + property;
		}

	}

	record IndexPath(Path parent, int index) implements Path {

		@Override
		public String format() {
			return parent == null
				? "index " + index
				: parent.format() + "[" + index + "]";
		}

	}

}
