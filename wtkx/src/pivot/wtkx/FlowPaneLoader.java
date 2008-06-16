/*
 * Copyright (c) 2008 VMware, Inc.
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
package pivot.wtkx;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pivot.wtk.Component;
import pivot.wtk.Container;
import pivot.wtk.FlowPane;
import pivot.wtk.Orientation;

class FlowPaneLoader extends ContainerLoader {
    public static final String FLOW_PANE_TAG = "FlowPane";
    public static final String ORIENTATION_ATTRIBUTE = "orientation";

    protected Container createContainer() {
        return new FlowPane();
    }

    protected Component load(Element element, ComponentLoader rootLoader)
        throws LoadException {
        FlowPane flowPane = (FlowPane)super.load(element, rootLoader);

        if (element.hasAttribute(ORIENTATION_ATTRIBUTE)) {
            String orientationAttribute = element.getAttribute(ORIENTATION_ATTRIBUTE);
            Orientation orientation = Orientation.decode(orientationAttribute);
            flowPane.setOrientation(orientation);
        }

        NodeList childNodes = element.getChildNodes();
        int n = childNodes.getLength();

        if (n > 0) {
            ComponentLoader componentLoader = new ComponentLoader();

            for (int i = 0; i < n; i++) {
                Node childNode = childNodes.item(i);

                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    Component component = componentLoader.load((Element)childNode, rootLoader);
                    flowPane.getComponents().add(component);
                }
            }
        }

        return flowPane;
    }
}
