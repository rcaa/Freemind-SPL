/*
 * FreeMind - A Program for creating and viewing Mindmaps Copyright (C)
 * 2000-2004 Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 * 
 * See COPYING for Details
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * Created on 25.08.2004
 */
/* $Id: ItalicAction.java,v 1.1.4.1 2004/10/17 23:00:10 dpolivaev Exp $ */
package freemind.modes.actions;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.xml.bind.JAXBException;

import freemind.controller.MenuItemEnabledListener;
import freemind.controller.actions.ActionPair;
import freemind.controller.actions.NodeActorXml;
import freemind.controller.actions.generated.instance.ItalicNodeAction;
import freemind.controller.actions.generated.instance.XmlAction;
import freemind.modes.ControllerAdapter;
import freemind.modes.MapAdapter;
import freemind.modes.MindMapNode;
import freemind.modes.NodeAdapter;


public class ItalicAction extends NodeGeneralAction implements NodeActorXml, MenuItemEnabledListener{
	private final ControllerAdapter modeController;
	/**
	 * @param textID
	 * @param iconPath
	 * @param actor
	 */
	public ItalicAction(ControllerAdapter modeController) {
		super(modeController, "italic", "images/Italic16.gif");
		this.modeController = modeController;
		addActor(this);			
	}

	public void act(XmlAction action) {
		ItalicNodeAction italicact = (ItalicNodeAction) action;
		NodeAdapter node = getNodeFromID(italicact.getNode());
		if(node.isItalic() != italicact.isItalic()) {
			node.setItalic(italicact.isItalic());
			this.modeController.nodeChanged(node);
		}
	}


	public Class getDoActionClass() {
		return ItalicNodeAction.class;
	}

	public ActionPair apply(MapAdapter model, MindMapNode selected) throws JAXBException {
		// every node is set to the inverse of the focussed node.
		boolean italic = modeController.getSelected().isItalic();
		return getActionPair(selected, !italic);
	}

	private ActionPair getActionPair(MindMapNode selected, boolean italic)
		throws JAXBException {
		ItalicNodeAction italicAction = toggleItalic(selected, italic);
		ItalicNodeAction undoItalicAction = toggleItalic(selected, selected.isItalic());
		return new ActionPair(italicAction, undoItalicAction);
	}

	private ItalicNodeAction toggleItalic(MindMapNode selected, boolean italic)
		throws JAXBException {
		ItalicNodeAction italicAction = getActionXmlFactory().createItalicNodeAction();
		italicAction.setNode(getNodeID(selected));
		italicAction.setItalic(italic);
		return italicAction;
	}

	public void setItalic(MindMapNode node, boolean  italic) {
		try {
			execute(getActionPair(node, italic));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

    public boolean isEnabled(JMenuItem item, Action action) {
		boolean italic = modeController.getSelected().isItalic();
		setSelected(item, italic);
        return true;
    }


}