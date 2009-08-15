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

import org.apache.pivot.util.ListenerList;

/**
 * Component representing an activity indicator.
 *
 */
public class ActivityIndicator extends Component {
    private static class ActivityIndicatorListenerList extends ListenerList<ActivityIndicatorListener>
        implements ActivityIndicatorListener {
        public void activeChanged(ActivityIndicator activityIndicator) {
            for (ActivityIndicatorListener listener : this) {
                listener.activeChanged(activityIndicator);
            }
        }
    }

    private boolean active;

    private ActivityIndicatorListenerList activityIndicatorListeners = new ActivityIndicatorListenerList();

    public ActivityIndicator() {
        installSkin(ActivityIndicator.class);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        if (this.active != active) {
            this.active = active;
            activityIndicatorListeners.activeChanged(this);
        }
    }

    public ListenerList<ActivityIndicatorListener> getActivityIndicatorListeners() {
        return activityIndicatorListeners;
    }
}
