private void deductRowRecursive(int rowIndex, int rowSeqIndex, int[] rowItemValues, int[][] matrix, int[] rowSeqEndIndices) {
		int numRows = this.rowSequences.length;
		int numCols = this.colSequences.length;
		int maxNumRowSeqs = this.rowSequences[rowIndex].length;

		// check end of sequences on this row
		if (rowSeqIndex >= maxNumRowSeqs || this.rowSequences[rowIndex][rowSeqIndex] == PUZZLE_NO_SEQUENCE) {
			// add 'false's to the end of the row
			int falseStart; // first index to add 'false's to
			if (rowSeqIndex == 0) falseStart = 0;
			else falseStart = rowSeqEndIndices[rowSeqIndex - 1] + 2;

			for (int i = falseStart; i < numCols; i++) {
				if (matrix[rowIndex][i] == PUZZLE_TRUE) return;
				else rowItemValues[i] = PUZZLE_TEMP_FALSE;
			}

			// current row ready
			this.configurationCount++;
			for (int c = 0; c < numCols; c++) {
				if (rowItemValues[c] == PUZZLE_TEMP_TRUE) {
					this.rowItemTrueCounts[c]++;
				}
				else if (rowItemValues[c] == PUZZLE_TEMP_FALSE) {
					// do nothing
				}
				else throw new RuntimeException("INTERNAL ERROR: illegal rowItem value");
			}
			return;
		}

		int sequenceLength = this.rowSequences[rowIndex][rowSeqIndex];

		int maxEndPosition; // last index where this sequence may end
		int seqLengthSum = 0;
		for (int s = rowSeqIndex + 1; s < maxNumRowSeqs; s++) {
			if (this.rowSequences[rowIndex][s] == PUZZLE_NO_SEQUENCE) break;

			// sequence length plus mandatory preceding 'false'
			seqLengthSum += this.rowSequences[rowIndex][s] + 1;
		}
		maxEndPosition = numCols - seqLengthSum - 1;

		int minStartPosition; // from what index current sequence is starting from?
		if (rowSeqIndex == 0) minStartPosition = 0; // first sequence on the row
		else minStartPosition = rowSeqEndIndices[rowSeqIndex - 1] + 2;

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

				if (matrix[rowIndex][undoIndex] == PUZZLE_TRUE) break outer;
				else rowItemValues[undoIndex] = PUZZLE_TEMP_FALSE;
			}

			// add sequence (new placement):
			for (int i = startPos; i <= endPos ; i++) {
				if (matrix[rowIndex][i] == PUZZLE_FALSE) {
					// Optimization starts -->
					// It is impossible to have 'true' now in (rowIndex, i).
					// Skip a few impossible sequence positions.
					int nextPossibleEndPos = i + sequenceLength;
					for (int j = startPos; j <= i - 1; j++) {
						if (matrix[rowIndex][j] == PUZZLE_TRUE) {
							// It is impossible to have 'false' now in (rowIndex, j).
							// Further position of this sequence won't help.
							break outer;
						}
						else rowItemValues[j] = PUZZLE_TEMP_FALSE;
					}
					endPos = nextPossibleEndPos - 1; // minus one to counter subsequent "++" in 'for' loop
					// <-- Optimization ends
					continue outer;
				}
				else rowItemValues[i] = PUZZLE_TEMP_TRUE;
			}
			// add a succeeding 'false', if not yet at the end of the row.
			if (endPos < numCols - 1) {
				if (matrix[rowIndex][endPos + 1] == PUZZLE_TRUE) continue outer;
				else rowItemValues[endPos + 1] = PUZZLE_TEMP_FALSE;
			}

			// update ending index for this sequence (new ending index)
			rowSeqEndIndices[rowSeqIndex] = endPos;

			// recursive call:
			this.deductRowRecursive(rowIndex, rowSeqIndex + 1, rowItemValues, matrix, rowSeqEndIndices);
		}
	}
