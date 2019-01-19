package com.wmt.framework.ha.leader;

import org.junit.Test;

public class LeaderSelectorTest {

    @Test
    public void demo_show() {
        //实例化ZkConfig
        ZkConfig zkConfig = new ZkConfig("28.163.0.65:2181", 5000, "/worker/ha/master/test_id");

        //实例化LeaderSelector
        LeaderSelector leaderSelector = new LeaderSelector(zkConfig);
        leaderSelector.setListener(new LeaderSelectorListener() {
            @Override
            public void isLeader() {
                //is leader.
                System.out.println("ls1 is leader.");
            }

            @Override
            public void notLeader() {
                //not leader.
                System.out.println("ls1 is not leader.");
            }
        });

        //启动selector
        leaderSelector.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        LeaderSelector ls2 = new LeaderSelector(zkConfig);
        ls2.setListener(new LeaderSelectorListener() {
            @Override
            public void isLeader() {
                System.out.println("ls2 is leader.");
            }

            @Override
            public void notLeader() {
                System.out.println("ls2 is not leader.");
            }
        });

        ls2.start();

        int i = 0;
        while (i < 10) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i == 4) {
                leaderSelector.stop();
            }
            i++;
        }
    }

}