package mimikko.zazalng.pudel.contracts.Command;

import mimikko.zazalng.pudel.contracts.SessionState;

public interface BaseCommandState extends SessionState {
    String INIT = "INIT";
    String END = "END";
    String AWAIT_INPUT = "AWAIT_INPUT";

    // Default implementations for INIT and END
    default boolean isInit() {
        return this.getName().equals(INIT);
    }

    default boolean isEnd() {
        return this.getName().equals(END);
    }

    default boolean isAwait(){
        return this.getName().equals(AWAIT_INPUT);
    }
}
