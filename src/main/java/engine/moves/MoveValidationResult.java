package engine.moves;

public class MoveValidationResult {

	private final boolean valid;
	private final String error;

	public MoveValidationResult(boolean valid, String error) {
		this.valid = valid;
		this.error = error;
	}

	public boolean isValid() {
		return valid;
	}

	public String getError() {
		return error;
	}
}
