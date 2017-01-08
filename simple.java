private void deductColumn(int colIndex, int[][] matrix, boolean[] checkNeedRows) {
		final int numRows = this.rowSequences.length;
		final int numCols = this.colSequences.length;
		int maxNumColSeqs = this.colSequences[colIndex].length;

		int[] colItemValues = new int[numRows];
		for (int r = 0; r < numRows; r++) {
			colItemValues[r] = PUZZLE_INITIAL_VALUE;
		}
		this.colItemTrueCounts = new int[numRows]; // init: zeros
		this.configurationCount = 0;

		int[] colSeqEndIndices = new int[maxNumColSeqs];
		for (int j = 0; j < maxNumColSeqs; j++) {
			colSeqEndIndices[j] = ILLEGAL_END_INDEX;
		}

		this.deductColRecursive(colIndex, 0, colItemValues, matrix, colSeqEndIndices);

		if (configurationCount == 0) throw new RuntimeException("INTERNAL ERROR: configurations == 0");

		for (int r = 0; r < numRows; r++) {
			if (this.colItemTrueCounts[r] == 0) {
				// it's impossible to legally place a 'true' --> 'false' is certain
				if (matrix[r][colIndex] != PUZZLE_FALSE) {
					checkNeedRows[r] = true;
					matrix[r][colIndex] = PUZZLE_FALSE;
					this.deductedItems++;
				}
			}
			else if (this.colItemTrueCounts[r] == this.configurationCount) {
				// it's impossible to legally place a 'false' --> 'true' is certain
				if (matrix[r][colIndex] != PUZZLE_TRUE) {
					checkNeedRows[r] = true;
					matrix[r][colIndex] = PUZZLE_TRUE;
					this.deductedItems++;
				}
			}
		}
	}
