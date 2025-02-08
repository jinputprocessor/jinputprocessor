package io.github.jinputprocessor;

public sealed interface Path {

	String format();

	static Path atRoot(String attr) {
		return new AttributePath(null, attr);
	}

	static Path atRootIndex(int index) {
		return new IndexPath(null, index);
	}

	default Path at(String attr) {
		return new AttributePath(this, attr);
	}

	default Path atIndex(int index) {
		return new IndexPath(this, index);
	}

	record AttributePath(Path parent, String attr) implements Path {

		@Override
		public String format() {
			return parent == null
				? attr
				: parent.format() + "." + attr;
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
