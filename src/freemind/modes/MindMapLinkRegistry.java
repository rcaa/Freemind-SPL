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
/*$Id: MindMapLinkRegistry.java,v 1.6.18.1 2004/10/17 23:00:08 dpolivaev Exp $*/

package freemind.modes;

import freemind.modes.MindMapNode;
import freemind.modes.MindMapLink;
import java.util.Vector;

/** Interface for the registry, which manages the ids of nodes and the existing links in a map.
    Thus, this interface is bound to a map model, because other maps have a different registry.*/
public interface MindMapLinkRegistry {
    ////////////////////////////////////////////////////////////////////////////////////////
    ////   State Model                                                                 /////
    ////////////////////////////////////////////////////////////////////////////////////////
    /** State parent interface.*/
    public interface ID_BasicState {
        /** Returns null for many states.*/
        public String getID();
        public String toString();
    };
    /** This state interface expresses the state that a node is blank (i.e. without an id, normal state).*/
    public interface ID_Blank extends ID_BasicState {};
    /** This state interface expresses the state that a node has an ID, but is abstract.*/
    public interface ID_UsedState extends ID_BasicState {
        public MindMapNode getTarget();
    };
    /** This state interface expresses the state that a node has an ID.*/
    public interface ID_Registered extends ID_UsedState {
    };
//     /** This state interface expresses the state that a node was recently cutted and waits to be inserted at another place.
//         After inserting the states changes to ID_Registered.
//     */
//     public interface ID_Pending extends ID_UsedState {
//     };

    /** The main method. Registeres a node with a new (or an existing) node-id. If the state of the id is pending,
     then it is set to registered again.
    */
    public ID_Registered registerLinkTarget(MindMapNode target);
    /** The second variant of the main method. The difference is that here an ID is proposed, but has not to be taken, though.
    */
    public ID_Registered registerLinkTarget(MindMapNode target, String proposedID);
    public void        deregisterLinkTarget(MindMapNode target)
        throws java.lang.IllegalArgumentException;
    public ID_BasicState getState(MindMapNode node);
    public String getLabel(MindMapNode target);
    /** This can be used, if the id has to be known, before a node can be labled. */
	public String generateUniqueID(String proposedID);
    /** Reverses the getLabel method: searches for a node with the id given as the argument.*/
    public MindMapNode getTargetForID(String ID);
    /** This can be used, if the id has to be known, before a link can be labled. */
	public String generateUniqueLinkID(String proposedID);
    /** Reverses the getUniqueID method: searches for a link with the id given as the argument.*/
    public MindMapLink getLinkForID(String ID);
//     /** Method to keep track of the targets associated to a target node. This method also sets the new id to the target. 
//         Moreover, it is not required that the target node is already registered. This will be done on the fly.*/
    /** Removes links to all nodes beginning from target with its children.*/
    public void        cutNode(MindMapNode target);
    /** Clears the set of recent cutted nodes.*/
    public void clearCuttedNodeBuffer();
    /** @return returns all links that have been cutted out recently.*/
    public Vector /* of MindMapLink s*/  getCuttedNode(String oldTargetID);
    public void   registerLink(MindMapLink link);
    public void deregisterLink(MindMapLink link);

    /** Returns a Vector of Nodes that point to the given node.*/
    public Vector /* of MindMapNode s */ getAllSources(MindMapNode target);
    /** @return returns all links from or to this node.*/
    public Vector /* of MindMapLink s */ getAllLinks(MindMapNode node);
    /** @return returns all links to this node.*/
    public Vector /* of MindMapLink s */ getAllLinksIntoMe(MindMapNode target);
    /** @return returns all links from this node.*/
    public Vector /* of MindMapLink s */ getAllLinksFromMe(MindMapNode source);


}
