package Pecas;

import TabuleiroDoJogo.Tabuleiro;
import TabuleiroDoJogo.Posicao;
import Xadrez.Partida;
import Xadrez.XadrezPeca;
import Xadrez.Cor;

public class Rei extends XadrezPeca {
	private Partida partida;

	public Rei(Tabuleiro tabuleiro, Cor cor, Partida partida) {
		super(tabuleiro, cor);
		this.setPartida(partida);
	}
	
	@Override
	public String toString() {
		return "K";
	}
	
	private boolean podeMover(Posicao posicao) {
		XadrezPeca p = (XadrezPeca)getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
	}

	private boolean testRookCastling(Posicao posicao) {
		XadrezPeca p = (XadrezPeca)getTabuleiro().peca(posicao);
		return p != null && p instanceof Torre && p.getCor() == getCor() && p.getContMov() == 0;
	}

	@Override
	public boolean[][] movimentoPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao p = new Posicao(0, 0);

		p.setValues(posicao.getLinha() - 1, posicao.getColuna());
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValues(posicao.getLinha() + 1, posicao.getColuna());
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValues(posicao.getLinha(), posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValues(posicao.getLinha(), posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValues(posicao.getLinha() - 1, posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValues(posicao.getLinha() - 1, posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValues(posicao.getLinha() + 1, posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValues(posicao.getLinha() + 1, posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
				if (getContMov() == 0 && !Partida.getCheck()) {
					Posicao posT1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
					if (testRookCastling(posT1)) {
						Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
						Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
						if (getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null) {
							mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
						}
					}
					Posicao posT2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
					if (testRookCastling(posT2)) {
						Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
						Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
						Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
						if (getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null && getTabuleiro().peca(p3) == null) {
							mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
						}
					}
				}

		return mat;
	}

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}
}