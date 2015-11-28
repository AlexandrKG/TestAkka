package com.kademika.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Launcher {

    static ActorSystem system = ActorSystem.create("TestActorSystem");
    final ActorRef testActor = system.actorOf(Props.create(TestUntypedActor.class), "testActor");

    public static void main(String[] args) {
        System.out.println("START TASK");
        Launcher launcher = new Launcher();
        launcher.read("src/main/resources/Debt.txt");
        try {
            Thread.sleep(5000);
            system.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("STOP TASK");
    }

    public void read(String fileName) {
        String str;
        try (FileInputStream fis = new FileInputStream(fileName);
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(reader);) {
            while ((str = bufferedReader.readLine()) != null) {
                testActor.tell(str, ActorRef.noSender());
            }
            str = "end";
            testActor.tell(str, ActorRef.noSender());
            testActor.tell(PoisonPill.getInstance(), ActorRef.noSender());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
