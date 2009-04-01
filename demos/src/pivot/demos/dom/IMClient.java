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
package pivot.demos.dom;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import pivot.collections.Dictionary;
import pivot.util.concurrent.Task;
import pivot.util.concurrent.TaskExecutionException;
import pivot.util.concurrent.TaskListener;
import pivot.wtk.Application;
import pivot.wtk.ApplicationContext;
import pivot.wtk.BrowserApplicationContext;
import pivot.wtk.Button;
import pivot.wtk.ButtonPressListener;
import pivot.wtk.CardPane;
import pivot.wtk.Component;
import pivot.wtk.ComponentKeyListener;
import pivot.wtk.Display;
import pivot.wtk.Form;
import pivot.wtk.Keyboard;
import pivot.wtk.Label;
import pivot.wtk.PushButton;
import pivot.wtk.TextInput;
import pivot.wtk.Window;
import pivot.wtk.effects.FadeTransition;
import pivot.wtk.effects.Transition;
import pivot.wtk.effects.TransitionListener;
import pivot.wtkx.WTKXSerializer;

public class IMClient implements Application {
    /**
     * Task for asynchronously logging into Jabber.
     *
     * @author gbrown
     */
    private class LoginTask extends Task<Void> {
        public Void execute() throws TaskExecutionException {
            try {
                String domain = domainTextInput.getText();

                ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(domain);
                xmppConnection = new XMPPConnection(connectionConfiguration);

                String username = usernameTextInput.getText();
                String password = passwordTextInput.getText();
                xmppConnection.connect();
                xmppConnection.login(username, password);
            } catch(XMPPException exception) {
                throw new TaskExecutionException(exception);
            }

            return null;
        }
    }

    private XMPPConnection xmppConnection = null;

    private Window window = null;
    private CardPane cardPane = null;
    private Form loginForm = null;

    private TextInput usernameTextInput;
    private TextInput passwordTextInput;
    private TextInput domainTextInput;

    private PushButton loginButton = null;
    private Label errorMessageLabel = null;

    private Label messageLabel = null;

    private ApplicationContext.ScheduledCallback scheduledFadeCallback = null;

    public void startup(Display display, Dictionary<String, String> properties)
        throws Exception {
        WTKXSerializer wtkxSerializer = new WTKXSerializer();
        window = new Window((Component)wtkxSerializer.readObject(getClass().getResource("im_client.wtkx")));

        cardPane = (CardPane)wtkxSerializer.getObjectByName("cardPane");
        loginForm = (Form)wtkxSerializer.getObjectByName("loginForm");

        loginForm.getComponentKeyListeners().add(new ComponentKeyListener() {
            public boolean keyTyped(Component component, char character) {
                return false;
            }

            public boolean keyPressed(Component component, int keyCode, Keyboard.KeyLocation keyLocation) {
                if (keyCode == Keyboard.KeyCode.ENTER) {
                    login();
                }

                return false;
            }

            public boolean keyReleased(Component component, int keyCode, Keyboard.KeyLocation keyLocation) {
                return false;
            }
        });

        usernameTextInput = (TextInput)wtkxSerializer.getObjectByName("usernameTextInput");
        passwordTextInput = (TextInput)wtkxSerializer.getObjectByName("passwordTextInput");
        domainTextInput = (TextInput)wtkxSerializer.getObjectByName("domainTextInput");

        loginButton = (PushButton)wtkxSerializer.getObjectByName("loginButton");
        loginButton.getButtonPressListeners().add(new ButtonPressListener() {
            public void buttonPressed(final Button button) {
                login();
            }
        });

        errorMessageLabel = (Label)wtkxSerializer.getObjectByName("errorMessageLabel");

        messageLabel = (Label)wtkxSerializer.getObjectByName("messageLabel");

        window.setMaximized(true);
        window.open(display);
    }

    public boolean shutdown(boolean optional) throws Exception {
        return false;
    }

    public void suspend() {
        // No-op
    }

    public void resume() {
        // No-op
    }

    private void login() {
        if (usernameTextInput.getText().length() == 0) {
            errorMessageLabel.setText("Username is required.");
        } else if (passwordTextInput.getText().length() == 0) {
            errorMessageLabel.setText("Password is required.");
        } else if (domainTextInput.getText().length() == 0) {
            errorMessageLabel.setText("Domain is required.");
        } else {
            LoginTask loginTask = new LoginTask();
            loginTask.execute(new TaskListener<Void>() {
                public void taskExecuted(Task<Void> task) {
                    loginButton.setEnabled(true);
                    cardPane.setSelectedIndex(1);
                    listenForMessages();
                }

                public void executeFailed(Task<Void> task) {
                    loginButton.setEnabled(true);
                    errorMessageLabel.setText(task.getFault().getMessage());
                }
            });

            errorMessageLabel.setText(null);
            loginButton.setEnabled(false);
        }
    }

    private void listenForMessages() {
        PacketFilter filter = new PacketTypeFilter(Message.class);

        PacketListener packetListener = new PacketListener() {
            public void processPacket(Packet packet) {
                final Message message = (Message)packet;

                ApplicationContext.queueCallback(new Runnable() {
                    public void run() {
                        // Show the message text
                        String body = message.getBody();
                        messageLabel.setText(body);

                        // Notify the page that a message was received
                        BrowserApplicationContext.eval("messageReceived(\"" + body + "\");", IMClient.this);

                        // Cancel any pending fade and schedule a new fade callback
                        if (scheduledFadeCallback != null) {
                            scheduledFadeCallback.cancel();
                        }

                        scheduledFadeCallback = ApplicationContext.scheduleCallback(new Runnable() {
                            public void run() {
                                FadeTransition fadeTransition = new FadeTransition(messageLabel, 500, 30);

                                fadeTransition.start(new TransitionListener() {
                                    public void transitionCompleted(Transition transition) {
                                        messageLabel.setText(null);
                                    }
                                });
                            }
                        }, 2500);
                    }
                });
            }
        };

        xmppConnection.addPacketListener(packetListener, filter);
    }
}
