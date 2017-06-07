package application;

public enum Environment {
	Develop("Deweloperskie"), Production("Produkcyjne"), Test("Testowe"), Null("Nie wybrano");
	private String text;

	Environment(String msg) {
		text = msg;
	}

	@Override
	public String toString() {
		return text;
	}

	public static Environment getResult(String text) {
		for (Environment result : Environment.values())
			if (result.toString() == text)
				return result;
		return null;
	}

}
