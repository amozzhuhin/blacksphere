package edu.nops.blacksphere.core.resources;

import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;

/**
 * Интерфейс для предоставления ввода редактору по IFileStore 
 * @author nops
 */
public interface IFileStoreEditorInput extends IEditorInput {

	public InputStream openInputStream(int options, IProgressMonitor monitor) throws CoreException ;
	
	public OutputStream openOutputStream(int options, IProgressMonitor monitor) throws CoreException ;

}
