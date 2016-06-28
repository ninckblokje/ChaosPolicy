package ninckblokje.oracle.wsm.policy.chaos.policy;

import oracle.wsm.common.sdk.IContext;
import oracle.wsm.common.sdk.IResult;
import oracle.wsm.common.sdk.WSMException;
import oracle.wsm.policy.model.IAssertion;
import oracle.wsm.policyengine.IExecutionContext;
import oracle.wsm.policyengine.impl.AssertionExecutor;

public class ChaosAssertionExecutor extends AssertionExecutor {
    
    public ChaosAssertionExecutor() {
        super();
    }

    @Override
    public IResult execute(IContext iContext) throws WSMException {
        // TODO Implement this method
        return null;
    }

    @Override
    public void init(IAssertion iAssertion, IExecutionContext iExecutionContext,
                     IContext iContext) throws WSMException {
        // TODO Implement this method

    }

    @Override
    public void destroy() {
        // TODO Implement this method
    }
}
