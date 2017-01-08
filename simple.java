private void deductRow(int rowIndex, int[][] matrix, boolean[] checkNeedCols) {
		final int numRows = this.rowSequences.length;
		final int numCols = this.colSequences.length;
		int maxNumRowSeqs = this.rowSequences[rowIndex].length;

		int[] rowItemValues = new int[numCols];
		for (int c = 0; c < numCols; c++) {
			rowItemValues[c] = PUZZLE_INITIAL_VALUE;
		}
		this.rowItemTrueCounts = new int[numCols]; // init: zeros
		this.configurationCount = 0;

		int[] rowSeqEndIndices = new int[maxNumRowSeqs];
		for (int j = 0; j < maxNumRowSeqs; j++) {
			rowSeqEndIndices[j] = ILLEGAL_END_INDEX;
		}

		this.deductRowRecursive(rowIndex, 0, rowItemValues, matrix, rowSeqEndIndices);

		if (configurationCount == 0) throw new RuntimeException("INTERNAL ERROR: configurations == 0");

		for (int c = 0; c < numCols; c++) {
			if (this.rowItemTrueCounts[c] == 0) {
				// it's impossible to legally place a 'true' --> 'false' is certain
				if (matrix[rowIndex][c] != PUZZLE_FALSE) {
					checkNeedCols[c] = true;
					matrix[rowIndex][c] = PUZZLE_FALSE;
					this.deductedItems++;
				}
			}
			else if (this.rowItemTrueCounts[c] == this.configurationCount) {
				// it's impossible to legally place a 'false' --> 'true' is certain
				if (matrix[rowIndex][c] != PUZZLE_TRUE) {
					checkNeedCols[c] = true;
					matrix[rowIndex][c] = PUZZLE_TRUE;
					this.deductedItems++;
				}
			}
		}
	}