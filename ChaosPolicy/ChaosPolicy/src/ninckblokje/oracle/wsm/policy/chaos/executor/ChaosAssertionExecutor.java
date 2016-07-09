package ninckblokje.oracle.wsm.policy.chaos.executor;
/*
    Copyright (c) 2016, ninckblokje
    All rights reserved.
    
    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:
    
    * Redistributions of source code must retain the above copyright notice, this
      list of conditions and the following disclaimer.
    
    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
    DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
    FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
    DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
    SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
    CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
    OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
    OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import java.util.logging.Level;

import oracle.adf.share.logging.ADFLogger;

import oracle.wsm.common.sdk.IContext;
import oracle.wsm.common.sdk.IResult;
import oracle.wsm.common.sdk.Result;
import oracle.wsm.common.sdk.WSMException;
import oracle.wsm.policy.model.IAssertion;
import oracle.wsm.policy.model.IAssertionBindings;
import oracle.wsm.policy.model.impl.SimpleAssertion;
import oracle.wsm.policyengine.IExecutionContext;
import oracle.wsm.policyengine.impl.AssertionExecutor;

public class ChaosAssertionExecutor extends AssertionExecutor {
    
    private static final ADFLogger logger = ADFLogger.createADFLogger(ChaosAssertionExecutor.class);
    
    private static final String CHANGE_KEY = "CHAOS_POLICY_CHANGE";

    @Override
    public IResult execute(IContext iContext) throws WSMException {
        IResult result = new Result();
        
        IAssertionBindings bindings = ((SimpleAssertion)(this.assertion)).getBindings();
        int change = getChange(bindings);
        
        if (induceChaos(change)) {
            String randomMessage = UUID.randomUUID().toString();
            
            result.setStatus(IResult.FAILED);
            result.setFault(new WSMException(randomMessage));
        } else {
            result.setStatus(IResult.SUCCEEDED);
        }
        
        return result;
    }

    @Override
    public void init(IAssertion iAssertion, IExecutionContext iExecutionContext,
                     IContext iContext) throws WSMException {
        // nothing todo
    }

    @Override
    public void destroy() {
        // nothing todo
    }
    
    int getChange(IAssertionBindings bindings) {
        String change = Optional
                            .ofNullable(System.getenv(CHANGE_KEY))
                            .ofNullable(bindings
                                            .getConfigs()
                                            .get(0)
                                            .getPropertySets()
                                            .get(0)
                                            .getPropertyByName(CHANGE_KEY)
                                            .getValue())
                            .orElse("8");
        
        logger.log(Level.CONFIG, "Change is configured to [1/{0}]", change);
        return Integer.parseInt(change);
    }
    
    boolean induceChaos(int change) {
        Random generator = new Random();
        return (change-1 == generator.nextInt(change));
    }
}
