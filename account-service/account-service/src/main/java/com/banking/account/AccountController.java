package com.banking.account;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final Map<String, Account> accountDatabase = new LinkedHashMap<>();

    public AccountController() {
        accountDatabase.put("ACC1001", new Account("ACC1001", "Alice Johnson", "SAVINGS", 15420.50, "ACTIVE"));
        accountDatabase.put("ACC1002", new Account("ACC1002", "Bob Smith", "CHECKING", 8350.75, "ACTIVE"));
        accountDatabase.put("ACC1003", new Account("ACC1003", "Charlie Brown", "SAVINGS", 24100.00, "ACTIVE"));
    }

    @GetMapping(value = {"/internal/accounts", "/api/v1/internal/accounts"})
    public ResponseEntity<Map<String, Object>> getAllAccounts(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        logger.info("=================================================================");
        logger.info("[ACCOUNT-SERVICE] Received request at path: {}", requestUri);
        logger.info("[ACCOUNT-SERVICE] Handled by internal route on port 8081");
        logger.info("=================================================================");

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("service", "Account Service (Backend Target)");
        response.put("status", "SUCCESS");
        response.put("receivedPath", requestUri);
        response.put("forwardedPort", request.getLocalPort());
        response.put("message", "Backend successfully received request on internal route!");
        response.put("data", new ArrayList<>(accountDatabase.values()));

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = {"/internal/accounts/{accountNumber}", "/api/v1/internal/accounts/{accountNumber}"})
    public ResponseEntity<Map<String, Object>> getAccountById(
            @PathVariable("accountNumber") String accountNumber,
            HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        logger.info("=================================================================");
        logger.info("[ACCOUNT-SERVICE] Received request for account [{}] at path: {}", accountNumber, requestUri);
        logger.info("=================================================================");

        Account account = accountDatabase.get(accountNumber);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("service", "Account Service (Backend Target)");
        response.put("receivedPath", requestUri);

        if (account != null) {
            response.put("status", "SUCCESS");
            response.put("data", account);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "NOT_FOUND");
            response.put("message", "Account not found for number: " + accountNumber);
            return ResponseEntity.status(404).body(response);
        }
    }
}
