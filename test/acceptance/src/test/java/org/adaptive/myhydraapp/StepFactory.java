package org.adaptive.myhydraapp;

import com.weareadaptive.hydra.cucumber.TestMode;
import com.weareadaptive.hydra.cucumber.steps.HydraPlatformStepFactory;

public class StepFactory extends HydraPlatformStepFactory
{

    @Override
    public void onStart(final TestMode testMode) {
        switch (testMode)
        {
            case EMBEDDED_DIRECT:
                addClass(EmbeddedDirectDeployment.class);
                break;
            case EMBEDDED_REAL:
                addClass(EmbeddedRealDeployment.class);
                break;
            case EXISTING:
                addClass(ExistingDeployment.class);
                break;
        }
    }

}
