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
package org.apache.pivot.wtk;

import org.apache.pivot.wtk.media.Image;

/**
 * Tab pane attribute listener interface.
 */
public interface TabPaneAttributeListener {
    /**
     * Tab pane attribute listener adapter.
     */
    public static class Adapter implements TabPaneAttributeListener {
        @Override
        public void labelChanged(TabPane tabPane, Component component, String previousLabel) {
        }

        @Override
        public void iconChanged(TabPane tabPane, Component component, Image previousIcon) {
        }

        @Override
        public void closeableChanged(TabPane tabPane, Component component) {
        }

        @Override
        public void tooltipTextChanged(TabPane tabPane, Component component,
            String previousTooltipText) {
        }
    }

    /**
     * Called when a tab's label attribute has changed.
     *
     * @param tabPane
     * @param component
     * @param previousLabel
     */
    public void labelChanged(TabPane tabPane, Component component, String previousLabel);

    /**
     * Called when a tab's icon attribute has changed.
     *
     * @param tabPane
     * @param component
     * @param previousIcon
     */
    public void iconChanged(TabPane tabPane, Component component, Image previousIcon);

    /**
     * Called when a tab's closeable attribute has changed.
     *
     * @param tabPane
     * @param component
     */
    public void closeableChanged(TabPane tabPane, Component component);

    /**
     * Called when a tab's tooltipText attribute has changed.
     *
     * @param tabPane
     * @param component
     */
    public void tooltipTextChanged(TabPane tabPane, Component component, String previousTooltipText);
}
