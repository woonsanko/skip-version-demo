package org.example.skip.version.demo.cms.workflow.action;

import java.rmi.RemoteException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.hippoecm.repository.api.WorkflowException;
import org.onehippo.repository.documentworkflow.action.VersionVariantAction;
import org.onehippo.repository.documentworkflow.task.VersionVariantTask;

@SuppressWarnings("serial")
public class SkippableVersionVariantAction extends VersionVariantAction {

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
                        return null;
                    }
                }

                return super.doExecute();
            }
        };
    }

}
