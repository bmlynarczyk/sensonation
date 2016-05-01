package com.sensonation.domain;

public enum State {

    READY_TO_GO {
        @Override
        public boolean readyToGo() {
            return true;
        }

        @Override
        public boolean stopOnly() {
            return false;
        }
    }, STOP_ONLY {
        @Override
        public boolean readyToGo() {
            return false;
        }

        @Override
        public boolean stopOnly() {
            return true;
        }
    };

    public abstract boolean readyToGo();

    public abstract boolean stopOnly();
}
