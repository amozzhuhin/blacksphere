package edu.nops.blacksphere.ide.editors.asm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import com.thoughtworks.xstream.XStream;

import edu.nops.blacksphere.core.intel8088.InstructionSet;
import edu.nops.blacksphere.core.intel8088.InstructionSet.Instruction;
import edu.nops.blacksphere.ide.BSP;

/**
 * Оброботка дополнения ассемблерного кода
 * @author nops
 */
public class ASMContentProcessor implements IContentAssistProcessor {

	public ASMContentProcessor() {
		loadInstructionSet();
	}
	
	InstructionSet instructionSet;
	
	/**
	 * Загрузка описания инструкций из файла
	 */
	public void loadInstructionSet() {
		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		xstream.alias("sets", InstructionSet.class); //$NON-NLS-1$
		try {
			FileInputStream is = new FileInputStream(
					BSP.getFile("/res/intel8088/instruction_set.xml")); //$NON-NLS-1$
			instructionSet = (InstructionSet) xstream.fromXML(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Получить префикс слова от offset до первого пробела
	 * @param doc текстовый документ
	 * @param offset смещение в тексте
	 * @return строка префикс
	 */
	public String getPrefix(IDocument doc,	int offset) {
		try {
			int len = 0;
			while (offset > 0) {
				char c = doc.getChar(offset - 1);
				if (c == ' ' || c == '\t') break;
				offset--;
				len++;
			}
			return doc.get(offset, len);
		} catch (BadLocationException e) {
			return ""; //$NON-NLS-1$
		}
	}
	
	/**
	 * Сгенерировать предложения для дополнения
	 */
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {

		String prefix = getPrefix(viewer.getDocument(), offset).toLowerCase();
		
		ArrayList<ICompletionProposal> proposals = new ArrayList<ICompletionProposal>();
		
		for(int i = 0; i < instructionSet.getInstructionCount(); i++) {
			Instruction instruction = instructionSet.getInstruction(i);
			if (instruction.getCommand().indexOf(prefix) == 0)
				proposals.add(new CompletionProposal(instruction.getCommand(),
						offset - prefix.length(), prefix.length(),
						instruction.getCommand().length(),
						null, instruction.getCommand() + " - " + instruction.getDescription(), //$NON-NLS-1$
						null, null));
		}
		return proposals.toArray(new ICompletionProposal[0]);
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		return null; // не используется
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return null; // не используется
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null; // не используется
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null; // не используется
	}

	@Override
	public String getErrorMessage() {
		return null; // не используется
	}

}
