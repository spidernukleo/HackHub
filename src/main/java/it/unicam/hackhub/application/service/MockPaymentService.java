package it.unicam.hackhub.application.service;


import it.unicam.hackhub.domain.Team;
import org.springframework.stereotype.Service;

@Service
public class MockPaymentService {
    public void sendPrize(Team team, double amount){
        // Simulazione del pagamento verso un servizio esterno //FARE DESIGN PATTERN STRATEGY? BOH
        System.out.println("\n========================================");
        System.out.println("💳 [PAGAMENTO ESTERNO ESEGUITO]");
        System.out.println("🏆 Team Vincitore: " + team.getName() + " (ID: " + team.getId() + ")");
        System.out.println("💰 Importo Inviato: €" + amount);
        System.out.println("========================================\n");
    }
}
