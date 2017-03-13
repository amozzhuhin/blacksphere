package edu.nops.blacksphere.core.resources;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

public class FileStoreEditorInput implements IFileStoreEditorInput {

	// файл с данными
	private IFileStore store;
	// мета информация о фале
	private IFileInfo info;
	
	public FileStoreEditorInput(IFileStore store) {
		this.store = store;
		this.info = store.fetchInfo();
	}
	
	public boolean exists() {
		return info.exists();
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		try {
			return store.toURI().toURL().getFile();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return info.getName();
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

	public InputStream openInputStream(int options, IProgressMonitor monitor) throws CoreException  {
		return store.openInputStream(options, monitor);
	}

	public OutputStream openOutputStream(int options, IProgressMonitor monitor) throws CoreException {
		return store.openOutputStream(options, monitor);
	}
	
}
