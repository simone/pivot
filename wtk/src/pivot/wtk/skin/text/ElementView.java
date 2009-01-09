/*
 * Copyright (c) 2009 VMware, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pivot.wtk.skin.text;

import java.awt.Graphics2D;

import pivot.collections.ArrayList;
import pivot.collections.Sequence;
import pivot.wtk.Bounds;
import pivot.wtk.text.Element;
import pivot.wtk.text.ElementListener;
import pivot.wtk.text.Node;

/**
 * Abstract base class for element views.
 *
 * @author gbrown
 */
public abstract class ElementView extends NodeView
    implements ElementListener {
    ArrayList<NodeView> nodeViews = new ArrayList<NodeView>();
    private boolean valid = true;

    public NodeView get(int index) {
        return nodeViews.get(index);
    }

    public int getLength() {
        return nodeViews.getLength();
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void invalidate() {
        if (valid) {
            valid = false;
            super.invalidate();
        }
    }

    @Override
    public void validate() {
        if (!valid) {
            try {
                super.validate();

                for (int i = 0, n = nodeViews.getLength(); i < n; i++) {
                    NodeView nodeView = nodeViews.get(i);
                    nodeView.validate();
                }
            } finally {
                valid = true;
            }
        }
    }

    public void paint(Graphics2D graphics) {
        java.awt.Rectangle clipBounds = graphics.getClipBounds();
        Bounds paintBounds = (clipBounds == null) ?
            new Bounds(0, 0, getWidth(), getHeight()) : new Bounds(clipBounds);

        for (NodeView nodeView : nodeViews) {
            Bounds nodeViewBounds = nodeView.getBounds();

            // Only paint node views that intersect the current clip rectangle
            if (nodeViewBounds.intersects(paintBounds)) {
                // Create a copy of the current graphics context and
                // translate to the node view's coordinate system
                Graphics2D nodeViewGraphics = (Graphics2D)graphics.create();
                nodeViewGraphics.translate(nodeViewBounds.x, nodeViewBounds.y);
                nodeViewGraphics.clipRect(0, 0, nodeViewBounds.width, nodeViewBounds.height);

                // Paint the node view
                nodeView.paint(nodeViewGraphics);

                // Dispose of the node views's graphics
                nodeViewGraphics.dispose();
            }
        }
    }

    public void nodeInserted(Element element, int index) {
        // TODO
    }

    public void nodesRemoved(Element element, int index, Sequence<Node> nodes) {
        // TODO
    }
}
