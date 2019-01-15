package com.wmt.framework.ha.leader;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.state.SessionConnectionStateErrorPolicy;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

@Slf4j
public class LeaderSelector {
    @Setter
    private LeaderSelectorListener listener;

    private final ZkConfig zkConfig;
    private CuratorFramework client;
    private LeaderLatch latch;



    public LeaderSelector(ZkConfig zkConfig) {
        this.zkConfig = zkConfig;
    }

    public void start() {
        client = CuratorFrameworkFactory.builder()
                .connectString(zkConfig.getConnectString())
                .connectionTimeoutMs(zkConfig.getConnectionTimeout())
                .sessionTimeoutMs(zkConfig.getSessionTimeout())
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectionStateErrorPolicy(new SessionConnectionStateErrorPolicy())
                .build();

        latch = new LeaderLatch(client, zkConfig.getZnodeName());

        try {
            client.start();
            latch.addListener(new LeaderLatchListener() {
                @Override
                public void isLeader() {
                    listener.isLeader();
                }

                @Override
                public void notLeader() {
                    listener.notLeader();
                }
            });
            latch.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        CloseableUtils.closeQuietly(latch);
        CloseableUtils.closeQuietly(client);
    }
}
