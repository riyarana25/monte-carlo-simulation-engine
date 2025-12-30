package com.riya.mcengine.experiment;

import com.riya.mcengine.core.State;

public class BernoulliState implements State {

    private final boolean success;

    public BernoulliState(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
