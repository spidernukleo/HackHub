package it.unicam.hackhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HackhubApplication {
	public static void main(String[] args) {
		System.out.println("Call schedulata con successo per l'hackathon "+1+"\nMentor: "+2+"\nTeam: "+3+"\nLink: "+ 4);
		SpringApplication.run(HackhubApplication.class, args);
	}

}
