package freemind.icons;

import java.awt.Color;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import freemind.controller.Controller;
import freemind.controller.MenuBar;
import freemind.controller.StructuredMenuHolder;
import freemind.controller.actions.generated.instance.ObjectFactory;
import freemind.main.FreeMind;
import freemind.main.FreeMindMain;
import freemind.main.Tools;
import freemind.main.XMLElement;
import freemind.modes.ControllerAdapter;
import freemind.modes.MindMapNode;
import freemind.modes.NodeAdapter;
import freemind.modes.StylePattern;
import freemind.modes.actions.ApplyPatternAction;
import freemind.modes.mindmapmode.EncryptedMindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapNodeModel;
import freemind.modes.mindmapmode.MindMapPopupMenu;
import freemind.modes.mindmapmode.MindMapToolBar;
import freemind.view.mindmapview.EditNodeTextField;
import freemind.view.mindmapview.MultipleImage;
import freemind.view.mindmapview.NodeView;

public privileged aspect AbstractIconsAspect {

	pointcut encrypted(EncryptedMindMapNode encrypted, FreeMindMain frame):
		execution(EncryptedMindMapNode.new(Object, FreeMindMain))
			&& this(encrypted) && args(.., frame);

	pointcut setVisible(EncryptedMindMapNode encrypted):
		execution(private void EncryptedMindMapNode.setVisible(boolean)) && this(encrypted);

	pointcut htmlIcons(MindMapNodeModel model, Writer fileout):
		execution(private void MindMapNodeModel.hookHTMLIcons(Writer))
			&& this(model) && args(fileout);

	pointcut applyStyle(XMLElement child):
    	execution(private void StylePattern.hookIconStyle(XMLElement)) && args(child);

	pointcut iconsActionHook(ControllerAdapter cthis) : execution(* ControllerAdapter.iconsActionHook()) 
	 && this(cthis);

	pointcut controllerAdapter(ControllerAdapter adapter): 
		execution(ControllerAdapter.new(..)) && this(adapter);

	pointcut hookPatterns(int i, List patternsList,
			ApplyPatternAction[] patterns, ControllerAdapter adapter):
		execution(private void ControllerAdapter.hookCreatePatterns(int, List, ApplyPatternAction[])) &&
			args(i, patternsList, patterns) && this(adapter);

	pointcut setAllActions(boolean enabled): 
		execution(protected void setAllActions(boolean)) && args(enabled);

	pointcut iconActions(MindMapController controller):
    	execution(MindMapController.new(..)) && this(controller);

	pointcut updateMenus(MindMapController controller,
			StructuredMenuHolder holder):
    	execution(public void MindMapController.updateMenus(StructuredMenuHolder))
    		&& this(controller) && args(holder);

	pointcut setAllIconActions(boolean enabled):
    	execution(protected void MindMapController.setAllActions(boolean))
    		&& args(enabled);

	pointcut setIconBlindIcon(JMenuItem item) 
	: execution(* StructuredMenuHolder.setIconBlindIcon_hook(..)) && args(item);

	pointcut update(MindMapPopupMenu popupMenu, StructuredMenuHolder holder):
    	call(public void StructuredMenuHolder.updateMenus(JPopupMenu, String))
    		&& this(popupMenu) && target(holder) 
    			&& withincode(public void MindMapPopupMenu.update(StructuredMenuHolder));

	pointcut applyPattern(ApplyPatternAction action, MindMapNode node,
			StylePattern pattern):
    	execution(private void ApplyPatternAction.hookApplyPattern(MindMapNode, StylePattern))
    		&& this(action) && args(node, pattern);

	pointcut updateNodeView(NodeView nodeView): 
    	execution(void NodeView.update()) && this(nodeView);

	pointcut getIcons(EditNodeTextField cthis) 
	 : execution(*  EditNodeTextField.getIconsHook(..)) && this(cthis);

	pointcut linkIconWidth(EditNodeTextField cthis, int xOffset,
			int linkIconWidth) 
	 : execution(*  EditNodeTextField.linkIconWidthHook(..)) && args(xOffset, linkIconWidth) && this(cthis);

	pointcut updateBar(MindMapToolBar toolBar):
    	execution(public void MindMapToolBar.update(StructuredMenuHolder))
    		&& this(toolBar);

	pointcut toggleLeft(Controller controller):
    	execution(public Controller.new(FreeMindMain)) && this(controller);

	pointcut quit(Controller controller):
    	call(public void FreeMindMain.saveProperties()) 
    		&& withincode(private void Controller.quit()) && this(controller);

	pointcut main(FreeMind frame):
    	execution(* FreeMind.hookLeftToolbar(FreeMind)) && args(frame);

	pointcut additionalPopups(MenuBar menu):
    	execution(private void MenuBar.addAdditionalPopupActions()) && this(menu);

	pointcut updateMenu(MenuBar menu): 
    	execution(private void MenuBar.updateEditMenu()) && this(menu);

//	pointcut element(XMLElementAdapter adapter):
//    	execution(protected void XMLElementAdapter.completeElement())
//    		&& this(adapter);
//
//	pointcut attribute(XMLElementAdapter adapter, Object value):
//    	execution(public void XMLElementAdapter.setAttribute(String, Object))
//    		&& this(adapter) && args(.., value);
//
//	pointcut addChildIcon(XMLElement child, NodeAdapter node):
//    	execution(private void XMLElementAdapter.hookAddChild(
//    			XMLElement, NodeAdapter)) && args(child, node);

	after(EncryptedMindMapNode encrypted, FreeMindMain frame): encrypted(encrypted, frame) {
		if (encryptedIcon == null) {
			encryptedIcon = MindIcon.factory("encrypted").getIcon(frame);
		}
		if (decryptedIcon == null) {
			decryptedIcon = MindIcon.factory("decrypted").getIcon(frame);
		}
		updateIcon(encrypted);
	}

	after(EncryptedMindMapNode encrypted): setVisible(encrypted) {
		updateIcon(encrypted);
	}

	before(MindMapNodeModel model, Writer fileout) throws IOException: htmlIcons(model, fileout) {
		if (model.getFrame().getProperty("export_icons_in_html").equals("true")) {
			for (int i = 0; i < model.getIcons().size(); ++i) {
				fileout.write("<img src=\""
						+ ((MindIcon) model.getIcons().get(i))
								.getIconFileName()
						+ "\" alt=\""
						+ ((MindIcon) model.getIcons().get(i))
								.getDescription(model.getFrame()) + "\">");
			}
		}
	}

	before(XMLElement child): applyStyle(child) {
		if (child.getStringAttribute("icon") != null) {
			appliesToNodeIcon = true;
			setNodeIcon(child.getStringAttribute("icon").equals("none") ? null
					: MindIcon.factory(child.getStringAttribute("icon")));
		}
	}

	before(ControllerAdapter cthis) : iconsActionHook(cthis) {
		removeLastIconAction = new RemoveLastIconAction(cthis);
		// this action handles the xml stuff: (undo etc.)
		unknwonIconAction = new IconAction(cthis,
				MindIcon.factory((String) MindIcon.getAllIconNames().get(0)),
				removeLastIconAction);
		removeLastIconAction.setIconAction(unknwonIconAction);
		removeAllIconsAction = new RemoveAllIconsAction(cthis,
				unknwonIconAction);
	}

	after(ControllerAdapter adapter): controllerAdapter(adapter) {
		removeLastIconAction = new RemoveLastIconAction(adapter);
		unknwonIconAction = new IconAction(adapter,
				MindIcon.factory((String) MindIcon.getAllIconNames().get(0)),
				removeLastIconAction);
		removeLastIconAction.setIconAction(unknwonIconAction);
		removeAllIconsAction = new RemoveAllIconsAction(adapter,
				unknwonIconAction);

	}

	before(int i, List patternsList, ApplyPatternAction[] patterns,
			ControllerAdapter adapter): hookPatterns(i, patternsList, patterns, adapter) {
		// MindIcon patternIcon = ((StylePattern) patternsList.get(i))
		// .getNodeIcon();
		MindIcon patternIcon = getNodeIcon();
		if (patternIcon != null) {
			patterns[i].putValue(Action.SMALL_ICON,
					patternIcon.getIcon(adapter.getFrame()));
		}
	}

	after(boolean enabled): setAllActions(enabled) {
		removeLastIconAction.setEnabled(enabled);
		removeAllIconsAction.setEnabled(enabled);
	}

	after(MindMapController controller): iconActions(controller) {
		this.createIconActions(controller);
	}

	after(MindMapController controller, StructuredMenuHolder holder):
		updateMenus(controller, holder) {
		// Chamando o mï¿½todo com o parï¿½metro controller
		addIconsToMenu(holder, MenuBar.INSERT_MENU + "icons", controller);
	}

	after(boolean enabled): setAllIconActions(enabled) {
		for (int i = 0; i < iconActions.size(); ++i) {
			((Action) iconActions.get(i)).setEnabled(enabled);
		}
	}

	before(JMenuItem item) : setIconBlindIcon(item) {
		item.setIcon(blindIcon);
	}

	before(MindMapPopupMenu popupMenu, StructuredMenuHolder holder):
		update(popupMenu, holder) {
		// Chamando o mï¿½todo com o parï¿½metro controller
		addIconsToMenu(holder, MindMapPopupMenu.MINDMAPMODE_POPUP + "icons/",
				popupMenu.c);
	}

	before(ApplyPatternAction action, MindMapNode node, StylePattern pattern):
    	applyPattern(action, node, pattern) {
		// sem usar pattern e action
		if (getAppliesToNodeIcon()) {
			if (getNodeIcon() == null) {
				while (removeLastIcon(node) > 0) {
				}
			} else {
				addIcon(node, getNodeIcon());
			}
		}
	}

	before(NodeView nodeView): updateNodeView(nodeView) {
		MultipleImage iconImages = new MultipleImage(nodeView.map.getZoom());
		boolean iconPresent = false;
		FreeMindMain frame = nodeView.map.getController().getFrame();
		Map stateIcons = (nodeView.getModel()).getStateIcons();
		for (Iterator i = stateIcons.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			iconPresent = true;
			ImageIcon myIcon = (ImageIcon) stateIcons.get(key);
			iconImages.addImage(myIcon);
		}

		List icons = (nodeView.getModel()).getIcons();
		for (Iterator i = icons.iterator(); i.hasNext();) {
			MindIcon myIcon = (MindIcon) i.next();
			iconPresent = true;
			// System.out.println("print the icon " +
			// myIcon.getIconFileName());
			iconImages.addImage(myIcon.getIcon(frame));
		}

		String link = ((NodeAdapter) nodeView.getModel()).getLink();
		if (link != null) {
			iconPresent = true;
			ImageIcon icon = new ImageIcon(frame.getResource(link
					.startsWith("mailto:") ? "images/Mail.png" : (Tools
					.executableByExtension(link) ? "images/Executable.png"
					: "images/Link.png")));
			iconImages.addImage(icon);
		}

		nodeView.setIcon(iconPresent ? iconImages : null);
	}

	boolean around(EditNodeTextField cthis) : getIcons(cthis) {
		return cthis.getNode().getModel().getIcons().size() != 0;
	}

	int around(EditNodeTextField cthis, int xOffset, int linkIconWidth) 
	 : linkIconWidth(cthis, xOffset, linkIconWidth) {
		return xOffset += linkIconWidth
				* cthis.getNode().getModel().getIcons().size();
	}

	after(MindMapToolBar toolBar): updateBar(toolBar) {
		toolBar.buttonToolBar.add(removeLastIconAction);
		toolBar.buttonToolBar.add(removeAllIconsAction);
		toolBar.buttonToolBar.addSeparator();
		for (int i = 0; i < iconActions.size(); ++i) {
			toolBar.buttonToolBar.add((Action) iconActions.get(i));
		}
	}

	after(Controller controller): toggleLeft(controller) {
		toggleLeftToolbar = new ToggleLeftToolbarAction(controller);
	}

	before(Controller controller): quit(controller) {
		controller.setProperty("leftToolbarVisible",
				((ToggleLeftToolbarAction) toggleLeftToolbar)
						.getLeftToolbarVisible() ? "true" : "false");
	}

	before(FreeMind frame): main(frame) {
		if (Tools.safeEquals(frame.getProperty("leftToolbarVisible"), "false")) {
			((ToggleLeftToolbarAction) toggleLeftToolbar)
					.setLeftToolbarVisible(false);
		}
	}

	after(MenuBar menu): additionalPopups(menu) {
		JMenuItem newPopupItem = new JMenuItem(toggleLeftToolbar);
		newPopupItem.setForeground(new Color(100, 80, 80));
		menu.menuHolder.addMenuItem(newPopupItem, MenuBar.POPUP_MENU
				+ "toggleLeftToolbar");
	}

	before(MenuBar menu): updateMenu(menu) {
		menu.menuHolder.addAction(toggleLeftToolbar, MenuBar.VIEW_MENU
				+ "toolbars/toggleLeftToolbar");
	}

	private static ImageIcon encryptedIcon;
	private static ImageIcon decryptedIcon;
	public Action toggleLeftToolbar;

	// Esse mŽtodo n‹o possu’a nenhum par‰metro em sua vers‹o original...
	public void updateIcon(EncryptedMindMapNode encrypted) {
		encrypted.setStateIcon("encryptedNode",
				(encrypted.isVisible()) ? decryptedIcon : encryptedIcon);
	}

	public IconAction unknwonIconAction = null;
	public RemoveLastIconAction removeLastIconAction = null;
	public RemoveAllIconsAction removeAllIconsAction = null;
	private MindIcon nodeIcon;
	private boolean appliesToNodeIcon = false;
	public Vector iconActions = new Vector();
	private String iconName;

	public MindIcon getNodeIcon() {
		return nodeIcon;
	}

	public void setNodeIcon(MindIcon nodeIcon) {
		this.nodeIcon = nodeIcon;
	}

	public boolean getAppliesToNodeIcon() {
		return appliesToNodeIcon;
	}

	public void addIconsToMenu(StructuredMenuHolder holder,
			String iconMenuString, MindMapController controller) {
		// não precisa incluir o feeder, pois o metodo so eh chamado dentros de
		// aspectos
		JMenu iconMenu = holder.addMenu(
				new JMenu(controller.getText("icon_menu")), iconMenuString
						+ "/.");
		holder.addAction(removeLastIconAction, iconMenuString
				+ "/removeLastIcon");
		holder.addAction(removeAllIconsAction, iconMenuString
				+ "/removeAllIcons");
		holder.addSeparator(iconMenuString);
		for (int i = 0; i < iconActions.size(); ++i) {
			JMenuItem item = holder.addAction((Action) iconActions.get(i),
					iconMenuString + "/" + i);
		}
	}

	private void createIconActions(MindMapController controller) {
		Vector iconNames = MindIcon.getAllIconNames();
		for (int i = 0; i < iconNames.size(); ++i) {
			String iconName = ((String) iconNames.get(i));
			MindIcon myIcon = MindIcon.factory(iconName);
			IconAction myAction = new IconAction(controller, myIcon,
					removeLastIconAction);
			iconActions.add(myAction);
		}
	}

	private static Icon blindIcon = new BlindIcon(16);

	public void addIcon(MindMapNode node, MindIcon icon) {
		unknwonIconAction.addIcon(node, icon);
	}

	public void removeAllIcons(MindMapNode node) {
		removeAllIconsAction.removeAllIcons(node);
	}

	public int removeLastIcon(MindMapNode node) {
		return removeLastIconAction.removeLastIcon(node);
	}

	interface IconsInterface {
	}

	declare parents : MindMapNode extends IconsInterface;

	private Vector<MindIcon> IconsInterface.icons = null;

	public List IconsInterface.getIcons() {
		if (icons == null) {
			return Collections.EMPTY_LIST;
		}
		return icons;
	}

	public void IconsInterface.addIcon(MindIcon _icon) {
		createIcons();
		icons.add(_icon);
	}

	public int IconsInterface.removeLastIcon() {
		createIcons();
		if (icons.size() > 0) {
			icons.setSize(icons.size() - 1);
		}
		int returnSize = icons.size();
		if (returnSize == 0) {
			icons = null;
		}
		return returnSize;
	}

	private void IconsInterface.createIcons() {
		if (icons == null) {
			icons = new Vector();
		}
	}

	public freemind.controller.actions.generated.instance.AddIconActionType ObjectFactory.createAddIconActionType()
			throws javax.xml.bind.JAXBException {
		return new freemind.icons.AddIconActionTypeImpl();
	}

	public freemind.controller.actions.generated.instance.AddIconAction ObjectFactory.createAddIconAction()
			throws javax.xml.bind.JAXBException {
		return new freemind.icons.AddIconActionImpl();
	}

	public freemind.controller.actions.generated.instance.RemoveLastIconXmlAction ObjectFactory.createRemoveLastIconXmlAction()
			throws javax.xml.bind.JAXBException {
		return new freemind.icons.RemoveLastIconXmlActionImpl();
	}

	public freemind.controller.actions.generated.instance.RemoveLastIconXmlActionType ObjectFactory.createRemoveLastIconXmlActionType()
			throws javax.xml.bind.JAXBException {
		return new freemind.icons.RemoveLastIconXmlActionTypeImpl();
	}

	public freemind.controller.actions.generated.instance.RemoveAllIconsXmlActionType ObjectFactory.createRemoveAllIconsXmlActionType()
			throws javax.xml.bind.JAXBException {
		return new freemind.icons.RemoveAllIconsXmlActionTypeImpl();
	}

	public freemind.controller.actions.generated.instance.RemoveAllIconsXmlAction ObjectFactory.createRemoveAllIconsXmlAction()
			throws javax.xml.bind.JAXBException {
		return new freemind.icons.RemoveAllIconsXmlActionImpl();
	}
}