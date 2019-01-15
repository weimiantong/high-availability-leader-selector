package com.wmt.framework.ha.leader;

public interface LeaderSelectorListener {
    void isLeader();

    void notLeader();
}
