package com.prueba.pruebaTecnica;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Controlador para encontrar números perfectos en un rango específico.
 */
@RestController
public class PerfectNumberController {

    private static final int MIN_RANGE = 1;
    private static final int MAX_RANGE = 1000000;

    /**
     * Obtiene los números perfectos dentro del rango especificado.
     *
     * @param start Valor de inicio del rango.
     * @param end   Valor final del rango.
     * @return ResponseEntity con los números perfectos encontrados o mensajes de error.
     */
    @GetMapping("/perfect-numbers")
    public ResponseEntity<String> getPerfectNumbers(@RequestParam("start") int start, @RequestParam("end") int end) {
        if (start < MIN_RANGE || end < MIN_RANGE || start > end || end > MAX_RANGE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rango inválido. Por favor, ingrese un rango válido.");
        }
        List<Integer> perfectNumbers = findPerfectNumbersInRange(start, end);
        if (perfectNumbers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No se encontraron números perfectos en el rango especificado.");
        }
        return ResponseEntity.ok("Se encontraron los siguientes números perfectos: " + perfectNumbers.toString());
    }

    /**
     * Busca los números perfectos dentro del rango especificado.
     *
     * @param start Valor de inicio del rango.
     * @param end   Valor final del rango.
     * @return Lista de números perfectos encontrados.
     */
    private List<Integer> findPerfectNumbersInRange(int start, int end) {
        List<Integer> perfectNumbers = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = start; i <= end; i++) {
            final int number = i;
            executor.submit(() -> {
                if (isPerfect(number)) {
                    perfectNumbers.add(number);
                }
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Espera a que todas las tareas se completen
        }
        return perfectNumbers;
    }

    /**
     * Verifica si un número dado es perfecto.
     *
     * @param number Número a verificar.
     * @return true si el número es perfecto, false en caso contrario.
     */
    private boolean isPerfect(int number) {
        int sum = 0;
        for (int i = 1; i < number; i++) {
            if (number % i == 0) {
                sum += i;
            }
        }
        return sum == number;
    }
}
