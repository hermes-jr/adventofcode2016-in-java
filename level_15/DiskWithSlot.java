package level_15;

class DiskWithSlot {
	private int totalPositions;
	private int currentPosition;

	DiskWithSlot(int positions, int startPosition) {
		this.currentPosition = startPosition;
		this.totalPositions = positions;
	}

	int getTotalPositions() {
		return totalPositions;
	}

	void setTotalPositions(int totalPositions) {
		this.totalPositions = totalPositions;
	}

	int getCurrentPosition() {
		return currentPosition;
	}

	void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	void move() {
		if (currentPosition + 1 == totalPositions)
			currentPosition = 0;
		else
			currentPosition++;
	}

	@Override
	public String toString() {
		return "DiskWithSlot {position=" + currentPosition +
				"/" + totalPositions +
				'}';
	}
}