package Pecas;

import TabuleiroDoJogo.Posicao;
import TabuleiroDoJogo.Tabuleiro;
import Xadrez.XadrezPeca;
import Xadrez.Cor;

public class Torre extends XadrezPeca {

	public Torre(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
		
	}
	
	@Override
	public String toString() {
		return "T";
	}

	@Override
	public boolean[][] movimentoPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao p = new Posicao(0, 0);
		
		// above
				p.setValues(posicao.getLinha() - 1, posicao.getColuna());
				while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temAPeca(p)) {
					mat[p.getLinha()][p.getColuna()] = true;
					p.setLinha(p.getLinha() - 1);
				}
				if (getTabuleiro().posicaoExiste(p) && temPecaOponente(p)) {
					mat[p.getLinha()][p.getColuna()] = true;
				}

				// left
				p.setValues(posicao.getLinha(), posicao.getColuna() - 1);
				while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temAPeca(p)) {
					mat[p.getLinha()][p.getColuna()] = true;
					p.setColuna(p.getColuna() - 1);
				}
				if (getTabuleiro().posicaoExiste(p) && temPecaOponente(p)) {
					mat[p.getLinha()][p.getColuna()] = true;
				}

				// right
				p.setValues(posicao.getLinha(), posicao.getColuna() + 1);
				while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temAPeca(p)) {
					mat[p.getLinha()][p.getColuna()] = true;
					p.setColuna(p.getColuna() + 1);
				}
				if (getTabuleiro().posicaoExiste(p) && temPecaOponente(p)) {
					mat[p.getLinha()][p.getColuna()] = true;
				}

				// below
				p.setValues(posicao.getLinha() + 1, posicao.getColuna());
				while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temAPeca(p)) {
					mat[p.getLinha()][p.getColuna()] = true;
					p.setLinha(p.getLinha() + 1);
				}
				if (getTabuleiro().posicaoExiste(p) && temPecaOponente(p)) {
					mat[p.getLinha()][p.getColuna()] = true;
				}
		
		return mat;
	}
	
}