package States;

/**
 * SchedulerState contains the scheduler state and the function which is used to update the state of the scheduler
 */
public class SchedulerState{

    private schedulerStates currState;  // The state

    public enum schedulerStates {
        ReceiveRequest, RequestSent, WaitingState
    }

    /**
     * updateState updates the currState depend on the currState
     *
     * @return
     */
    public static schedulerStates updateState(boolean isReceive, boolean isSend){
        if (isReceive) {
            return schedulerStates.ReceiveRequest;
        } else if (isSend) {
            return schedulerStates.RequestSent;
        }else {
            return schedulerStates.WaitingState;
        }
    }

}

