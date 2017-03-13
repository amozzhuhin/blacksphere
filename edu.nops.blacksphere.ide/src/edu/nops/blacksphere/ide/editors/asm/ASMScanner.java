package edu.nops.blacksphere.ide.editors.asm;

import java.util.ArrayList;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * Сканер ассемблерных файлов для выделения синтаксиса 
 * @author nops
 */
public class ASMScanner extends RuleBasedScanner {

	private static Color getColor(int R, int G, int  B){
		return new Color(Display.getCurrent(), R, G, B);
	}
	
	private static final Token DEFAULT_TOKEN =
		new Token(new TextAttribute(ColorConstants.black));
	private static final Token DIRECTIVE_TOKEN =
		new Token(new TextAttribute(getColor(127, 0, 85), null, SWT.BOLD));
	//new Token(new TextAttribute(getColor(0, 90, 0)));
	private static final Token PREPROC_TOKEN = DIRECTIVE_TOKEN;
	private static final Token STRING_TOKEN =
		new Token(new TextAttribute(getColor(0, 0, 192)));
	private static final Token COMMENT_TOKEN =
		new Token(new TextAttribute(getColor(63, 127, 95)));

	private static final String[] DIRICTIVES = {
		"assume", "at", "nobits", "bits", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		"db", "dd", "dw", "dq", "dup", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		"equ", "incbin", //$NON-NLS-1$ //$NON-NLS-2$
		"org", //$NON-NLS-1$
		"resb", "resw", "resd", "resq", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		"section", "segment", "equ", "times", "seg", "wrt", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
/*		"ax", "ah", "al", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-1$
		"bx", "bh", "bl", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-1$
		"cx", "ch", "cl", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-1$
		"dx", "dh", "dl", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-1$
		"si", "di", "bp", "sp", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-1$
		"cs", "ds", "es", "ss" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-1$
*/	};

	private static final String[] SPECIAL_WORDS = {
		"$", "$$", "CPU", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		"%define", "%include", //$NON-NLS-1$ //$NON-NLS-2$
		"%macro", "%endmacro", "%rotate", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		"%if", "%ifdef", "%ifid", "%ifnum", "%ifstr", "%error", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		"%push", "%pop" //$NON-NLS-1$ //$NON-NLS-2$
	};

	public ASMScanner() {
		// TODO Усилить правила
		ArrayList<IRule> rules = new ArrayList<IRule>();
		// текст
		setDefaultReturnToken(DEFAULT_TOKEN);
		// пробелы
		WordRule wordRule = new WordRule(new ASMWordDetector());
		rules.add(new WhitespaceRule(new WhiteSpaceDetector()));
		// директивы (розовый)
		for (String word : DIRICTIVES) {
			wordRule.addWord(word, DIRECTIVE_TOKEN);
		}
		rules.add(wordRule);
		// специальные слова
		wordRule = new WordRule(new ASMWordDetector());
		rules.add(new WhitespaceRule(new WhiteSpaceDetector()));
		// специальные слова
		for (String word : SPECIAL_WORDS) {
			wordRule.addWord(word, PREPROC_TOKEN);
		}
		rules.add(wordRule);		
		// TODO Правило для чисел 
		// строки (синий)
		rules.add(new PatternRule("\"", "\"", STRING_TOKEN, '\\', true));  //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new PatternRule("'", "'", STRING_TOKEN, '\\', true));  //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new EndOfLineRule(";", COMMENT_TOKEN)); //$NON-NLS-1$
		setRules(rules.toArray(new IRule[0]));
	}
	
	private class ASMWordDetector implements IWordDetector {

		public boolean isWordPart(char c) {
			return (isWordStart(c) || (c >= '0') && (c <= '9'));
		}

		public boolean isWordStart(char c) {
			return (((c >= 'a') && (c <='z'))
					  || ((c >= 'A') && (c <='Z'))
					  || (c == '_') || (c == '%') || (c == '.') || (c == '$'));
		}
		
	}
	
	private class WhiteSpaceDetector implements IWhitespaceDetector {

		public boolean isWhitespace(char c) {
			return Character.isWhitespace(c);
		}
		
	}
	
}
