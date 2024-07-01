package Oficial;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Jogo {

    Jogador jogador1;
    Jogador jogador2;
    Tabuleiro tabuleiro;
    Scanner scanner = new Scanner(System.in);
    Jogo(String nomeJogador1, String nomeJogador2) {
        jogador1 = new Jogador(nomeJogador1);
        jogador2 = new Jogador(nomeJogador2);
        tabuleiro = new Tabuleiro();
    }
    public void jogar() {
        //faz jogo continuar enquanto a vida de nenhum for vazia
        while (jogador1.getVida() > 0 && jogador2.getVida() > 0) {
            turno(jogador1, jogador2);
            if (jogador2.getVida() > 0) {
                turno(jogador2, jogador1);
            }
        }
        //declara o vencedor

        System.out.println("FIM de jogo!!!!");
        if (jogador1.getVida() > 0) {
            System.out.println(jogador1.getNome() + " venceuuuu!");
        } else {
            System.out.println(jogador2.getNome() + " venceuuuu!");
        }
    }

    public void turno(Jogador jogadorAtual, Jogador jogadorOponente) {
        boolean jogadaValida = false;

        //enquanto a jogada for valida
        do {
            tabuleiro.mostrarTabuleiro();
            System.out.println(jogadorAtual.getNome() + ", sua vez de jogar");
            System.out.println(jogadorAtual);

            while (jogadaValida == false) {
                System.out.println("Digite a posição por linha x coluna (começa em 0) e a direção (w, a, s, d) para mover:");
                int linha = scanner.nextInt();
                int coluna = scanner.nextInt();
                char direcao = scanner.next().charAt(0);

                //aplica o tabuleiro no turno
                if (tabuleiro.validaJogada(linha, coluna, direcao)) {
                    tabuleiro.mexer(linha, coluna, direcao, jogadorAtual, jogadorOponente);
                    int novaLinha = linha, novaColuna = coluna;
                    switch (direcao) {
                        case 'w': novaLinha--; break;
                        case 'a': novaColuna--; break;
                        case 's': novaLinha++; break;
                        case 'd': novaColuna++; break;
                    }
                    //aplica efeito
                    char elemento = Tabuleiro.tabuleiro[novaLinha][novaColuna];
                    tabuleiro.aplicarEfeitoSimbolo(jogadorAtual, jogadorOponente, elemento);
                    jogadaValida = true;
                } else {
                    System.out.println("✘✘✘ Jogada inválida, tente novamente.");
                }
            }
            //opção de sair do jogo a qualquer momento
            boolean opcaoValida = false;
            while (opcaoValida == false) {
                System.out.println("Deseja salvar o jogo (s), sair (x) ou continuar (c)?");
                char opcao = scanner.next().charAt(0);
                switch (opcao) {
                    case 's':
                        System.out.println("Digite o nome do arquivo para salvar:");
                        String nomeArquivo = scanner.next();
                        Menu.salvarJogo(this, nomeArquivo);
                        opcaoValida = true;
                        break;
                    case 'x':
                        System.out.println("--------- Saindo do jogo...");;
                        System.exit(0);
                        opcaoValida = true;
                        break;
                    case 'c':
                        opcaoValida = true;
                        break;
                    default:
                        System.out.println("Opção inválida, digite de novo.");
                }
            }

            //jogada extra caso faça 4
            if (jogadorAtual.temJogadaExtra()) {
                jogadorAtual.usarJogadaExtra();
                jogadaValida = false; // reinicia a validação da jogada para a próxima jogada extra
            } else {
                break; // sai do loop se não houver mais jogadas extras
            }
        } while (true);
    }


    //aqui ta o método pra conseguir salvar e dps pegar os dados
    public List<String> exportarDados() {
        List<String> dadosJogo = new ArrayList<>();
        dadosJogo.addAll(jogador1.exportarDados());
        dadosJogo.addAll(jogador2.exportarDados());
        dadosJogo.addAll(Tabuleiro.exportarDados());
        return dadosJogo;
    }
    public static Jogo importarDados(List<String> dadosJogo) {
        int offset = 0;

        String nomeJogador1 = dadosJogo.get(offset++);
        int vidaJogador1 = Integer.parseInt(dadosJogo.get(offset++));
        int ouroJogador1 = Integer.parseInt(dadosJogo.get(offset++));
        int expJogador1 = Integer.parseInt(dadosJogo.get(offset++));

        String nomeJogador2 = dadosJogo.get(offset++);
        int vidaJogador2 = Integer.parseInt(dadosJogo.get(offset++));
        int ouroJogador2 = Integer.parseInt(dadosJogo.get(offset++));
        int expJogador2 = Integer.parseInt(dadosJogo.get(offset++));

        Jogo jogo = new Jogo(nomeJogador1, nomeJogador2);
        jogo.jogador1.setVida(vidaJogador1);
        jogo.jogador1.setOuro(ouroJogador1);
        jogo.jogador1.setExperiencia(expJogador1);

        jogo.jogador2.setVida(vidaJogador2);
        jogo.jogador2.setOuro(ouroJogador2);
        jogo.jogador2.setExperiencia(expJogador2);

        List<String> dadosTabuleiro = dadosJogo.subList(offset, dadosJogo.size());
        jogo.tabuleiro.importarDados(dadosTabuleiro);

        return jogo;
    }


}
