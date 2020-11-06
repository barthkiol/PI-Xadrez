package main;

public class Jogadores {

	private static String jogador1;
	private static String jogador2;
	public Jogadores(String jogador1, String jogador2) {
		Jogadores.jogador1 = jogador1;
		Jogadores.jogador2 = jogador2;
	}
	public static String getJogador1() {
		return jogador1;
	}
	public void setJogador1(String jogador1) {
		Jogadores.jogador1 = jogador1;
	}
	public static String getJogador2() {
		return jogador2;
	}
	public void setJogador2(String jogador2) {
		Jogadores.jogador2 = jogador2;
	}
	
}
