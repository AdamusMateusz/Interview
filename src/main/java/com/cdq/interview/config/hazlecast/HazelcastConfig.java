package com.cdq.interview.config.hazlecast;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class HazelcastConfig {


    @Value("${cdq.hazelcast.own}")
    private String ownAddress;

    @Value("${cdq.hazelcast.others}")
    private String[] others;

    @Bean
    public HazelcastInstance hazelcastInstance() {

        Config config = new Config();
        config.setClusterName("cdq-interview-adamus");
        config.getNetworkConfig().setPublicAddress(ownAddress);

        JoinConfig join = config.getNetworkConfig().getJoin();
        join.getAwsConfig().setEnabled(false);
        join.getMulticastConfig().setEnabled(false);

        join.getTcpIpConfig().setEnabled(true);
        for (String otherMemberAddress : others) {
            join.getTcpIpConfig().addMember(otherMemberAddress);
        }

        return Hazelcast.newHazelcastInstance(config);
    }


}
