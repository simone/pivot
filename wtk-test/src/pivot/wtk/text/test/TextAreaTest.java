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
package pivot.wtk.text.test;

import java.io.InputStream;

import pivot.collections.Dictionary;
import pivot.wtk.Application;
import pivot.wtk.Border;
import pivot.wtk.Display;
import pivot.wtk.FlowPane;
import pivot.wtk.Frame;
import pivot.wtk.HorizontalAlignment;
import pivot.wtk.ScrollPane;
import pivot.wtk.TextArea;
import pivot.wtk.text.Document;
import pivot.wtk.text.PlainTextSerializer;

public class TextAreaTest implements Application {
    private Frame frame = null;

    public void startup(Display display, Dictionary<String, String> properties)
        throws Exception {
        PlainTextSerializer serializer = new PlainTextSerializer("UTF-8");
        InputStream inputStream = PlainTextSerializerTest.class.getResourceAsStream("pivot.txt");

        Document text = null;
        try {
            text = (Document)serializer.readObject(inputStream);
        } catch(Exception exception) {
            System.out.println(exception);
        }

        TextArea textArea = new TextArea();
        textArea.setPreferredWidth(120);
        textArea.setText(text);

        /*
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPane.ScrollBarPolicy.FILL);
        scrollPane.setView(textArea);
        */

        FlowPane flowPane = new FlowPane();
        flowPane.getStyles().put("horizontalAlignment", HorizontalAlignment.JUSTIFY);
        flowPane.add(textArea);

        Border border = new Border();
        border.getStyles().put("padding", 0);
        border.setContent(flowPane);

        frame = new Frame(border);
        frame.setTitle("Test");
        frame.setPreferredSize(320, 240);
        frame.open(display);
    }

    public boolean shutdown(boolean optional) {
        frame.close();
        return true;
    }

    public void suspend() {
    }

    public void resume() {
    }
}
