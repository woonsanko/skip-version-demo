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
