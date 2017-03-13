package edu.nops.blacksphere.ide.editors.device.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import edu.nops.blacksphere.ide.editors.device.parts.SchemeElementEditPart;

public class PropertiesCommand extends Command {

	private List<SchemeElementEditPart> objects;
	
	public PropertiesCommand(List objects) {
		this.objects = new ArrayList<SchemeElementEditPart>();
		for(Object object : objects) {
			if (object instanceof SchemeElementEditPart)
				this.objects.add((SchemeElementEditPart) object);
		}
	}
	
	@Override
	public boolean canExecute() {
		return !objects.isEmpty();
	}

	@Override
	public boolean canUndo() {
		return false;
	}

	@Override
	public void execute() {
		if (canExecute())
			redo();
	}

	@Override
	public void redo() {
		// TODO сделеать отображение окна свойств
	}

}
