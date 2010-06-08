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
package org.apache.pivot.tutorials.webqueries.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;

import org.apache.pivot.collections.HashMap;
import org.apache.pivot.collections.List;
import org.apache.pivot.json.JSON;
import org.apache.pivot.json.JSONSerializer;
import org.apache.pivot.serialization.CSVSerializer;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.serialization.Serializer;
import org.apache.pivot.web.Query;
import org.apache.pivot.web.QueryException;
import org.apache.pivot.web.server.QueryServlet;

public class ExpenseServlet extends QueryServlet {
    private static final long serialVersionUID = 0;

    private List<Expense> expenses = null;
    private HashMap<Integer, Expense> expenseMap = new HashMap<Integer, Expense>();

    @Override
    @SuppressWarnings("unchecked")
    public void init() throws ServletException {
        CSVSerializer csvSerializer = new CSVSerializer();
        csvSerializer.getKeys().add("date");
        csvSerializer.getKeys().add("type");
        csvSerializer.getKeys().add("amount");
        csvSerializer.getKeys().add("description");
        csvSerializer.setItemClass(Expense.class);

        // Load the initial expense data
        InputStream inputStream = ExpenseServlet.class.getResourceAsStream("expenses.csv");

        try {
            expenses = (List<Expense>)csvSerializer.readObject(inputStream);
        } catch (IOException exception) {
            throw new ServletException(exception);
        } catch (SerializationException exception) {
            throw new ServletException(exception);
        }

        // Index the initial expenses
        for (Expense expense : expenses) {
            expenseMap.put(expense.getID(), expense);
        }
    }

    @Override
    protected Object doGet(Path path) throws QueryException {
        if (path.getLength() == 0) {
            throw new QueryException(Query.Status.BAD_REQUEST);
        }

        int id = Integer.parseInt(path.get(0));

        Object value = expenseMap.get(id);
        if (value == null) {
            throw new QueryException(Query.Status.NOT_FOUND);
        }

        return value;
    }

    @Override
    protected URL doPost(Path path, Object value) throws QueryException {
        if (path.getLength() > 0
            || value == null) {
            throw new QueryException(Query.Status.BAD_REQUEST);
        }

        Expense expense = JSON.bind(value, Expense.class);

        expenses.add(expense);
        expenseMap.put(expense.getID(), expense);

        URL location = getLocation();

        try {
            location = new URL(location, Integer.toString(expense.getID()));
        } catch (MalformedURLException exception) {
            throw new QueryException(Query.Status.INTERNAL_SERVER_ERROR);
        }

        return location;
    }

    @Override
    protected boolean doPut(Path path, Object value) throws QueryException {
        if (path.getLength() == 0
            || value == null) {
            throw new QueryException(Query.Status.BAD_REQUEST);
        }

        // TODO IMPORTANT This method will create IDs that are never used; don't auto-assign
        // IDs in constructor

        // TODO Update list and map

        return false;
    }

    @Override
    protected void doDelete(Path path) throws QueryException {
        if (path.getLength() == 0) {
            throw new QueryException(Query.Status.BAD_REQUEST);
        }

        // TODO
    }
    @Override
    protected Serializer<?> createSerializer(Path path) throws QueryException {
        return new JSONSerializer();
    }
}