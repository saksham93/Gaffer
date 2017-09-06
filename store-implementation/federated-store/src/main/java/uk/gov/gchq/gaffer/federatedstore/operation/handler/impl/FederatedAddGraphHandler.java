/*
 * Copyright 2017 Crown Copyright
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

package uk.gov.gchq.gaffer.federatedstore.operation.handler.impl;

import uk.gov.gchq.gaffer.federatedstore.FederatedStore;
import uk.gov.gchq.gaffer.federatedstore.operation.AddGraph;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.operation.export.graph.handler.CreateGraphDelegate;
import uk.gov.gchq.gaffer.store.Context;
import uk.gov.gchq.gaffer.store.Store;
import uk.gov.gchq.gaffer.store.operation.handler.OperationHandler;

import java.util.Set;

/**
 * A handler for AddGraph operation for the FederatedStore.
 * To load a graph into the FederatedStore you need to provide three things.
 * <ul>
 * <li>GraphID
 * <li>Graph Schema
 * <li>Graph Properties file
 * </ul>
 *
 * @see OperationHandler
 * @see FederatedStore
 * @see AddGraph
 * @see CreateGraphDelegate
 */
public class FederatedAddGraphHandler implements OperationHandler<AddGraph> {
    @Override
    public Void doOperation(final AddGraph operation, final Context context, final Store store) throws OperationException {
        final Set<String> graphAuths = operation.getGraphAuths();
        FederatedAccessHook hook = new FederatedAccessHook();
        hook.setGraphAuths(graphAuths);
        if (graphAuths == null) {
            hook.setAddingUserId(context.getUser().getUserId());
        }

        final Graph graph = CreateGraphDelegate.createGraph(store, operation.getGraphId(),
                operation.getSchema(), operation.getStoreProperties(),
                operation.getParentSchemaIds(), operation.getParentPropertiesId(), hook);

        ((FederatedStore) store).addGraphs(graph);
        return null;
    }
}
