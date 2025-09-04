package app;

import java.util.Scanner;

public class Desafio2 {
  // Odeio esse jogo.
  // TODO: Pular da ponte semana que vem

  // PASSO A PASSO:
  // 1- Analizar todas possíveis condições de vitória(8 no total)
  // 2- Simular mentalmente todas as jogadas possíveis da IA
  // 3- Manter controle de quem é a vez, placar, múltiplas partidas
  // 4- Validar entrada do usuário constantemente
  // 5- Coordenar todas essas partes trabalhando juntas

  private char[][] tabuleiro;
  private char jogadorAtual;
  private boolean contraComputador;
  private Scanner scanner;
  private int vitoriasHumano;
  private int vitoriasComputador;
  private int empates;

  // Tutorial de Como ir contra todos Principios do Clean Code
  public Desafio2() {
    tabuleiro = new char[3][3];
    scanner = new Scanner(System.in);
    vitoriasHumano = 0;
    vitoriasComputador = 0;
    empates = 0;
  }

  public void iniciarJogo() {
    // Vo apagar essa velha na paulada
    System.out.println("=== JOGO DA VELHA ===");
    System.out.println("Escolha o modo de jogo:");
    System.out.println("1 - Humano vs Humano");
    System.out.println("2 - Humano vs Computador");
    System.out.print("Digite sua opção (1 ou 2): ");

    int opcao = scanner.nextInt();
    contraComputador = (opcao == 2);

    do {
      jogarPartida();
      exibirPlacar();
    } while (perguntarNovaPartida());

    exibirPlacarFinal();
    System.out.println("Obrigado por jogar!");
  }

  private void jogarPartida() {
    inicializarTabuleiro();
    jogadorAtual = 'X';
    boolean jogoTerminado = false;

    System.out.println("\n=== NOVA PARTIDA ===");
    if (contraComputador) {
      System.out.println("Você é 'X' e o computador é 'O'");
    } else {
      System.out.println("Jogador 1 é 'X' e Jogador 2 é 'O'");
    }

    exibirTabuleiro();

    while (!jogoTerminado) {
      if (jogadorAtual == 'X' || !contraComputador) {
        String nomeJogador = contraComputador ? "Você" : (jogadorAtual == 'X' ? "Jogador 1" : "Jogador 2");
        System.out.println("\nVez de " + nomeJogador + " (" + jogadorAtual + ")");
        fazerJogadaHumana();
      } else {
        System.out.println("\nVez do Computador (O)");
        fazerJogadaComputador();
        System.out.println("Computador jogou!");
      }

      exibirTabuleiro();

      if (verificarVitoria()) {
        String vencedor;
        if (contraComputador) {
          vencedor = (jogadorAtual == 'X') ? "Você venceu!" : "Computador venceu!";
          if (jogadorAtual == 'X') {
            vitoriasHumano++;
          } else {
            vitoriasComputador++;
          }
        } else {
          vencedor = "Jogador " + (jogadorAtual == 'X' ? "1" : "2") + " venceu!";
          vitoriasHumano++;
        }
        System.out.println("\n" + vencedor);
        jogoTerminado = true;
      } else if (tabuleiroCompleto()) {
        System.out.println("\nEmpate!");
        empates++;
        jogoTerminado = true;
      } else {
        // Alternar jogador
        jogadorAtual = (jogadorAtual == 'X') ? 'O' : 'X';
      }
    }
  }

