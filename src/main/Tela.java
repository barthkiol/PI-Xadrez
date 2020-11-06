package main;


import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import Xadrez.Cor;
import Xadrez.Partida;
import Xadrez.PosicaoXadrez;
import Xadrez.XadrezPeca;

public class Tela {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	
	public static PosicaoXadrez lerPosicaoXadrez (Scanner sc) {
		try {
			String s = sc.nextLine();
			char coluna = s.charAt(0);
			int linha = Integer.parseInt(s.substring(1));
			return new PosicaoXadrez(coluna, linha);
		} catch (RuntimeException e) {
			throw new InputMismatchException("Entre com um valor entre a1 e h8");
		}
	}
	
	public static void printPartida(Partida partida, List<XadrezPeca> capturado) {
		printTabuleiro(Partida.getPecas());
		System.out.println();
		printcapturadoPecas(capturado);
		System.out.println();
		System.out.println("Turno : " + Partida.getTurno());
		if(Partida.getTurno() / 2 == 0) {
			System.out.println("Esperando Jogador: " + Jogadores.getJogador1() + " " + Partida.getJogadorAtual());
		}else {
			System.out.println("Esperando Jogador: " + Jogadores.getJogador2() + " "+ Partida.getJogadorAtual());
		}
		
		if (!Partida.getCheckMate()) {
			//System.out.println("Esperando Jogador: " + Partida.getJogadorAtual());
			if (Partida.getCheck()) {
				System.out.println("CHECK!");
			}
		}
		else {
			System.out.println("CHECKMATE!");
			if(Partida.getJogadorAtual() == Cor.BRANCO) {
				System.out.println("Vencedor: " + Jogadores.getJogador1() + " " + Partida.getJogadorAtual());
			}else {
				System.out.println("Vencedor: " + Jogadores.getJogador2() + " " + Partida.getJogadorAtual());
			}
			
		}
	}
	
	public static void printTabuleiro(XadrezPeca[][] pecas) {
		for (int i=0; i<pecas.length; i++) {
			System.out.print(ANSI_CYAN + (8 - i) + ANSI_RESET + " ");
			for (int j=0; j<pecas.length; j++) {
				printPeca(pecas[i][j], false);
			}
			System.out.println();
		}
		System.out.println(ANSI_CYAN + "  A B C D E F G H" + ANSI_RESET);
	}
	
	public static void printTabuleiro(XadrezPeca[][] pecas, boolean[][] movimentosPossiveis) {
		for (int i=0; i<pecas.length; i++) {
			System.out.print(ANSI_CYAN + (8 - i) + ANSI_RESET + " ");
			for (int j=0; j<pecas.length; j++) {
				printPeca(pecas[i][j], movimentosPossiveis[i][j]);
			}
			System.out.println();
		}
		System.out.println(ANSI_CYAN + "  A B C D E F G H" + ANSI_RESET);
	}
	

	private static void printPeca(XadrezPeca peca, boolean background) {
		if (background) {
			System.out.print(ANSI_RED_BACKGROUND);
		}
		if (peca == null) {
            System.out.print("-" + ANSI_RESET);
        }
        else {
            if (peca.getCor() == Cor.BRANCO) {
                System.out.print(ANSI_WHITE + peca + ANSI_RESET);
            }
            else {
                System.out.print(ANSI_GREEN + peca + ANSI_RESET);
            }
        }
        System.out.print(" ");
	}
	
	private static void printcapturadoPecas(List<XadrezPeca> capturado) {
		List<XadrezPeca> white = capturado.stream().filter(x -> x.getCor() == Cor.BRANCO).collect(Collectors.toList());
		List<XadrezPeca> black = capturado.stream().filter(x -> x.getCor() == Cor.PRETO).collect(Collectors.toList());
		System.out.println("Pecas capturadas:");
		System.out.print("Branco: ");
		System.out.print(ANSI_WHITE);
		System.out.println(Arrays.toString(white.toArray()));
		System.out.print(ANSI_RESET);
		System.out.print("Preto: ");
		System.out.print(ANSI_GREEN);
		System.out.println(Arrays.toString(black.toArray()));
		System.out.print(ANSI_RESET);
	}
}