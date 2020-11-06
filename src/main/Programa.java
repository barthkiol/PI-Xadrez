package main;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import Xadrez.Partida;
import Xadrez.PosicaoXadrez;
import Xadrez.XadrezEr;
import Xadrez.XadrezPeca;

public class Programa {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		Partida xadrezPartida = new Partida();
		List<XadrezPeca> capturado = new ArrayList<>();
		
		Tela.clearScreen();
		System.out.print("Digite o nome do Jogador 1 (pecas brancas): ");
		String j1 = scan.next();
		System.out.print("Digite o nome do Jogador 2 (pecas pretas): ");
		String j2 = scan.next();
		Jogadores jogadores = new Jogadores(j1, j2);
		
		while (!Partida.getCheckMate()) {
			try {
				Tela.clearScreen();
				Tela.printPartida(xadrezPartida, capturado);
				System.out.println();
				System.out.print("Origem: ");
				PosicaoXadrez origem = Tela.lerPosicaoXadrez(scan);
				boolean[][] movimentoPossiveis = xadrezPartida.movimentoPossivel(origem);
				Tela.clearScreen();
				Tela.printTabuleiro(Partida.getPecas(), movimentoPossiveis);
				System.out.println();
				System.out.print("Destino: ");
				PosicaoXadrez alvo = Tela.lerPosicaoXadrez(scan);
				XadrezPeca capturadoPeca = xadrezPartida.relizaMovimentoXadrez(origem, alvo);
				if (capturadoPeca != null) {
					capturado.add(capturadoPeca);
				}
			} catch (XadrezEr e) {
				System.out.println(e.getMessage());
				scan.nextLine();				
				if (xadrezPartida.getPromocao() != null) {
					System.out.print("Escolha Peça Para Promoção (B/N/R/Q): ");
					String type = scan.nextLine().toUpperCase();
					while (!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
						System.out.print("Valor Invalido, Escolha Peça Para Promoção (B/N/R/Q): ");
						type = scan.nextLine().toUpperCase();
					}
					xadrezPartida.trocaPecaPromovida(type);
				}
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				scan.nextLine();
			}
		}
		Tela.clearScreen();
		Tela.printPartida(xadrezPartida, capturado);
		scan.close();
	}
}