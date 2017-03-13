package edu.nops.blacksphere.ide.editors.device.actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.SaveAsDialog;

import edu.nops.blacksphere.ide.BSP;
import edu.nops.blacksphere.ide.BSPImages;
import edu.nops.blacksphere.ide.BSPMessages;

/**
 * Действие сохраниения редактора GEF в картинку 
 * @author nops
 */
public class GEFEditorAsImageAction extends Action {

	public static final String ID = "bsp.GEFEditorAsImageAction";  //$NON-NLS-1$
	
	GraphicalViewer viewer;
	IPath path;
	
	public GEFEditorAsImageAction(GraphicalViewer viewer) {
		this.viewer = viewer; 
		setId(ID);
		setText(BSPMessages.getString("GEFEditorAsImageAction.text")); //$NON-NLS-1$
		setImageDescriptor(BSP.getImages().getImageDescriptor(BSPImages.IMG_SAVE_AS_IMAGE));
	}
	
	protected boolean selectFile() {
		SaveAsDialog dialog = new SaveAsDialog(Display.getDefault().getActiveShell());
		dialog.setOriginalName("image.jpg"); //$NON-NLS-1$
		if (dialog.open() == Window.OK) {
			path = dialog.getResult();
		} else
			path = null;
		return path != null;
	}
	
	@Override
	public void run() {
		if (selectFile()) {
			ScalableFreeformRootEditPart rootEditPart =
				(ScalableFreeformRootEditPart) viewer.getEditPartRegistry().get(LayerManager.ID);
			IFigure rootFigure = ((LayerManager) rootEditPart).getLayer(LayerConstants.PRINTABLE_LAYERS);
			Rectangle rootFigureBounds = rootFigure.getBounds();
				
			Control figureCanvas = viewer.getControl();
			GC figureCanvasGC = new GC(figureCanvas);
	
			Image img = new Image(null, rootFigureBounds.width, rootFigureBounds.height);
			GC imageGC = new GC(img);
			imageGC.setBackground(figureCanvasGC.getBackground());
			imageGC.setForeground(figureCanvasGC.getForeground());
			imageGC.setFont(figureCanvasGC.getFont());
			imageGC.setLineStyle(figureCanvasGC.getLineStyle());
			imageGC.setLineWidth(figureCanvasGC.getLineWidth());
			//imageGC.setXORMode(figureCanvasGC.getXORMode());
			Graphics imgGraphics = new SWTGraphics(imageGC);
			imgGraphics.translate(-rootFigureBounds.x, -rootFigureBounds.y);
			
			rootFigure.paint(imgGraphics);
	
			ImageData[] imgData = new ImageData[1];
			imgData[0] = img.getImageData();
	
			ImageLoader imgLoader = new ImageLoader();
			imgLoader.data = imgData;
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			imgLoader.save(os, SWT.IMAGE_JPEG);
			ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
			
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			try {
				if (!file.exists())
					file.create(is, true, null);
				else
				file.setContents(is, IFile.FORCE, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
			figureCanvasGC.dispose();
			imageGC.dispose();
			img.dispose();
		}
	}

}
