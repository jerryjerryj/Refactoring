private void deductColRecursive(int colIndex, int colSeqIndex, int[] colItemValues, int[][] matrix, int[] colSeqEndIndices) {
		int numRows = this.rowSequences.length;
		int numCols = this.colSequences.length;
		int maxNumColSeqs = this.colSequences[colIndex].length;

		// check end of sequences on this column
		if (colSeqIndex >= maxNumColSeqs || this.colSequences[colIndex][colSeqIndex] == PUZZLE_NO_SEQUENCE) {
			// add 'false's to the end of the column
			int falseStart; // first index to add 'false's to
			if (colSeqIndex == 0) falseStart = 0;
			else falseStart = colSeqEndIndices[colSeqIndex - 1] + 2;

			for (int i = falseStart; i < numRows; i++) {
				if (matrix[i][colIndex] == PUZZLE_TRUE) return;
				else colItemValues[i] = PUZZLE_TEMP_FALSE;
			}

			// current column ready
			this.configurationCount++;
			for (int r = 0; r < numRows; r++) {
				if (colItemValues[r] == PUZZLE_TEMP_TRUE) {
					this.colItemTrueCounts[r]++;
				}
				else if (colItemValues[r] == PUZZLE_TEMP_FALSE) {
					// do nothing
				}
				else throw new RuntimeException("INTERNAL ERROR: illegal colItem value");
			}
			return;
		}

		int sequenceLength = this.colSequences[colIndex][colSeqIndex];

		int maxEndPosition; // last index where this sequence may end
		int seqLengthSum = 0;
		for (int s = colSeqIndex + 1; s < maxNumColSeqs; s++) {
			if (this.colSequences[colIndex][s] == PUZZLE_NO_SEQUENCE) break;

			// sequence length plus mandatory preceding 'false'
			seqLengthSum += this.colSequences[colIndex][s] + 1;
		}
		maxEndPosition = numRows - seqLengthSum - 1;

		int minStartPosition; // from what index current sequence is starting from?
		if (colSeqIndex == 0) minStartPosition = 0; // first sequence on the column
		else minStartPosition = colSeqEndIndices[colSeqIndex - 1] + 2;

		int minEndPosition = minStartPosition + sequenceLength - 1;

		// is there room for this sequence (and the others)?
		if (minEndPosition > maxEndPosition) {
			// no room!
			throw new RuntimeException("ERROR: No space for all sequences!");
		}

		// browse through all possible placements
		outer: // label for 'for' loop
		for (int endPos = minEndPosition; endPos <= maxEndPosition; endPos++) {
			int startPos = endPos - sequenceLength + 1;

			// undo previous placement of this sequence
			if (endPos != minEndPosition) { // not first placement?
				// Undo the first 'true' of PRECEDING sequence position.
				// It is the item just before first 'true' of current sequence position.
				int undoIndex = startPos - 1; 

				if (matrix[undoIndex][colIndex] == PUZZLE_TRUE) break outer;
				else colItemValues[undoIndex] = PUZZLE_TEMP_FALSE;
			}

			// add sequence (new placement):
			for (int i = startPos; i <= endPos ; i++) {
				if (matrix[i][colIndex] == PUZZLE_FALSE) {
					// Optimization starts -->
					// It is impossible to have 'true' now in (i, colIndex).
					// Skip a few impossible sequence positions.
					int nextPossibleEndPos = i + sequenceLength;
					for (int j = startPos; j <= i - 1; j++) {
						if (matrix[j][colIndex] == PUZZLE_TRUE) {
							// It is impossible to have 'false' now in (j, colIndex).
							// Further position of this sequence won't help.
							break outer;
						}
						else colItemValues[j] = PUZZLE_TEMP_FALSE;
					}
					endPos = nextPossibleEndPos - 1; // minus one to counter subsequent "++" in 'for' loop
					// <-- Optimization ends
					continue outer;
				}
				else colItemValues[i] = PUZZLE_TEMP_TRUE;
			}
			// add a succeeding 'false', if not yet at the end of the column.
			if (endPos < numRows - 1) {
				if (matrix[endPos + 1][colIndex] == PUZZLE_TRUE) continue outer;
				else colItemValues[endPos + 1] = PUZZLE_TEMP_FALSE;
			}

			// update ending index for this sequence (new ending index)
			colSeqEndIndices[colSeqIndex] = endPos;

			// recursive call:
			this.deductColRecursive(colIndex, colSeqIndex + 1, colItemValues, matrix, colSeqEndIndices);
		}
	}
}