  private void inicializarTabuleiro() {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        tabuleiro[i][j] = ' ';
      }
    }
  }

  private void exibirTabuleiro() {
    System.out.println("\n   0   1   2");
    for (int i = 0; i < 3; i++) {
      System.out.print(i + " ");
      for (int j = 0; j < 3; j++) {
        System.out.print(" " + tabuleiro[i][j] + " ");
        if (j < 2)
          System.out.print("|");
      }
      System.out.println();
      if (i < 2)
        System.out.println("  ---|---|---");
    }
  }

  private void fazerJogadaHumana() {
    boolean jogadaValida = false;

    while (!jogadaValida) {
      try {
        System.out.print("Digite a linha (0-2): ");
        int linha = scanner.nextInt();
        System.out.print("Digite a coluna (0-2): ");
        int coluna = scanner.nextInt();

        if (linha >= 0 && linha < 3 && coluna >= 0 && coluna < 3) {
          if (tabuleiro[linha][coluna] == ' ') {
            tabuleiro[linha][coluna] = jogadorAtual;
            jogadaValida = true;
          } else {
            System.out.println("Posição já ocupada! Tente novamente.");
          }
        } else {
          System.out.println("Posição inválida! Use números de 0 a 2.");
        }
      } catch (Exception e) {
        System.out.println("Entrada inválida! Digite apenas números.");
        scanner.nextLine();
      }
    }
  }

  private void fazerJogadaComputador() {
    // Minimax simplificado - É a 'IA' do Jogo
    int[] melhorJogada = encontrarMelhorJogada();
    tabuleiro[melhorJogada[0]][melhorJogada[1]] = 'O';
  }

  // Sério, quem foi que inventou isso?
  private int[] encontrarMelhorJogada() {
    // Primeiro: tentar vencer
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (tabuleiro[i][j] == ' ') {
          tabuleiro[i][j] = 'O';
          if (verificarVitoria()) {
            tabuleiro[i][j] = ' '; // Desfazer
            return new int[] { i, j }; // Jogada vencedora
          }
          tabuleiro[i][j] = ' '; // Desfazer
        }
      }
    }

    // Segundo: bloquear vitória do adversário
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (tabuleiro[i][j] == ' ') {
          tabuleiro[i][j] = 'X';
          if (verificarVitoria()) {
            tabuleiro[i][j] = ' '; // Desfazer
            return new int[] { i, j }; // Bloquear vitória
          }
          tabuleiro[i][j] = ' '; // Desfazer
        }
      }
    }

    // Terceiro: estratégia posicional
    // Sempre preferir o centro
    if (tabuleiro[1][1] == ' ') {
      return new int[] { 1, 1 };
    }

    // Preferir cantos
    int[][] cantos = { { 0, 0 }, { 0, 2 }, { 2, 0 }, { 2, 2 } };
    for (int[] canto : cantos) {
      if (tabuleiro[canto[0]][canto[1]] == ' ') {
        return new int[] { canto[0], canto[1] };
      }
    }

    // Sem opções - vai em qualquer posição livre
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (tabuleiro[i][j] == ' ') {
          return new int[] { i, j };
        }
      }
    }

    // Fallback (não deveria ter chego aqui - ou seja, deu b.o)
    return new int[] { 0, 0 };
  }

  private boolean verificarVitoria() {
    // Verificar linhas
    for (int i = 0; i < 3; i++) {
      if (tabuleiro[i][0] == jogadorAtual &&
          tabuleiro[i][1] == jogadorAtual &&
          tabuleiro[i][2] == jogadorAtual) {
        return true;
      }
    }

    // Verificar colunas
    for (int j = 0; j < 3; j++) {
      if (tabuleiro[0][j] == jogadorAtual &&
          tabuleiro[1][j] == jogadorAtual &&
          tabuleiro[2][j] == jogadorAtual) {
        return true;
      }
    }

    // Verificar diagonais
    if (tabuleiro[0][0] == jogadorAtual &&
        tabuleiro[1][1] == jogadorAtual &&
        tabuleiro[2][2] == jogadorAtual) {
      return true;
    }

    if (tabuleiro[0][2] == jogadorAtual &&
        tabuleiro[1][1] == jogadorAtual &&
        tabuleiro[2][0] == jogadorAtual) {
      return true;
    }

    return false;
  }

  private boolean tabuleiroCompleto() {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (tabuleiro[i][j] == ' ') {
          return false;
        }
      }
    }
    return true;
  }

  private boolean perguntarNovaPartida() {
    // Por favor, não jogue
    System.out.print("\nDeseja jogar novamente? (s/n): ");
    String resposta = scanner.next().toLowerCase();
    return resposta.equals("s") || resposta.equals("sim");
  }

  private void exibirPlacar() {
    System.out.println("\n=== PLACAR ATUAL ===");
    if (contraComputador) {
      System.out.println("Suas vitórias: " + vitoriasHumano);
      System.out.println("Vitórias do computador: " + vitoriasComputador);
    } else {
      System.out.println("Vitórias dos jogadores: " + vitoriasHumano);
    }
    System.out.println("Empates: " + empates);
  }

  private void exibirPlacarFinal() {
    System.out.println("\n=== PLACAR FINAL ===");
    if (contraComputador) {
      System.out.println("Suas vitórias: " + vitoriasHumano);
      System.out.println("Vitórias do computador: " + vitoriasComputador);
      System.out.println("Empates: " + empates);

      if (vitoriasHumano > vitoriasComputador) {
        System.out.println("Parabéns! Você venceu no geral!");
      } else if (vitoriasComputador > vitoriasHumano) {
        System.out.println("O computador venceu no geral!");
      } else {
        System.out.println("Empate geral!");
      }
    } else {
      System.out.println("Total de partidas jogadas: " + (vitoriasHumano + empates));
      System.out.println("Empates: " + empates);
    }
  }

  public static void main(String[] args) {
    Desafio2 jogo = new Desafio2();
    jogo.iniciarJogo();
  }
}
