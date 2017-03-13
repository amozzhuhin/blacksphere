package edu.nops.blacksphere.ide.editors.asm;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

public class ASMAutoEditStrategy implements IAutoEditStrategy {

	/** Получение смещения в строке где проижошла комманда */
	public int getLinePosition(IDocument document,
			DocumentCommand command) {
		try {
			IRegion lineRegion = document.getLineInformationOfOffset(command.offset);
			return command.offset - lineRegion.getOffset();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/** Обработка табуляции */
	@Override
	public void customizeDocumentCommand(IDocument document,
			DocumentCommand command) {
		if (command.text.compareTo("\t") == 0) { //$NON-NLS-1$
			int linePos = getLinePosition(document, command); 
			if (linePos < 9 && linePos >= 0) {
				command.text = ""; //$NON-NLS-1$
				for (int i = linePos; i < 9; i++) {
					command.text = command.text + " "; //$NON-NLS-1$
				}
			} else if (linePos < 16) {
				command.text = ""; //$NON-NLS-1$
				for (int i = linePos; i < 16; i++) {
					command.text = command.text + " "; //$NON-NLS-1$
				}
			} else if (linePos < 39) {
				command.text = ""; //$NON-NLS-1$
				for (int i = linePos; i < 39; i++) {
					command.text = command.text + " "; //$NON-NLS-1$
				}
			}
		}
	}

}
