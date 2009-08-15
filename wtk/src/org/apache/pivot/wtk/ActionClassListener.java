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

/**
 * Action class listener interface.
 *
 */
public interface ActionClassListener {
    /**
     * Action class listener adapter.
     *
     */
    public class Adapter implements ActionClassListener {
        public void actionAdded(String id) {
        }

        public void actionUpdated(String id, Action previousAction) {
        }

        public void actionRemoved(String id, Action action) {
        }
    }

    /**
     * Called when an action has been added to the named action dictionary.
     *
     * @param id
     */
    public void actionAdded(String id);

    /**
     * Called when an action has been updated in the named action dictionary.
     *
     * @param id
     * @param previousAction
     */
    public void actionUpdated(String id, Action previousAction);

    /**
     * Called when an action has been removed from the named action dictionary.
     *
     * @param id
     * @param action
     */
    public void actionRemoved(String id, Action action);
}
