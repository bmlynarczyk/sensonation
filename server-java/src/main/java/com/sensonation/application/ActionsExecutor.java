package com.sensonation.application;

import com.sensonation.domain.BlindEvent;

public interface ActionsExecutor {

    boolean shouldExecute(BlindEvent blindEvent);

    void execute(BlindEvent blindEvent);

}