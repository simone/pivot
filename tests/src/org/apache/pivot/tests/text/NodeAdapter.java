/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pivot.tests.text;

import org.apache.pivot.wtk.text.Element;
import org.apache.pivot.wtk.text.Node;
import org.apache.pivot.wtk.text.NodeListener;

public abstract class NodeAdapter {
    private Node node;
    private ElementAdapter parent = null;

    private NodeListener nodeListener = new NodeListener() {
        @Override
        public void offsetChanged(Node node, int previousOffset) {
        }

        @Override
        public void parentChanged(Node node, Element previousParent) {
        }

        @Override
        public void rangeInserted(Node node, int offset, int span) {
        }

        @Override
        public void rangeRemoved(Node node, int offset, int span) {
        }
    };

    public NodeAdapter(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public ElementAdapter getParent() {
        return parent;
    }

    protected void setParent(ElementAdapter parent) {
        if (parent == null) {
            node.getNodeListeners().remove(nodeListener);
        } else {
            node.getNodeListeners().add(nodeListener);
        }

        this.parent = parent;
    }

    public abstract String getText();
}
