/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.example.skip.version.demo.cms.workflow.action;

import java.rmi.RemoteException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.hippoecm.repository.api.Document;
import org.hippoecm.repository.api.WorkflowException;
import org.onehippo.repository.documentworkflow.action.VersionVariantAction;
import org.onehippo.repository.documentworkflow.task.VersionVariantTask;

/**
 * Custom <code>hippo:version</code> SCXML action by extending the default implementation, {@link VersionVariantAction}.
 * <P>
 * This custom implementation checks if the target unpublished node is the custom mixin type,
 * <code>skipversiondemo:skipversioning</code>, and in that case, just skip the invocation not to create a new version
 * out of it.
 * </P>
 * <P>
 * To apply this custom SCXML action, you should replace <code>/hippo:configuration/hippo:modules/scxmlregistry/hippo:moduleconfig/hipposcxml:definitions/documentworkflow/version/@hipposcxml:classname</code>
 * property by the FQCN of this class.
 * </P>
 */
@SuppressWarnings("serial")
public class SkippableVersionVariantAction extends VersionVariantAction {

    /**
     * Custom mixin node type to indicate if the unpublished variant node should skip versioning during the document workflow
     * process.
     */
    private static final String NT_SKIPVERSIONING = "skipversiondemo:skipversioning";

    @Override
    protected VersionVariantTask createWorkflowTask() {
        return new VersionVariantTask() {
            @Override
            public Object doExecute() throws WorkflowException, RepositoryException, RemoteException {
                if (getVariant() != null && getVariant().hasNode()) {
                    final Session workflowSession = getWorkflowContext().getInternalWorkflowSession();
                    Node targetNode = getVariant().getNode(workflowSession);

                    if (targetNode.isNodeType(NT_SKIPVERSIONING)) {
                        // If it should skip versioning, just return a Document without proceeding.
                        return new Document(targetNode);
                    }
                }

                return super.doExecute();
            }
        };
    }

}
