package com.mycompany.hazelcastdemo;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class HazelcastExample {

    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("dev");
        // Docker container IP adresin veya localhost olabilir:
        clientConfig.getNetworkConfig().addAddress("localhost:5701");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        IMap<Integer, Person> map = client.getMap("persons");

        // 10.000 Person objesi ekle
        for (int i = 0; i < 10000; i++) {
            map.put(i, new Person("Person " + i));
        }

        // İlk 10 Person objesini çek ve yazdır
        for (int i = 0; i < 10; i++) {
            Person p = map.get(i);
            System.out.println(p);
        }

        client.shutdown();
    }
}
