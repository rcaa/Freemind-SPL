/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/*$Id: NodeHookAdapter.java,v 1.1.4.3 2005/04/12 21:12:14 christianfoltin Exp $*/
package freemind.extensions;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import freemind.modes.MindMap;
import freemind.modes.MindMapNode;

/**
 * @author christianfoltin
 *
 * @file NodeHookAdapter.java 
 * @package freemind.modes
 * */
public abstract class NodeHookAdapter extends HookAdapter implements NodeHook {

	private boolean selfUpdateExpected;

	private MindMap map;

	private MindMapNode node;

	/**
	 * 
	 */
	public NodeHookAdapter() {
		super();
	}

	/* (non-Javadoc)
	 * @see freemind.modes.NodeHook#invoke()
	 */
	public void invoke(MindMapNode node) {
		logger.finest("invoke(node) called.");
	}

	/**
	 * @return
	 */
	protected MindMapNode getNode() {
		return node;
	}

	/**
	 * @param node
	 */
	public void setNode(MindMapNode node) {
		this.node = node;
	}

	/**
	 * @return
	 */
	protected MindMap getMap() {
		return map;
	}

	/**
	 * @param node
	 */
	protected void nodeChanged(MindMapNode node) {
//		if(node == getNode()) {
//			setSelfUpdate(true);
//		}
		// fc, 29.2.2004 (yes, this day exists!)
		// this is not nice. The node should know itself, if it is updateable, but...
		if(node.getViewer() != null)
			getController().nodeChanged(node);
//		setSelfUpdate(false);
	}

	/**
	 * @param value
	 */
	protected void setToolTip(String key, String value) {
		setToolTip(getNode(), key, value);
	}
	protected void setToolTip(MindMapNode node, String key, String value) {
		getController().setToolTip(node, key, value);
	}

	

	/**
	 * @param map
	 */
	public void setMap(MindMap map) {
		this.map = map;
	}

}
