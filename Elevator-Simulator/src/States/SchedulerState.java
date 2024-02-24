package States;

/**
 * SchedulerState contains the scheduler state and the function which is used to update the state of the scheduler
 */
public class SchedulerState{

    private schedulerStates currState;  // The state

    public enum schedulerStates {
        NoRequest, HasRequest, RequestSent, RequestFinish
    }

    public SchedulerState(){
        this.currState = schedulerStates.NoRequest;
    }

    /**
     * updateState updates the currState depend on the currState
     */
    public void updateState(){
        switch (currState){
            case NoRequest -> currState = schedulerStates.HasRequest;
            case HasRequest -> currState = schedulerStates.RequestSent;
            case RequestSent -> currState = schedulerStates.RequestFinish;
        }
    }

    /**
     * getCurrState returns the currState. Only used in SchedulerStateTest
     * @return
     */
    public schedulerStates getCurrState(){
        return currState;
    }

    /**
     * toString returns the current state in String
     * @return
     */
    public String toString(){
        return "Current Scheduler state is " + currState.toString();
    }
}

