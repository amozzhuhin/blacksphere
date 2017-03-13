package edu.nops.blacksphere.ide.editors.asm;

import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;

import edu.nops.blacksphere.ide.BSP;

/**
 * Конфигурация редактора файлов ассемблера 
 * @author nops
 */
public class ASMSourceViewerConfiguration extends
		org.eclipse.jface.text.source.SourceViewerConfiguration {

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		

	/*	RuleBasedScanner scanner = new RuleBasedScanner();
		IRule[] rules = new IRule[1];
		rules[0] = new EndOfLineRule(";",
			new Token(new TextAttribute(new Color(Display.getCurrent(), 0, 255, 0))));
		scanner.setRules(rules);
		
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
		reconciler.setDamager(dr, COMMENT_CONTENT_TYPE);
		reconciler.setRepairer(dr, COMMENT_CONTENT_TYPE);
		*/
		
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(new ASMScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		return reconciler;
	}
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {IDocument.DEFAULT_CONTENT_TYPE, BSP.COMMENT_CONTENT_TYPE};
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		assistant.setContentAssistProcessor(
				new ASMContentProcessor(), IDocument.DEFAULT_CONTENT_TYPE);
		assistant.setContextInformationPopupOrientation(
				IContentAssistant.CONTEXT_INFO_ABOVE);
		//assistant.enableAutoActivation(true);
		assistant.enableAutoInsert(true);
		//assistant.setAutoActivationDelay(500);
		return assistant;
	}

	private IAutoEditStrategy autoEditStrategy;
	
	public IAutoEditStrategy getAutoEditStrategy() {
		if (autoEditStrategy == null) {
			autoEditStrategy = new ASMAutoEditStrategy();
		}
		return autoEditStrategy;
	}
	
	@Override
	public IAutoEditStrategy[] getAutoEditStrategies(
			ISourceViewer sourceViewer, String contentType) {
		return new IAutoEditStrategy[] { getAutoEditStrategy() };
	}

	
	
}
