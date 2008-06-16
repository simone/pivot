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

import pivot.wtk.Component;
import pivot.wtk.Container;

abstract class ContainerLoader extends Loader {
    public static final String CONTEXT_KEY_ATTRIBUTE = "contextKey";

    protected abstract Container createContainer();

    @Override
    protected Component load(Element element, ComponentLoader rootLoader)
        throws LoadException {
        Container container = createContainer();

        if (element.hasAttribute(CONTEXT_KEY_ATTRIBUTE)) {
            String contextKey = element.getAttribute(CONTEXT_KEY_ATTRIBUTE);
            container.setContextKey(contextKey);
        }

        return container;
    }
}
