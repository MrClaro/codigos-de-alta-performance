package app;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Desafio1 {
  public static void main(String[] args) {
    // imagine que o líder da sua equipe de trabalho lhe solicitou um método que
    // fizesse algo bem específico:
    // um programa em Java que receba do usuário um número com 3 algarismos (123,
    // 435 ou 786, etc.) e imprima esse número ao contrário (321, 534 ou 687,
    // e assim por diante). Contudo, você só pode usar operações aritméticas (+, -,
    // *, /, %).

    try (Scanner scanner = new Scanner(System.in)) {
      while (true) {
        System.out.println("Digite um número de 3 algarismos (ou 0 para sair):");
        int numeroRecebido;

        try {
          numeroRecebido = scanner.nextInt();
        } catch (InputMismatchException e) {
          System.out.println("Entrada inválida. Por favor, digite apenas números inteiros.");
          scanner.next();
          continue;
        }

        if (numeroRecebido == 0) {
          System.out.println("Programa encerrado.");
          break;
        }

        if (numeroRecebido < 100 || numeroRecebido > 999) {
          System.out.println("O número deve ter exatamente 3 algarismos. Por favor, tente novamente.");
          continue;
        }

        int ultimoDigito = numeroRecebido % 10;
        int numeroParcial = numeroRecebido / 10;
        int segundoDigito = numeroParcial % 10;
        int primeiroDigito = numeroParcial / 10;

        int numeroInvertido = (ultimoDigito * 100) + (segundoDigito * 10) + (primeiroDigito);

        System.out.println("O número invertido é: " + numeroInvertido);
      }
    }
  }
}
